# AGENTS.md · zone-boot-template（后端模板）

> **本文件是子项目存根。开发（含启动任何服务）之前，必须先阅读总工作区总纲 [`../AGENTS.md`](../AGENTS.md)**，
> 遵守其中的环境说明、共享服务与端口规范；本存根与总纲冲突时，以总纲为准。

## 开发前检查（强制）

1. **阅读总纲** `../AGENTS.md`：第二节（环境）→ 第三节（共享服务）→ 第四节（端口规则）→ 第五节（项目总表）。
2. **环境检查**：

   ```bash
   java -version; mvn -v; python3 --version; node -v; pnpm -v
   mysql -uroot -prootroot -e "SELECT VERSION();"   # MySQL 8.4.5，root/rootroot
   redis-cli ping && docker ps                      # Redis 8.8.0 无密码；RocketMQ 等容器
   ```

3. **端口双重检查（仅当本项目存在监听端口的启动场景时执行；非服务型项目——控制台 / 共享库 / 纯移动端 / CLI / 研究 PoC，速览端口为「—/无端口」——跳过本步，日后新增监听产物再按此检查）**：
   - ① **实时占用**：逐个执行 `lsof -nP -iTCP:<端口> -sTCP:LISTEN`，有输出 = 被占用；
   - ② **登记预留**：对照总纲第五节总表，避开**其他子项目已登记**的端口（含推荐端口与推荐端口汇总）——`lsof` 此刻空闲 ≠ 可用，那是别人登记了还没启动的端口。
4. ①② 任一命中，或撞系统保留端口（3306/33060/6379/7890/9090/9200/9876/10909-10911/5432/11434 等）→
   按总纲 4.2 / 4.3 规则换到默认段下一个**通过双重检查**的空闲端口（前端 5200–5299 / 后端 8200–8299 / Metro 8082–8089 / 工具 4700–4799）。
5. **端口确定后立即回填**：更新总纲第五节总表 + 更新下方速览 + 总纲第十节变更记录。
   MySQL / Redis 为全局共享实例（3306 / 6379），本项目直接连接，用独立 database 名 / db index 隔离，**禁止另起实例占新端口**。
6. **需要大模型 API Key 时**：统一从工作区根 `.env` 读取（`SILICONFLOW_API_KEY` / `DEEPSEEK_API_KEY` / `ZHIPU_API_KEY` 等槽位，加载方式与变量清单见总纲第六节）；**禁止**把完整 key 复制进本项目 `.env`，若引入**新的模型厂商 / key**：先加根 `.env`（按 `<厂商>_API_KEY/_BASE_URL/_MODEL` 命名），开发完成后**必须同步登记总纲第六节（脱敏）**；引入新中间件（向量库、MQ 等）/ Docker 容器时，同步登记总纲第三节 + 第五节总表。
7. **编码约定（Java/JVM 项目）**：JSON 处理统一使用 **fastjson**（新依赖建议 fastjson2），**不使用 jackson**；存量项目已用 jackson 的保持现状、不强制迁移，详见总纲第九节「开发规范」。

## 项目速览（登记于 2026-11-11；端口权威来源：总纲第五节）

- **路径**：`zone-boot-template/`
- **类型 / 技术栈**：Java 21 · Spring Boot 3.3.5 多模块（模板）· Zone 中间件二方包 `zone-middleware-suite`
- **端口登记**：**8212** ✅（2026-11-13 迁移出 8080 冲突组；由本模板派生的新项目仍须按总纲 4.2 申请独立端口并回填总表）
- **中间件**：MySQL 3306 独立库 `zone_boot_template` · Redis 6379（共享实例；dev profile 用 db 1）
- **备注**：模板项目：由它派生的正式项目必须改端口并回填总纲；2026-11-21 更名自 shoan-boot-template（groupId `cn.shoanadmin`→`com.zone.boot`，见总纲 v1.55）；同日接入 `zone-middleware-suite`（**引入必须声明 `type=pom`**）：ZoneResult/ZoneBusinessException 替换自有 ApiResult/BusinessException、`zone.middleware.db.*` 替换 druid-starter、`ZoneHttpTemplate` 替换 RestTemplate、ZoneRedisTemplate/ZoneLog 就绪，接入手册见 `zone-middleware-support/docs/接入手册.md`（见总纲 v1.56）；同日晚二方包**去方言化**（总纲 v1.58）随之迁移：redis/db 连接与池参数、flyway 改用官方键 `spring.data.redis.*` / `spring.datasource.*` / `spring.flyway.*`（对照表见手册 §14），zone 键仅剩 mode/mybatis；布尔 `ssl` 键已删（Boot 3.1+ 为对象键 `ssl.enabled`，缺省即关）；2026-11-21 按 **DDD 分层规范化**：domain 框架无关（仓储/防腐接口在 domain、实现在 infrastructure，依赖倒置），service=应用层（禁依赖 infra）、web=适配层（禁依赖 infra），DTO 移至 service，DO/Mapper/ZoneHttpTemplate 归 infra，zone-boot-bootstrap（原 zone-boot-application，同日更名避让 DDD 应用层术语）=装配根显式聚合 infra；ArchUnit 六条守护规则在 `zone-boot-bootstrap` 测试域（违规 mvn test 即红，规范详见项目 README「分层规范」节）；2026-11-22 依二方包手册（785 行，2026-11-21 版）逐项对齐：自有 BaseReq 删除改继承二方包 `com.zone.middleware.common.request.BaseRequest`、新增 web 层 `TraceContextFilter`（ZoneTraceContext bind/clear + Zone-Trace-Id 头透传/生成/回写）、示例服务补 ZoneLog 门面示范、ArchUnit 追加 service 禁触 `com.zone.middleware.{http,redis,database}`、README/注释去方言化残留清零（redis/db 连接官方键描述、`spring.flyway.enabled`、`zone.middleware.security.sign.secret` 完整键名）；2026-11-22 二方包新增 `zone-middleware-web` 模块（上游采纳 P1 建议并打包）随之对齐：启用 `zone.middleware.web.{exception-advice-enabled,trace-filter-enabled}=true`，删除自有 `GlobalExceptionHandler` 与 `TraceContextFilter`（二方包等价自动装配接管，前者 @Order 最低可被上游 advice 覆盖）
