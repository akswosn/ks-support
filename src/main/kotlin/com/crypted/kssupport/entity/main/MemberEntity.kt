package com.crypted.kssupport.entity.main

import jakarta.persistence.*
import java.time.ZonedDateTime

@Entity
@Table(name = "member")
class MemberEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "user_id")
    var userId: String? = null,

    @Column(name = "password")
    var password: String? = null,

    @Column(name = "name")
    var name: String? = null,

    @Column(name = "email")
    var email: String? = null,

    @Column(name = "phone")
    var phone: String? = null,

    @Column(name = "state")
    var state: String? = null,

    @Column(name = "logged_in_at")
    var loggedInAt: ZonedDateTime? = null,

    @Column(name = "password_changed_at")
    var passwordChangedAt: ZonedDateTime? = null,

    @Column(name = "role")
    var role: String? = null,

    @Column(name = "referral_code")
    var referralCode: String? = null,

    @Column(name = "address")
    var address: String? = null,

    @Column(name = "created_at")
    var createdAt: ZonedDateTime? = null,

    @Column(name = "updated_at")
    var updatedAt: ZonedDateTime? = null,
){
}