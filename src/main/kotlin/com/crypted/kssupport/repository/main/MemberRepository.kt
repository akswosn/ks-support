package com.crypted.kssupport.repository.main

import com.crypted.kssupport.entity.main.MemberEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository: JpaRepository<MemberEntity, Long> {
    fun findByUserId(userId: String): MemberEntity?
}