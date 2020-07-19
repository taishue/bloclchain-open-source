package com.coinsthai.bitcoin.rpc;

import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient;
import wf.bitcoin.javabitcoindrpcclient.GenericRpcException;

import java.util.List;

/**
 * @author 
 */
public interface BitcoinRpcClient extends BitcoindRpcClient {

    public List<Transaction> listTransactions(String account, int count, int from, boolean includeWatchOnly)
            throws GenericRpcException;
}
