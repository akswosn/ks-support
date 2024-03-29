package com.crypted.kssupport.entity.doc

import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("voting_powers")
class VotingPowerEntity(
        @Id
        var id: ObjectId? = null,
        var checkpoint: ObjectId? = null,
        var soId: Long? = null,
        var memberId: Long? = null,
        var protocalId: Long? = null,
        var sop: String? = null,
        var soSopTotal: String? = null,
        var soVotingPower: String? = null,
        var votingPower: String? = null,
        var createdAt: LocalDateTime? = null
) {

}