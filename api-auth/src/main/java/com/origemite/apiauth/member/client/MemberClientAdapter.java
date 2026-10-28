package com.origemite.apiauth.member.client;

import com.origemite.lib.common.web.CommonResponse;
import com.origemite.lib.model.member.dto.MemberReq;
import com.origemite.lib.model.member.dto.MemberRes;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * {@link MemberClientPort} — {@code GET/POST /members}, {@code POST /members/save-all}.
 */
public class MemberClientAdapter implements MemberClientPort {

    private final RestClient restClient;

    public MemberClientAdapter(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public MemberRes.Item findById(String id) {
        CommonResponse<MemberRes.Item> body = restClient.get()
                .uri("/members/{id}", id)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return body == null ? null : body.getData();
    }

    @Override
    public MemberRes.Id save(MemberReq.Create create) {
        CommonResponse<MemberRes.Id> body = restClient.post()
                .uri("/members")
                .body(create)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return body == null ? null : body.getData();
    }

    @Override
    public List<MemberRes.Id> saveAll(List<MemberReq.Create> createList) {
        CommonResponse<List<MemberRes.Id>> body = restClient.post()
                .uri("/members/save-all")
                .body(createList)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return body == null ? null : body.getData();
    }
}
