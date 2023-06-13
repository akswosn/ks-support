package com.crypted.kssupport.repository.doc

import com.crypted.kssupport.entity.doc.VotingHistoryEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface VotingHistoryRepository: MongoRepository<VotingHistoryEntity, ObjectId> {
}