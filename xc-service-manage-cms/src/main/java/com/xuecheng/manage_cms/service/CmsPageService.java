package com.xuecheng.manage_cms.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.CastException;
import com.xuecheng.framework.model.response.*;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Auther: 星仔
 * @Date: 2018/12/10 09:43
 * @Description:
 */
@Service
public class CmsPageService {

    @Autowired
    CmsPageRepository cmsPageRepository;

    @Autowired
    CmsSiteRepository cmsSiteRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    @Autowired
    CmsTemplateRepository cmsTemplateRepository;

    /**
     *
     * @param pageNum
     * @param pageSize
     * @param queryPageRequest
     * @return
     */
    public QueryResponseResult findList(int pageNum, int pageSize, QueryPageRequest queryPageRequest) {

        if(queryPageRequest == null){
            queryPageRequest = new QueryPageRequest();
        }
        if(pageNum<=0){
            pageNum = 1;
        }
        pageNum -= 1;
        if(pageSize<0){
            pageSize = 20;
        }
        //设置mongoDB的匹配器
        //别名查询
        //页面名称对应CmsPage模型类中的pageName属性，模糊查询
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("pageName",ExampleMatcher.GenericPropertyMatchers.contains());

        CmsPage cmsPage = new CmsPage();
        if(!StringUtils.isEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        //根据页面名称进行模糊查询
        if(!StringUtils.isEmpty(queryPageRequest.getPageName())){
            cmsPage.setPageAliase(queryPageRequest.getPageName());
        }
        //页面类型对应CmsPage模型类中的pageType属性。
        if(!StringUtils.isEmpty(queryPageRequest.getPageType())){
            cmsPage.setPageType(queryPageRequest.getPageType());
        }
        if(!StringUtils.isEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if(!StringUtils.isEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);
        QueryResult<CmsPage> queryResult = new QueryResult();
        queryResult.setList(all.getContent());
        queryResult.setTotal(all.getTotalElements());
        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }

    /**
     * 查询站点信息
     * @return
     */
    public QueryResponseResult findSites(){
        List<CmsSite> sites = cmsSiteRepository.findAll();
        QueryResult queryResult = new QueryResult();
        queryResult.setList(sites);
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }

    /**
     * 添加站点信息
     * @param cmsPage
     * @return
     */
    public CmsPageResult add(CmsPage cmsPage){
        //检验是否重复插入,根据页面名称，站点id，页面的访问路径
        CmsPage page = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName()
                , cmsPage.getSiteId(), cmsPage.getPageWebPath());

        if(page != null){
            CastException.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }


        cmsPage.setPageId(null);
        CmsPage cmsPage1 = cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS,cmsPage1);

    }

    /**
     * 查询页面信息
     * @param id
     * @return
     */
    public CmsPageResult find(String id){
        Optional<CmsPage> page = cmsPageRepository.findById(id);
        if(page.isPresent()){
            CmsPage cmsPage = page.get();
            return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    /**
     * 修改页面信息
     * @param id
     * @param cmsPage
     * @return
     */
    public CmsPageResult update(String id,CmsPage cmsPage){
        CmsPageResult cmsPageResult = this.find(id);
        CmsPage one = cmsPageResult.getCmsPage();
        if(one != null){
            //更新模板id
            one.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            one.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            one.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            one.setPageName(cmsPage.getPageName());
            //更新访问路径
            one.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            //更改数据url
            one.setDataUrl(cmsPage.getDataUrl());
            cmsPageRepository.save(one);
            return new CmsPageResult(CommonCode.SUCCESS,one);
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    /**
     * 删除页面信息
     * @param id
     * @return
     */
    public ResponseResult deleteCmsPage(String id){
        CmsPageResult cmsPage = this.find(id);
        if(cmsPage != null){
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    /**
     * 获取页面的html
     * @param id
     * @return
     */
    public String getPageHtml(String id){
        /**
         * 获取模型数据
         * 获取页面模版
         * 将模型数据赋给模版数据
         */

        Map<String, Object> map = getPageMap(id);
        if(map==null){
            CastException.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String pageModel = getPageModel(id);
        if(StringUtils.isEmpty(pageModel)){
            CastException.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String html = getHtmlModel(map, pageModel);
        if(StringUtils.isEmpty(html)){
            CastException.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        return html;
    }

    private String getHtmlModel(Map<String,Object> map,String template){
        try {
            //生成配置类
            Configuration configuration = new Configuration(Configuration.getVersion());
            //模板加载器
            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            stringTemplateLoader.putTemplate("template",template);
            //配置模板加载器
            configuration.setTemplateLoader(stringTemplateLoader);
            //获取模板
            Template configurationTemplate = configuration.getTemplate("template");
            System.out.println(configurationTemplate.getOutputFormat().toString());
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(configurationTemplate, map);
            return html;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取页面模版
     * @param id
     * @return
     */
    private String getPageModel(String id){
        CmsPageResult cmsPageResult = this.find(id);
        if(cmsPageResult==null){
            CastException.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        CmsPage cmsPage = cmsPageResult.getCmsPage();
        String templateId = cmsPage.getTemplateId();
        if(StringUtils.isEmpty(templateId)){
            CastException.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        Optional<CmsTemplate> cmsTemplateOpt = cmsTemplateRepository.findById(templateId);
        if(!cmsTemplateOpt.isPresent()){
            CastException.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String cmsTemplateId = cmsTemplateOpt.get().getTemplateFileId();
        //查找gridFSFile对象
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(cmsTemplateId)));
        //打开下载流
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getId());
        //创建GridSource
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
        try {
            String content = IOUtils.toString(gridFsResource.getInputStream(), "UTF-8");
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取页面的模型数据
     * @param id
     * @return
     */
    private Map<String,Object> getPageMap(String id){
        CmsPageResult cmsPageResult = this.find(id);
        if(cmsPageResult==null){
            CastException.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        CmsPage cmsPage = cmsPageResult.getCmsPage();
        String dataUrl = cmsPage.getDataUrl();
        if(StringUtils.isEmpty(dataUrl)){
            CastException.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        ResponseEntity<Map> entity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = entity.getBody();
        return body;
    }

}