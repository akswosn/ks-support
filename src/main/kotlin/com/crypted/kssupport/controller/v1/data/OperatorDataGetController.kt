package com.crypted.kssupport.controller.v1.data

import com.crypted.kscommon.response.KsResponse
import com.crypted.kscommon.response.KstadimuResponseEntity
import com.crypted.kssupport.service.data.OperatorDataGetService
import io.swagger.v3.oas.annotations.Operation
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/operation-data")
class OperatorDataGetController (
        private val operatorDataGetService: OperatorDataGetService
){
    private val log = KotlinLogging.logger {}

    @Operation(
            summary = "community pool V1 investment list ",
            description = ""
    )
    @GetMapping("/community-investment/{startBlock}/{endBlock}")
    fun communityInvestment(
            @PathVariable("startBlock") startBlock: Long,
            @PathVariable("endBlock") endBlock: Long
    ): ResponseEntity<KstadimuResponseEntity>{

        return try {
            val data = operatorDataGetService.communityInvestment(startBlock, endBlock)
            KsResponse.KS_SUCCESS.toDataResponse(data)
        }
        catch (e: Exception){
            log.error ("### communityInvestment error ::: $e")
            KsResponse.KS_SERVER_INTERNAL_ERROR.toResponse()
        }

    }
}