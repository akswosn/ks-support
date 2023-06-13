package com.crypted.kssupport.entity.sub

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import java.time.LocalDateTime
@Entity
@Table(name = "TB_CH_PROPOSAL")
class TbChProposalEntity(
    indexNumber : Int? = null,
    proposalId: String? = null,
    userId: String? = null,
    title: String? = null,
    content: String? = null,
    status: String? = null,
    statusDetail: String? = null,
    displayYn: String? = null,
    deleteYn: String? = null,
    forumUrl: String? = null,
    agreeYn: String? = null,
    expirationDate: LocalDateTime? = null,
    txStatus: String? = null,
    txResponseDate: String?= null,
    txHash: String?= null,
    firstRegisterDate: LocalDateTime?= null,
    lastModifyDate: LocalDateTime?= null,
) {
    @Id
    @Column(name = "INDEX_NUMBER")
    var indexNumber : Int? = indexNumber
    @Column(name = "PROPOSAL_ID")
    var proposalId: String? = proposalId
    @Column(name = "USER_ID")
    var userId: String? = userId
    @Column(name = "TITLE")
    var title: String? = title
    @Column(name = "CONTENT")
    var content: String? = content
    @Column(name = "STATUS")
    var status: String? = status
    @Column(name = "STATUS_DETAIL")
    var statusDetail: String? = statusDetail
    @Column(name = "DISPLAY_YN")
    var displayYn: String? = displayYn
    @Column(name = "DELETE_YN")
    var deleteYn: String? = deleteYn
    @Column(name = "FORUM_URL")
    var forumUrl: String? = forumUrl
    @Column(name = "AGREE_YN")
    var agreeYn: String? = agreeYn
    @Column(name = "EXPIRATION_DATE")
    var expirationDate: LocalDateTime? = expirationDate
    @Column(name = "TX_STATUS")
    var txStatus: String? = txStatus
    @Column(name = "TX_RESPONSE_DATE")
    var txResponseDate: String? = txResponseDate
    @Column(name = "TX_HASH")
    var txHash: String? = txHash
    @Column(name = "FIRST_REGISTER_DATE")
    var firstRegisterDate: LocalDateTime? = firstRegisterDate
    @Column(name = "LAST_MODIFY_DATE")
    var lastModifyDate: LocalDateTime? = lastModifyDate


    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}