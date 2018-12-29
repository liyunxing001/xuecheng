package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

/**
 * @Auther: 星仔
 * @Date: 2018/12/13 17:05
 * @Description:
 */
@Component
public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {
}
