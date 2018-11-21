package com.github.life.lab.leisure.common.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.github.life.lab.leisure.common.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.rmi.UnexpectedException;
import java.text.MessageFormat;
import java.util.Date;

/**
 * 日期反序列化类
 *
 * @author 李伟超
 * @date 2017/10/17
 */
public class DateDeserializer extends JsonDeserializer<Date> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateDeserializer.class);

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t.equals(JsonToken.VALUE_STRING)) {
            String value = jp.getText().trim();
            if (StringUtils.isBlank(value)) {
                return null;
            }
            try {
                return ReflectionUtils.convert(value, Date.class);
            } catch (Exception e) {
                LOGGER.error(MessageFormat.format("{0} : 不能转换日期格式!", value), e);
                throw new RuntimeException(e.getMessage());
            }
        }
        throw new UnexpectedException(
                MessageFormat.format("JsonToken = [{0}],是不能处理的类型!", t));
    }

}
