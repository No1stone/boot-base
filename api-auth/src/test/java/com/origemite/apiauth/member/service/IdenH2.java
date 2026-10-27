package com.origemite.apiauth.member.service;


import com.origemite.apiauth.autoconfig.H2Config;
import com.origemite.lib.common.util.TransformUtils;
import com.origemite.lib.model.auth.dto.MemberIdentificationReq;
import com.origemite.lib.model.auth.dto.MemberIdentificationRes;
import com.origemite.lib.model.auth.persistence.MemberIdentificationAdapter;
import com.origemite.lib.model.enums.common.EnMobileCarrierCode;
import org.assertj.core.api.Assertions;
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
@ActiveProfiles({"test", "test-h2"})
@Import({H2Config.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IdenH2 {

    @Autowired
    MemberIdentificationAdapter memberIdentificationAdapter;

    @Test
    @DisplayName("MemberIdentification h2 test")
    public void idenTest() {
        MemberIdentificationReq.Create create = new MemberIdentificationReq.Create();
        create.setName("장원석");
        create.setMobilePhoneNumber("010-0000-1234");
        create.setEmail("jangws1003@naver.com");
        create.setCi("ciasjdklasdjqkljasdlk");
        create.setBirthday("1989-10-03");
        create.setGender("M");
        create.setForeignYn("N");
        create.setMobileCarrierCode(EnMobileCarrierCode.SKT.getCode());
        create.setStatus("A");
        MemberIdentificationRes.Id entity = memberIdentificationAdapter.save(create);

        Assertions.assertThat(entity.getId()).isNotNull();
        MemberIdentificationRes.Item res = memberIdentificationAdapter.findById(1);
        Assertions.assertThat(res.getId()).isNotNull();
        System.out.println(TransformUtils.toString(res));
    }

}
