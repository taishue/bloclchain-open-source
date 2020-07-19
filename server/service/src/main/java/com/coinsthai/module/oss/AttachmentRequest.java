package com.coinsthai.module.oss;

import com.coinsthai.model.User;
import com.coinsthai.pojo.common.BasePojo;

import java.io.InputStream;

/**
 * @author
 */
public class AttachmentRequest extends BasePojo {

    private String contentKey;

    private int contentLength;

    private String contentType;

    private User user;

    InputStream inputStream;

    public String getContentKey() {
        return contentKey;
    }

    public void setContentKey(String contentKey) {
        this.contentKey = contentKey;
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

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
