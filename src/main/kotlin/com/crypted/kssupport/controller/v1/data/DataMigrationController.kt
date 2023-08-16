package com.crypted.kssupport.controller.v1.data

import com.crypted.kscommon.response.KsResponse
import com.crypted.kscommon.response.KstadimuResponseEntity
import com.crypted.kssupport.service.DataMigrationService
import io.swagger.v3.oas.annotations.Operation
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/data")
class DataMigrationController(
    private val dataMigrationService: DataMigrationService
) {
    private val log = KotlinLogging.logger {}

    class DataParam(
        var compulsion: Boolean = false
    ){

    }

    @Operation(
        summary = "[Anyway] checkpoint API ",
        description = "proposal checkpoint API"
    )
    @GetMapping("/proposal/checkpoint")
    fun proposalCheckpoint(): ResponseEntity<KstadimuResponseEntity>{
        try {
            var insertCount = dataMigrationService.proposalCheckPoint()

            return KsResponse.KS_SUCCESS.toDataResponse(mapOf("callDates" to insertCount))
        }
        catch (e: Exception){
            log.error("### /proposal ERROR ::: $e")
            return KsResponse.KS_SERVER_INTERNAL_ERROR.toResponse()
        }
    }

    @Operation(
        summary = "[Anyway] checkpoint, sosnapshot, votingpower API ",
        description = "proposal 사전 데이터 로드 API"
    )
    @GetMapping("/proposal/init")
    fun proposalInit(): ResponseEntity<KstadimuResponseEntity>{
        try {
            var insertCount = dataMigrationService.initProposalDataCallAPI()

            return KsResponse.KS_SUCCESS.toDataResponse(mapOf("callDates" to insertCount))
        }
        catch (e: Exception){
            log.error("### /proposal ERROR ::: $e")
            return KsResponse.KS_SERVER_INTERNAL_ERROR.toResponse()
        }
    }

    @Operation(
        summary = "[One Time] init proposal new MongoDB 마이그레이션",
        description = ""
    )
    @PostMapping("/proposal")
    fun proposal(@RequestBody dataParam: DataParam ): ResponseEntity<KstadimuResponseEntity>{
        try {
            var insertCount = dataMigrationService.proposalDataMigration(dataParam.compulsion)

            return KsResponse.KS_SUCCESS.toDataResponse(mapOf("insertCount" to insertCount))
        }
        catch (e: Exception){
            log.error("### /proposal ERROR ::: $e")
            return KsResponse.KS_SERVER_INTERNAL_ERROR.toResponse()
        }
    }

    @Operation(
        summary = "[One Time] init proposal voting new MongoDB 마이그레이션",
        description = ""
    )
    @PostMapping("/voting-history")
    fun votingHistory( @RequestBody dataParam: DataParam ): ResponseEntity<KstadimuResponseEntity>{
        try {
            var insertCount = dataMigrationService.votingHistoryDataMigration(dataParam.compulsion)

            return KsResponse.KS_SUCCESS.toDataResponse(mapOf("insertCount" to insertCount))
        }
        catch (e: Exception){
            log.error("### /voting-history ERROR ::: ${e.printStackTrace()}")
            return KsResponse.KS_SERVER_INTERNAL_ERROR.toResponse()
        }
    }

    @Operation(
        summary = "[Anyway] proposal checkpoint update",
        description = "현재 proposal checkpoint 없는 데이터 체크포인트 조회 및 업데이트"
    )
    @PostMapping("/proposal/checkpoint")
    fun updateCheckpointProposal(): ResponseEntity<KstadimuResponseEntity>{
        try {
            var insertCount = dataMigrationService.updateCheckpointProposal()

            return KsResponse.KS_SUCCESS.toDataResponse(mapOf("proposalId" to insertCount))
        }
        catch (e: Exception){
            log.error("### /voting-history ERROR ::: ${e.printStackTrace()}")
            return KsResponse.KS_SERVER_INTERNAL_ERROR.toResponse()
        }
    }

    @Operation(
        summary = "[Anyway] voting history update",
        description = "현재 proposal voting-history proposalId 없는 데이터 조회 및 proposal 값 업데이트"
    )
    @PostMapping("/voting-history/proposal-id")
    fun updateProposalIdForVotingHistory(): ResponseEntity<KstadimuResponseEntity>{
        try {
            var result = dataMigrationService.updateVotingHistoryAddToProposalId()

            return KsResponse.KS_SUCCESS.toDataResponse(mapOf("result" to result))
        }
        catch (e: Exception){
            log.error("### /voting-history ERROR ::: ${e.printStackTrace()}")
            return KsResponse.KS_SERVER_INTERNAL_ERROR.toResponse()
        }
    }
}