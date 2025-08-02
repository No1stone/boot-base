package io.origemite.lib.common.web;

public enum SystemServiceModule {
    DEFAULT("0000"),

    A4_MEMBER_PORTAL_API("A4AUPI"),
    A4_TARGET_API("A4TAPI"),

    PROVISIONING_CODESHOP_CONTENT_API("PSS1PI"),

    SHARED_SYSTEM_COMMON_API("SSCOPI"),
    SHARED_SYSTEM_LEGACY_API("SSLEPI"),
    SHARED_SYSTEM_PARTNER("SSPAPI");

    private final String prefix;

    SystemServiceModule(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

}
