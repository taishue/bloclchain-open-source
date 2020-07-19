package com.coinsthai.repository;

import com.coinsthai.model.Attachment;

/**
 * @author 
 */
public interface AttachmentRepository extends AbstractRepository<Attachment> {

    Attachment findByContentKey(String key);
}
