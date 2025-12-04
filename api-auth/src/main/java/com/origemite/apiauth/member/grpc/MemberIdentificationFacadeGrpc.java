package com.origemite.apiauth.member.grpc;

import com.origemite.apiauth.member.facade.MemberIdentificationFacade;
import com.origemite.lib.grpc.MemberIdentificationGrpc.MemberIdentificationFindByIdRequest;
import com.origemite.lib.grpc.MemberIdentificationGrpc.MemberIdentificationItemResponse;
import com.origemite.lib.grpc.MemberIdentificationGrpc.MemberIdentificationServiceGrpc;
import com.origemite.lib.model.auth.dto.MemberIdentificationRes;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class MemberIdentificationFacadeGrpc extends MemberIdentificationServiceGrpc.MemberIdentificationServiceImplBase {

    private final MemberIdentificationFacade memberIdentificationFacade;


    @Override
    public void findById(MemberIdentificationFindByIdRequest request,
                         io.grpc.stub.StreamObserver<com.origemite.lib.grpc.MemberIdentificationGrpc.MemberIdentificationItemResponse> responseObserver) {

        MemberIdentificationRes.Item item = memberIdentificationFacade.findById(request.getId());

        MemberIdentificationItemResponse response =
                MemberIdentificationItemResponse.newBuilder()
                        .setId(item.getId())
                        .setMemberId(item.getMemberId())
                        .setName(item.getName())
                        .setMobilePhoneNumber(item.getMobilePhoneNumber())
                        .setEmail(item.getEmail())
                        .setCi(item.getCi())
                        .setStatus(item.getStatus())
                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
