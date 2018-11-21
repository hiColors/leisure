package com.github.life.lab.leisure.common.utils.json;

import com.fasterxml.jackson.annotation.JsonFilter;

import java.io.Serializable;

/**
 * JsonResultFilterSupport
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/13
 */
@JsonFilter("jsonResultFilterSupport")
public interface JsonResultFilterSupport extends Serializable {
}