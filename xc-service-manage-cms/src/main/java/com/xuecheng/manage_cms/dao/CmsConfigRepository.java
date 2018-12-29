package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

/**
 * @Auther: 星仔
 * @Date: 2018/12/18 23:34
 * @Description:
 */
@Component
public interface CmsConfigRepository extends MongoRepository<CmsConfig,String> {
}