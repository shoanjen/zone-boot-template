# 多模块Spring Boot项目基础模板

一个基于 Spring Boot 3 的**DDD 分层**多模块后端模板，按「适配层 → 应用层 → 领域层 ← 基础设施层」组织，依赖倒置 + ArchUnit 自动守护。支持多环境配置（dev/test/prod），整合 **Zone 中间件二方包（zone-middleware-suite）**、MyBatis-Plus、Knife4j/SpringDoc、Actuator/Prometheus 等。

## 功能定位
**骨架模板**（原小程序登录示例业务已于 2026-11-21 移除），内置一条 **hello 示例链路**（见「示例链路」节）演示完整请求流——数据为 infrastructure mock，派生项目可整体删除后从零填充业务。

## 目录结构

```
zone-boot
├── pom.xml                          # 根 POM，聚合与依赖版本管理
├── zone-boot-common                 # 通用模块（工具类/常量，无方向工具箱）
├── zone-boot-domain                 # 【领域层】框架无关：实体/值对象/领域服务/事件/仓储接口/防腐接口
│   └── com.zone.boot.domain.{entity, valueobject, service, event, repository, gateway}
├── zone-boot-infrastructure         # 【基础设施层】技术实现：DO/Mapper/仓储实现/转换器/外部网关
│   ├── com.zone.boot.infrastructure.persistence.{dataobject, mapper, repository, converter}
│   ├── com.zone.boot.infrastructure.gateway
│   └── src/main/resources/mapper/   # Mapper XML
├── zone-boot-service                # 【应用层】用例编排：应用服务 + Req/Res DTO + 装配器
│   └── com.zone.boot.service.{request, response, assembler}
├── zone-boot-web                    # 【适配层】REST 接入：Controller/Interceptor/Web 配置（异常处理与 Trace Filter 由二方包 web 模块自动装配）
└── zone-boot-bootstrap              # 【装配层】唯一启动入口（组合根），打包为可执行 Jar；ArchUnit 守护测试
```

## 分层规范（DDD）

### 依赖方向

```
            ┌─────────────┐
            │   common    │（任何层可用；不依赖任何业务层）
            └─────────────┘
 web ──────► service ──────► domain ◄────── infrastructure
（适配层）   （应用层）      （领域层）      （基础设施层：实现领域接口）
                                ▲
                application（装配层/组合根：运行期注入实现，测试域 ArchUnit 守护）
```

模块名 ↔ DDD 术语映射：`zone-boot-service` = 应用层（Application Layer）；`zone-boot-bootstrap` = 装配启动层（Composition Root，原 `zone-boot-application`，更名以避让 DDD 应用层术语）。

### 各层职责与红线

| 模块 | DDD 角色 | 放什么 | 红线（ArchUnit 守护） |
|---|---|---|---|
| zone-boot-domain | 领域层 | 充血实体、值对象、领域服务、领域事件、**仓储接口**、**防腐接口** | 禁止 Spring / MyBatis-Plus / Servlet / zone.middleware 依赖；实体禁持久化注解 |
| zone-boot-infrastructure | 基础设施层 | DO（MP 注解只在此）、Mapper、仓储实现、DO↔实体转换器、外部网关实现（ZoneHttpTemplate） | 禁止反向依赖 web / service |
| zone-boot-service | 应用层 | 应用服务（用例编排 + 事务边界）、Req/Res DTO、DTO 装配器 | 禁止依赖 infrastructure / MyBatis-Plus（数据访问只经领域仓储接口）；返回 Res 不返回 ZoneResult |
| zone-boot-web | 适配层 | Controller、Interceptor、Web 配置（全局异常处理/Trace Filter 由二方包 `zone-middleware-web` 自动装配，模板不自有） | 禁止依赖 infrastructure / MyBatis-Plus；Controller 只注入应用服务，不写业务、不做 DTO 转换 |
| zone-boot-bootstrap | 装配层 | 启动类、多环境配置、显式聚合 infrastructure（依赖倒置的运行期装配）、`DddLayerRulesTest` | — |
| zone-boot-common | 通用层 | 工具类、常量 | 不依赖任何业务层 |

### 派生项目开发动线（照此填代码即合规）

1. **domain**：建实体（业务规则写成方法）→ 需要持久化时在 `repository` 定义接口；
2. **infrastructure**：建 DO（表映射）+ Mapper + 转换器 → `persistence.repository` 实现仓储接口；外部 API 在 `domain.gateway` 定义接口、`gateway` 包用 ZoneHttpTemplate 实现；
3. **service**：写应用服务（注入仓储/网关接口编排用例，事务加在这里），Req/Res DTO 放 `request`/`response`；
4. **web**：Controller 接 Req → 调应用服务 → `ZoneResult.ok(res)`。

### 分层守护

`zone-boot-bootstrap/src/test/.../arch/DddLayerRulesTest.java` 六条规则（领域框架无关、应用/适配层不触基础设施、基础设施不反向依赖、common 中立、依赖方向总纲）。派生项目违反任何一条，`mvn test` 直接红；调整规则须同步本节依赖方向图。

## 示例链路（hello + 订单查询 演示）

两条纵切示例完整走通「web → service → domain ← infrastructure（mock）」四层，数据为 mock（不触 MySQL）：

### 示例一：hello（最小链路）

| 层 | 文件 | 职责示范 |
|---|---|---|
| domain | `domain.entity.Greeting` | 充血实体：模板渲染规则（`renderFor` + 不变式守卫）在实体行为里；纯 POJO 零框架依赖 |
| domain | `domain.valueobject.GreetingMessage` | 值对象：不可变 record、按值判等 |
| domain | `domain.repository.GreetingRepository` | 仓储接口（依赖倒置锚点）：签名只出现领域对象 |
| service | `service.HelloAppService` | 应用服务：事务边界 + 编排（仓储接口→实体行为→装配）；只依赖 domain 接口 |
| service | `service.request.HelloReq`（extends 二方包 BaseRequest）/ `service.response.HelloRes` | 接口契约 DTO（校验注解在 Req；通用字段继承 zone-middleware-common 基类） |
| service | `service.assembler.HelloAssembler` | 领域对象 → Res 转换（DTO→VO 归应用层） |
| infrastructure | `infrastructure.persistence.repository.MockGreetingRepository` | 仓储接口实现：返回 mock 数据；接真实库时换 Mapper+Converter+本包实现 |
| web | `web.controller.HelloController` | 三件事：接参数（@Valid）→ 调应用服务 → ZoneResult.ok |

```
$ curl "http://localhost:8212/api/v1/hello?visitorName=Zone"
{"success":true,"code":"SUCCESS","data":{"greetingId":"greeting-001","greetingMessage":"Hello, Zone!（本条数据来自 infrastructure mock，未查询数据库）"},...}

$ curl "http://localhost:8212/api/v1/hello"        # 缺 visitorName → @NotBlank 触发（二方包 advice 转 BIZ-4001）
{"success":false,"code":"BIZ-4001","message":"visitorName 访客名不能为空","data":null}
```

### 示例二：订单查询（进阶：值对象/枚举行为/不变式/双用例裁剪）

| 层 | 文件 | 职责示范 |
|---|---|---|
| domain | `domain.valueobject.Money` | 金额值对象：分存 long 杜绝浮点误差 + `isGreaterThan` 比较行为 |
| domain | `domain.valueobject.OrderStatus` | 状态枚举：`canPay`/`isTerminal` 业务判断内聚枚举，不散落 if-else |
| domain | `domain.entity.Order` | 实体不变式（应付不超总额、状态/金额非空守卫在构造器）+ 领域行为 `canPay()` |
| domain | `domain.repository.OrderRepository` | 单查（Optional）+ 列表查（List）两种集合语义的仓储接口 |
| service | `service.OrderQueryAppService` | 查询侧双用例（detail/list）；写侧另建 Command 服务（读写职责分开） |
| service | `service.request.OrderQueryReq` / `OrderListReq` | 两个用例各自的入参 DTO |
| service | `service.response.OrderDetailRes` / `OrderSummaryRes` | 同一实体的全量视图 vs 列表瘦视图（用例裁剪） |
| service | `service.assembler.OrderAssembler` | 实体→两种 VO + 展示加工（金额/时间格式化） |
| infrastructure | `infrastructure.persistence.repository.MockOrderRepository` | 内存 Map mock 3 笔订单（含已支付/已取消态） |
| web | `web.controller.OrderController` | 两个 GET 端点，各自接 Req → 调用例 → 包 ZoneResult |

```
$ curl "http://localhost:8212/api/v1/orders/detail?orderId=ORDER-20261121-001"
{"success":true,"code":"SUCCESS","data":{"orderId":"ORDER-20261121-001","userId":"user-1001",
 "statusName":"CREATED","statusDescription":"已创建","totalAmountDisplay":"199.00",
 "payableAmountDisplay":"189.00","payable":true,"createdTimeDisplay":"2026-11-21 20:00:00"},...}

$ curl "http://localhost:8212/api/v1/orders/detail?orderId=NOT-EXIST"   # 不存在 → 业务异常
{"success":false,"code":"BIZ-4004","message":"订单不存在","data":null}

$ curl "http://localhost:8212/api/v1/orders/list?userId=user-1001"      # 列表瘦视图 ×2
{"success":true,"code":"SUCCESS","data":[{"orderId":"ORDER-20261121-001","statusName":"CREATED",
 "payableAmountDisplay":"189.00"},{"orderId":"ORDER-20261120-002","statusName":"PAID",
 "payableAmountDisplay":"59.00"}],...}

$ curl "http://localhost:8212/api/v1/orders/detail"                      # 缺 orderId → 校验（二方包 advice）
{"success":false,"code":"BIZ-4001","message":"orderId 订单号不能为空","data":null}
```

派生项目：删除两示例共 20 个文件（`Greeting*`、`Hello*`、`Money`、`OrderStatus`、`Order`、`OrderRepository`、`Order*Req/Res`、`OrderAssembler`、`OrderQueryAppService`、`MockGreetingRepository`、`MockOrderRepository`、`OrderController`）即可回到纯骨架。

## Zone 中间件二方包

工作区共享二方包 `com.zone.middleware:zone-middleware-suite`（源自 `zone-middleware-support` 项目），**接入手册见该项目 `docs/接入手册.md`**。本模板的接入约定：

| 能力 | 接入方式 |
|---|---|
| 统一返回体 / 错误码 / 业务异常 | `ZoneResult` / `BusinessError`·`SystemError` / `ZoneBusinessException`（自有 ApiResult/BusinessException 已删除） |
| 数据源 + MyBatis-Plus + Flyway | 连接/池/迁移用官方键 `spring.datasource.*` / `spring.flyway.*`；zone 键仅行为项 `zone.middleware.db.mode` + `zone.middleware.db.mybatis.*`；`mybatis-plus.*` 配置块继续生效 |
| Redis | `ZoneRedisTemplate`（fastjson2 JSON 存取）；连接用官方键 `spring.data.redis.*`，zone 键仅行为项 `zone.middleware.redis.mode`（LOCAL/CLOUD 预设） |
| HTTP 出站 | `ZoneHttpTemplate`（JDK HttpClient，重试/熔断可选）；依赖归 infrastructure（防腐实现专用） |
| 统一日志 | `ZoneLog.info/warn/error("类名", "方法名", "消息 {}", 参数...)`；链路上下文 `ZoneTraceContext.bind/clear`（入口绑定由二方包 Trace Filter 自动装配） |
| Web 入站支撑 | `zone-middleware-web`（手册 §10，默认关）：`exception-advice-enabled=true` 启用全局异常处理器（ZoneBusinessException 原码透传 / 参数校验转 BIZ-4001 / 兜底 SYS-9999，@Order 最低可被上游 advice 覆盖）；`trace-filter-enabled=true` 启用 Trace 入站 Filter（Zone-Trace-Id 头透传/生成、reqId、出口清理、响应头回写） |
| 接口签名鉴权 / 登录令牌 | suite 已含 security 模块；模板默认 `zone.middleware.security.sign.enabled=false`，启用需配 `zone.middleware.security.sign.secret` 并在端点标注 `@ZoneSignCheck` |

> ⚠️ **引入 suite 必须声明 `<type>pom</type>`**：suite 是 pom 聚合坐标，按手册 3.2 示例省略 type 会以默认 jar 类型解析失败（`Could not find artifact ...:jar:...`）。

> MySQL 驱动由上游自备（接入手册 7.1）：application 模块声明 `com.mysql:mysql-connector-j`（版本随 Boot 3.3.5 BOM）。

## 依赖与版本管理

- 根 POM 继承：`spring-boot-starter-parent:3.3.5`（所有 Spring Boot starter 版本由父 POM 统一管理）
- Zone 二方包版本：`zone-middleware.version=1.0.0-SNAPSHOT`（根 POM `dependencyManagement` 统一钉版）
- fastjson2：`2.0.53`（与二方包对齐）；MyBatis-Plus 注解：`3.5.12`（运行期版本随 zone-middleware-database 传递）
- 已移除的自管依赖：`druid-spring-boot-starter`、`spring-boot-starter-data-redis`、`mybatis-plus-boot-starter`、`mybatis-spring`、`mysql-connector-java`（旧坐标）、`redisson`

## 配置与环境

- 主配置：`zone-boot-bootstrap/src/main/resources/application.yml`
  - `spring.profiles.active: dev`（默认激活 dev）
  - 中间件官方键：`spring.datasource.*` / `spring.data.redis.*` / `spring.flyway.*`；zone 行为键：`zone.middleware.{db,redis}.mode`、`zone.middleware.db.mybatis.*`、`zone.middleware.log.*`、`zone.middleware.security.*`（签名鉴权，默认关闭）、`zone.middleware.http.*`
  - MyBatis-Plus、日志、Actuator/Prometheus、Knife4j 等。
- 环境扩展：
  - `application-dev.yml`：开发环境（独立 database `zone_boot_template`，Redis db 1）
  - `application-prod.yml`：生产环境（连接信息走环境变量 `DB_*`/`REDIS_*`，禁用 Knife4j）
  - `application-test.yml`：测试环境（H2 内存数据库，Redis db 15；启用需自行添加 `com.h2:h2` 依赖）

## 快速开始

### 前置要求
- JDK 21
- Maven 3.9+
- 本地仓库需先安装二方包：在 `zone-middleware-support` 项目执行 `mvn clean install -DskipTests`
- MySQL 与 Redis（dev 环境默认指向本机：`localhost:3306`、`localhost:6379`；建库脚本见下）

### 构建
```
mvn -DskipTests clean package
```

### 建库（dev 环境）
```
mysql -uroot -prootroot -e "CREATE DATABASE IF NOT EXISTS zone_boot_template DEFAULT CHARACTER SET utf8mb4"
```
建表脚本由派生项目自行管理：推荐开启 Flyway（`spring.flyway.enabled=true`，脚本放 `classpath:db/migration`；已有数据的库加 `spring.flyway.baseline-on-migrate=true`）。

### 运行（开发环境）
- 方式一：打包运行
```
java -jar zone-boot-bootstrap/target/zone-boot-bootstrap-*.jar
```
- 方式二：显式激活 profile
```
java -jar zone-boot-bootstrap/target/zone-boot-bootstrap-*.jar --spring.profiles.active=dev
```
- Knife4j 文档：
```
http://localhost:8212/doc.html
```

### 运行（IDE）
- 选择 `ZoneBootApplication` 主类运行。
- 确认 Working Directory 指向根项目或 application 模块。
- 如需显式激活：在 Program arguments 添加 `--spring.profiles.active=dev`。

### 验证中间件接入成功
启动日志应出现（缺哪个 = 对应模块未生效）：
```
zone-datasource init: {...}     # zone-middleware-database
zone-redis enabled: {...}       # zone-middleware-redis
zone-log enabled: {...}         # zone-middleware-log
```
