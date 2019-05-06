# leisure 项目介绍


## 版本升级日志

### 0.0.12.RELEASE

- access 日志记录
- json 处理增强
- 预期非预期异常处理
- 非预期异常钉钉预警
- metrics 封装
- feign 编码解码器
- jpa 封装
- 工具类封装
- ...

## quick start

[ swagger quick start](https://github.com/life-lab/leisure/blob/master/docs/qs-swagger.md)


[ 钉钉预警 quick start](https://github.com/life-lab/leisure/blob/master/docs/qs-ding-talk.md)


[ log quick start](https://github.com/life-lab/leisure/blob/master/docs/qs-log.md)





### 异常码说明

> 异常码说明是由 8位 数字组成，前三位系统标识（从100开始），中间两位是模块标识（业务划分），后三位是异常标识（特定异常）


| 系统 | 3位系统标识 | 2位模块标识 | 3位异常标识 |
| :--------: | :--------:| :-------- | :--------  |
| leisure | 100 |  00：公共   | 
| leisure | 100 |  01：common-exception   | 
| leisure | 100 |  02：common-model   | 
| leisure | 100 |  03：common-utils   | 
| leisure | 100 |  04：common-metrics   | 
| leisure | 100 |  05：common-framework   | 
| leisure | 100 |  06：common-jpa   | 
| leisure | 100 |  07：common-feign  | 


