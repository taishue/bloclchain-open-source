package com.coinsthai.pojo;

import com.coinsthai.pojo.common.ModifiedAtPojo;

/**
 * @author
 */
public class AttachmentPojo extends ModifiedAtPojo {

    /**
     * 文件存储在阿里云上OSS的Key，格式如下：
     * <code>
     *     {用户ID}/[业务目录/]{UUID}-文件名.后缀
     * </code>
     * 如：9d6d1b45-d2bf-4f73-85d2-134c564f382a/kyc/1048e627bf7175ff9e20da69b07e861f-id_card.png
     */
    private String contentKey;

    private String bucket;

    private int contentLength;

    private String contentType;

    public String getContentKey() {
        return contentKey;
    }

    public void setContentKey(String contentKey) {
        this.contentKey = contentKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
