package com.xuecheng.api.cms;

/**
 * @Auther: 星仔
 * @Date: 2018/12/18 23:29
 * @Description:
 */

import com.xuecheng.framework.domain.cms.CmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "cms配置管理接口",description = "cms配置管理接口，提供数据模型的管理、查询接口")
public interface CmsConfigControllerApi {

    @ApiOperation("根据id查询配置信息")
    public CmsConfig getModel(String id);
}