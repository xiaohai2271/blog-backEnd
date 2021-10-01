package cn.celess.siteinfo.serviceimpl;

import cn.celess.common.enmu.ResponseEnum;
import cn.celess.common.entity.WebUpdate;
import cn.celess.common.entity.vo.PageData;
import cn.celess.common.entity.vo.WebUpdateModel;
import cn.celess.common.exception.BlogResponseException;
import cn.celess.common.mapper.WebUpdateInfoMapper;
import cn.celess.common.service.WebUpdateInfoService;
import cn.celess.common.util.DateFormatUtil;
import cn.celess.common.util.HttpUtil;
import cn.celess.common.util.ModalTrans;
import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

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
            throw new BlogResponseException(ResponseEnum.PARAMETERS_ERROR);
        }
        WebUpdate webUpdate = new WebUpdate(info);
        if (webUpdateInfoMapper.insert(webUpdate) == 0) {
            throw new BlogResponseException(ResponseEnum.FAILURE);
        }
        return ModalTrans.webUpdate(webUpdateInfoMapper.findById(webUpdate.getId()));
    }

    @Override
    public Boolean del(long id) {
        if (!webUpdateInfoMapper.existsById(id)) {
            throw new BlogResponseException(ResponseEnum.DATA_NOT_EXIST);
        }
        return webUpdateInfoMapper.delete(id) == 1;
    }

    @Override
    public WebUpdateModel update(long id, String info) {
        WebUpdate webUpdate = webUpdateInfoMapper.findById(id);
        if (webUpdate == null) {
            throw new BlogResponseException(ResponseEnum.DATA_NOT_EXIST);
        }
        if (info == null || info.replaceAll(" ", "").isEmpty()) {
            throw new BlogResponseException(ResponseEnum.PARAMETERS_ERROR);
        }
        webUpdate.setUpdateInfo(info);
        webUpdateInfoMapper.update(id, info);
        return ModalTrans.webUpdate(webUpdate);
    }

    @Override
    public PageData<WebUpdateModel> pages(int count, int page) {
        PageHelper.startPage(page, count);
        List<WebUpdate> updateList = webUpdateInfoMapper.findAll();
        return new PageData<>(new PageInfo<>(updateList), list2List(updateList));
    }

    @Override
    public List<WebUpdateModel> findAll() {
        List<WebUpdate> all = webUpdateInfoMapper.findAll();
        return list2List(all);
    }

    @Override

    public Map<String, Object> getLastestUpdateTime() {
        Map<String, Object> map = new HashMap<>();
        map.put("lastUpdateTime", DateFormatUtil.get(webUpdateInfoMapper.getLastestOne().getUpdateTime()));
        map.put("lastUpdateInfo", webUpdateInfoMapper.getLastestOne().getUpdateInfo());
        try {
            ObjectMapper mapper = new ObjectMapper();
            String respStr = HttpUtil.getHttpResponse("https://api.github.com/repos/xiaohai2271/blog-frontEnd/commits?page=1&per_page=1");
            if (!StringUtils.isEmpty(respStr)) {
                JsonNode root = mapper.readTree(respStr);
                Iterator<JsonNode> elements = root.elements();
                JsonNode next = elements.next();
                JsonNode commit = next.get("commit");
                map.put("lastCommit", commit.get("message"));
                map.put("committerAuthor", commit.get("committer").get("name"));
                Instant parse = Instant.parse(commit.get("committer").get("date").asText());
                map.put("committerDate", DateFormatUtil.get(Date.from(parse)));
                map.put("commitUrl", "https://github.com/xiaohai2271/blog-frontEnd/tree/" + next.get("sha").asText());
            }
        } catch (IOException e) {
            log.info("网络请求失败{}", e.getMessage());
        }
        return map;
    }

    private List<WebUpdateModel> list2List(List<WebUpdate> webUpdates) {
        return webUpdates.stream().map(ModalTrans::webUpdate).collect(Collectors.toList());
    }
}
