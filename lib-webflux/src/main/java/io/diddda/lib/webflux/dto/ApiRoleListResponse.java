package io.diddda.lib.webflux.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApiRoleListResponse {
    private String code;
    private String message;
    private List<ApiRoleDto> data;
}

