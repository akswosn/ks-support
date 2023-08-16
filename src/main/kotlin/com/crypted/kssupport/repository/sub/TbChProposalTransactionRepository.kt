package com.crypted.kssupport.repository.sub

import com.crypted.kssupport.entity.sub.TbChProposalTransactionEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TbChProposalTransactionRepository : JpaRepository<TbChProposalTransactionEntity, Long> {

    fun findAllByStatus(status: String) : List<TbChProposalTransactionEntity>

    fun findFirstByProposalIdAndStatusNotIn(proposalId: String, status: List<String>): TbChProposalTransactionEntity?
}