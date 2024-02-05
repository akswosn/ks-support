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


//    @Query(value = "{'blockTimestamp': {'\$gte': ?0}, 'value': {'\$gt': ?1}, 'functionName': ?2}", count = false)
//    fun findAllBlockTimestampAndToAndFunctionNameAndValue(start: Long, value: String ,functionName: String ): List<TransactionEntity>
//
//    @Query(value = "{'blockTimestamp': {'\$gte': ?0, '\$lte': ?1}, 'value': {'\$gt': ?2}, 'functionName': ?3}", count = false)
//    fun findAllBlockTimestampAndToAndFunctionNameAndValue(start: Long, end: Long, value: String,functionName: String ): List<TransactionEntity>

    @Query(value = "{'blockNumber': {'\$gte': ?0}, 'functionName': ?2}", count = false)
    fun findAllBlockNumberAndToAndFunctionName(start: Long, functionName: String ): List<TransactionEntity>

    @Query(value = "{'blockNumber': {'\$gte': ?0, '\$lte': ?1}, 'functionName': ?2}", count = false)
    fun findAllBlockNumberAndToAndFunctionName(start: Long, end: Long, functionName: String ): List<TransactionEntity>

}