package com.origemite.utilcodegen;


import com.origemite.utilcodegen.gen.MultiPkCodeGenRunner;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MultiPkCodeGenApplication {


    public static void main(String[] args) {
        String[] data = {
                "--url=jdbc:mysql://localghost:3306/"
                , "--username=root"
                , "--password=11"
                , "--schemaName=11"
//                , "--table=m_ap_trmnl_inf"
                , "--table=m_clnt_grp_menu_athr_inf"
                ,
        };
        MultiPkCodeGenRunner.run(data);
    }
}