package com.truckintransit.user.pojo.service

data class ResponseFromServerForServiceList(
    val Livedriver: List<Livedriver>,
    val response_code: String,
    val response_message: String,
    val services: List<Service>,
    val truckloaddetail: List<Truckloaddetail>,
    val vehiclebodytype: List<Vehiclebodytype>,
    val vehiclefueltype: List<Vehiclefueltype>,
    val vehicletype: List<Vehicletype>
)