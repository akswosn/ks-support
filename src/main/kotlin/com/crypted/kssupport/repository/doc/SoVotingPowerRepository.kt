package com.crypted.kssupport.repository.doc

import com.crypted.kssupport.entity.doc.SoVotingPowerEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface SoVotingPowerRepository : MongoRepository<SoVotingPowerEntity, ObjectId> {

    @Query("{'checkpoint' :  ?0}")
    fun findAllByCheckpoint(checkPoint: ObjectId): List<SoVotingPowerEntity>

}