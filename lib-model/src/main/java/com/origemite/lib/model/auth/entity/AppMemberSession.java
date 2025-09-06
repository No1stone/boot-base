package com.origemite.lib.model.auth.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.origemite.lib.common.base.UpdatedEntity;
import com.origemite.lib.common.util.BeanUtils;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

/**
 * swms_app_member_session Entity
 */
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "app_member_session")
@DynamicInsert
@FieldNameConstants
public class AppMemberSession extends UpdatedEntity {

    /** 중복 로그인 허용 로그인 세션 아이디 */
    @Column(name = "id", length = 22, nullable = false, unique = true)
    @Id
    @Schema(description = "중복 로그인 허용 로그인 세션 아이디")
    private String id;

    /** 회원 아이디 */
    @Column(name = "member_id", length = 22, nullable = false)
    @Schema(description = "회원 아이디")
    private String memberId;

    /** Refresh token */
    @Column(name = "refresh_token", length = 22, nullable = false)
    @Schema(description = "Refresh token")
    private String refreshToken;

    /** Refresh token 만료 일시 */
    @Column(name = "expiration", nullable = false)
    @Schema(description = "Refresh token 만료 일시")
    private LocalDateTime expiration;

    public void delete() {
        // 삭제 처리 로직 (상황에 맞게 구현)
    }

    public void update(AppMemberSession entity) {
        BeanUtils.map(entity, this);
    }
}
