package com.crypted.kssupport.controller.v1.transaction

import com.crypted.kscommon.response.KsResponse
import com.crypted.kscommon.response.KstadimuResponseEntity
import com.crypted.kssupport.service.transaction.TransactionService
import io.swagger.v3.oas.annotations.Operation
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/tx")
class TransactionController(
        private val transactionService: TransactionService
) {
    private val log = KotlinLogging.logger {}

    @Operation(
            summary = "transaction monthly count ",
            description = "transaction monthly count"
    )
    @GetMapping("/statics/monthly-count")
    fun proposalCheckpoint(): ResponseEntity<KstadimuResponseEntity> {
        try {
            var data = transactionService.getMonthlyCount()
            return KsResponse.KS_SUCCESS.toDataResponse(data)
        } catch (e: Exception) {
            log.error("### /monthly-coun ERROR ::: $e")
            return KsResponse.KS_SERVER_INTERNAL_ERROR.toResponse()
        }
    }
}