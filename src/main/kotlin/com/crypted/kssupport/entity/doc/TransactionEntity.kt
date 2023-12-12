package com.crypted.kssupport.entity.doc

import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal

@Document("transactions")
class TransactionEntity (
        @Id
        var id: ObjectId? = null,
        var hash: String? = null,
        var from: String? = null,
        var to: String? = null,
        var blockNumber: Long? = null,
        var functionName: String? = null,
        var functionSignature: String? = null,
        var blockTimestamp: Long? = null,
        var value: BigDecimal? = BigDecimal.ZERO,
        var status: Int? = 0

        ){


}