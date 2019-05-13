package com.truckintransit.user.pojo.service

data class Truckloaddetail(
    val description: String,
    val id: String,
    val load_kg: String,
    val truck_image: String,
    @Transient var isCloured: Boolean
)