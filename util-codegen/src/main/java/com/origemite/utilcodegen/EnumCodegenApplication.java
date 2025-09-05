package com.origemite.utilcodegen;

import com.origemite.utilcodegen.gen.EnumGenRenner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EnumCodegenApplication {

    public static void main(String[] args) {
        String[] data = {
                "--classname=EnCodeShopFileType"
        };
        EnumGenRenner.run(data);
    }

}
