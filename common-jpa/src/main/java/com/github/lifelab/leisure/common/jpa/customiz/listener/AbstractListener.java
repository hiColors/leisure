package com.github.lifelab.leisure.common.jpa.customiz.listener;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

/**
 * 监听器基类  主要是注册到监听器注册器中
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/11/2
 */
public abstract class AbstractListener {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void postConstruct() {
        for (EventType type : EventType.values()) {
            Class<?> listenerInterface = type.baseListenerInterface();
            if (listenerInterface.isAssignableFrom(this.getClass())) {
                SessionFactoryImplementor sessionFactory = entityManagerFactory.unwrap(SessionFactoryImplementor.class);
                EventListenerRegistry eventListenerRegistry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
                eventListenerRegistry.appendListeners(type, this);
            }
        }
    }
}
