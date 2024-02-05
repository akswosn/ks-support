package com.crypted.kssupport.repository.doc

import com.crypted.kssupport.entity.doc.Erc20TransferEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface Erc20TransferRepository : MongoRepository<Erc20TransferEntity, ObjectId> {

    @Query(value = "{'blockTimestamp': {'\$gte': ?0}}", count = false)
    fun findAllByBlockTimestamp(start: Long): List<Erc20TransferEntity>

    @Query(value = "{'blockTimestamp': {'\$gte': ?0, '\$lte': ?1}}", count = false)
    fun findAllByBlockTimestamp(start: Long, end: Long): List<Erc20TransferEntity>
}