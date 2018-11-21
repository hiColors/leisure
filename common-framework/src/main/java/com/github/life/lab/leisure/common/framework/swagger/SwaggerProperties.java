package com.github.life.lab.leisure.common.framework.swagger;


import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * swagger 公共配置
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

    private Boolean status = true;

    private String title = "{0} 服务端接口文档 ";

    private String description = " restful api 规范说明";

    private String version = "1.0";

    public Boolean getStatus() {
        return status;
    }

    public SwaggerProperties setStatus(Boolean status) {
        this.status = status;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public SwaggerProperties setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public SwaggerProperties setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public SwaggerProperties setVersion(String version) {
        this.version = version;
        return this;
    }
}
