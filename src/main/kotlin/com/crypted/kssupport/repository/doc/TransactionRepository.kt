package com.crypted.kssupport.repository.doc

import com.crypted.kssupport.entity.doc.TransactionEntity
import org.bson.types.ObjectId
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : MongoRepository<TransactionEntity, ObjectId> {

    fun countBy():Long

    fun findAllBy(pageable: Pageable): List<TransactionEntity>

    @Query(value = "{'blockTimestamp': {'\$gte': ?0, '\$lte': ?1}}", count = true)
    fun countByBlockTimestamp(start: Long, end: Long): Long


    @Query(value = "{'blockNumber': {'\$gte': ?0, '\$lte': ?1}, 'to': ?2, 'functionName': ?3}", count = false)
    fun findAllByBlockNumberAndToAndFunctionName(start: Long, end: Long, to: String, functionName: String): List<TransactionEntity>
}