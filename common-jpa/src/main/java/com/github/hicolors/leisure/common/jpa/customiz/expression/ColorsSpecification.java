package com.github.hicolors.leisure.common.jpa.customiz.expression;

import com.github.hicolors.leisure.common.model.expression.ColorsExpression;
import com.github.hicolors.leisure.common.model.expression.ExpressionException;
import com.github.hicolors.leisure.common.model.expression.MatchType;
import com.github.hicolors.leisure.common.utils.ClassUtils;
import com.github.hicolors.leisure.common.utils.ReflectionUtils;
import com.github.hicolors.leisure.common.utils.reflect.ObjectProperty;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 条件转换器
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/16
 */
public class ColorsSpecification implements Specification {

    private static final Integer ONE = 2;
    private static final Integer TWO = 2;

    private List<ColorsExpression> filters;

    public ColorsSpecification(List<ColorsExpression> filters) {
        this.filters = filters;
    }

    @SuppressWarnings("unchecked")
    private Object getPropertyValue(ColorsExpression filter, Root root) {
        if (filter.getPropertyType() == null) {
            return null;
        }
        //获取当前操作 bean 的 class 信息
        Class<?> entityClassTemp = root.getJavaType();
        //获取属性信息
        String[] propertyNames = filter.getPropertyName().split("\\.");
        try {
            entityClassTemp = Class.forName(entityClassTemp.getName());
            for (int i = 0; i < propertyNames.length - 1; i++) {
                String fieldName = propertyNames[i];
                ObjectProperty objectProperty = ClassUtils.getProperty(entityClassTemp, fieldName);
                if (Objects.nonNull(objectProperty)) {
                    entityClassTemp = objectProperty.getPropertyType();
                }
            }
            //通过反射 拿到 属性对应的 class 类型
            String fieldName = propertyNames[propertyNames.length - 1];
            ObjectProperty objectProperty = ClassUtils.getProperty(entityClassTemp, fieldName);
            //如果为空
            if (Objects.isNull(objectProperty)) {
                throw new ExpressionException(String.format("没有匹配的属性 [%s] 。", fieldName));
            }
            Class propertyType = objectProperty.getPropertyType();
            //如果是枚举
            if (propertyType.isEnum()) {
                return filter.getMatchType().isMultiParams() ? filter.getPropertyValue(Array.newInstance(propertyType, 0).getClass()) : filter.getPropertyValue(propertyType);
            }
            if (filter.getPropertyValue().getClass().isAssignableFrom(String[].class)) {
                String[] tempArray = (String[]) filter.getPropertyValue();
                Object array = Array.newInstance(propertyType, tempArray.length);
                for (int i = 0; i < tempArray.length; i++) {
                    Array.set(array, i, ReflectionUtils.convert(tempArray[i], propertyType));
                }
                return array;
            }
            return ReflectionUtils.convert(filter.getPropertyValue(), propertyType);
        } catch (Exception e) {
            throw new ExpressionException(e.getMessage(), e);
        }

    }


    @Override
    public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {

        List<Predicate> restrictions = new ArrayList<>();
        for (ColorsExpression filter : filters) {
            restrictions.add(buildPropertyFilterPredicate(root, cb, filter.getPropertyName(), getPropertyValue(filter, root), filter.getMatchType()));
        }
        return cb.and(restrictions.toArray(new Predicate[restrictions.size()]));
    }

    @SuppressWarnings("unchecked")
    protected Predicate buildPropertyFilterPredicate(Root root, CriteriaBuilder builder, String propertyName, Object propertyValue, MatchType matchType) {
        String[] properties = StringUtils.tokenizeToStringArray(propertyName, "\\.");
        if (properties.length > TWO || properties.length <= 0) {
            throw new ExpressionException(MessageFormat.format("propertyName[{0}]不正确！", "propertyName"));
        }
        if (MatchType.EQ.equals(matchType)) {
            return properties.length == TWO ? builder.equal(root.get(properties[0]).get(properties[1]), propertyValue) : builder.equal(root.get(propertyName), propertyValue);
        } else if (MatchType.LIKE.equals(matchType)) {
            return properties.length == TWO ? builder.like(root.get(properties[0]).get(properties[1]), (String) propertyValue) : builder.like(root.get(propertyName), (String) propertyValue);
        } else if (MatchType.LE.equals(matchType)) {
            return properties.length == TWO ? builder.le(root.get(properties[0]).get(properties[1]), (Number) propertyValue) : builder.le(root.get(propertyName), (Number) propertyValue);
        } else if (MatchType.LT.equals(matchType)) {
            return properties.length == TWO ? builder.lt(root.get(properties[0]).get(properties[1]), (Number) propertyValue) : builder.lt(root.get(propertyName), (Number) propertyValue);
        } else if (MatchType.GE.equals(matchType)) {
            return properties.length == TWO ? builder.ge(root.get(properties[0]).get(properties[1]), (Number) propertyValue) : builder.ge(root.get(propertyName), (Number) propertyValue);
        } else if (MatchType.GT.equals(matchType)) {
            return properties.length == TWO ? builder.gt(root.get(properties[0]).get(properties[1]), (Number) propertyValue) : builder.gt(root.get(propertyName), (Number) propertyValue);
        } else if (MatchType.NE.equals(matchType)) {
            return properties.length == TWO ? builder.notEqual(root.get(properties[0]).get(properties[1]), propertyValue) : builder.notEqual(root.get(propertyName), propertyValue);
        } else if (MatchType.NULL.equals(matchType)) {
            return properties.length == TWO ? builder.isNull(root.get(properties[0]).get(properties[1])) : builder.isNull(root.get(propertyName));
        } else if (MatchType.NOTNULL.equals(matchType)) {
            return properties.length == TWO ? builder.isNotNull(root.get(properties[0]).get(properties[1])) : builder.isNotNull(root.get(propertyName));
        } else if (MatchType.EMPTY.equals(matchType)) {
            return properties.length == TWO ? builder.isEmpty(root.get(properties[0]).get(properties[1])) : builder.isEmpty(root.get(propertyName));
        } else if (MatchType.NOTEMPTY.equals(matchType)) {
            return properties.length == TWO ? builder.isNotEmpty(root.get(properties[0]).get(properties[1])) : builder.isNotEmpty(root.get(propertyName));
        } else if (MatchType.IN.equals(matchType)) {
            if (Array.getLength(propertyValue) == 0) {
                return null;
            }
            if (properties.length == TWO) {
                return builder.and(root.get(properties[0]).get(properties[1]).in((Object[]) propertyValue));
            } else {
                return builder.and(root.get(propertyName).in((Object[]) propertyValue));
            }
        } else if (MatchType.NOTIN.equals(matchType)) {
            if (Array.getLength(propertyValue) == 0) {
                return null;
            }
            if (properties.length == TWO) {
                return builder.not(root.get(properties[0]).get(properties[1]).in((Object[]) propertyValue));
            } else {
                return builder.not(root.get(propertyName).in((Object[]) propertyValue));
            }
        } else if (MatchType.BETWEEN.equals(matchType)) {
            Comparable x = (Comparable) Array.get(propertyValue, 0);
            Comparable y = (Comparable) Array.get(propertyValue, 1);
            if (properties.length == TWO) {
                return builder.between(root.get(properties[0]).get(properties[1]), x, y);
            } else {
                return builder.between(root.get(propertyName), x, y);
            }
        }
        throw new ExpressionException("不支持的查询");
    }
}