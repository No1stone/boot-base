package com.origemite.utilcodegen.gen.metadata;


import lombok.Builder;

@Builder
public record ColumnMetadata(
        String name,
        String dataType,
        Long charLength,
        Integer numericPrecision,
        Integer numericScale,
        boolean nullable,
        boolean primaryKey,
        boolean autoIncrement, // ✅ AUTO_INCREMENT 여부 추가
        String comment
) {
    public String fullType() {
        return switch (dataType) {
            case "varchar", "char" -> (charLength != null) ? dataType + "(" + charLength + ")" : dataType;
            case "decimal", "numeric", "double", "float" ->
                    (numericPrecision != null) ? dataType + "(" + numericPrecision + "," + (numericScale != null ? numericScale : 0) + ")" : dataType;
            case "int", "bigint", "tinyint", "smallint", "mediumint" ->
                    (numericPrecision != null) ? dataType + "(" + numericPrecision + ")" : dataType;
            default -> dataType;
        };
    }
}