package com.xuecheng.test;

import com.xuecheng.test.freemarker.FreemarkerTestApplication;
import com.xuecheng.test.freemarker.model.Student;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.*;
import java.util.*;

/**
 * @Auther: 星仔
 * @Date: 2018/12/18 21:46
 * @Description:
 */
@SpringBootTest(classes = FreemarkerTestApplication.class)
@RunWith(SpringRunner.class)
public class FreemarkerTest {

    @Test
    public void test1() throws IOException, TemplateException {
        //创建配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //获取路径
        String path = this.getClass().getResource("/").getPath();
        //设置模板的目录
        configuration.setDirectoryForTemplateLoading(new File(path+"/templates/"));
        //设置字符集
        configuration.setDefaultEncoding("utf-8");
        //获取模板
        Template template = configuration.getTemplate("test1.ftl");
        Map map = getModel();
        //获取静态化之后的内容
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        InputStream inputStream = IOUtils.toInputStream(content);
        OutputStream outputStream = new FileOutputStream(new File("/Users/yunxingli/Desktop/test1.html"));
        IOUtils.copy(inputStream,outputStream);

    }

    //获取数据模型
    public Map getModel(){
        Map map = new HashMap();
        map.put("name","奥特曼");
        //向数据模型放数据 map.put("name","黑马程序员");
        Student stu1 = new Student();
        stu1.setName("小明");
        //stu1.setAge(18);
        stu1.setMoney(1000.86f);
        stu1.setBirthday(new Date());
        Student stu2 = new Student();
        stu2.setName("小红");
        stu2.setMoney(200.1f);
        stu2.setAge(19);
        stu2.setBirthday(new Date());
        List<Student> friends = new ArrayList<>();
        friends.add(stu1);
        stu2.setFriends(friends);
        stu2.setBestFriend(stu1);
        List<Student> stus = new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);
        //向数据模型放数据
        map.put("stus",stus);
        //准备map数据
        HashMap<String,Student> stuMap = new HashMap<>();
        stuMap.put("stu1",stu1);
        stuMap.put("stu2",stu2);
        //向数据模型放数据
        map.put("stu1",stu1);
        //向数据模型放数据
        map.put("stuMap",stuMap);
        return map;
    }
}