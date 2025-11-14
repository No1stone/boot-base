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
 * me_member_out Entity
 */
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "me_member_out")
@DynamicInsert
@FieldNameConstants
public class MemberOut extends UpdatedEntity {

    /** 아이디 */
    @Column(name = "id", nullable = false, unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "아이디")
    private Integer id;

    /** 회원 아이디 */
    @Column(name = "member_id", length = 22, nullable = false)
    @Schema(description = "회원 아이디")
    private String memberId;

    /** 탈퇴 일시 */
    @Column(name = "out_at", nullable = false)
    @Schema(description = "탈퇴 일시")
    private LocalDateTime outAt;

    /** 탈퇴 이유 */
    @Column(name = "out_reason", length = 1024, nullable = false)
    @Schema(description = "탈퇴 이유")
    private String outReason;

    public void delete() {
        // 삭제 처리 로직 (상황에 맞게 구현)
    }

    public void update(MemberOut entity) {
        BeanUtils.map(entity, this);
    }
}
