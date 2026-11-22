package com.zone.boot.domain.entity;

import com.zone.boot.domain.valueobject.GreetingMessage;

import java.util.Objects;

/**
 * 问候语实体（示例链路演示用）
 * 充血模型：模板渲染规则（占位符替换 + 不变式守卫）内聚在实体行为里，
 * 应用服务只编排不写规则；纯 POJO，不依赖任何框架
 *
 * @author ZoneBoot
 * @since 2026-11-21
 */
public class Greeting {

    private final String greetingId;

    /** 问候模板，必须包含 {name} 占位符 */
    private final String template;

    private Greeting(String greetingId, String template) {
        this.greetingId = Objects.requireNonNull(greetingId, "greetingId 不能为空");
        this.template = requireTemplate(template);
    }

    public static Greeting of(String greetingId, String template) {
        return new Greeting(greetingId, template);
    }

    public String getGreetingId() {
        return greetingId;
    }

    public String getTemplate() {
        return template;
    }

    /**
     * 用访客名渲染最终问候语（领域行为）
     *
     * @param visitorName 访客名
     * @return 渲染后的问候消息
     */
    public GreetingMessage renderFor(String visitorName) {
        if (visitorName == null || visitorName.isBlank()) {
            throw new IllegalArgumentException("visitorName 不能为空");
        }
        return new GreetingMessage(template.replace("{name}", visitorName.strip()));
    }

    private static String requireTemplate(String template) {
        if (template == null || template.isBlank() || !template.contains("{name}")) {
            throw new IllegalArgumentException("问候模板必须包含 {name} 占位符");
        }
        return template;
    }
}
