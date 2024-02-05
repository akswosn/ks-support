package com.crypted.kssupport.repository.main

import com.crypted.kssupport.entity.main.TokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TokenRepository : JpaRepository<TokenEntity, Long>{

}