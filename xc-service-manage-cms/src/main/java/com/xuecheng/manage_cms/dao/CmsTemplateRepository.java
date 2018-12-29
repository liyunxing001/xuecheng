package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

/**
 * @Auther: 星仔
 * @Date: 2018/12/22 16:20
 * @Description:
 */
@Component
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String> {
}