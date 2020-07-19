package com.coinsthai.vo;

import com.coinsthai.pojo.MarketPojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author
 */
@ApiModel(description = "市场")
public class MarketView extends MarketPojo {

    private String targetId;

    private String targetName;

    private String baseId;

    private String baseName;

    @ApiModelProperty(value = "目标币种ID")
    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    @ApiModelProperty(value = "目标币种名称")
    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    @ApiModelProperty(value = "基准币种ID")
    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }

    @ApiModelProperty(value = "基准币种名称")
    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    @ApiModelProperty
    @Override
    public String getId() {
        return super.getId();
    }

    @ApiModelProperty(value = "名称")
    @Override
    public String getName() {
        return super.getName();
    }

    @ApiModelProperty(value = "创建时间")
    @Override
    public Date getCreatedAt() {
        return super.getCreatedAt();
    }

    @ApiModelProperty(value = "最后修改时间")
    @Override
    public Date getModifiedAt() {
        return super.getModifiedAt();
    }

    @ApiModelProperty(value = "是否启用")
    @Override
    public boolean isActive() {
        return super.isActive();
    }
}
