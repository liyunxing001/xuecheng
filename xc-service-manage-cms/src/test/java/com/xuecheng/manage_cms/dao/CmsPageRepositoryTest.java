package com.xuecheng.manage_cms.dao;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.awt.print.Pageable;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Auther: 星仔
 * @Date: 2018/12/7 11:09
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    private CmsPageService cmsPageService;

    @Test
    public void getHtml(){
        String pageHtml = cmsPageService.getPageHtml("5c1de27c72884ed5c1e23e95");
        System.out.println(pageHtml);
    }

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void cmsListTest(){
        List<CmsPage> list = cmsPageRepository.findAll();
        System.out.println(list);
    }

    @Test
    public void cmsPageTest(){
        int pageSize = 0;
        int pageNum = 10;
        Pageable pageable = (Pageable) PageRequest.of(pageSize,pageNum);
        Page<CmsPage> pages = cmsPageRepository.findAll((org.springframework.data.domain.Pageable) pageable);
        System.out.println(pages.getTotalPages());
        System.out.println("=====================");
        System.out.println(pages);
    }

    //测试增加
    @Test
    public void savePage(){
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName("测试页面");
        cmsPage.setDataUrl("00001");
        cmsPage.setPageAliase("testPage");
        cmsPage.setPageCreateTime(new Date());
        cmsPageRepository.save(cmsPage);
    }

    //测试删除
    @Test
    public void deletePage(){
        cmsPageRepository.deleteById("5c0a32e6411435ad6bdec9b2");
    }

    //测试修改
    @Test
    public void updatePage(){
        Optional<CmsPage> optional = cmsPageRepository.findById("5c0a351b411435b714f5abb8");
        //java8中新增的类，用于做非空校验，避免出现空指针异常
        if(optional.isPresent()){
            CmsPage cmsPage = optional.get();
            cmsPage.setPageName("页面测试");
            cmsPageRepository.save(cmsPage);
        }
    }

    //查询测试
//    @Test
//     public void findById(){
//        int page = 0;
//        int size = 10;
//        Pageable pageable = PageRequest.of(page,size);
//        Page<CmsPage> cmsPages = cmsPageRepository.findBySiteIdAndPageName("5a751fab6abb5044e0d19ea1", "课程详情页面", pageable);
//        int num = cmsPages.getSize();
//        System.out.println(num);
//     }

    //测试条件查询
    @Test
    public void findAllTest(){
        //分页查询
        int page = 0;
        int size = 1;
        PageRequest pageable = PageRequest.of(page, size);

        //条件匹配，构建条件
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageAliase("课程");
        //构建匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        //构建匹配器的匹配器条件，(想要模糊查询的字段，匹配规则)
        //ExampleMatcher.GenericPropertyMatchers.contains():代表包含，即模糊匹配
        //Exa
        exampleMatcher = exampleMatcher.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        //ExampleMatcher.GenericPropertyMatchers.endsWith():代表结尾匹配
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);
        //根据分页以及条件进行查询
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        int i = all.getTotalPages();
        System.out.println(i);
    }

    @Test
    public void test2(){
        ResponseEntity<Map> okHttp = restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f",
                Map.class);
        Map body = okHttp.getBody();
        System.out.println(body);
    }


    @Autowired
    GridFsTemplate gridFsTemplate;

    @Test
    public void GridFsTest() throws FileNotFoundException {
        //选择要存储的文件
        File file = new File("/Users/yunxingli/Desktop/index_banner.ftl");
        InputStream inputStream = new FileInputStream(file);
        //存储文件并起名称
        ObjectId objectId = gridFsTemplate.store(inputStream, "index_banner");
        String id = objectId.toString();
        System.out.println(id);
    }

    @Autowired
    GridFSBucket gridFSBucket;

    @Test
    public void queryFile() throws IOException {
        String id = "5c1b8fac72884e389ae3df82";
        //根据id查找文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        //打开下载流对象
        GridFSDownloadStream gridFS = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建gridFsSource，用于获取流对象
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFS);
        //获取流中的数据
        String string = IOUtils.toString(gridFsResource.getInputStream(), "UTF-8");
        System.out.println(string);


    }
}