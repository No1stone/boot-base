package io.diddda.lib.common.base;

import io.diddda.lib.common.util.EnvUtils;
import io.diddda.lib.common.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;


public interface FileUrlReadable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    default String getFileUrl() {
        if( getFileId() == null || getFileId().isEmpty() ) return null;
        return StringUtils.concat(
                EnvUtils.getProperty("legacy.client.s3.endpoint.url"),
                "/",
                getFileNamespace(),
                "/",
                getFileId()
        );

    }

    String getFileId();

    @JsonIgnore
    default String getFileNamespace(){
        return "content";
    };


}
