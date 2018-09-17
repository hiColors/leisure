package com.github.hicolors.leisure.common.feign.customiz;

import com.github.hicolors.leisure.common.exception.ExtensionException;
import com.github.hicolors.leisure.common.model.response.ErrorResponse;
import com.github.hicolors.leisure.common.utils.JsonUtils;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Colors Error Decoder
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/17
 */
public class ColorsErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        try {
            String body = IOUtils.toString(response.body().asInputStream(), Charset.defaultCharset());
            ErrorResponse errorResponse = JsonUtils.deserialize(body, ErrorResponse.class);
            throw new ExtensionException(response.status(),
                    errorResponse.getCode(),
                    errorResponse.getMessage(),
                    null,
                    new Exception(errorResponse.getException())
            );
        } catch (IOException e) {
            return new RuntimeException(e.getMessage());
        }
    }
}