package com.origemite.lib.model.auth.entity;

import com.origemite.lib.common.util.BeanUtils;
import com.origemite.lib.common.base.UpdatedEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.DynamicInsert;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * me_member_connect Entity
 */
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "me_member_connect")
@DynamicInsert
@FieldNameConstants
public class MemberConnect extends UpdatedEntity {

    /** 회원 연결 아이디 */
    @Column(name = "id", nullable = false, unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "회원 연결 아이디")
    private Integer id;

    /** 회원 아이디 */
    @Column(name = "member_id", length = 22, nullable = false)
    @Schema(description = "회원 아이디")
    private String memberId;

    /** 연결 유형 (구글, 네이버, 카카오 등) */
    @Column(name = "connect_type_code", length = 512, nullable = false)
    @Schema(description = "연결 유형 (구글, 네이버, 카카오 등)")
    private String connectTypeCode;

    /** 연결된 계정의 고유 식별자 (구글, 네이버, 카카오 등) */
    @Column(name = "connected_id", length = 255, nullable = false)
    @Schema(description = "연결된 계정의 고유 식별자 (구글, 네이버, 카카오 등)")
    private String connectedId;

    /** (Encrypted) 이름 */
    @Column(name = "name", length = 512)
    @Schema(description = "(Encrypted) 이름")
    private String name;

    /** (Encrypted) 이메일 */
    @Column(name = "email", length = 512)
    @Schema(description = "(Encrypted) 이메일")
    private String email;

    /** (Encrypted) 휴대폰번호 */
    @Column(name = "mobile", length = 512)
    @Schema(description = "(Encrypted) 휴대폰번호")
    private String mobile;

    /** 프로필 사진 URL */
    @Column(name = "picture", length = 512)
    @Schema(description = "프로필 사진 URL")
    private String picture;

    /** 성별 (M: 남성, F: 여성, U: 알 수 없음) */
    @Column(name = "gender", length = 1)
    @Schema(description = "성별 (M: 남성, F: 여성, U: 알 수 없음)")
    private String gender;

    /** 연령대 (e.g., 20~29) */
    @Column(name = "age", length = 10)
    @Schema(description = "연령대 (e.g., 20~29)")
    private String age;

    /** 생일 (MMDD, e.g., 0923) */
    @Column(name = "birthday", length = 4)
    @Schema(description = "생일 (MMDD, e.g., 0923)")
    private String birthday;

    /** 출생년도 (YYYY, e.g., 1973) */
    @Column(name = "birthyear", length = 4)
    @Schema(description = "출생년도 (YYYY, e.g., 1973)")
    private String birthyear;

    /** 언어 및 지역 코드 (e.g., ko_KR) */
    @Column(name = "locale", length = 8)
    @Schema(description = "언어 및 지역 코드 (e.g., ko_KR)")
    private String locale;

    /** 암호화 키 아이디 (co_cipher_key) */
    @Column(name = "cipher_key_id", length = 22, nullable = false)
    @Schema(description = "암호화 키 아이디 (co_cipher_key)")
    private String cipherKeyId;

    /** 상태 (I: 초대, A: 활성, D: 비활성) */
    @Column(name = "status", length = 1, nullable = false)
    @Schema(description = "상태 (I: 초대, A: 활성, D: 비활성)")
    private String status;

    public void delete() {
        // 삭제 처리 로직 (상황에 맞게 구현)
    }

    public void update(MemberConnect entity) {
        BeanUtils.map(entity, this);
    }
}
