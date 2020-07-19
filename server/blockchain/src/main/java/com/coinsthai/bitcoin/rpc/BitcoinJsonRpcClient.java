package com.coinsthai.bitcoin.rpc;

import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;
import wf.bitcoin.javabitcoindrpcclient.GenericRpcException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author
 */
public class BitcoinJsonRpcClient extends BitcoinJSONRPCClient implements BitcoinRpcClient {

    public BitcoinJsonRpcClient(String rpcUrl) throws MalformedURLException {
        super(rpcUrl);
    }

    public BitcoinJsonRpcClient(URL rpc) {
        super(rpc);
    }

    public BitcoinJsonRpcClient(boolean testNet) {
        super(testNet);
    }

    public BitcoinJsonRpcClient() {
        super();
    }

    @Override
    public List<Transaction> listTransactions(String account, int count, int from,
                                              boolean includeWatchOnly) throws GenericRpcException {
        return new TransactionListMapWrapper((List) query("listtransactions",
                                                          account,
                                                          count,
                                                          from,
                                                          includeWatchOnly));
    }
}
