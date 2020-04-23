package cn.celess.blog.service.serviceimpl;

import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.entity.WebUpdate;
import cn.celess.blog.entity.model.WebUpdateModel;
import cn.celess.blog.exception.MyException;
import cn.celess.blog.mapper.WebUpdateInfoMapper;
import cn.celess.blog.service.WebUpdateInfoService;
import cn.celess.blog.util.DateFormatUtil;
import cn.celess.blog.util.HttpUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : xiaohai
 * @date : 2019/05/12 11:43
 */
@Service
@Slf4j
public class WebUpdateInfoServiceImpl implements WebUpdateInfoService {
    @Autowired
    WebUpdateInfoMapper webUpdateInfoMapper;


    @Override
    public WebUpdateModel create(String info) {
        if (info == null || info.replaceAll(" ", "").isEmpty()) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        }
        WebUpdate webUpdate = new WebUpdate(info, new Date());
        if (webUpdateInfoMapper.insert(webUpdate) == 0) {
            throw new MyException(ResponseEnum.FAILURE);
        }
        return trans(webUpdate);
    }

    @Override
    public Boolean del(long id) {
        if (!webUpdateInfoMapper.existsById(id)) {
            throw new MyException(ResponseEnum.DATA_NOT_EXIST);
        }
        return webUpdateInfoMapper.delete(id) == 1;
    }

    @Override
    public WebUpdateModel update(long id, String info) {
        WebUpdate webUpdate = webUpdateInfoMapper.findById(id);
        if (webUpdate == null) {
            throw new MyException(ResponseEnum.DATA_NOT_EXIST);
        }
        if (info == null || info.replaceAll(" ", "").isEmpty()) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        }
        webUpdate.setUpdateInfo(info);
        webUpdateInfoMapper.update(id, info);
        return trans(webUpdate);
    }

    @Override
    public PageInfo<WebUpdateModel> pages(int count, int page) {
        PageHelper.startPage(page, count);
        List<WebUpdate> updateList = webUpdateInfoMapper.findAll();
        PageInfo pageInfo = new PageInfo(updateList);
        pageInfo.setList(list2List(updateList));
        return pageInfo;
    }

    @Override
    public List<WebUpdateModel> findAll() {
        List<WebUpdate> all = webUpdateInfoMapper.findAll();
        List<WebUpdateModel> webUpdateModels = new ArrayList<>();
        for (WebUpdate w : all) {
            webUpdateModels.add(trans(w));
        }
        return webUpdateModels;
    }

    @Override
    public JSONObject getLastestUpdateTime() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("lastUpdateTime", DateFormatUtil.get(webUpdateInfoMapper.getLastestOne().getUpdateTime()));
        jsonObject.put("lastUpdateInfo", webUpdateInfoMapper.getLastestOne().getUpdateInfo());
        try {
            JSONArray array = JSONArray.fromObject(HttpUtil.get("https://api.github.com/repos/xiaohai2271/blog-frontEnd/commits?page=1&per_page=1"));
            JSONObject object = array.getJSONObject(0);
            JSONObject commit = object.getJSONObject("commit");
            jsonObject.put("lastCommit", commit.getString("message"));
            jsonObject.put("committerAuthor", commit.getJSONObject("committer").getString("name"));
            SimpleDateFormat sdf = new SimpleDateFormat();
            Instant parse = Instant.parse(commit.getJSONObject("committer").getString("date"));
            jsonObject.put("committerDate", DateFormatUtil.get(Date.from(parse)));
            jsonObject.put("commitUrl", "https://github.com/xiaohai2271/blog-frontEnd/tree/"+object.getString("sha"));
        } catch (IOException e) {
            log.info("网络请求失败{}", e.getMessage());
        }
        return jsonObject;
    }

    private List<WebUpdateModel> list2List(List<WebUpdate> webUpdates) {
        List<WebUpdateModel> webUpdateModels = new ArrayList<>();
        for (WebUpdate w : webUpdates) {
            webUpdateModels.add(trans(w));
        }
        return webUpdateModels;
    }

    private WebUpdateModel trans(WebUpdate webUpdate) {
        return new WebUpdateModel(webUpdate.getId(), webUpdate.getUpdateInfo(), DateFormatUtil.get(webUpdate.getUpdateTime()));
    }

}
