package com.origemite.apiauth.member.service;


import com.origemite.apiauth.autoconfig.MysqlConfig;
import com.origemite.lib.common.util.TransformUtils;
import com.origemite.lib.model.auth.dto.MemberIdentificationRes;
import com.origemite.lib.model.auth.service.MemberIdentificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = MemberIdentificationService.class)
@ActiveProfiles({"test", "test-db"})
@Import({MysqlConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IdenMysql {

    @Autowired
    MemberIdentificationService memberIdentificationService;

    @Test
    @DisplayName("MemberIdentification mysql test")
    public void idenTest() {
        MemberIdentificationRes.Item res = memberIdentificationService.findById(1);
        System.out.println(TransformUtils.toString(res));
    }

}
