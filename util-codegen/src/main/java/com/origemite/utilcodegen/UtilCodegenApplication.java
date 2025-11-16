package com.origemite.utilcodegen;

import com.origemite.utilcodegen.gen.CodeGenRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UtilCodegenApplication {

    public static void main(String[] args) {
        String[] data = {
                "--url=jdbc:mysql://localhost:3306/"
                , "--username=root"
                , "--password=pass1234"
                , "--schemaName=appdb"
                , "--table=me_member_password_change_history"
                ,
        };
        CodeGenRunner.run(data);
    }

}


//  me_member_connect
//        me_member
//  me_member_login_history
//        me_member_out
//  me_member_password_change_history