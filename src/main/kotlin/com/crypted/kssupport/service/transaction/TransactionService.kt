package com.crypted.kssupport.service.transaction

import com.crypted.kssupport.repository.doc.TransactionRepository
import com.crypted.kssupport.vo.TransactionMonthlyStaticsVO
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation.*
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class TransactionService(
        private val transactionRepository: TransactionRepository,
        private val mongoTemplate: MongoTemplate,
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
        var max = 10
        var date = LocalDate.parse("2023-01-01")
        val result = mutableListOf<TransactionMonthlyStaticsVO>()
        for (i in 0 .. max){ // 22: 2023 10월
            if(i > 0){
                date = date.plusMonths(1)
            }
            var start = date.withDayOfMonth(1).atTime(0,0,0)
            var end = date.withDayOfMonth(date.lengthOfMonth()).atTime(23,59,59)
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

    fun getMonthlyCountAggregation(){
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


}

