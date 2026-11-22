package com.zone.boot.service;

import com.zone.boot.domain.entity.Greeting;
import com.zone.boot.domain.repository.GreetingRepository;
import com.zone.boot.domain.valueobject.GreetingMessage;
import com.zone.boot.service.assembler.HelloAssembler;
import com.zone.boot.service.request.HelloReq;
import com.zone.boot.service.response.HelloRes;
import com.zone.middleware.common.error.BusinessError;
import com.zone.middleware.common.exception.ZoneBusinessException;
import com.zone.middleware.log.ZoneLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 问候应用服务（示例链路演示用）
 * 用例编排三步：查仓储接口 → 调实体领域行为 → 装配出参；事务边界在此层
 * 只依赖 domain 接口（GreetingRepository），实现由 bootstrap 装配注入
 *
 * @author ZoneBoot
 * @since 2026-11-21
 */
@Service
@RequiredArgsConstructor
public class HelloAppService {

    private final GreetingRepository greetingRepository;
    private final HelloAssembler helloAssembler;

    /**
     * 渲染问候语用例
     */
    @Transactional(readOnly = true)
    public HelloRes greet(HelloReq request) {
        ZoneLog.info("HelloAppService", "greet", "问候用例开始：visitorName={}", request.getVisitorName());
        Greeting greeting = greetingRepository.findDefault()
                .orElseThrow(() -> new ZoneBusinessException(BusinessError.DATA_NOT_FOUND, "问候模板不存在"));
        GreetingMessage message = greeting.renderFor(request.getVisitorName());
        HelloRes result = helloAssembler.toRes(greeting, message);
        ZoneLog.info("HelloAppService", "greet", "问候用例完成：greetingId={}, message={}", greeting.getGreetingId(), message.value());
        return result;
    }
}
