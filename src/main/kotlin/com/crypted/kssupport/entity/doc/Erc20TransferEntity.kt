package com.crypted.kssupport.entity.doc

import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime

@Document("erc20_transfers")
class Erc20TransferEntity (
        @Id
        var id: ObjectId? = null,
        var blockNumber: Long? = null,
        var transactionHash: String? = null,
        var logIndex: Int = 0,
        var createdAt: LocalDateTime? = null,
        var blockTimestamp: Long? = null,
        var from: String? = null,
        var to: String? = null,
        var functionSignature: String? = null,
        var tokenAddress: String? = null,
        var updatedAt: LocalDateTime? = null,
        var value: BigDecimal? = BigDecimal.ZERO,

        ){
}