package com.crypted.kssupport.repository.sub

import com.crypted.kssupport.entity.sub.TbChProposalEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TbChProposalRepository : JpaRepository<TbChProposalEntity, Long> {
}