package com.crypted.kssupport.repository.doc

import com.crypted.kssupport.entity.doc.VotingHistoryEntity
import com.crypted.kssupport.entity.doc.VotingPowerEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface VotingPowerRepository : MongoRepository<VotingPowerEntity, ObjectId> {

    @Query("{'checkpoint' :  ?0}")
    fun findAllByCheckpoint(checkPoint: ObjectId): List<VotingPowerEntity>

    fun findFirstByCheckpointAndSoIdAndMemberId(checkPoint: ObjectId, soId: Long, memberId: Long ):VotingPowerEntity?
}