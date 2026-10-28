package com.origemite.apiauth.member.client;

import com.origemite.lib.model.member.dto.MemberReq;
import com.origemite.lib.model.member.dto.MemberRes;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

class MemberClientAdapterTest {

    @Test
    void findById_unwrapsCommonResponse() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://localhost/auth");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("http://localhost/auth/members/m1"))
                .andExpect(method(GET))
                .andRespond(withSuccess(
                        "{\"code\":\"0000\",\"message\":\"OK\",\"data\":{\"id\":\"m1\",\"loginId\":\"a@b.com\"}}",
                        MediaType.APPLICATION_JSON));

        MemberClientAdapter adapter = new MemberClientAdapter(builder.build());
        MemberRes.Item item = adapter.findById("m1");

        assertThat(item.getId()).isEqualTo("m1");
        assertThat(item.getLoginId()).isEqualTo("a@b.com");
        server.verify();
    }

    @Test
    void save_postsCreateBody() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://localhost/auth");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("http://localhost/auth/members"))
                .andExpect(method(POST))
                .andRespond(withSuccess(
                        "{\"code\":\"0000\",\"data\":{\"id\":\"m2\"}}",
                        MediaType.APPLICATION_JSON));

        MemberClientAdapter adapter = new MemberClientAdapter(builder.build());
        MemberRes.Id id = adapter.save(MemberReq.Create.builder().loginId("x@y.com").status("A").build());

        assertThat(id.getId()).isEqualTo("m2");
        server.verify();
    }

    @Test
    void saveAll_postsList() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://localhost/auth");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("http://localhost/auth/members/save-all"))
                .andExpect(method(POST))
                .andRespond(withSuccess(
                        "{\"code\":\"0000\",\"data\":[{\"id\":\"m2\"},{\"id\":\"m3\"}]}",
                        MediaType.APPLICATION_JSON));

        MemberClientAdapter adapter = new MemberClientAdapter(builder.build());
        var ids = adapter.saveAll(List.of(
                MemberReq.Create.builder().loginId("a@b.com").status("A").build(),
                MemberReq.Create.builder().loginId("c@d.com").status("A").build()));

        assertThat(ids).extracting(MemberRes.Id::getId).containsExactly("m2", "m3");
        server.verify();
    }
}
