package com.coinsthai.util;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;

/**
 */
public class MultipartUtils {
    
    private MultipartUtils() {
    }
    
    public static MultipartFile getMultipartFile(HttpServletRequest request) {
        if (!(request instanceof MultipartHttpServletRequest)) {
            return null;
        }
        
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Iterator<String> it = multipartRequest.getFileNames();
        if (it == null || !it.hasNext()) {
            return null;
        }
        
        return multipartRequest.getFile(it.next());
    }
    
}
