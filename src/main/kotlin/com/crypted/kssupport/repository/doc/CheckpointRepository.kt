package com.crypted.kssupport.repository.doc

import com.crypted.kssupport.entity.doc.CheckpointEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.time.LocalDateTime
import java.time.ZonedDateTime

interface CheckpointRepository : MongoRepository<CheckpointEntity, ObjectId>{
    @Query(value = "{'createdAt' : {'\$gte':?0,'\$lt':?1} }")
    fun findFirstByCreatedAtBetween(start: LocalDateTime, end: LocalDateTime): CheckpointEntity?

}