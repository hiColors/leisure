package com.github.hicolors.leisure.common.model.expression;

import com.github.hicolors.leisure.common.utils.ReflectionUtils;
import lombok.NoArgsConstructor;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 条件表达式
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2019/09/11
 */
@NoArgsConstructor
public class ColorsExpression {

    /**
     * 分隔符
     */
    private static final String OR_SEPARATOR = "_OR_";

    /**
     * 名称
     */
    private String[] propertyNames;
    /**
     * 类型
     */
    private Class<?> propertyType;
    /**
     * 值
     */
    private Object propertyValue;
    /**
     * 条件类型
     */
    private MatchType matchType;
    /**
     * 完整表达式
     */
    private String filterName;

    public ColorsExpression(String filterName) {
        this.filterName = filterName;
        String matchTypeCode = StringUtils.substringBefore(filterName, "_");
        this.matchType = MatchType.get(matchTypeCode);
        if (Objects.isNull(this.matchType)) {
            throw new ExpressionException(String.format("filter 名称 %s 没有按规则编写,无法得到属性比较类型。", filterName));
        }
        if (!(MatchType.NULL.equals(this.matchType)
                || MatchType.NOTNULL.equals(this.matchType)
                || MatchType.EMPTY.equals(this.matchType)
                || MatchType.NOTEMPTY.equals(this.matchType))) {
            throw new ExpressionException("没有设置 value 时,查询条件必须为 is null,not null,empty,not empty。");
        }
        String propertyNameStr = StringUtils.substringAfter(filterName, "_");
        this.propertyNames = propertyNameStr.split(OR_SEPARATOR);
        this.propertyValue = new Object();
    }

    public ColorsExpression(String filterName, Enum<?> value) {
        this.initialize(filterName);
        this.propertyValue = value;
    }

    public ColorsExpression(String filterName, Enum<?>... value) {
        this.initialize(filterName);
        if (!(MatchType.IN.equals(this.matchType) || MatchType.NOTIN.equals(this.matchType))) {
            throw new ExpressionException("有多个条件时,查询条件必须为 in 或者 not in 。");
        }
        this.propertyValue = value;
    }

    public ColorsExpression(String filterName, String value) {
        this.initialize(filterName);
        this.setPropertyValue(value);
    }

    @SafeVarargs
    public <T> ColorsExpression(String filterName, T... value) {
        this.initialize(filterName);
        //值参数个数大于1个时，条件必须为 IN 或者 NOTIN
        boolean lengthGtOne = value.length > 1;
        boolean expressionInOrNotin = MatchType.IN.equals(this.matchType) || MatchType.NOTIN.equals(this.matchType);
        if (!(expressionInOrNotin) && lengthGtOne) {
            throw new ExpressionException("有多个值参数时,查询条件必须为 in 或者 not in 。");
        }
        if (MatchType.IN.equals(this.matchType) || MatchType.NOTIN.equals(this.matchType)) {
            Object array = this.propertyType.isAssignableFrom(Enum.class) ? new String[value.length] : Array.newInstance(this.propertyType, Array.getLength(value));
            for (int i = 0; i < Array.getLength(value); i++) {
                Array.set(array, i, this.propertyType == Enum.class ? Array.get(value, i).toString() : BeanUtilsBean.getInstance().getConvertUtils().convert(Array.get(value, i).toString(), this.propertyType));
            }
            this.propertyValue = array;
        } else {
            setPropertyValue(value[0].toString());
        }
    }

    private void setPropertyValue(String value) {
        if (MatchType.BETWEEN.equals(this.matchType)) {
            Object array = Array.newInstance(this.propertyType, 2);
            String[] tempArray = StringUtils.split(value, "~");
            for (int i = 0; i < tempArray.length; i++) {
                Array.set(array, i, BeanUtilsBean.getInstance().getConvertUtils().convert(tempArray[i], this.propertyType));
            }
            this.propertyValue = array;
        } else if (this.propertyType == Enum.class) {
            this.propertyValue = value;
        } else {
            this.propertyValue = BeanUtilsBean.getInstance().getConvertUtils().convert(value, this.propertyType);
        }
    }

    private void initialize(String filterName) {
        this.filterName = filterName;
        String matchTypeStr = StringUtils.substringBefore(filterName, "_");
        this.matchType = MatchType.get(matchTypeStr);
        if (Objects.isNull(this.matchType)) {
            throw new ExpressionException(String.format("filter 名称 %s 没有按规则编写,无法得到属性比较类型。", filterName));
        }
        this.propertyType = String.class;
        String propertyNameStr = StringUtils.substringAfter(filterName, "_");
        this.propertyNames = propertyNameStr.split(OR_SEPARATOR);
        if (this.propertyNames.length <= 0) {
            throw new ExpressionException(String.format("filter 名称 %s 没有按规则编写,无法得到属性比较类型。", filterName));
        }
    }

    public boolean isMultiProperty() {
        return this.propertyNames.length > 1;
    }

    public String[] getPropertyNames() {
        return this.propertyNames;
    }

    public ColorsExpression setPropertyNames(String[] propertyNames) {
        this.propertyNames = propertyNames;
        return this;
    }

    public String getPropertyName() {
        if (this.propertyNames.length > 1) {
            throw new ExpressionException("此处只允许有一个属性名称。");
        }
        return this.propertyNames[0];
    }

    public Object getPropertyValue() {
        return this.propertyValue;
    }

    public ColorsExpression setPropertyValue(Object propertyValue) {
        this.propertyValue = propertyValue;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T getPropertyValue(T o) {
        return (T) getPropertyValue(o.getClass());
    }

    @SuppressWarnings("unchecked")
    public <T> T getPropertyValue(Class<T> clazz) {
        boolean isEnum = clazz.isEnum();
        boolean isArrayAndcomponentIsEnum = clazz.isArray() && clazz.getComponentType().isEnum();
        if (isEnum || isArrayAndcomponentIsEnum) {
            AtomicReference<Class> enumClass = new AtomicReference<>(clazz.isArray() ? clazz.getComponentType() : clazz);
            if (propertyValue instanceof String) {
                return (T) Enum.valueOf(enumClass.get(), (String) propertyValue);
            } else if (propertyValue instanceof String[]) {
                Object array = Array.newInstance(enumClass.get(), Array.getLength(propertyValue));
                for (int i = 0; i < Array.getLength(propertyValue); i++) {
                    Array.set(array, i, Enum.valueOf(enumClass.get(), (String) Array.get(propertyValue, i)));
                }
                return clazz.cast(array);
            }
            return (T) propertyValue;
        }
        return ReflectionUtils.convert(this.getPropertyValue(), clazz);
    }

    public Class<?> getPropertyType() {
        return propertyType;
    }

    public ColorsExpression setPropertyType(Class<?> propertyType) {
        this.propertyType = propertyType;
        return this;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public ColorsExpression setMatchType(MatchType matchType) {
        this.matchType = matchType;
        return this;
    }

    public String getFilterName() {
        return filterName;
    }

    public ColorsExpression setFilterName(String filterName) {
        this.filterName = filterName;
        return this;
    }

}
