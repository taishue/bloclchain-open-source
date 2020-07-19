package com.coinsthai.security;

import in.clouthink.daas.security.token.spi.DigestMetadataProvider;

/**
 * Created by
 */
public class UserDigestMetadataProvider implements
                                        DigestMetadataProvider<SecurityUser> {
                                        
    @Override
    public String getDigestAlgorithm(SecurityUser user) {
        return "MD5";
    }
    
    @Override
    public String getSalt(SecurityUser user) {
        return user.getPasswordSalt();
    }
}
