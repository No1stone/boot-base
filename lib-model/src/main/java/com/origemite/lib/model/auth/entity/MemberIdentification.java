package com.origemite.lib.model.auth.entity;

import com.origemite.lib.common.util.BeanUtils;
import com.origemite.lib.common.base.UpdatedEntity;
import com.origemite.lib.model.enums.common.EnMobileCarrierCode;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.DynamicInsert;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * me_member_identification Entity
 */
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "me_member_identification")
@DynamicInsert
@FieldNameConstants
public class MemberIdentification extends UpdatedEntity {

    /** 패스 본인 확인 아이디 */
    @Column(name = "id", nullable = false, unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "패스 본인 확인 아이디")
    private Integer id;

    /** (Encrypted) 이름 */
    @Column(name = "name", length = 512)
    @Schema(description = "(Encrypted) 이름")
    private String name;

    /** (Encrypted) 휴대폰번호 */
    @Column(name = "mobile_phone_number", length = 512)
    @Schema(description = "(Encrypted) 휴대폰번호")
    private String mobilePhoneNumber;

    /** (Encrypted) 이메일 */
    @Column(name = "email", length = 512)
    @Schema(description = "(Encrypted) 이메일")
    private String email;

    /** (Encrypted) CI */
    @Column(name = "ci", length = 512)
    @Schema(description = "(Encrypted) CI")
    private String ci;

    /** 생년월일 */
    @Column(name = "birthday")
    @Schema(description = "생년월일")
    private String birthday;

    /** 성별 (M: 남성, W: 여성, U: 알 수 없음) */
    @Column(name = "gender", length = 1)
    @Schema(description = "성별 (M: 남성, W: 여성, U: 알 수 없음)")
    private String gender;

    /** 외국인 여부 (Y: 외국인, N: 내국인) */
    @Column(name = "foreign_yn", length = 1)
    @Schema(description = "외국인 여부 (Y: 외국인, N: 내국인)")
    private String foreignYn;

    /** (Hashed) 이름 */
    @Column(name = "name_sha", length = 512)
    @Schema(description = "(Hashed) 이름")
    private String nameSha;

    /** (Hashed) 휴대폰번호 */
    @Column(name = "mobile_phone_number_sha", length = 512)
    @Schema(description = "(Hashed) 휴대폰번호")
    private String mobilePhoneNumberSha;

    /** (Hashed) 이메일 */
    @Column(name = "email_sha", length = 512)
    @Schema(description = "(Hashed) 이메일")
    private String emailSha;

    /** (Hashed) CI */
    @Column(name = "ci_sha", length = 512)
    @Schema(description = "(Hashed) CI")
    private String ciSha;

    /** 암호화 키 아이디 (co_cipher_key) */
    @Column(name = "cipher_key_id", length = 22)
    @Schema(description = "암호화 키 아이디 (co_cipher_key)")
    private String cipherKeyId;

    /** 이동 통신사 코드 (CC. 공통) */
    @Column(name = "mobile_carrier_code", length = 10, nullable = false)
    @Schema(description = "이동 통신사 코드 (CC. 공통)")
    private EnMobileCarrierCode mobileCarrierCode;

    /** 회원 아이디 */
    @Column(name = "member_id", length = 22)
    @Schema(description = "회원 아이디")
    private String memberId;

    /** 상태 (R: 요청, C: 완료, A: 취소, E: 만료) */
    @Column(name = "status", length = 1, nullable = false)
    @Schema(description = "상태 (R: 요청, C: 완료, A: 취소, E: 만료)")
    private String status;

    public void delete() {
        // 삭제 처리 로직 (상황에 맞게 구현)
    }

    public void update(MemberIdentification entity) {
        BeanUtils.map(entity, this);
    }
}
