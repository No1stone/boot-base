package com.origemite.eventsaga.saga.controller;

import com.origemite.eventsaga.saga.dto.SagaReq;
import com.origemite.eventsaga.saga.dto.SagaRes;
import com.origemite.eventsaga.saga.facade.SagaPublishFacade;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sagas")
public class SagaRestController {

    private final SagaPublishFacade facade;

    public SagaRestController(SagaPublishFacade facade) {
        this.facade = facade;
    }

    /**
     * 자기 이벤트 Outbox 적재. COMPLETE(성공) 또는 FAIL+COMPENSATE*(보상 이벤트).
     */
    @PostMapping
    public SagaRes.Accepted accept(@Valid @RequestBody SagaReq.Ingress body) {
        return facade.accept(body);
    }
}
