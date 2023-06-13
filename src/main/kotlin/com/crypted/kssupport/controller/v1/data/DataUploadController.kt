package com.crypted.kssupport.controller.v1.data

import com.crypted.kscommon.response.KsResponse
import com.crypted.kscommon.response.KstadimuResponseEntity
import com.crypted.kssupport.service.DataUploadService
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/data")
class DataUploadController(
    private val dataUploadService: DataUploadService
) {
    private val logger = KotlinLogging.logger {}

    @PostMapping("/proposal")
    fun proposal(): ResponseEntity<KstadimuResponseEntity>{
        return KsResponse.KS_SUCCESS.toResponse()
    }


    @PostMapping("/voting-history")
    fun votingHistory(): ResponseEntity<KstadimuResponseEntity>{
        return KsResponse.KS_SUCCESS.toResponse()
    }
}