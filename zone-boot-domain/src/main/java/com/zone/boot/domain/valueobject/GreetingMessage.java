package com.zone.boot.domain.valueobject;

import java.util.Objects;

/**
 * 问候消息值对象（示例链路演示用）
 * 不可变、按值判等；渲染完成后的最终问候语以此承载
 *
 * @param value 消息文本
 */
public record GreetingMessage(String value) {

    public GreetingMessage {
        Objects.requireNonNull(value, "问候消息不能为空");
    }
}
