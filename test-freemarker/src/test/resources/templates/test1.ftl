<!DOCTYPE html>
<html>
<head>
   <meta charset="utf-8">
   <title>Hello World!</title>
</head>
    <body>
    Hello ${name}!
    <br/>
    使用list指令
    <br/>
    size:${stus?size}
    <table>
        <tr>
            <td>序号</td>
            <td>姓名</td>
            <td>年龄</td>
            <td>生日</td>
            <td>钱包</td>
        </tr>
        <#if suts??>
            <#list stus as stu>
            <tr>
                <td>${stu_index+1}</td>
                <td>${stu.name!''}</td>
                <td>${stu.age}</td>
                <td>生日</td>
                <td>${stu.money}</td>
                显示年月日：<td>${stu.birthday?date}</td>
                显示时分秒：<td>${stu.birthday?time}</td>
                显示年月日时分秒：<td>${stu.birthday?datetime}</td>
                指定格式日期：<td>${stu.birthday?string("YYYY年MM月DD日")}</td>
            </tr>
            </#list>
        </#if>
    </table>
    <br/>
    获取map列表中的数据
    1。通过[]来获取数据<br/>
    姓名：${stuMap['stu1'].name}<brs/>
    年龄：${(stuMap['stu1'].age)!''}<br/>
    <br/>
    2.通过点来获取数据<br/>
    姓名：${stuMap.stu1.name}<br/>
    年龄：${(stuMap.stu1.age)!''}
    <br/>
    3.遍历Map
    <table>
        <tr>
            <td>序号</td>
            <td>姓名</td>
            <td>年龄</td>
            <td>钱包</td>
        </tr>
        <#list stuMap?keys as key>
            <tr>
                <td>${key_index+1}</td>
                <td <#if (stuMap[key].age>17)> bgcolor="blue" </#if>>${stuMap[key].name}</td>
                <td>${(stuMap[key].age)!''}</td>
                <td>${stuMap[key].money}</td>
            </tr>
        </#list>
    </table>

    <br/>
    日期格式化<br/>
    <table>
        <tr>

        </tr>
    </table>
</body>
</html>