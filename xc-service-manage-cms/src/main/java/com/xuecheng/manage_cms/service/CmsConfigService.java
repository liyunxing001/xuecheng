package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Auther: 星仔
 * @Date: 2018/12/18 23:35
 * @Description:
 */
@Service
public class CmsConfigService {

    @Autowired
    private CmsConfigRepository cmsConfigRepository;

    public CmsConfig getModel(String id){
        Optional<CmsConfig> cmsConfigOpt = cmsConfigRepository.findById(id);
        if(cmsConfigOpt.isPresent()){
            return cmsConfigOpt.get();
        }
        return null;
    }
}