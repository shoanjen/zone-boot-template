package com.zone.boot.bootstrap.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * DDD 分层守护规则
 *
 * <p>规则与 README「分层规范」一一对应；派生项目违反任何一条，mvn test 直接红。
 * 新增规则时同步更新 README 依赖方向图。
 */
class DddLayerRulesTest {

    private static JavaClasses classes;

    @BeforeAll
    static void importClasses() {
        classes = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("com.zone.boot");
    }

    /** 规则 1：领域层零框架依赖 —— 纯 POJO + 接口，不认识任何框架与中间件 */
    @Test
    void 领域层应为框架无关纯领域层() {
        noClasses().that().resideInAPackage("com.zone.boot.domain..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "org.springframework..",
                        "com.baomidou..",
                        "jakarta.servlet..",
                        "com.zone.middleware..")
                .because("领域层只描述业务本质；持久化/中间件实现一律下沉 infrastructure（依赖倒置）")
                .check(classes);
    }

    /** 规则 2：应用层不触基础设施 —— 数据访问只能经 domain 仓储接口；中间件能力包同理下沉 */
    @Test
    void 应用层禁止依赖基础设施与MyBatisPlus() {
        noClasses().that().resideInAPackage("com.zone.boot.service..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "com.zone.boot.infrastructure..",
                        "com.baomidou..",
                        "com.zone.middleware.http..",
                        "com.zone.middleware.redis..",
                        "com.zone.middleware.database..")
                .because("应用层依赖 infrastructure/中间件能力包会绕过仓储与防腐接口，破坏依赖倒置（由 bootstrap 装配根注入实现）")
                .check(classes);
    }

    /** 规则 3：适配层不触基础设施 —— Controller 只调应用服务 */
    @Test
    void 适配层禁止依赖基础设施与MyBatisPlus() {
        noClasses().that().resideInAPackage("com.zone.boot.web..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "com.zone.boot.infrastructure..",
                        "com.baomidou..")
                .because("接入层直连持久层 = 跨层访问；统一经 zone-boot-service 应用服务")
                .check(classes);
    }

    /** 规则 4：基础设施不反向依赖 —— 只实现领域接口，不认识上层 */
    @Test
    void 基础设施层禁止反向依赖应用层与适配层() {
        noClasses().that().resideInAPackage("com.zone.boot.infrastructure..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "com.zone.boot.web..",
                        "com.zone.boot.service..")
                .because("基础设施是领域层的实现细节，反向依赖会造成环（domain ← infra → service → domain）")
                .check(classes);
    }

    /** 规则 5：通用层保持中立 —— 任何层可用工具类，但 common 不得反向依赖任何业务层 */
    @Test
    void 通用层不得依赖任何业务层() {
        noClasses().that().resideInAPackage("com.zone.boot.common..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "com.zone.boot.web..",
                        "com.zone.boot.service..",
                        "com.zone.boot.domain..",
                        "com.zone.boot.infrastructure..")
                .because("common 是无方向工具箱；一旦反向依赖业务层，所有模块都被迫认识上层")
                .check(classes);
    }

    /** 规则 6：分层依赖方向总纲 —— web → service → domain ← infrastructure；装配根自由聚合各层 */
    @Test
    void 分层依赖方向应为_适配层到应用层到领域层_基础设施实现领域层() {
        layeredArchitecture().consideringAllDependencies()
                .layer("适配层").definedBy("com.zone.boot.web..")
                .layer("应用层").definedBy("com.zone.boot.service..")
                .layer("领域层").definedBy("com.zone.boot.domain..")
                .layer("基础设施层").definedBy("com.zone.boot.infrastructure..")
                .layer("装配根").definedBy("com.zone.boot.bootstrap..")

                .whereLayer("适配层").mayOnlyBeAccessedByLayers("装配根")
                .whereLayer("应用层").mayOnlyBeAccessedByLayers("适配层", "装配根")
                .whereLayer("领域层").mayOnlyBeAccessedByLayers("适配层", "应用层", "基础设施层", "装配根")
                .whereLayer("基础设施层").mayOnlyBeAccessedByLayers("装配根") // 实现由装配根运行期装配（组件扫描/显式 Bean）
                .whereLayer("装配根").mayNotBeAccessedByAnyLayer()
                .check(classes);
    }
}
