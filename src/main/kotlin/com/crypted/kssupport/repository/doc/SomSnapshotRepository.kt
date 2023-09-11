package com.crypted.kssupport.repository.doc

import com.crypted.kssupport.entity.doc.SomSnapshotEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.time.LocalDateTime

interface SomSnapshotRepository : MongoRepository<SomSnapshotEntity, ObjectId>{
    @Query("{'checkpoint' :  ?0}")
    fun findAllByCheckpoint(checkPoint: ObjectId): List<SomSnapshotEntity>?

    @Query(value = "{ 'checkpoint' :  ?0,  'protocolId':  ?1}")
    fun findAllByCheckpoint(checkPoint: ObjectId,
                                  protocolId: Long): List<SomSnapshotEntity>?
}