package com.crypted.kssupport.entity.doc

import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime

@Document("voting_powers")
class VotingPowerEntity {
    @Id
    var id: ObjectId? = null
    var checkpoint: ObjectId? = null
    var soId: Long? = null
    var memberId: Long? = null
    var sop: BigDecimal? = null
    var ratio: BigDecimal? = null
    var createdAt: LocalDateTime? = null
}