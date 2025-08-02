package io.origemite.lib.common.base;

import io.origemite.lib.common.util.EnvUtils;
import io.origemite.lib.common.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonInclude;


public interface DeleteionCertificationFileUrlReadable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    default String getFileUrl() {
        if( getDeleteionCertificationFileId() == null || getDeleteionCertificationFileId().isEmpty() ) return null;
        return StringUtils.concat(
                EnvUtils.getProperty("legacy.client.s3.endpoint.url"),
                "/",
                getDeleteionCertificationFileNamespace(),
                "/",
                getDeleteionCertificationFileId()
        );
    }

    String getDeleteionCertificationFileId();

    default String getDeleteionCertificationFileNamespace(){
        return "deletion-certificate";
    }
}
