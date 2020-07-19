package com.coinsthai.btc;

import com.coinsthai.pojo.common.BasePojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author YeYifeng
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LatestBlock extends BasePojo {

	private int height;

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
