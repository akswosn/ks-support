package com.crypted.kssupport.repository.doc

import com.crypted.kssupport.entity.doc.VotingHistoryEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface VotingHistoryRepository: MongoRepository<VotingHistoryEntity, ObjectId> {
    fun findAllByProposalIdFromContract(proposalIdFromContract: String?): List<VotingHistoryEntity>

    @Query("{'memberId' : {'\$gt': ?0}}")
    fun findAllByMemberIdGt(memberId: Long): List<VotingHistoryEntity>

    @Query("{'proposalIdFromContract': ?0, 'memberId' : {'\$gt': ?1}}")
    fun findAllByProposalIdFromContractAndMemberIdGt(proposalIdFromContract: String, memberId: Long): List<VotingHistoryEntity>

}