package com.truckintransit.user.activity


import android.os.Bundle
import com.google.gson.Gson

import com.truckintransit.user.R
import com.truckintransit.user.base.BaseActivity
import com.truckintransit.user.base.BaseApplication
import com.truckintransit.user.constants.UserBooking.MUserDropPlace1
import com.truckintransit.user.constants.UserBooking.MUserDropPlace2
import com.truckintransit.user.constants.UserBooking.MUserDropPlace3
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace1
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace2
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace3
import com.truckintransit.user.dataprefence.DataManager
import com.truckintransit.user.pojo.notificationAccept.ResponseNotificationBookingConfirmed
import kotlinx.android.synthetic.main.app_bar_dash_board_actvity1.*
import kotlinx.android.synthetic.main.bottom_sheet_for_user_pickup1.*


class RiderBookDeatialActivity : BaseActivity() {

    private lateinit var dataManager: DataManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rider_book_deatial)
        tv_pickup1.text = "1. " + MUserSelectPlace1
        tv_pickup2.text = "2. " + MUserSelectPlace2
        tv_pickup3.text = "3. " + MUserSelectPlace3
        tv_drop1.text = "1. " + MUserDropPlace1
        tv_drop2.text = "2. " + MUserDropPlace2
        tv_drop3.text = "3. " + MUserDropPlace3

        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()

        val notification = Gson().fromJson(dataManager.getUserDetails(), ResponseNotificationBookingConfirmed::class.java)
        tv_operator_name.text="Driver Name"+" "+ notification.data1.drivername
        tv_vehiclenumber.text= "Vehicle Number"+" "+notification.data1.drivername
        tvcalldriver.text= "Driver Number:"+" "+notification.data1.drivernumber
        tv_otp.text= "Show Otp to driver :"+" "+notification.data1.startrideotp



    }


}
