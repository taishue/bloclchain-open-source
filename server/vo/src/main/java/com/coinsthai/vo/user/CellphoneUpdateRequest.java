package com.coinsthai.vo.user;

import com.coinsthai.pojo.common.StringIdentifier;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author
 */
@ApiModel(description = "修改手机号请求")
public class CellphoneUpdateRequest extends StringIdentifier {

    private String cellphone;

    @ApiModelProperty("用户ID")
    @Override
    public String getId() {
        return super.getId();
    }

    @ApiModelProperty("手机号码")
    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }
}
