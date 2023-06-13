package com.crypted.kssupport.entity.doc

import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("proposals")
class VotingHistoryEntity(
    var id: ObjectId? = null,

    var proposal: ObjectId? = null,
    var soId: Long? = null,
    var memberId: Long? = null,
    // public String soName;
    var votingPower: String? = null,
    var result: String? = null,
    var userId: String? = null,
    var address: String? = null,
    var createdAt: LocalDateTime? = null,

    var type: String? = null, //Vote, Deposit, Passed, Failed

    var txHash: String? = null,
    var tokenName: String? = null,
    var tokenAmount: String? = null,
) {
    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}