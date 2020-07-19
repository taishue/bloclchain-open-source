package com.coinsthai.module.oss;

import java.io.InputStream;

/**
 * @author 
 */
public interface OssService {

    /**
     * 上传文件
     *
     * @param bucket
     * @param key
     * @param content
     */
    void upload(String bucket, String key, InputStream content);

    /**
     * 下载文件
     *
     * @param bucket
     * @param key
     * @return
     */
    InputStream download(String bucket, String key);

    /**
     * 删除文件
     *
     * @param bucket
     * @param key
     */
    void delete(String bucket, String key);
}
