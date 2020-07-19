package com.coinsthai.module.oss;

import com.coinsthai.model.Attachment;

import java.io.InputStream;

/**
 * @author 
 */
public interface CloudFileService {

    Attachment save(AttachmentRequest request);

    InputStream get(String key);
}
