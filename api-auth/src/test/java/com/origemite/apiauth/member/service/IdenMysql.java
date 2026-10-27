package com.origemite.apiauth.member.service;


import com.origemite.apiauth.autoconfig.MysqlConfig;
import com.origemite.lib.common.util.TransformUtils;
import com.origemite.lib.model.auth.dto.MemberIdentificationRes;
import com.origemite.lib.model.auth.persistence.MemberIdentificationAdapter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Disabled("legacy manual IT — needs full context after Port/Adapter split")
@SpringBootTest(classes = MemberIdentificationAdapter.class)
@ActiveProfiles({"test", "test-db"})
@Import({MysqlConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IdenMysql {

    @Autowired
    MemberIdentificationAdapter memberIdentificationAdapter;

    @Test
    @DisplayName("MemberIdentification mysql test")
    public void idenTest() {
        MemberIdentificationRes.Item res = memberIdentificationAdapter.findById(1);
        System.out.println(TransformUtils.toString(res));
    }

}
