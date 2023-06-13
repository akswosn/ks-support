package com.crypted.kssupport.entity.doc

import jakarta.persistence.Id
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime

@Document("proposals")
class ProposalEntity (
    @Id
    var id: ObjectId? = null,

    var checkpoint: ObjectId? = null,
    var memberId: Long? = null,
    var currency: Long? = null,
    var deposit: BigDecimal? = null,
    var txHash: String? = null,
    var txStatus: String? = null,
    var proposalIdFromContract: String? = null,
    var title: String? = null,
    var content: String? = null,
    var status: String? = null,
    var isExposed: String? = null,
    var isDeleted: String? = null,
    var forumUrl: String? = null,
    var expirationDate: LocalDateTime? = null,

    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,

    var userId: String? = null,
){
    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}