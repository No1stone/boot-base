package com.origemite.lib.model.auth.entity;

import com.origemite.lib.common.util.BeanUtils;
import com.origemite.lib.common.base.CreatedEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.DynamicInsert;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.*;

/**
 * me_member_login_history Entity
 */
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "me_member_login_history")
@DynamicInsert
@FieldNameConstants
public class MemberLoginHistory extends CreatedEntity {

    /** 회원 로그인 이력 아이디 */
    @Column(name = "id", nullable = false, unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "회원 로그인 이력 아이디")
    private Integer id;

    /** 회원 아이디 */
    @Column(name = "member_id", length = 22, nullable = false)
    @Schema(description = "회원 아이디")
    private String memberId;

    /** 로그인 일시 */
    @Column(name = "login_at", nullable = false)
    @Schema(description = "로그인 일시")
    private LocalDateTime loginAt;

    /** 로그인 이력 사용자 에이전트 */
    @Column(name = "login_history_user_agent", length = 255, nullable = false)
    @Schema(description = "로그인 이력 사용자 에이전트")
    private String loginHistoryUserAgent;

    /** 로그인 이력 사용자 IP 주소 */
    @Column(name = "login_history_ip", length = 64, nullable = false)
    @Schema(description = "로그인 이력 사용자 IP 주소")
    private String loginHistoryIp;

    /** 로그인 성공 여부 (Y: 성공, N: 실패) */
    @Column(name = "login_success_yn", length = 1, nullable = false)
    @Schema(description = "로그인 성공 여부 (Y: 성공, N: 실패)")
    private String loginSuccessYn;

    public void delete() {
        // 삭제 처리 로직 (상황에 맞게 구현)
    }

    public void update(MemberLoginHistory entity) {
        BeanUtils.map(entity, this);
    }
}
