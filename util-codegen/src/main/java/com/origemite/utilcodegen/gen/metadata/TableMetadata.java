package com.origemite.utilcodegen.gen.metadata;

import lombok.*;

import java.util.List;

@Builder
public record TableMetadata(
        String schemaName,
        String tableName,
        List<ColumnMetadata> columns
) {

    // ✅ static 유틸성 메서드 (외부에서도 사용 가능)
    public static String toClassName(String tableName) {
        String[] parts = tableName.split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (!part.isBlank()) {
                sb.append(part.substring(0, 1).toUpperCase());
                sb.append(part.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    // ✅ 인스턴스용 클래스명 반환 메서드
    public String className() {
        return toClassName(this.tableName);
    }
}