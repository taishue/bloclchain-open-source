package com.coinsthai.service.impl;

import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

/**
 * @author 
 */
@Service
public class ModelFactory {

    @Autowired
    private EntityManager em;

    /**
     * 根据主键获得实体引用对象
     *
     * @param entityClass 实体类型
     * @param id          主键ID
     * @param <T>         实体类型
     * @return
     */
    public <T> T getReference(Class<T> entityClass, String id) {
        return em.getReference(entityClass, id);
    }

    /**
     * 获得真实对象，如果是代理对象则加载
     *
     * @param entity
     * @param <T>
     * @return
     */
    public <T> T loadEntity(T entity) {
        if (this instanceof HibernateProxy) {
            return (T) HibernateProxy.class.cast(entity).getHibernateLazyInitializer().getImplementation();
        }
        else {
            return entity;
        }
    }

    /**
     * 判断
     * @param entity
     * @return
     */
    public boolean isHibernateProxy(Object entity) {
        return entity instanceof HibernateProxy;
    }
}
