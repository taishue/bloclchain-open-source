package com.coinsthai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.http.HttpService;

/**
 * @author YeYifeng
 */
@Configuration
public class Web3jConfig {

	@Value("${app.web3j.json-rpc-url}")
	private String JSON_RPC_URL;

	@Bean
	public Web3j web3j() {
		Web3j web3j = Web3j.build(new HttpService(JSON_RPC_URL));
		return web3j;
	}

	@Bean
	public Admin web3jAdmin() {
		Admin web3jAdmin = Admin.build(new HttpService(JSON_RPC_URL));
		return web3jAdmin;
	}
}
