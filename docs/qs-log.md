## access 日志

### quick start

#### 1. 在 spring boot 项目中引入 common-framework 配置，不需要引入其他相关 jar 包。

```
<dependency>
    <groupId>com.github.life-lab</groupId>
    <artifactId>common-framework</artifactId>
</dependency>


```

#### 2. 在已有的 logback 配置文件中加入 access 日志信息

```xml

    <appender name="fileAccess" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_PATH}/access/access.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <FileNamePattern>${LOG_PATH}/access/access.%d{yyyy-MM-dd}.log</FileNamePattern>
            <MaxHistory>100</MaxHistory>
        </rollingPolicy>
        <layout class="ch.qos.logback.classic.PatternLayout">
            <pattern>${LOG_PATTERN}</pattern>
        </layout>
    </appender>
    
    
     <appender name="asyncAccess" class="ch.qos.logback.classic.AsyncAppender">
        <discardingThreshold>0</discardingThreshold>
        <queueSize>1024</queueSize>
        <neverBlock>true</neverBlock>
        <appender-ref ref="fileAccess"/>
     </appender>
    
    <!-- request 日志
        additivity: 是否向上级logger传递打印信息。默认是true。
        同<logger>一样，可以包含零个或多个<appender-ref>元素，标识这个appender将会添加到这个logger。
    -->
    <logger name="com.github.lifelab.leisure.common.framework.logger" level="info" additivity="false">
        <appender-ref ref="asyncAccess"/>
    </logger>
        
```