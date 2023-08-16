package com.crypted.kssupport.repository.doc

import com.crypted.kssupport.entity.doc.CheckpointEntity
import com.crypted.kssupport.entity.doc.SoSnapshotEntity
import com.crypted.kssupport.entity.doc.SomSnapshotEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.time.LocalDateTime
import java.time.ZonedDateTime

interface SomSnapshotRepository : MongoRepository<SomSnapshotEntity, ObjectId>{
    @Query(value = "{'createdAt' : {'\$gte':?0,'\$lt':?1} }")
    fun findAllByCreatedAtBetween(start: LocalDateTime, end: LocalDateTime): List<SomSnapshotEntity>

    @Query("{'checkpoint' :  ?0}")
    fun findAllByCheckpoint(checkPoint: ObjectId): List<SomSnapshotEntity>

}