package com.crypted.kssupport.api

import jakarta.annotation.PostConstruct
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

@Component
class GndServiceApi {
    private lateinit var _webClient: WebClient

    @Value(value ="\${kstadium.api.ground-chain-service}")
    lateinit var url: String
    private val log = KotlinLogging.logger {}

    @PostConstruct
    private fun gndServiceApi(){
        _webClient = WebClient.create(url)
    }


    /**
     * Response 정보 없음
     */
    fun postCheckPoint(datetime: String) {
        log.info("#### checkpoint API datetime :: $datetime")
        _webClient.post()
            .uri("/admin/checkpoints")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromFormData("dateString", datetime))
            .retrieve()
            .bodyToMono(String::class.java)
            .block()
    }

    fun postSnapShots(datetime: String) {
        log.info("#### checkpoint API datetime :: $datetime")
        _webClient.post()
            .uri("/admin/snapshots")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromFormData("dateString", datetime))
            .retrieve()
            .bodyToMono(String::class.java)
            .block()
    }
}