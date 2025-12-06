package com.origemite.graphqldgskt.graphql.member

import ch.qos.logback.core.helpers.Transform
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import com.origemite.lib.grpc.MemberIdentificationGrpc.MemberIdentificationFindByIdRequest
import com.origemite.lib.grpc.MemberIdentificationGrpc.MemberIdentificationServiceGrpc
import org.slf4j.LoggerFactory

data class MemberIdentificationGql (
    val id: Int,
    val memberId: String,
    val name: String?,
    val mobilePhoneNumber: String?,
    val email: String?,
    val ci: String?,
    val status: String?,
)

@DgsComponent
class MemberIdentificationQuery(
    private val memberIdentificationBlockingStub: MemberIdentificationServiceGrpc.MemberIdentificationServiceBlockingStub,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @DgsQuery
    fun memberIdentification(@InputArgument id: Int): MemberIdentificationGql? {
        log.info("DGS[memberIdentification] id={}", id)

        val req = MemberIdentificationFindByIdRequest.newBuilder()
            .setId(id)
            .build()

        val res = memberIdentificationBlockingStub.findById(req)

        return MemberIdentificationGql(
            id = res.id,
            memberId = res.memberId,
            name = res.name,
            mobilePhoneNumber = res.mobilePhoneNumber,
            email = res.email,
            ci = res.ci,
            status = res.status,
        )
    }
}