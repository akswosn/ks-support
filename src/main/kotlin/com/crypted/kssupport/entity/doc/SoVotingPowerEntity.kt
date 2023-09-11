package com.crypted.kssupport.entity.doc

import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime

@Document("so_voting_powers")
class SoVotingPowerEntity(
        @Id
        var id: ObjectId? = null,
        var checkpoint: ObjectId? = null,
        var soId: Long? = null,
        var somCount: Int? = null,
        var sop: String? = null,
        var votingPower: String? = null,
        var createdAt: LocalDateTime? = null,
) {
}