package com.crypted.kssupport.repository.doc

import com.crypted.kssupport.entity.doc.ProposalEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface ProposalRepository: MongoRepository<ProposalEntity, ObjectId> {
}