package io.diddda.utilcodegen.gen.metadata;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.*;

@Slf4j

public class TableMetadataReader {

    public static TableMetadata read(String schemaName, String tableName, String urlName, String user, String pw) {
        String url = urlName + schemaName;
        String username = user;
        String password = pw; // 실제 비밀번호로 변경

        List<ColumnMetadata> columns = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String query = """
                SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH,
                       NUMERIC_PRECISION, NUMERIC_SCALE, IS_NULLABLE,
                       COLUMN_KEY, COLUMN_COMMENT, EXTRA
                FROM INFORMATION_SCHEMA.COLUMNS
                WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?
                ORDER BY ORDINAL_POSITION
            """;

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, schemaName);
                pstmt.setString(2, tableName);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        String name = rs.getString("COLUMN_NAME");
                        String type = rs.getString("DATA_TYPE");
                        Long charLength = rs.getObject("CHARACTER_MAXIMUM_LENGTH", Long.class);
                        Integer precision = rs.getObject("NUMERIC_PRECISION", Integer.class);
                        Integer scale = rs.getObject("NUMERIC_SCALE", Integer.class);
                        boolean nullable = "YES".equalsIgnoreCase(rs.getString("IS_NULLABLE"));
                        boolean primaryKey = "PRI".equalsIgnoreCase(rs.getString("COLUMN_KEY"));
                        String extra = rs.getString("EXTRA");
                        boolean autoIncrement = extra != null && extra.toLowerCase().contains("auto_increment");
                        String comment = rs.getString("COLUMN_COMMENT");

                        columns.add(ColumnMetadata.builder()
                                .name(name)
                                .dataType(type)
                                .charLength(charLength)
                                .numericPrecision(precision)
                                .numericScale(scale)
                                .nullable(nullable)
                                .primaryKey(primaryKey)
                                .autoIncrement(autoIncrement)
                                .comment(comment)
                                .build());
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("DB 메타데이터 읽기 실패: " + e.getMessage(), e);
        }

        return TableMetadata.builder()
                .schemaName(schemaName)
                .tableName(tableName)
                .columns(columns)
                .build();
    }
}
