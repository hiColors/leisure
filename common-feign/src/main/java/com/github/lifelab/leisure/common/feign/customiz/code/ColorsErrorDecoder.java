package com.github.lifelab.leisure.common.feign.customiz.code;

import com.github.lifelab.leisure.common.exception.ExtensionException;
import com.github.lifelab.leisure.common.feign.exception.CommonFeignException;
import com.github.lifelab.leisure.common.feign.exception.EnumExceptionMessageFeign;
import com.github.lifelab.leisure.common.model.response.ErrorResponse;
import com.github.lifelab.leisure.common.utils.JsonUtils;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Colors Error Decoder
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/17
 */
public class ColorsErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        if (Objects.nonNull(response.body())) {
            try {
                String body = IOUtils.toString(response.body().asInputStream(), Charset.defaultCharset());
                ErrorResponse errorResponse = JsonUtils.deserialize(body, ErrorResponse.class);
                throw new ExtensionException(response.status(),
                        errorResponse.getCode(),
                        errorResponse.getMessage(),
                        null,
                        new RuntimeException(StringUtils.isNotBlank(errorResponse.getException()) ? errorResponse.getException() : "no exception message!"));
            } catch (IOException e) {
                return new RuntimeException(e.getMessage());
            }
        } else {
            return new CommonFeignException(EnumExceptionMessageFeign.REMOTE_INVOKE_ERROR);
        }
    }
}