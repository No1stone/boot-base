package io.diddda.lib.common.base;

import io.diddda.lib.common.util.EnvUtils;
import io.diddda.lib.common.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ImageFileUrlReadable {

    default String getImageFileUrl() {
        return StringUtils.concat(
                EnvUtils.getProperty("legacy.client.s3.endpoint.url"),
                "/",
                getImageFileNamespace(),
                "/",
                getImageFileId()
        );
    }
    String getImageFileId();

    @JsonIgnore
    default String getImageFileNamespace(){
        return "content";
    }
}
