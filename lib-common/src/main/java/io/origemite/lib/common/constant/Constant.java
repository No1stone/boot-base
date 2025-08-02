package io.origemite.lib.common.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;


@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Constant {

    SUCCESS("S", "성공", List.of(0, "SUCCESS")),
    FAIL("F", "실패", List.of(1, "FAIL")),
    NO("N", "아니오", List.of()),
    YES("Y", "예", List.of()),
    ALL("A", "전체", List.of()),
    IN_PROGRESS("P", "진행 중", List.of()),
    ENDED("E", "종료", List.of()),
    UNREGISTERED("U", "미등록", List.of()),
    ;

    final String code;
    final String desc;
    final List<Object> alternativeValues;

    public boolean equal(Object obj) {
        if(ObjectUtils.isEmpty(obj)) {
            return false;
        }

        if( ObjectUtils.nullSafeEquals(this.code, obj)) {
            return true;
        }

        if (CollectionUtils.contains(this.alternativeValues.iterator(), obj)) {
            return true;
        }

        return false;
    }
}