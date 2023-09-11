package com.crypted.kssupport.service

import com.crypted.kssupport.api.GndServiceApi
import com.crypted.kssupport.entity.doc.*
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
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.ArrayList
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
        private val votingPowerRepository: VotingPowerRepository,
        private val soVotingPowerRepository: SoVotingPowerRepository
) {
    private val log = KotlinLogging.logger {}

    @Value(value = "\${kstadium.time}")
    lateinit var time: String

    fun proposalCheckPoint(): List<String> {
        var callDate = arrayListOf<String>()
        var result = arrayListOf<String>()
        var proposals = tbChProposalRepository.findAll()

        if (proposals.isNotEmpty()) {
            for (asisProposal in proposals) {

                log.info("### asisProposal.firstRegisterDate ::: ${asisProposal.firstRegisterDate}")


                var paramDateStr = asisProposal.firstRegisterDate!!.minusDays(1L).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                if (callDate.indexOf(paramDateStr) == -1) {
                    callDate.add(paramDateStr)

                    var date = asisProposal.firstRegisterDate
                    var start = date!!.minusDays(2L)
                    var end = date!!.minusDays(1L)


                    var checkpoint = checkpointRepository.findFirstByCreatedAtBetween(
                            start, end
                    )
                    log.info("### [ paramDateStr = $paramDateStr ]checkpoint : $checkpoint")
                    if (checkpoint == null) {
                        gndServiceApi.postCheckPoint(paramDateStr)
                        result.add(paramDateStr)
                    }
                }
            }
        }
        return result
    }

    fun initProposalDataCallAPI(): List<String> {
        var callDate = arrayListOf<String>()
        var proposals = tbChProposalRepository.findAll()
        if (proposals.isNotEmpty()) {
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
                    if (checkpoint != null) {
                        var soSnapshot = soSnapshotRepository.findAllByCheckpoint(checkpoint.id!!)
                        var somSnapshot = somSnapshotRepository.findAllByCheckpoint(checkpoint.id!!)
                        if (soSnapshot.isEmpty() || somSnapshot!!.isEmpty()) {
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
        var callDate = arrayListOf<String>()
        if (data.isEmpty() || isCompulsion) {// 데이터 없는경우
            if (data.isNotEmpty()) {//데이터 조기화
                proposalRepository.deleteAll()
            }
            var asIsProposalList = tbChProposalRepository.findAll()


            var apiCall = 0;
            var insertList = arrayListOf<ProposalEntity>()
            for (asisProposal in asIsProposalList) {
                var date = asisProposal.firstRegisterDate
                var start = date!!.minusDays(2L)
                var end = date!!.minusDays(1L)

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

    fun updateCheckpointProposal(): MutableList<String?>? {
        var notCheckPoint = proposalRepository.findAllByCheckpoint(null)

        log.info("#### notCheckPoint :: ${notCheckPoint.size}")

        var updateEntity = mutableListOf<ProposalEntity>()

        for (it in notCheckPoint) {
            var date = it.createdAt
            var start = date!!.minusDays(1L)
            var end = date!!
            log.info("#### notCheckPoint :: ${it.createdAt}")
            log.info("#### notCheckPoint start :: ${start}")
            log.info("#### notCheckPoint end :: ${end}")


            var checkpoint = checkpointRepository.findFirstByCreatedAtBetween(start, end)
            log.info("#### checkpoint ::: $checkpoint")
            if (checkpoint != null) {
                it.checkpoint = checkpoint.id   //checkpoint update
                updateEntity.add(it)
            } else {
                log.info("### notting ::: $it")
            }
        }

        log.info("[siez=${updateEntity.size}] ::: $updateEntity")
        proposalRepository.saveAll(updateEntity)


        return updateEntity.map { it.proposalIdFromContract }.toList().toMutableList()
    }

    fun updateVotingHistoryAddToProposalId(): Int {
        var votingHistoryNotProposalId = votingHistoryRepository.findAllByProposalIdFromContract(null)
        var proposalMap: Map<ObjectId, String?> = proposalRepository.findAll().map { it.id!! to it.proposalIdFromContract }.toMap()

        log.info("### votingHistoryNotProposalId ::: $votingHistoryNotProposalId")
        var result = 0
        for (voting in votingHistoryNotProposalId) {
            if (proposalMap.get(voting.proposal) != null) {
                voting.proposalIdFromContract = proposalMap[voting.proposal]!!
                result++
            }
        }
        log.info("### votingHistoryNotProposalId ::: $votingHistoryNotProposalId")
        votingHistoryRepository.saveAll(votingHistoryNotProposalId)

        return result
    }

    /**
     * VotingPower Collection Set Request
     */
    fun updateVotingPower(): Int {
        var proposalList = proposalRepository.findAll()
        var result = 0
        for (proposal in proposalList) {
            if (proposal.checkpoint != null) {
                result += setVotingPower(checkPoint = proposal.checkpoint!!)
            }
        }

        return result
    }

    fun updateSoVotingPower(): Int {
        var proposalList = proposalRepository.findAll()
        var result = 0
        for (proposal in proposalList) {
            if (proposal.checkpoint != null) {
                result += setSoVotingPower(checkPoint = proposal.checkpoint!!)
            }
        }

        return result
    }

    /**
     * Voting history for votingPower
     */
    fun updateVotingHistoryForVotingPower(proposalId: String?): Int {
        //voting history voting power 재계산
        var historyList = if(proposalId == null){
            votingHistoryRepository.findAllByMemberIdGt(0L)
        }
        else {
            votingHistoryRepository.findAllByProposalIdFromContractAndMemberIdGt(proposalId, 0L)
        }

        var proposalList = proposalRepository.findAll()
        var proposalMap = proposalList.associateBy { entity: ProposalEntity -> entity.id }



        log.info("### voting ::: $historyList")
        var updateHistory = mutableListOf<VotingHistoryEntity>()
        var i = 0
        for (history in historyList) {
            i++
            var proposal = proposalMap[history.proposal]
            var checkPoint = proposal?.checkpoint

            if (history.memberId == null || history.soId == null) {//member , so 정보 없는 데이터 제외
                continue
            }

            if (checkPoint != null) {
                //VotingPower create
                var votingPowerEntity = votingPowerRepository.findFirstByCheckpointAndSoIdAndMemberId(checkPoint, history.soId!!, history.memberId!!)
                log.info("#### [$i / ${historyList.size}] votingPowerEntity ::: $votingPowerEntity")
//                var votingPowerEntity = votingPowerRepository.findFirstByCheckpointAndSoIdAndMemberId(checkPoint, history.soId!!, history.memberId!!)
                if (votingPowerEntity != null) {
                    if (votingPowerEntity?.votingPower != history?.votingPower) {
                        history.votingPower = votingPowerEntity?.votingPower
                        updateHistory.add(history)
                    }
                }
            }
        }
        if (updateHistory.size > 0) {
            votingHistoryRepository.saveAll(updateHistory)
        }

        return updateHistory.size
    }

    /**
     * check point 기준으로 저장
     */
    fun setVotingPower(checkPoint: ObjectId): Int {
        var votingPowerList = votingPowerRepository.findAllByCheckpoint(checkPoint)
        if (votingPowerList.isNotEmpty()) {
            return 0
        }
        var result = 0

        var somList = somSnapshotRepository.findAllByCheckpoint(checkPoint, 1L)
        somList = somList?.filter {
            it.sop?.compareTo(
                    BigDecimal(100L).scaleByPowerOfTen(18))!! > -1
        }

        if (somList != null) {
            var totalSop = somList.sumOf { it.sop!! }
            var somListMap = somList.groupBy { it.soId!! }

            var insertVotingPower = mutableListOf<VotingPowerEntity>()

            for (soId in somListMap.keys) {
                //회원 member voting 데이터 적제
                if (somListMap[soId] != null) {
                    val totalSoSop = somListMap[soId]?.sumOf { selector: SomSnapshotEntity -> selector.sop!! }
                    for (som in somListMap[soId]!!) {
                        var votingPower = som.sop?.scaleByPowerOfTen(18)!!
                                .divide(totalSoSop, 0, RoundingMode.DOWN)
                        var soVotingPowerEntity = totalSoSop?.scaleByPowerOfTen(18)!!
                                .divide(totalSop, 0, RoundingMode.DOWN)

                        log.info("###  soId=$soId, som.memberId=${som.memberId}")
                        if (som.memberId == null) {
                            continue
                        }

                        var votingPowerEntity = votingPowerRepository.findFirstByCheckpointAndSoIdAndMemberId(checkPoint, soId, som.memberId!!)

                        if (votingPowerEntity != null) {
                            continue
                        } else {
                            var insert = VotingPowerEntity(
                                    checkpoint = checkPoint,
                                    soId = soId,
                                    memberId = som.memberId,
                                    sop = som.sop?.toPlainString(),
                                    soSopTotal = totalSoSop?.toPlainString(),
                                    votingPower = votingPower.toPlainString(),
                                    createdAt = LocalDateTime.now(),
                                    soVotingPower = soVotingPowerEntity.toPlainString()
                            )

                            insertVotingPower.add(insert)
                        }
                    }
                }
                votingPowerRepository.saveAll(insertVotingPower)
                result = insertVotingPower.size
                insertVotingPower.clear()
            }
        }
        return result
    }

    fun setSoVotingPower(checkPoint: ObjectId): Int {
        var somSnapshot = somSnapshotRepository.findAllByCheckpoint(checkPoint, 1L)
        somSnapshot = somSnapshot?.filter { (it.sop?.compareTo(BigDecimal(100L).scaleByPowerOfTen(18))!! > -1) }
        var soVotingPowerList = ArrayList<SoVotingPowerEntity>()

        if (somSnapshot != null) {
            var total = somSnapshot.sumOf { it.sop!! }
            //.sumOf { selector: SomSnapshotEntity -> selector.sop }
            var grouping = somSnapshot.groupBy { it.soId!! }


            var soSnapshotMap = mutableMapOf<Long, BigDecimal>()
            for (soId in grouping.keys) {
//                var total = grouping[soId]?.sumOf { selector: SomSnapshotEntity -> selector.ratio }
                var totalSop = if (grouping[soId] == null) {
                    BigDecimal.ZERO
                } else {
                    grouping[soId]!!.sumOf { selector: SomSnapshotEntity -> selector.sop!! }
                }

                var count = grouping[soId]?.size
                var soVotingPower = if (totalSop != BigDecimal.ZERO) {
                    totalSop.scaleByPowerOfTen(18).divide(total, 0, RoundingMode.FLOOR)
                } else {
                    totalSop
                }

                soSnapshotMap[soId] = soVotingPower

                var soVoting = SoVotingPowerEntity(
                        checkpoint = checkPoint,
                        soId = soId,
                        somCount = count,
                        sop = totalSop.toPlainString(),
                        votingPower = soVotingPower.toPlainString(),
                        createdAt = LocalDateTime.now()
                )


                soVotingPowerList.add(soVoting)
            }
        }
        soVotingPowerRepository.saveAll(soVotingPowerList)
        return soVotingPowerList.size
    }

    //static 제거
//    companion object {
//        val somSnapShotMap: MutableMap<ObjectId, MutableList<SomSnapshotEntity>> = mutableMapOf()
//    }
}