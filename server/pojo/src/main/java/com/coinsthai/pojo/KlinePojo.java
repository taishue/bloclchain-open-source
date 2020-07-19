package com.coinsthai.pojo;

import com.coinsthai.pojo.common.ModifiedAtPojo;
import com.coinsthai.pojo.intenum.KlineType;

/**
 * @author
 */
public class KlinePojo extends ModifiedAtPojo {

    private KlineType type;

    /**
     * 开始的时间戳
     */
    private long timestamp;

    private long first;

    private long last;

    private long high;

    private long low;

    private long volume;

    public KlineType getType() {
        return type;
    }

    public void setType(KlineType type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getFirst() {
        return first;
    }

    public void setFirst(long first) {
        this.first = first;
    }

    public long getLast() {
        return last;
    }

    public void setLast(long last) {
        this.last = last;
    }

    public long getHigh() {
        return high;
    }

    public void setHigh(long high) {
        this.high = high;
    }

    public long getLow() {
        return low;
    }

    public void setLow(long low) {
        this.low = low;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }
}
