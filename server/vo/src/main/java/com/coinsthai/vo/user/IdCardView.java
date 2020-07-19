package com.coinsthai.vo.user;

import com.coinsthai.pojo.IdCardPojo;
import com.coinsthai.pojo.intenum.Gender;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author
 */
@ApiModel(description = "身份证信息")
public class IdCardView extends IdCardPojo {

    private String userId;

    @ApiModelProperty("用户ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @ApiModelProperty("姓名")
    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getCardNumber() {
        return super.getCardNumber();
    }

    @ApiModelProperty("地址")
    @Override
    public String getAddress() {
        return super.getAddress();
    }

    @ApiModelProperty("生日")
    @Override
    public Date getBirthday() {
        return super.getBirthday();
    }

    @ApiModelProperty("性别")
    @Override
    public Gender getGender() {
        return super.getGender();
    }

    @ApiModelProperty("民族")
    @Override
    public String getRace() {
        return super.getRace();
    }

    @ApiModelProperty("发证机关")
    @Override
    public String getIssuedBy() {
        return super.getIssuedBy();
    }

    @ApiModelProperty("有效期-开始")
    @Override
    public Date getValidBeginAt() {
        return super.getValidBeginAt();
    }

    @ApiModelProperty("有效期-结束，空即表示长期")
    @Override
    public Date getValidEndAt() {
        return super.getValidEndAt();
    }
}
