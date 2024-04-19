# 鸽子框架：以Spring Cloud微服务实现的后台框架

本项目构建了一个全面、高效的微服务基础框架。旨在为开发者提供一个稳定、灵活、易于扩展的微服务解决方案，以支持各种规模的企业级应用开发。

## 技术组件

- **微服务架构**：采用SpringBoot和SpringCloud，为微服务提供了一套轻量级的服务开发框架，简化了微服务的开发和部署。
- **数据持久化**：使用Mysql作为关系型数据库，结合MybatisPlus优化数据访问层代码，提高开发效率与性能。
- **高效缓存处理**：集成Redis，实现数据的快速缓存，大幅提升应用响应速度和吞吐量。
- **服务注册与发现**：通过Nacos实现服务的注册与发现机制，增强了服务间的通信效率和稳定性。
- **API网关**：利用Gateway作为微服务架构中的API网关，统一入口，简化了系统的内部结构。
- **服务间通信**：通过Feign实现声明式的REST客户端，简化了服务间的HTTP调用。
- **消息驱动**：集成RabbitMQ，提供了强大的异步通信能力，优化了服务间的解耦和通信效率。
- **分布式事务管理**：引入Seata处理分布式事务，确保跨服务调用的数据一致性和系统的稳定性。
- **安全与认证**：结合Spring Security和Jwt，实现了安全的用户认证和权限控制，保障了系统的安全性。

~~以上内容来自chatgpt，不过确实用了以上技术栈~~



## 框架特色

- [x] **集中配置**：将常用组件（如nacos、redis等）封装成starter进行引入，模块中无需单独进行配置
- [x] **逻辑删除**：框架整体不使用真实删除，业务sql无需手动判断`is_deleted`的值，拦截器自动进行拼接
- [x] **接口统一的返回结果**：controller层可以返回任意类型的结果，会被自动包装成`CommonResponse`对象
- [x] **用户中心**：单点登录、注册、校验token、分配角色权限等操作集中在用户中心、使用RBAC进行角色划分、鉴权集中在gateway中实现



## 快速开始

- 搭建并启动`nacos.2.3.1`服务，导入`/doc/nacos`中的配置，将**项目中**所有的**nacos的地址**进行正确的替换
- 搭建并启动`mysql.8.0.25`服务，将**nacos配置中**所有的**mysql的地址**及对应的**密码**进行正确的替换。创建数据库表，建表语句见`/doc/sql/scheme.sql`以`doc.sql.data.sql`
- 搭建并启动`redis`服务，将**项目及nacos配置中**所有的**redis的地址**及对应的**密码**进行正确的替换
- 搭建并启动`rabbitmq.3.8.34`服务，将**nacos配置中**所有的**rabbitmq的地址**及对应的**密码**进行正确的替换
- 搭建并启动`seata.2.0.0`服务，使用`mysql`进行事务控制，并成功注册到**nacos**中。创建额外的`seata`库，建表语句见`/doc/sql/seata.sql`
- 启动`pigeon-platform`及`pigeon-domain`下的服务，使用**postman**引入`/doc/postman/pigeon.postman_collection.json`



## 模块说明

存在的用户密码均为`qweqwe`

### pigeon-library

框架的基础建设，包含通用的接口与配置等

- pigeon-library-base-api
  - controller拦截器，拦截返回的类型包装成通用类型
  - controller切面，判断是否需要开启分页查询
  - 自动填充基础属性
  - mp插件，拼接sql进行逻辑删除判断
  - 真实删除
  - 自定义注解
    - 通过jwt中的用户信息，请求用户中心获取用户详细信息，保存到UserHolder中，并对token进行续签
    - 通过`@IsUnique`注解判断不可重复字段，这个注解应该放在实体类中
    - 通过`@NoControllerResponseAdvice`不自动包装Controller的返回结果
    - 通过`@SkipDeletedCheck`注解不自动为sql拼接逻辑删除判断
- pigeon-library-infrastructure
  - BaseConfig、BaseEntitiy等父类及枚举类
  - UserHolder，用来存储当前请求中的用户信息
  - 全局异常信息及捕获
  - 一些工具类



### pigeon-platform

框架的平台，提供给其他微服务模块进行交互，目前包含网关和用户中心

- pigeon-platform-gateway
  - gateway的全局异常捕获
  - 鉴权过滤器
- pigeon-platform-user-center
  - 通过spring security实现单点登录、注册、登出功能
  - 基于RBAC实现用户-角色-权限的资源分配
  - token
    - token以jwt的形式保存到redis中，TTL即token的过期时间
    - 调用除白名单（登录，注册，登出）内的接口时，需要在请求头中加入Authorization（Bearer xxx）
    - token中只保存用户的username和id，详细信息（如权限等）保存到redis中，TTL为3天，每次获取时判断TTL如果剩余时间小于一半则重置TTL。所以用户角色、权限、菜单信息变更时，请使用用户中心的相关接口，或者手动删除redis中的用户信息



### pigeon-starter

框架的插件，如果微服务需要该插件则进行引入

- pigeon-starter-feign
  - feign组件的starter模块，所有模块都必须引入
  - 过滤器
    - 判断请求是否来自feign，如果不是则从UserHolder中获取用户信息并保存到请求头中
    - 从RootContext中获取事务ID，保持通过feign调用的请求上下文xid一致
- pigeon-starter-nacos
  - nacos组件的starter模块，所有模块都必须引入
  - 没有进行特殊配置
- pigeon-starter-rabbitmq
  - rabbitmq组件的starter模块，需要使用消息队列的模块引入
  - 需要发送消息的可以使用`RabbitMQManager.send`发送消息
  - 需要监听消息的可以继承`MessageListener`并重写`receiveMessage`方法实现接收到消息后提交ACK之前需要做的事
  - 通过redis同步消息
    - 消息发送前保存到redis中，TTL为-1、成功发送后，TTL为7天
    - 消息接收后保存到redis中，TTL为-1、成功消费后，TTL为7天
- pigeon-starter-redis
  - redis组件的starter模块，所有模块都必须引入
  - `RedisManager`包装了一些`redisTemplate`的方法，提供相关接口
  - `RedisLockManager`包装了一些分布式锁的方法，提供相关接口
- pigeon-starter-seata
  - seata组件的starter模块，需要使用分布式事务的模块引入
  - 默认采用AT模式，需要seata服务及`undo_log`表
  - 没有进行特殊配置



### pigeon-domain

使用本框架的业务模块，目前提供的所有接口均为测试框架基础功能所用。

#### 菜单目录
- 1 root
  - 1000 后台
    - 1100 用户管理
      - 1110 用户增删改查

    - 1200 角色管理
      - 1210 角色增删改查
      - 1220 用户赋予角色

    - 1300 权限管理
      - 1310 权限增删改查
      - 1320 角色赋予权限

    - 1400 菜单管理
      - 1410 菜单增删改查
      - 1420 角色赋予菜单

    - 1500 仓库管理
      - 1510 仓库增删改查

    - 1600商品管理
      - 1610 商品增删改查

    - 1700 订单管理
      - 1710 订单增删改查

    - 1800 支付管理
      - 1810 支付增删改查

    - ...

  - 2000 普通
    - 2100 查看商品
    - 2200 管理个人信息
    - ...




