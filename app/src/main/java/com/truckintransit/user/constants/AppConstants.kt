package com.truckintransit.user.constants

object AppConstants {

    internal const val PREFILENAME = "TTUCKAPPPREFENCE"
    internal const val PREF_KEY_IS_LOGGED_IN = "PREF_KEY_IS_LOGGED_IN"
    internal const val PREF_KEY_USER_DETAILS = "PREF_KEY_USER_DETAILS"
    internal const val PREF_KEY_USER_Id = "PREF_KEY_USER_Id"
    internal const val SPLASH_DELAY: Long = 3000L // 3 seconds
    internal const val BASE_URL:String="http://technowhizzit.com/Truckintransit/"
    internal const val INTENTBOOKINGCONFIRMED: String="INTENTBOOKINGCONFIRMED"
    internal const val NOTIFACTIONBOOKINGAPPROVEDCHANEELID: String="1"
    internal const val NOTIFACTIONBOOKINGAPPROVEDCHANEELNAME: String="NOTIFICATION APPRROVED"
    internal const val NOTIFACTIONBOOKINGAPPROVEDCHANEELDESCRIPTION: String="NOTIFICATION APPRROVED "
    internal const val NOTIFACTIONBOOKINGAPPROVEDID: Int=1

    internal const val REQUEST_LOCATION_PERMISSION: Int=1
    internal const val REQUEST_LOCATION_PERMISSION_LOCATION: Int=2
    internal const val  PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP1 :Int= 1
    internal const val  PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP2 :Int= 2
    internal const val  PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP3 :Int= 3

    val PAYTM_CALLBACK_URL = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=%s"
    val IS_PATM_STAGIN = true


}