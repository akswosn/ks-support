package com.crypted.kssupport.entity.sub

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "TB_CH_PROPOSAL_TRANSACTION")
class TbChProposalTransactionEntity (
    indexNumber: Int? = null,
    proposalId: String? = null,
    userId: String? = null,
    soId: String? = null,
    soName: String? = null,
    votingPower: String? = null,
    voteYn: String? = null,
    tokenName: String? = null,
    tokenValue: String? = null,
    status: String? = null,
    txHash: String? = null,
    address: String? = null,
    firstRegisterDate: LocalDateTime? = null,
){
    @Id
    @Column(name = "INDEX_NUMBER")
    var indexNumber: Int? = indexNumber
    @Column(name = "PROPOSAL_ID")
    var proposalId: String? = proposalId
    @Column(name = "USER_ID")
    var userId: String? = userId
    @Column(name = "SO_ID")
    var soId: String? = soId
    @Column(name = "SO_NAME")
    var soName: String? = soName
    @Column(name = "VOTING_POWER")
    var votingPower: String? = votingPower
    @Column(name = "VOTE_YN")
    var voteYn: String? = voteYn
    @Column(name = "TOKEN_NAME")
    var tokenName: String? = tokenName
    @Column(name = "TOKEN_VALUE")
    var tokenValue: String? = tokenValue
    @Column(name = "STATUS")
    var status: String? = status
    @Column(name = "TX_HASH")
    var txHash: String? = txHash
    @Column(name = "ADDRESS")
    var address: String? = address
    @Column(name = "FIRST_REGISTER_DATE")
    var firstRegisterDate: LocalDateTime? = firstRegisterDate
}