package com.origemite.lib.model.auth.entity;

import com.origemite.lib.common.util.BeanUtils;
import com.origemite.lib.common.base.UpdatedEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.DynamicInsert;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.*;

/**
 * me_member_password_change_history Entity
 */
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "me_member_password_change_history")
@DynamicInsert
@FieldNameConstants
public class MemberPasswordChangeHistory extends UpdatedEntity {

    /** 회원 비밀번호 변경 이력 아이디 */
    @Column(name = "id", nullable = false, unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "회원 비밀번호 변경 이력 아이디")
    private Integer id;

    /** 회원 아이디 */
    @Column(name = "member_id", length = 22, nullable = false)
    @Schema(description = "회원 아이디")
    private String memberId;

    /** 비밀번호 */
    @Column(name = "login_password", length = 255, nullable = false)
    @Schema(description = "비밀번호")
    private String loginPassword;

    /** 비밀번호 salt_key */
    @Column(name = "login_password_salt_key", length = 255, nullable = false)
    @Schema(description = "비밀번호 salt_key")
    private String loginPasswordSaltKey;

    /** 비밀번호 변경 일시 */
    @Column(name = "change_at", nullable = false)
    @Schema(description = "비밀번호 변경 일시")
    private LocalDateTime changeAt;

    /** 암호화 키 아이디 */
    @Column(name = "cipher_key_id", length = 22, nullable = false)
    @Schema(description = "암호화 키 아이디")
    private String cipherKeyId;

    public void delete() {
        // 삭제 처리 로직 (상황에 맞게 구현)
    }

    public void update(MemberPasswordChangeHistory entity) {
        BeanUtils.map(entity, this);
    }
}
