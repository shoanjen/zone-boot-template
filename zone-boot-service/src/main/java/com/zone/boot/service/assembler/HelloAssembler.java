package com.zone.boot.service.assembler;

import com.zone.boot.domain.entity.Greeting;
import com.zone.boot.domain.valueobject.GreetingMessage;
import com.zone.boot.service.response.HelloRes;
import org.springframework.stereotype.Component;

/**
 * 问候装配器（示例链路演示用）
 * 领域对象 → 出参 Res 的转换收敛在此（无状态）；与 infrastructure 的 DO↔实体 Converter 严格分开
 */
@Component
public class HelloAssembler {

    /**
     * 领域问候语 + 渲染结果 → 出参 Res
     */
    public HelloRes toRes(Greeting greeting, GreetingMessage message) {
        return HelloRes.builder()
                .greetingId(greeting.getGreetingId())
                .greetingMessage(message.value())
                .build();
    }
}
