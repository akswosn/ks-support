package com.crypted.kssupport.entity.doc

import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime

@Document("so_snapshots")
class SoSnapshotEntity {
    @Id
    var id: ObjectId? = null
    var checkpoint: ObjectId? = null
    var soId: Long? = null
    var name: String? = null
    var title: String? = null
    var description: String? = null
    var image: String? = null
    var sns: String? = null
    var homepage: String? = null
    var sop: BigDecimal? = null
    var ratio: BigDecimal? = null
    var ranking: Int? = null
    var claim: BigDecimal? = null
    var contract: String? = null
    var leader: String? = null
    var somCount: Int? = null
    var createdAt: LocalDateTime? = null
}