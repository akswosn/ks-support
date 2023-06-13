package com.crypted.kssupport.service

import com.crypted.kssupport.entity.doc.ProposalEntity
import com.crypted.kssupport.entity.main.MemberEntity
import com.crypted.kssupport.repository.doc.CheckpointRepository
import com.crypted.kssupport.repository.main.MemberRepository
import com.crypted.kssupport.repository.sub.TbChProposalRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class DataUploadService (
    private val tbChProposalRepository: TbChProposalRepository,
    private val checkpointRepository: CheckpointRepository,
    private val memberRepository: MemberRepository,
){
    private val log = KotlinLogging.logger {}


    fun proposalDataMigration(){
        //1.as-is 조회
        var asIsProposalList = tbChProposalRepository.findAll()
//        log.info ("### asIsProposalList : $asIsProposalList")
        var insertList = arrayListOf<ProposalEntity>()
        for(asisProposal in asIsProposalList){
            var date = asisProposal.firstRegisterDate?.toLocalDate()
            var checkpoint = checkpointRepository.findFirstByCreatedAtBetween(
                LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX))
            log.info ("### checkpoint : $checkpoint")

            if(checkpoint == null){
                //API CALL
            }
            var member = memberRepository.findByUserId(asisProposal.userId!!)
            log.info ("### member : $member")

            var proposal = ProposalEntity(
                checkpoint = checkpoint?.id,
                memberId = member?.id,
                currency = 0L,
                deposit = BigDecimal("10000").scaleByPowerOfTen(18),
                txHash = asisProposal.txHash,
                txStatus = asisProposal.txStatus,
                proposalIdFromContract = asisProposal.proposalId,
                title = asisProposal.title,
                content = asisProposal.content,
                status = asisProposal.status,
                isDeleted = asisProposal.deleteYn,
                isExposed = asisProposal.displayYn,
                forumUrl = asisProposal.forumUrl,
                expirationDate = asisProposal.expirationDate,
                createdAt = asisProposal.firstRegisterDate,
                updatedAt = asisProposal.lastModifyDate,
                userId = asisProposal.userId
            )
            log.info("### proposal ::: $proposal")
            insertList.add(proposal)
        }

    }
}