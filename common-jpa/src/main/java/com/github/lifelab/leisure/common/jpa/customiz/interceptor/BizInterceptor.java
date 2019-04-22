package com.github.lifelab.leisure.common.jpa.customiz.interceptor;

import com.github.lifelab.leisure.common.jpa.customiz.model.BaseJpaModel;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 业务默认 拦截器
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/6/3
 */
public class BizInterceptor extends EmptyInterceptor {

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }

    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (entity instanceof BaseJpaModel) {
            Long modifier = 0L;
            if (Objects.nonNull(((BaseJpaModel) entity).getModifier())) {
                modifier = ((BaseJpaModel) entity).getModifier();
            }
            int count = 0;
            for (int i = 0; i < propertyNames.length; ++i) {
                if ("modifier".equals(propertyNames[i])) {
                    state[i] = modifier;
                    ++count;
                } else if ("modifyTime".equals(propertyNames[i])) {
                    state[i] = new Date();
                    ++count;
                }
                if (count >= 2) {
                    return true;
                }
            }
        }
        return super.onLoad(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (entity instanceof BaseJpaModel) {
            //默认创建人
            Long creator = 1L;
            Date now = new Date();
            if (Objects.nonNull(((BaseJpaModel) entity).getCreator())) {
                creator = ((BaseJpaModel) entity).getCreator();
            }
            int count = 0;
            for (int i = 0; i < propertyNames.length; i++) {
                if ("creator".equals(propertyNames[i])) {
                    state[i] = creator;
                    count++;
                } else if ("createTime".equals(propertyNames[i])) {
                    state[i] = now;
                    count++;
                } else if ("modifier".equals(propertyNames[i])) {
                    state[i] = creator;
                    count++;
                } else if ("modifyTime".equals(propertyNames[i])) {
                    state[i] = now;
                    count++;
                } else if ("deleteFlag".equals(propertyNames[i])) {
                    state[i] = false;
                    count++;
                }
                if (count >= 5) {
                    return true;
                }
            }
        }
        return super.onSave(entity, id, state, propertyNames, types);
    }
}