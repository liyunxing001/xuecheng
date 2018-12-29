package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * @Auther: 星仔
 * @Date: 2018/12/15 22:32
 * @Description:
 */
public class CastException {

    public static void cast(ResultCode resultCode){
        throw new CustomException(resultCode);
    }
}