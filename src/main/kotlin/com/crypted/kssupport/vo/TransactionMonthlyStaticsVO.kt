package com.crypted.kssupport.vo

import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class TransactionMonthlyStaticsVO(
        var dateString: String? = null,
        var transactionCount: Long? = 0L
) {

    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}