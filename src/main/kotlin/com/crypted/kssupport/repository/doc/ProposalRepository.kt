package com.crypted.kssupport.repository.doc

import com.crypted.kssupport.entity.doc.ProposalEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface ProposalRepository: MongoRepository<ProposalEntity, ObjectId> {

    @Query(value = "{'proposalIdFromContract': ?0}")
    fun findFirstByProposalIdFromContract(proposalIdFromContract: String): ProposalEntity?


    fun findAllByCheckpoint(checkPoint: ObjectId?) : List<ProposalEntity>

}