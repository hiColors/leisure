package com.github.life.lab.leisure.common.model.expression;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.life.lab.leisure.common.utils.ReflectionUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 条件表达式
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2019/09/11
 */
public class ColorsExpression {

    /**
     * 分隔符
     */
    @JsonIgnore
    private static final String OR_SEPARATOR = "_OR_";

    /**
     * 完整 name 表达式
     */
    private String filterName;

    /**
     * 完整 value 表达式
     */
    private String[] filterValues;

    /**
     * 属性名称
     */
    @JsonIgnore
    private String[] propertyNames;

    /**
     * 类型
     */
    @JsonIgnore
    private Class<?> propertyType;

    /**
     * 值
     */
    @JsonIgnore
    private Object propertyValue;

    /**
     * 条件类型
     */
    @JsonIgnore
    private MatchType matchType;

    public ColorsExpression() {
    }

    public ColorsExpression(String filterName, String[] filterValues) {
        initialize(filterName);
        this.setFilterValues(filterValues);
        if (this.getMatchType().isExistParams()) {
            withoutArg();
        } else if (this.getMatchType().isMultiParams()) {
            List<String> tValues = new ArrayList<>();
            assert filterValues != null;
            for (String val : filterValues) {
                tValues.addAll(Arrays.asList(StringUtils.split(val, ",; \t\n")));
            }
            withMoreTValues(this, tValues.toArray(new String[tValues.size()]));
        } else {
            if (filterValues.length != 0 && StringUtils.isNotBlank(filterValues[0])) {
                withString(this, filterValues[0]);
            }
        }
    }

    private void initialize(String filterName) {
        this.setFilterName(filterName);
        String matchTypeStr = StringUtils.substringBefore(filterName, "_");
        MatchType matchType = MatchType.get(matchTypeStr);
        if (Objects.isNull(matchType)) {
            throw new ExpressionException(String.format("filter 名称 %s 没有按规则编写,无法得到属性比较类型。", filterName));
        }
        this.setMatchType(matchType);
        this.setPropertyType(String.class);
        String propertyNameStr = StringUtils.substringAfter(filterName, "_");
        String[] propertyNames = propertyNameStr.split(OR_SEPARATOR);
        if (propertyNames.length <= 0) {
            throw new ExpressionException(String.format("filter 名称 %s 没有按规则编写,无法得到属性比较类型。", filterName));
        }
        this.setPropertyNames(propertyNames);
    }

    public void withoutArg() {
        MatchType matchType = this.getMatchType();
        if (Objects.isNull(matchType)) {
            throw new ExpressionException("ColorsExpression withoutArg 构造时出错！");
        }
        if (!(MatchType.NULL.equals(matchType) || MatchType.NOTNULL.equals(matchType) || MatchType.EMPTY.equals(matchType) || MatchType.NOTEMPTY.equals(matchType))) {
            throw new ExpressionException("没有设置 value 时,查询条件必须为 is null,not null,empty,not empty。");
        }
        this.setPropertyValue(new Object());
    }

    public void withEnum(Enum<?> value) {
        this.setPropertyValue(value);
    }

    public void withMoreEnums(ColorsExpression ce, Enum<?>... value) {
        MatchType matchType = ce.getMatchType();
        if (Objects.isNull(matchType)) {
            throw new ExpressionException("ColorsExpression withMoreEnums 构造时出错！");
        }
        if (!(MatchType.IN.equals(matchType) || MatchType.NOTIN.equals(matchType))) {
            throw new ExpressionException("有多个条件时,查询条件必须为 in 或者 not in 。");
        }
        ce.setPropertyValue(value);
    }

    public void withString(ColorsExpression ce, String value) {
        ce.setPropertyValue(value);
    }

    public <T> void withMoreTValues(T... value) {
        MatchType matchType = this.getMatchType();
        if (Objects.isNull(matchType)) {
            throw new ExpressionException("ColorsExpression withMoreTValues 构造时出错！");
        }
        //值参数个数大于1个时，条件必须为 IN 或者 NOTIN
        boolean lengthGtOne = value.length > 1;

        boolean expressionInOrNotin = MatchType.IN.equals(matchType) || MatchType.NOTIN.equals(matchType);

        if (!(expressionInOrNotin) && lengthGtOne) {
            throw new ExpressionException("有多个值参数时,查询条件必须为 in 或者 not in 。");
        }
        if (MatchType.IN.equals(matchType) || MatchType.NOTIN.equals(matchType)) {
            Object array = this.getPropertyType().isAssignableFrom(Enum.class) ? new String[value.length] : Array.newInstance(this.getPropertyType(), Array.getLength(value));
            for (int i = 0; i < Array.getLength(value); i++) {
                Array.set(array, i, this.getPropertyType() == Enum.class ? Array.get(value, i).toString() : BeanUtilsBean.getInstance().getConvertUtils().convert(Array.get(value, i).toString(), this.getPropertyType()));
            }
            this.setPropertyValue(array);
        } else {
            this.setPropertyValue(value[0].toString());
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


    public boolean hasMultiProperty() {
        return this.propertyNames.length > 1;
    }

    public String[] getPropertyNames() {
        return this.propertyNames;
    }

    public ColorsExpression setPropertyNames(String[] propertyNames) {
        this.propertyNames = propertyNames;
        return this;
    }


    @JsonIgnore
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

    public String[] getFilterValues() {
        return filterValues;
    }

    public ColorsExpression setFilterValues(String[] filterValues) {
        this.filterValues = filterValues;
        return this;
    }
}
