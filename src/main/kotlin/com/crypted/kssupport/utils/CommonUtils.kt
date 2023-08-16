package com.crypted.kssupport.utils

import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.util.function.Supplier

@Component
class CommonUtils {
    private val log = KotlinLogging.logger {}

    fun <T> passException(supplier: Supplier<T>): T? {
        try {
            return supplier.get()
        } catch (e: Exception) {
            //checked Exception은 로그에 남기지 말자
            log.debug("### passException ::: {}", e)
        }
        return null
    }

    /**
     * PassException void 용
     * @param runnable
     */
    fun passException(runnable: Runnable) {
        try {
            runnable.run()
        } catch (e: Exception) {
            //checked Exception은 로그에 남기지 말자
            log.debug("### passException ::: {}", e)
        }
    }
}