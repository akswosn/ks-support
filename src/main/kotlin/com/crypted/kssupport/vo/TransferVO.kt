package com.crypted.kssupport.vo

import java.time.LocalDateTime

class TransferVO(
        var txHash: String? = null,
        var from: String? = null,
        var to: String? = null,
        var symbol: String? = null,
        var amount: String? = null,
        var time: String? = null,
) {
}