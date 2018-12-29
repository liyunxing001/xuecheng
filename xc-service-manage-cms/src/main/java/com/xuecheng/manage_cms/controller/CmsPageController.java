package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: 星仔
 * @Date: 2018/12/7 10:04
 * @Description:
 */
@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    private CmsPageService cmsPageService;

    @Override
    @GetMapping("/list/{pageNum}/{pageSize}")
    public QueryResponseResult findList(@PathVariable("pageNum") int pageNum,@PathVariable("pageSize") int pageSize, QueryPageRequest queryPageRequest) {
        return cmsPageService.findList(pageNum,pageSize,queryPageRequest);
    }

    @Override
    @GetMapping("/sites")
    public QueryResponseResult findSites() {
        return cmsPageService.findSites();
    }

    @Override
    @PostMapping("/add")
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        if(cmsPage==null|| StringUtils.isEmpty(cmsPage.getPageName())
                ||StringUtils.isEmpty(cmsPage.getSiteId())
        ||StringUtils.isEmpty(cmsPage.getPageWebPath())){
            return new CmsPageResult(CommonCode.FAIL,null);
        }
        return cmsPageService.add(cmsPage);
    }

    @Override
    @GetMapping("/find/{id}")
    public CmsPageResult find(@PathVariable("id") String id) {
        return cmsPageService.find(id);
    }

    @Override
    @PutMapping("/update/{id}")
    public CmsPageResult updateCmsPage(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
        return cmsPageService.update(id,cmsPage);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteCmsPage(@PathVariable("id") String id) {
        return cmsPageService.deleteCmsPage(id);
    }


}