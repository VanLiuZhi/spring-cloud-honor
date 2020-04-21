# Spring Cloud 能力开放平台

开放平台（Open Platform），开放平台是指软件系统通过公开其应用程序编程接口（API）
或函数（function)来使外部的程序可以增加该软件系统的功能或使用该软件系统的资源，而不需要更改该软件系统的源代码

## 寄语

```
when everybody says someone is a hero（当大家说某些人是英雄时）
no one really knows the truth about an idol（没人真正知道一个偶像的背后）
whose inside is pretty lonely n vulnerable （他的内心其实非常是孤独又脆弱）
wishing there‘ll be someone who do know（期盼着有谁能知道）
one time, he set himself a high goal （有次，他给自己设定了很高的目标）
he wants to be there as a role model （他希望成为他人的榜样）
ever since then life becomes a live show（从那时开始他的生活成为了一场现场直播）
real time show without any rehearsal （从来没有过排演的直播）
```

## 工程结构

``` 
SpringCloudHonor

├── auth-server -- 授权中心
├── api-gateway -- Spring Cloud 网关
├── custom-spring-boot-starter -- 自定义starter模块
├    ├── custom-db-spring-boot-starter -- starter 数据源
├    └── custom-log-spring-boot-starter -- starter 日志
├── register-center -- 注册中心
├    ├── consul-service -- consul 注册中心
├    └── eureka-service -- eureka 注册中心
├── oauth2-client -- oauth2
├── oauth2-jwt -- oauth2
├── oauth2-simple -- oauth2

```
