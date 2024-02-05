package com.crypted.kssupport.entity.main

import jakarta.persistence.*

@Entity
@Table(name = "token")
class TokenEntity (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        var id: Long? = null,
        @Column(name = "address")
        var address: String = "",
        @Column(name = "symbol")
        var symbol: String = ""
){
}