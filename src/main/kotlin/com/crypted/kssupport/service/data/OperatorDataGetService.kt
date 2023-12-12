package com.crypted.kssupport.service.data

import com.crypted.kssupport.repository.doc.TransactionRepository
import com.crypted.kssupport.repository.main.MemberRepository
import com.crypted.kssupport.vo.ComunityTxVo
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class OperatorDataGetService (
        private val transactionRepository: TransactionRepository,
        private val memberRepository: MemberRepository
){
    private val log = KotlinLogging.logger {}

    fun communityInvestment(start: Long, end: Long): MutableList<ComunityTxVo>{
        //PROD
        var contractAddress = "0xc7bb408ca8b0a6af52C2699919eE9E443E92806B"
        var functionName = "investment"

        var list = transactionRepository.findAllByBlockNumberAndToAndFunctionName(start, end, contractAddress, functionName)
        log.info ("tx list ::: $list")

        var addressList: List<String> = list.map { it.from }.distinct().toList() as List<String>
        log.info ("addressList ::: $addressList")

        var members = memberRepository.findAllByAddressIn(addressList)
                .map { it.address to it.userId }.toMap()

        var result = mutableListOf<ComunityTxVo>()
        for(tx in list){
            var userId = members[tx.from]
            result.add(ComunityTxVo(
                    address = tx.from,
                    userId = userId,
                    txHash = tx.hash,
                    amount = tx.value?.toPlainString(),
                    isSuccess = if(tx.status == 1){
                        "Success"
                    }
                    else {
                        "Fail"
                    }
            ))
        }
        return result
    }
}