package com.coinsthai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by
 */
@ApiModel(description = "传统模式的响应结果")
public class TraditionalResult<D> {

    private boolean succeed;

    private String message;

    private String errorCode;

    private D data;

    @ApiModelProperty(value = "操作是否成功")
    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    @ApiModelProperty(value = "失败信息")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @ApiModelProperty(value = "错误代码")
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @ApiModelProperty(value = "响应数据")
    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }
}
