package com.coinsthai.bitcoin.rpc;

import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.coinsthai.bitcoin.rpc.MapWrapper.*;

/**
 * @author 
 */
public class TransactionListMapWrapper extends ListMapWrapper<BitcoindRpcClient.Transaction> {

    public TransactionListMapWrapper(List<Map> list) {
        super(list);
    }

    @Override
    protected BitcoindRpcClient.Transaction wrap(Map m) {
        return new BitcoindRpcClient.Transaction() {

            @Override
            public String account() {
                return mapStr(m, "account");
            }

            @Override
            public String address() {
                return mapStr(m, "address");
            }

            @Override
            public String category() {
                return mapStr(m, "category");
            }

            @Override
            public double amount() {
                return mapDouble(m, "amount");
            }

            @Override
            public double fee() {
                return mapDouble(m, "fee");
            }

            @Override
            public int confirmations() {
                return mapInt(m, "confirmations");
            }

            @Override
            public String blockHash() {
                return mapStr(m, "blockhash");
            }

            @Override
            public int blockIndex() {
                return mapInt(m, "blockindex");
            }

            @Override
            public Date blockTime() {
                return mapCTime(m, "blocktime");
            }

            @Override
            public String txId() {
                return mapStr(m, "txid");
            }

            @Override
            public Date time() {
                return mapCTime(m, "time");
            }

            @Override
            public Date timeReceived() {
                return mapCTime(m, "timereceived");
            }

            @Override
            public String comment() {
                return mapStr(m, "comment");
            }

            @Override
            public String commentTo() {
                return mapStr(m, "to");
            }

            private BitcoindRpcClient.RawTransaction raw = null;

            @Override
            public BitcoindRpcClient.RawTransaction raw() {
                return raw;
            }

            @Override
            public String toString() {
                return m.toString();
            }

        };
    }
}
