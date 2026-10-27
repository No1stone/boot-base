package com.origemite.lib.common.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenSignature {

    private int latestVersion;
    private String signature;
}
