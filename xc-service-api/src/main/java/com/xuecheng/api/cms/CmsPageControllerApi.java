package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @Auther: 星仔
 * @Date: 2018/12/6 17:22
 * @Description:
 */
@Api(value="cms页面管理接口",description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {

    /**
     *
     * @param pageNum
     * @param pageSize
     * @param queryPageRequest
     * @return
     */
    @ApiOperation("分页查询页面列表") @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum",value = "页码",required=true,paramType="path",dataType="int"),
            @ApiImplicitParam(name="pageSize",value = "每页记录数",required=true,paramType="path",dataType="int")
    })
    QueryResponseResult findList(int pageNum, int pageSize, QueryPageRequest queryPageRequest);

    @ApiOperation("查询站点信息")
    QueryResponseResult findSites();

    @ApiOperation("添加页面信息")
    CmsPageResult add(CmsPage cmsPage);

    @ApiOperation("查询页面信息")
    CmsPageResult find(String id);

    @ApiOperation("修改页面信息")
    CmsPageResult updateCmsPage(String id,CmsPage cmsPage);

    @ApiOperation("删除页面信息")
    ResponseResult deleteCmsPage(String id);

}
