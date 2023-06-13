package com.crypted.kssupport.entity.doc

import jakarta.persistence.Id
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("checkpoints")
class CheckpointEntity (
    @Id
    var id: ObjectId? = null,
    var blockNumber: Long? = null,
    var createdAt: LocalDateTime? = null
){
    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}