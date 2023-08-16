package com.crypted.kssupport.entity.doc

import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime

@Document("som_snapshots")
class SomSnapshotEntity {
    @Id
    var id: ObjectId? = null
    var checkpoint: ObjectId? = null
    var soId: Long? = null
    var memberId: Long? = null
    var protocolId: Long? = null
    var address: String? = null
    var sop: BigDecimal? = null
    var ratio: BigDecimal? = null
    var claim: BigDecimal? = null

    // public String reward;
    var createdAt: LocalDateTime? = null
}