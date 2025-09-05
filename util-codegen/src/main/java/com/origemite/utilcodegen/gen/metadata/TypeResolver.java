package com.origemite.utilcodegen.gen.metadata;

public class TypeResolver {

    public static String resolveJavaType(ColumnMetadata column) {
        String type = column.dataType().toLowerCase();

        return switch (type) {
            case "varchar", "char", "text" -> "String";
            case "int", "integer", "smallint", "mediumint" -> "Integer";
            case "bigint" -> "Long";
            case "datetime", "timestamp" -> "LocalDateTime";
            case "date" -> "LocalDate";
            case "float" -> "Double";                    // ✅ 추가
            case "double" -> "Double";                  // ✅ 추가
            case "decimal", "numeric" -> "BigDecimal";
            case "bit", "boolean", "tinyint" -> "Boolean";
            default -> "String"; // fallback
        };
    }
}