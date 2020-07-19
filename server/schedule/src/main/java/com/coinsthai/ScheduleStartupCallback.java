package com.coinsthai;

import com.coinsthai.btc.BtcService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author YeYifeng
 */
@Component
public class ScheduleStartupCallback implements InitializingBean {

	@Autowired
	BtcService btcService;

	@Override
	public void afterPropertiesSet() throws Exception {
		btcService.syncLatestBlock();
	}
}
