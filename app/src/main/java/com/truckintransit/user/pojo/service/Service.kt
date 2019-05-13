package com.truckintransit.user.pojo.service

data class Service(
    val id: String,
    val name: String,
    @Transient var isCloured: Boolean
)