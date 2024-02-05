package com.crypted.kssupport.service.transaction

import com.crypted.kssupport.repository.doc.Erc20TransferRepository
import com.crypted.kssupport.repository.doc.TransactionRepository
import com.crypted.kssupport.repository.main.TokenRepository
import com.crypted.kssupport.vo.TransactionMonthlyStaticsVO
import com.crypted.kssupport.vo.TransferVO
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation.*
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Service
class TransactionService(
        private val transactionRepository: TransactionRepository,
        private val mongoTemplate: MongoTemplate,
        private val erc20TransferRepository: Erc20TransferRepository,
        private val tokenRepository: TokenRepository
) {
    private val log = KotlinLogging.logger {}

    @Value(value = "\${spring.datasource.mongodb.url}")
    lateinit var url: String

    @Value(value = "\${spring.datasource.mongodb.database}")
    lateinit var dataBaseName: String

    fun getMonthlyCount(): List<TransactionMonthlyStaticsVO> {
//        var total = transactionRepository.countBy()
//        log.info("#### total transaction $total")

        //2022 1월 부터
        var max = 11
        var date = LocalDate.parse("2023-01-01")
        val result = mutableListOf<TransactionMonthlyStaticsVO>()
        for (i in 0..max) { // 22: 2023 10월
            if (i > 0) {
                date = date.plusMonths(1)
            }
            var start = date.withDayOfMonth(1).atTime(0, 0, 0)
            var end = date.withDayOfMonth(date.lengthOfMonth()).atTime(23, 59, 59)
            var startTimestemp = Timestamp.valueOf(start).time / 1000
            var endTimestemp = Timestamp.valueOf(end).time / 1000

            var count = transactionRepository
                    .countByBlockTimestamp(startTimestemp, endTimestemp)

            log.info("### ${date.format(DateTimeFormatter.ofPattern("yyyy-MM"))} :::: $count")

            result.add(
                    TransactionMonthlyStaticsVO(
                            dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM")),
                            transactionCount = count
                    )
            )
        }

        return result
    }

    fun getMonthlyCountAggregation() {
        // timestampField를 Date로 변환
        val toDateStage = project()
                .andExpression("{ \$toDate: \$blockTimestamp }").`as`("convertedDate")

        // Date를 String으로 변환
        val toStringStage = project()
                .andExpression("{ \$blockTimestamp: { format: '%Y-%m', date: \$convertedDate } }")
                .`as`("dateString")

        // 변환된 dateString을 기준으로 그룹화하고 카운트
        val groupStage = group("dateString").count().`as`("transactionCount")

        // 집계 파이프라인 구성
        val aggregation = newAggregation(toDateStage, toStringStage, groupStage)

        val results = mongoTemplate.aggregate(aggregation, "transactions", TransactionMonthlyStaticsVO::class.java)

        log.info("### groupedResults:: $results")
    }


    fun getTransferList(start: Long, end: Long): MutableList<TransferVO> {
        val currencyTransfer = if (end > 0L) {
            transactionRepository.findAllBlockNumberAndToAndFunctionName(start, end,  "transfer");

        } else {
            transactionRepository.findAllBlockNumberAndToAndFunctionName(start,  "transfer");
        }

//        log.info("#### currencyTransfer ::: $currencyTransfer")

//        val tokenTransfer = if (end > 0L) {
//            erc20TransferRepository.findAllByBlockTimestamp(start, end)
//        } else {
//            erc20TransferRepository.findAllByBlockTimestamp(start)
//        }
//        log.info("#### currencyTransfer ::: $tokenTransfer")


        val tokenMap = tokenRepository.findAll().map { it.address to it.symbol }.toMap()

        val result = mutableListOf<TransferVO>()

        //
        for (currency in currencyTransfer) {
            var to: String? = ""
            var amount: String? = ""
            var symbol: String? = ""
            if(currency.value != null && currency.value!! > BigDecimal.ZERO){ //KSTA
                to = currency.to
                amount = currency.value!!.toPlainString()
                symbol = "KSTA"
            }
            else {
                symbol =  tokenMap[currency.to]
                if(currency.decodedInputData != null){
                    for (data in currency.decodedInputData!!){
                        if(data.type == "address"){
                            to = data.value
                        }
                        else if(data.name == "amount"){
                            amount = data.value
                        }
                    }
                }
            }


            result.add(TransferVO(
                    txHash = currency.hash,
                    from = currency.from,
                    to = to,
                    symbol = symbol,
                    amount = amount,
                    time = if (currency.blockTimestamp != null) {
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(currency.blockTimestamp!! * 1000), ZoneOffset.UTC).plusHours(9).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    } else {
                        ""
                    }
            ))
        }

//        //Token Parse
//        for (token in tokenTransfer){
//            result.add(TransferVO(
//                    txHash = token.transactionHash,
//                    from = token.from,
//                    to= token.to,
//                    symbol = tokenMap[token.tokenAddress],
//                    amount = if(token.value == null){
//                        "0"
//                    }
//                    else {
//                        token.value!!.toPlainString()
//                    },
//                    time = if(token.blockTimestamp != null){
//                        LocalDateTime.ofInstant(Instant.ofEpochMilli(token.blockTimestamp!! * 1000), ZoneOffset.UTC).plusHours(9).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
//                    }
//                    else {
//                        ""
//                    }
//            ))
//        }
        return result
    }
}

