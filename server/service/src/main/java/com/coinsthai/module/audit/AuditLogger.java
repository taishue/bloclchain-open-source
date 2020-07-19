package com.coinsthai.module.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 
 */
@Service
public class AuditLogger {

    @Autowired
    private ActorResolver actorResolver;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditLogger.class);

//    public Logger logger() {
//        return LOGGER;
//    }

    /**
     * 记录成功日志
     *
     * @param action     操作类型
     * @param type       被操作的对象类型
     * @param identifier 被操作对象的标识
     */
    public void success(final String action, final String type, final String identifier) {
        LOGGER.info(new Audit(actorResolver.resolve(), action, type, identifier, "successfully").getMessage());
    }

    /**
     * 记录失败日志并抛出异常
     *
     * @param action     操作类型
     * @param type       被操作的对象类型
     * @param identifier 被操作对象的标识
     * @param ex         异常
     */
    public void fail(final String action, final String type, final String identifier,
                     final RuntimeException ex) {
        LOGGER.error(new Audit(actorResolver.resolve(), action, type, identifier, "failed").getMessage(), ex);
        if (ex != null) {
            throw ex;
        }
    }

    /**
     * 记录失败日志
     *
     * @param action     操作类型
     * @param type       被操作的对象类型
     * @param identifier 被操作对象的标识
     */
    public void fail(final String action, final String type, final String identifier) {
        fail(action, type, identifier, null);
    }

}
