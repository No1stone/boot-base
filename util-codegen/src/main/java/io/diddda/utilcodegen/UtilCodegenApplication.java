package io.diddda.utilcodegen;

import io.diddda.utilcodegen.gen.CodeGenRunner;
import io.diddda.utilcodegen.gen.EnumGenRenner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UtilCodegenApplication {

    public static void main(String[] args) {
        String[] data = {
                "--url=jdbc:mysql://localhost:3306/"
                , "--username=root"
                , "--password=11"
                , "--schemaName=shared"
                , "--table=co_exchange_rate"
                ,
        };
        CodeGenRunner.run(data);
    }

}
