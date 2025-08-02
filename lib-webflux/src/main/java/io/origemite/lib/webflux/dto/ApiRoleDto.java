package io.origemite.lib.webflux.dto;

import lombok.Data;

@Data
public class ApiRoleDto {
    private Long id;
    private String name;
    private String description;
    private String url;
    private String uri;
    private String httpMethod;
    private Integer roles;
    private String status;
}
