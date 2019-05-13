package com.truckintransit.user.pojo.paytm

data class ResponseFromPaytmChecksum(
    val CHECKSUMHASH: String,
    val ORDER_ID: String,
    val payt_STATUS: String
)