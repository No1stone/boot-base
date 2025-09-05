package ai.budit.lib.common.util;

import com.origemite.lib.common.util.UuidUtils;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class UuidUtilsTest {



    @Test
    void uuidToBase64() {

        for (int i = 0; i < 100; i++) {
            System.out.println(UuidUtils.uuidToBase64(UUID.randomUUID()));
        }

    }


}