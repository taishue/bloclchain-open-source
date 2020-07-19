package com.coinsthai.module.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @author
 */
@Service
public class OssServiceImpl implements OssService, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(OssServiceImpl.class);

    private OSSClient client;

    @Autowired
    private AliyunOssConfig ossConfig;

    @Override
    public void upload(String bucket, String key, InputStream content) {
        PutObjectResult result = client.putObject(bucket, key, content);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(result.getETag());
        }
    }

    @Override
    public InputStream download(String bucket, String key) {
        OSSObject ossObject = client.getObject(bucket, key);
        return ossObject.getObjectContent();
    }

    @Override
    public void delete(String bucket, String key) {
        client.deleteObject(bucket, key);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(ossConfig.getAccessKey(),
                                                                                ossConfig.getSecretKey());
        client = new OSSClient(ossConfig.getEndpoint(), credentialsProvider, null);
    }
}
