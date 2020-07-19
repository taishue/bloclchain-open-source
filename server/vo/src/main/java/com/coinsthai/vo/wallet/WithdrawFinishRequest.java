package com.coinsthai.vo.wallet;

import com.coinsthai.pojo.common.BasePojo;
import com.coinsthai.pojo.common.StringIdentifier;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author 
 */
@ApiModel(description = "完成提现请求")
public class WithdrawFinishRequest extends StringIdentifier {

    private String txid;

    @ApiModelProperty(value = "区块链交易ID")
    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }
}
