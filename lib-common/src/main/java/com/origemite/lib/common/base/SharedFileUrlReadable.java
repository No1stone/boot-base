package com.origemite.lib.common.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.origemite.lib.common.util.EnvUtils;
import com.origemite.lib.common.util.StringUtils;


public interface SharedFileUrlReadable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    default String getFileUrl() {
        if( getUploadedFileName() == null || getUploadedFileName().isEmpty() ) return null;
        String nameSpaceType = "url.public.fileserver";
//        if(getNamespace().contains("private")) nameSpaceType = "url.private.fileserver";
        return StringUtils.concat(
                EnvUtils.getProperty(nameSpaceType),
//                "/",
//                getNamespace(),
//                "/",r
//                getUploadedFileName()
//                "/",
                "/common/files/images/",
                getId()
        );
    }

    String getId();
    String getNamespace();
    String getUploadedFileName();
}
