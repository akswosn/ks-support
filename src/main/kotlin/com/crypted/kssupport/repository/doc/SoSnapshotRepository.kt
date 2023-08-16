package com.crypted.kssupport.repository.doc

import com.crypted.kssupport.entity.doc.CheckpointEntity
import com.crypted.kssupport.entity.doc.SoSnapshotEntity
import com.crypted.kssupport.entity.doc.SomSnapshotEntity
import com.crypted.kssupport.entity.doc.VotingPowerEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.time.LocalDateTime
import java.time.ZonedDateTime

interface SoSnapshotRepository : MongoRepository<SoSnapshotEntity, ObjectId>{
    @Query(value = "{'createdAt' : {'\$gte':?0,'\$lt':?1} }")
    fun findAllByCreatedAtBetween(start: LocalDateTime, end: LocalDateTime): List<SoSnapshotEntity>

    @Query("{'checkpoint' :  ?0}")
    fun findAllByCheckpoint(checkPoint: ObjectId): List<SoSnapshotEntity>

}