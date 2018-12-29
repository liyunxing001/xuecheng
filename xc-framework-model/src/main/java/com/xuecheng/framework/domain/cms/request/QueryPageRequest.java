package com.xuecheng.framework.domain.cms.request;

import com.xuecheng.framework.model.request.RequestData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: 星仔
 * @Date: 2018/12/6 17:13
 * @Description:
 */
@Data
public class QueryPageRequest extends RequestData {
    //页面id
    @ApiModelProperty("页面id")
    private String pageId;
    //页面名称
    @ApiModelProperty("页面名称")
    private String pageName;
    //别名
    @ApiModelProperty("页面别名")
    private String pageAliase;
    //站点id
    @ApiModelProperty("站点id")
    private String siteId;
    //模板id
    @ApiModelProperty("模板id")
    private String templateId;
    //页面类型
    @ApiModelProperty("页面类型")
    private String pageType;
}