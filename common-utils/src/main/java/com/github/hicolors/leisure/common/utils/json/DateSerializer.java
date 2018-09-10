package com.github.hicolors.leisure.common.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.IOException;
import java.util.Date;

/**
 * 时间序列化类
 *
 * @author 李伟超
 * @date 2017/10/17
 */
public class DateSerializer extends JsonSerializer<Date> {

    private String dateFormat;

    public DateSerializer(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(StringUtils.isNotBlank(dateFormat) ?
                DateFormatUtils.format(value, dateFormat) :
                DateFormatUtils.format(value, "yyyy-MM-dd HH:mm:ss.SSS"));
    }

}