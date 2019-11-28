package cn.celess.blog.service.serviceimpl;

import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.entity.Visitor;
import cn.celess.blog.entity.model.VisitorModel;
import cn.celess.blog.exception.MyException;
import cn.celess.blog.mapper.VisitorMapper;
import cn.celess.blog.service.VisitorService;
import cn.celess.blog.util.DateFormatUtil;
import cn.celess.blog.util.RedisUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author : xiaohai
 * @date : 2019/04/02 23:04
 */
@Service
public class VisitorServiceImpl implements VisitorService {
    @Autowired
    VisitorMapper visitorMapper;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public String location(String ip) {
        return getLocation(ip);
    }

    @Override
    public PageInfo<VisitorModel> visitorPage(int page, int count, boolean showLocation) {
        PageHelper.startPage(page, count);
        List<Visitor> visitorList = visitorMapper.findAll();
        PageInfo pageInfo = new PageInfo(visitorList);
        pageInfo.setList(list2List(visitorList, showLocation));
        return pageInfo;
    }

    @Override
    public VisitorModel addVisitor(HttpServletRequest request) {
        //新session
        if (!request.getSession().isNew()) {
            return null;
        }
        if (isSpiderBot(request.getHeader("User-Agent"))) {
            return null;
        }
        Visitor visitor = new Visitor();
        visitor.setIp(request.getRemoteAddr());
        visitor.setDate(new Date());
        visitor.setUa(request.getHeader("User-Agent"));
        //记录当日的访问
        String dayVisitCount = redisUtil.get("dayVisitCount");
        long secondsLeftToday = 86400 - DateUtils.getFragmentInSeconds(Calendar.getInstance(), Calendar.DATE);
        Date date = new Date(Calendar.YEAR);
        if (dayVisitCount == null) {
            redisUtil.setEx("dayVisitCount", "1", secondsLeftToday, TimeUnit.SECONDS);
        } else {
            int count = Integer.parseInt(dayVisitCount) + 1;
            redisUtil.setEx("dayVisitCount", count + "", secondsLeftToday, TimeUnit.SECONDS);
        }
        if (visitorMapper.insert(visitor) == 0) {
            throw new MyException(ResponseEnum.FAILURE);
        }
        return trans(visitor);
    }


    /**
     * 数据修改
     *
     * @return
     */
    private List<VisitorModel> list2List(List<Visitor> visitorList, boolean showLocation) {
        List<VisitorModel> visitorModelList = new ArrayList<>();
        for (Visitor v : visitorList) {
            VisitorModel trans = trans(v);
            if (showLocation) {
                trans.setLocation(getLocation(v.getIp()));
            }
            visitorModelList.add(trans);
        }
        return visitorModelList;
    }

    /***
     * 转化为model
     *
     * @param v
     * @return
     */
    private VisitorModel trans(Visitor v) {
        UserAgent userAgent = UserAgent.parseUserAgentString(v.getUa());
        VisitorModel visitor = new VisitorModel();
        visitor.setId(v.getId());
        visitor.setDate(DateFormatUtil.get(v.getDate()));
        visitor.setIp(v.getIp());
        Browser browser = userAgent.getBrowser();
        visitor.setBrowserName(browser == null ? "" : browser.getName());
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        visitor.setOSName(operatingSystem == null ? "" : operatingSystem.getName());
        Version browserVersion = userAgent.getBrowserVersion();
        visitor.setBrowserVersion(browserVersion == null ? "" : browserVersion.getVersion());
        return visitor;
    }

    /**
     * 根据ua判断是不是爬虫
     *
     * @param ua ua
     * @return true：爬虫 false ：不是爬虫
     */
    private boolean isSpiderBot(String ua) {
        if (ua == null) {
            return false;
        }
        //服务器端的缓存抓取
        if (ua.contains("https://github.com/prerender/prerender")) {
            return true;
        }
        //搜索引擎得爬虫ua一般有链接
        if (ua.contains("http://")) {
            return true;
        }
        //防止没有匹配到http
        return ua.toLowerCase().contains("spider");
    }

    /**
     * 获取ip的地址
     *
     * @param ip
     * @return
     */
    private String getLocation(String ip) {
        StringBuilder result = new StringBuilder();
        URL url;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            url = new URL("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            inputStream = conn.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String tmp;
            while ((tmp = bufferedReader.readLine()) != null) {
                result.append(tmp);
            }
        } catch (Exception e) {
            // ignore
        } finally {
            try {
                if (conn != null) {
                    conn.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e) {
                // ignore
            }
        }
        StringBuilder sb = new StringBuilder();
        if ("".equals(result.toString())) {
            throw new MyException(ResponseEnum.FAILURE);
        }
        Map<String, Object> stringObjectMap = JsonParserFactory.getJsonParser().parseMap(result.toString());
        if ((Integer) stringObjectMap.get("code") == 0) {
            LinkedHashMap data = (LinkedHashMap) stringObjectMap.get("data");
            sb.append(data.get("country"))
                    .append("-")
                    .append(data.get("region"))
                    .append("-")
                    .append(data.get("city"));
        }
        return sb.toString();

    }


}
