## swagger 接入

### quick start

#### 1. 在 spring boot 项目中引入 common-framework 配置，不需要引入 swagger 相关 jar 包。

```
<dependency>
    <groupId>com.github.life-lab</groupId>
    <artifactId>common-framework</artifactId>
</dependency>

```

#### 2. 设置部分属性说明

> 以下配置为默认配置，如有自定义需求可在 apollo 中配置对应的值，会覆盖默认配置。

```
#文档 标题
swagger.title=leisure 服务端接口文档
#文档 说明
swagger.description=restful api 规范说明

# 状态开关，在 生产环境关闭 (默认为关闭）
swagger.status=true
swagger.version=1.0
```

#### 3. 个性化说明

> 因部分服务对 swagger 有定制需求，如 api 项目中 中需要在参数中传 header。

需要在 服务中注意一个 bean，即可完成此类需求。

```java
import springfox.documentation.service.Parameter;

@Bean
public Parameter parameter() {
    return new ParameterBuilder()
            .name(AuthConstants.ACCESS_TOKEN)
            .description("用户信息")
            .modelRef(new ModelRef("string"))
            .parameterType("header")
            .required(false)
            .build();
}
```