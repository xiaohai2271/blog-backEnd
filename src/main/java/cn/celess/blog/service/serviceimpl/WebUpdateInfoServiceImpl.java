package cn.celess.blog.service.serviceimpl;

import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.entity.WebUpdate;
import cn.celess.blog.entity.model.WebUpdateModel;
import cn.celess.blog.exception.MyException;
import cn.celess.blog.mapper.WebUpdateInfoMapper;
import cn.celess.blog.service.WebUpdateInfoService;
import cn.celess.blog.util.DateFormatUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : xiaohai
 * @date : 2019/05/12 11:43
 */
@Service
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
    public String getLastestUpdateTime() {
        return DateFormatUtil.get(webUpdateInfoMapper.getLastestOne());
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
