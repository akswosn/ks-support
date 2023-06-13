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
    @Column(name = "member_user_id")
    var indexNumber : Int? = indexNumber
    @Column(name = "member_user_id")
    var proposalId: String? = proposalId
    @Column(name = "member_user_id")
    var userId: String? = userId
    @Column(name = "member_user_id")
    var title: String? = title
    @Column(name = "member_user_id")
    var content: String? = content
    @Column(name = "member_user_id")
    var status: String? = status
    @Column(name = "member_user_id")
    var statusDetail: String? = statusDetail
    @Column(name = "member_user_id")
    var displayYn: String? = displayYn
    @Column(name = "member_user_id")
    var deleteYn: String? = deleteYn
    @Column(name = "member_user_id")
    var forumUrl: String? = forumUrl
    @Column(name = "member_user_id")
    var agreeYn: String? = agreeYn
    @Column(name = "member_user_id")
    var expirationDate: LocalDateTime? = expirationDate
    @Column(name = "member_user_id")
    var txStatus: String? = txStatus
    @Column(name = "member_user_id")
    var txResponseDate: String? = txResponseDate
    @Column(name = "member_user_id")
    var txHash: String? = txHash
    @Column(name = "member_user_id")
    var firstRegisterDate: LocalDateTime? = firstRegisterDate
    @Column(name = "member_user_id")
    var lastModifyDate: LocalDateTime? = lastModifyDate


    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}