package com.origemite.apiauth.member.service;


import com.origemite.apiauth.autoconfig.H2Config;
import com.origemite.lib.common.util.TransformUtils;
import com.origemite.lib.model.auth.dto.MemberIdentificationReq;
import com.origemite.lib.model.auth.dto.MemberIdentificationRes;
import com.origemite.lib.model.auth.service.MemberIdentificationService;
import com.origemite.lib.model.enums.common.EnMobileCarrierCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = MemberIdentificationService.class)
@ActiveProfiles({"test", "test-h2"})
@Import({H2Config.class})
public class IdenH2 {

    @Autowired
    MemberIdentificationService memberIdentificationService;

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
        MemberIdentificationRes.Id entity = memberIdentificationService.save(create);

        Assertions.assertThat(entity.getId()).isNotNull();
        MemberIdentificationRes.Item res = memberIdentificationService.findById(1);
        Assertions.assertThat(res.getId()).isNotNull();
        System.out.println(TransformUtils.toString(res));
    }

}
