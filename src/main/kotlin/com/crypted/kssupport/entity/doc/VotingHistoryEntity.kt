package com.crypted.kssupport.entity.doc

import jakarta.persistence.Id
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("voting_histories")
class VotingHistoryEntity(
    @Id
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
    var proposalIdFromContract: String? = null
) {
    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}