package com.crypted.kssupport.controller.v1.transaction

import com.crypted.kscommon.response.KsResponse
import com.crypted.kscommon.response.KstadimuResponseEntity
import com.crypted.kssupport.service.transaction.TransactionService
import io.swagger.v3.oas.annotations.Operation
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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
    fun monthlyCount(): ResponseEntity<KstadimuResponseEntity> {
        try {
            var data = transactionService.getMonthlyCount()
            return KsResponse.KS_SUCCESS.toDataResponse(data)
        } catch (e: Exception) {
            log.error("### /monthly-coun ERROR ::: $e")
            return KsResponse.KS_SERVER_INTERNAL_ERROR.toResponse()
        }
    }


    @Operation(
            summary = "transaction transfer 조회 ",
            description = "transaction transfer 리스트 조회"
    )
    @GetMapping(value = ["/statics/transfer/{startBlock}/{endBlock}"])
    fun getTransferList(@PathVariable("startBlock") startBlock: Long,
                        @PathVariable("endBlock") endBlock: Long
    ): ResponseEntity<KstadimuResponseEntity> {

        return try {
            val data = transactionService.getTransferList(startBlock, endBlock)
            return KsResponse.KS_SUCCESS.toDataResponse(data)
        } catch (e: Exception) {
            log.error("### /monthly-coun ERROR ::: $e")
            KsResponse.KS_SERVER_INTERNAL_ERROR.toResponse()
        }
    }

    @Operation(
            summary = "transaction transfer 조회 (start)",
            description = "transaction transfer 리스트 조회(start)"
    )
    @GetMapping(value = ["/statics/transfer/{startBlock}"])
    fun getTransferListStart(
            @PathVariable("startBlock") startBlock: Long
    ): ResponseEntity<KstadimuResponseEntity> {
//1702393200 : 2023-12-13 09:00:00 UTC-9
        return try {
            val data = transactionService.getTransferList(startBlock, 0L)
            return KsResponse.KS_SUCCESS.toDataResponse(data)
        } catch (e: Exception) {
            log.error("### /monthly-coun ERROR ::: $e")
            KsResponse.KS_SERVER_INTERNAL_ERROR.toResponse()
        }
    }
}