package io.origemite.lib.common.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@FieldNameConstants
public abstract class UpdatedEntity extends CreatedEntity {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;    //수정일시

    @Column(name = "updated_by", nullable = false, length = 255)
    @LastModifiedBy
    private String updatedBy;   //수정자

    @Version
    @Column(name = "version", nullable = false)
    private Integer version; // 버전

}