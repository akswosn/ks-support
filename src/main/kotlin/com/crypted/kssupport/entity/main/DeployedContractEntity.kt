package com.crypted.kssupport.entity.main

import jakarta.persistence.*

@Entity
@Table(name = "member")
class DeployedContractEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        var id: Long? = null,

) {
}