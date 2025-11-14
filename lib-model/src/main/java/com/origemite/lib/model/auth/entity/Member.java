package com.origemite.lib.model.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.origemite.lib.common.util.BeanUtils;
import com.origemite.lib.common.base.UpdatedEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.DynamicInsert;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * me_member Entity
 */
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "me_member")
@DynamicInsert
@FieldNameConstants
public class Member extends UpdatedEntity {

    /** 회원 아이디 */
    @Column(name = "id", length = 22, nullable = false, unique = true)
    @Id
    @Schema(description = "회원 아이디")
    private String id;

    /** 로그인 아이디 (Portal: 전화번호, 코드샵: 이메일) */
    @Column(name = "login_id", length = 128, nullable = false)
    @Schema(description = "로그인 아이디 (Portal: 전화번호, 코드샵: 이메일)")
    private String loginId;

    /** 로그인 패스워드 (nullable 가능, varchar(255) → varchar(512) 변경) */
    @Column(name = "login_password", length = 512)
    @Schema(description = "로그인 패스워드 (nullable 가능, varchar(255) → varchar(512) 변경)")
    private String loginPassword;

    /** 로그인 패스워드 솔트 키 */
    @Column(name = "login_password_salt_key", length = 255)
    @Schema(description = "로그인 패스워드 솔트 키")
    private String loginPasswordSaltKey;

    /** 상태 (I: 초대, A: 활성, D: 비활성) */
    @Column(name = "status", length = 1, nullable = false)
    @Schema(description = "상태 (I: 초대, A: 활성, D: 비활성)")
    private String status;

    public void delete() {
        // 삭제 처리 로직 (상황에 맞게 구현)
    }

    public void update(Member entity) {
        BeanUtils.map(entity, this);
    }


    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    @Schema(description = "멤버 로그인 히스토리 목록")
    private List<MemberLoginHistory> memberLoginHistories = new ArrayList<>();


    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    @Schema(description = "멤버 탈퇴 목록")
    private List<MemberOut> memberOuts = new ArrayList<>();


    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    @Schema(description = "멤버 탈퇴 목록")
    private List<MemberPasswordChangeHistory> MemberPasswordChangeHistories = new ArrayList<>();

}
