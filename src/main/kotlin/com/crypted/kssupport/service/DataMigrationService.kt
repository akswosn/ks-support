package com.crypted.kssupport.service

import com.crypted.kssupport.api.GndServiceApi
import com.crypted.kssupport.entity.doc.ProposalEntity
import com.crypted.kssupport.entity.doc.VotingHistoryEntity
import com.crypted.kssupport.entity.sub.TbChProposalTransactionEntity
import com.crypted.kssupport.repository.doc.*
import com.crypted.kssupport.repository.main.MemberRepository
import com.crypted.kssupport.repository.sub.TbChProposalRepository
import com.crypted.kssupport.repository.sub.TbChProposalTransactionRepository
import com.crypted.kssupport.utils.CommonUtils
import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.*
import java.time.format.DateTimeFormatter
import javax.swing.text.DateFormatter

@Service
class DataMigrationService(
    private val tbChProposalRepository: TbChProposalRepository,
    private val checkpointRepository: CheckpointRepository,
    private val memberRepository: MemberRepository,
    private val gndServiceApi: GndServiceApi,
    private val proposalRepository: ProposalRepository,
    private val votingHistoryRepository: VotingHistoryRepository,
    private val proposalTransactionRepository: TbChProposalTransactionRepository,
    private val commonUtils: CommonUtils,
    private val soSnapshotRepository: SoSnapshotRepository,
    private val somSnapshotRepository: SomSnapshotRepository,
    private val votingPowerRepository: VotingPowerRepository
) {
    private val log = KotlinLogging.logger {}

    @Value(value = "\${kstadium.time}")
    lateinit var time: String

    fun proposalCheckPoint() : List<String>{
        var callDate = arrayListOf<String>()
        var result = arrayListOf<String>()
        var proposals = tbChProposalRepository.findAll()

        if(proposals.isNotEmpty()){
            for (asisProposal in proposals) {

                log.info("### asisProposal.firstRegisterDate ::: ${asisProposal.firstRegisterDate}")


                var paramDateStr = if(asisProposal.firstRegisterDate!!.hour >= 16){
                    asisProposal.firstRegisterDate!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                } else {
                    asisProposal.firstRegisterDate!!.minusDays(1L).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                }


                if (callDate.indexOf(paramDateStr) == -1) {
                    callDate.add(paramDateStr)

                    var date = asisProposal.firstRegisterDate
                    var start = date!!.minusDays(1L)
                    var end = date!!


                    var checkpoint = checkpointRepository.findFirstByCreatedAtBetween(
                        start, end
                    )
                    log.info("### [ paramDateStr = $paramDateStr ]checkpoint : $checkpoint")
                    if(checkpoint == null){
                        gndServiceApi.postCheckPoint(paramDateStr)
                        result.add(paramDateStr)
                    }
                }
            }
        }
        return result
    }

    fun initProposalDataCallAPI(): List<String>{
        var callDate = arrayListOf<String>()
        var proposals = tbChProposalRepository.findAll()
        if(proposals.isNotEmpty()){
            for (asisProposal in proposals) {
                var paramDateStr =
                    asisProposal.firstRegisterDate!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                var date = asisProposal.firstRegisterDate
                var start = date!!.minusDays(1L)
                var end = date!!

                if (callDate.indexOf(paramDateStr) == -1) {
                    callDate.add(paramDateStr)

                    var checkpoint = checkpointRepository.findFirstByCreatedAtBetween(
                        start, end
                    )
                    log.info("### checkpoint : $checkpoint")
                    if(checkpoint != null) {
                        var soSnapshot = soSnapshotRepository.findAllByCheckpoint(checkpoint.id!!)
                        var somSnapshot = somSnapshotRepository.findAllByCheckpoint(checkpoint.id!!)
                        if(soSnapshot.isEmpty() || somSnapshot.isEmpty()){
                            gndServiceApi.postSnapShots(paramDateStr)
                        }
                    }
                }
            }
        }
        return callDate
    }

    @Transactional
    fun proposalDataMigration(isCompulsion: Boolean): Int {
        //1.as-is 조회
        var df = DecimalFormat("0")
        var data = proposalRepository.findAll()
        var callDate = arrayListOf<String>();
        if (data.isEmpty() || isCompulsion) {// 데이터 없는경우
            if (data.isNotEmpty()) {//데이터 조기화
                proposalRepository.deleteAll()
            }
            var asIsProposalList = tbChProposalRepository.findAll()


            var apiCall = 0;
            var insertList = arrayListOf<ProposalEntity>()
            for (asisProposal in asIsProposalList) {
                var date = asisProposal.firstRegisterDate
                var start = date!!.minusDays(1L)
                var end = date!!

                var checkpoint = checkpointRepository.findFirstByCreatedAtBetween(
                    start, end
                )
                log.info("### checkpoint : $checkpoint")


                var transaction: TbChProposalTransactionEntity? = null
                if (asisProposal.proposalId != null) {
                    transaction = proposalTransactionRepository.findFirstByProposalIdAndStatusNotIn(
                        asisProposal.proposalId!!, arrayListOf("Deposit", "Vote")
                    )
                }

                log.info("#### transaction ::: $transaction")

                if (checkpoint == null) {
                    //API CALL
//                    log.info("### checkpoint start : $start")
//                    log.info("### checkpoint end : $end")
//체크포인트 이관
//                    try {
//                        var paramDateStr =
//                            asisProposal.firstRegisterDate!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//                        if (callDate.indexOf(paramDateStr) == -1) {
//                            callDate.add(paramDateStr)
//                            gndServiceApi.postCheckPoint(paramDateStr)
//                            apiCall++;
//                        }
//                    } catch (e: Exception) {
//                        log.warn("### API ERROR ::: ${e.printStackTrace()}")
//                    } finally {
////                        Thread.sleep(5000)
//                        checkpoint = checkpointRepository.findFirstByCreatedAtBetween(start, end)
//                        log.info("### checkpoint API After : $checkpoint")
//                    }
                }
                var member = memberRepository.findByUserId(asisProposal.userId!!)
                log.info("### member : $member")

//                val supplier: () -> String? = { asisProposal.status.substring(0, 1).uppercase()}

                var proposal = ProposalEntity(
                    checkpoint = checkpoint?.id,
                    memberId = member?.id,
                    currency = 0L,
                    deposit = df.format(BigDecimal(10000).multiply(BigDecimal.ONE.scaleByPowerOfTen(18))),
                    txHash = asisProposal.txHash,
                    txStatus = asisProposal.txStatus,
                    proposalIdFromContract = asisProposal.proposalId,
                    title = asisProposal.title,
                    content = asisProposal.content,
//                    status = commonUtils.passException<String?>(supplier),
                    status = asisProposal.status,
                    isDeleted = asisProposal.deleteYn,
                    isExposed = asisProposal.displayYn,
                    forumUrl = asisProposal.forumUrl,
                    expirationDate = asisProposal.expirationDate,
                    createdAt = asisProposal.firstRegisterDate,
                    updatedAt = asisProposal.lastModifyDate,
                    userId = asisProposal.userId,
                    endStatus = transaction?.status,
                    endTxHash = transaction?.txHash,
                    endCreateAt = transaction?.firstRegisterDate
                )
                log.info("### proposal ::: $proposal")
                insertList.add(proposal)
            }
            proposalRepository.saveAll(insertList)
            return insertList.size
        }
        return 0
    }

    @Transactional
    fun votingHistoryDataMigration(isCompulsion: Boolean): Int {
        var votingHisList = votingHistoryRepository.findAll()
        var df = DecimalFormat("0")
        log.info("### votingHisList ::: $votingHisList")
        if (votingHisList.isEmpty() || isCompulsion) {
            if (votingHisList.isNotEmpty()) {//데이터 조기화
                votingHistoryRepository.deleteAll()
            }

            var proposalMap = proposalRepository.findAll()
                .map { it.proposalIdFromContract to it }.toMap()
            var asIsProposalTxList = proposalTransactionRepository.findAllByStatus("Vote")
            var insertList = arrayListOf<VotingHistoryEntity>()


            for (proposalTx in asIsProposalTxList) {
                var proposal = proposalMap[proposalTx.proposalId]
                var memberId = null as Long?

                if (proposalTx?.userId != null) {
                    var member = memberRepository.findByUserId(proposalTx.userId!!)
                    memberId = member?.id
                } else {
                    memberId = 0
                }

                var entity = VotingHistoryEntity(
                    proposal = proposal?.id,
                    soId = commonUtils.passException<Long?> { proposalTx?.soId?.toLong() },
                    memberId = memberId,
                    votingPower = if (proposalTx?.votingPower != null) {
                        df.format(BigDecimal(proposalTx?.votingPower).scaleByPowerOfTen(11))
                    } else {
                        null
                    },
                    result = proposalTx?.voteYn,
                    userId = proposalTx?.userId,
                    address = proposalTx?.address,
                    createdAt = proposalTx.firstRegisterDate,
                    proposalIdFromContract = proposal?.proposalIdFromContract
                )
                log.info("#### entity :::: $entity")
                insertList.add(entity)
            }

            votingHistoryRepository.saveAll(insertList)
            return insertList.size
        }

        return 0
    }

    fun updateCheckpointProposal(): MutableList<String?>?{
        var notCheckPoint = proposalRepository.findAllByCheckpoint(null)

        log.info("#### notCheckPoint :: ${notCheckPoint.size}")

        var updateEntity = mutableListOf<ProposalEntity>()

        for (it in notCheckPoint){
            var date = it.createdAt
            var start = date!!.minusDays(1L)
            var end = date!!
            log.info("#### notCheckPoint :: ${it.createdAt}")
            log.info("#### notCheckPoint start :: ${start}")
            log.info("#### notCheckPoint end :: ${end}")


            var checkpoint = checkpointRepository.findFirstByCreatedAtBetween(start, end)
            log.info("#### checkpoint ::: $checkpoint")
            if(checkpoint != null){
                it.checkpoint = checkpoint.id   //checkpoint update
                updateEntity.add(it)
            }
            else {
                log.info("### notting ::: $it")
            }
        }

        log.info("[siez=${updateEntity.size}] ::: $updateEntity")
        proposalRepository.saveAll(updateEntity)


        return updateEntity.map { it.proposalIdFromContract }.toList().toMutableList()
    }

    fun updateVotingHistoryAddToProposalId() : Int{
        var votingHistoryNotProposalId = votingHistoryRepository.findAllByProposalIdFromContract(null)
        var proposalMap: Map<ObjectId, String?> = proposalRepository.findAll().map { it.id!! to it.proposalIdFromContract }.toMap()

        log.info("### votingHistoryNotProposalId ::: $votingHistoryNotProposalId")
        var result = 0
        for(voting in votingHistoryNotProposalId){
            if(proposalMap.get(voting.proposal) != null){
                voting.proposalIdFromContract = proposalMap[voting.proposal]!!
                result++
            }
        }
        log.info("### votingHistoryNotProposalId ::: $votingHistoryNotProposalId")
        votingHistoryRepository.saveAll(votingHistoryNotProposalId)

        return result
    }
}