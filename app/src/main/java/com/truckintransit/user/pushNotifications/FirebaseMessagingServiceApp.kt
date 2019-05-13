package com.truckintransit.user.pushNotifications

import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.truckintransit.user.base.BaseApplication
import com.truckintransit.user.constants.AppConstants.INTENTBOOKINGCONFIRMED
import com.truckintransit.user.constants.AppConstants.NOTIFACTIONBOOKINGAPPROVEDCHANEELDESCRIPTION
import com.truckintransit.user.constants.AppConstants.NOTIFACTIONBOOKINGAPPROVEDCHANEELID
import com.truckintransit.user.constants.AppConstants.NOTIFACTIONBOOKINGAPPROVEDCHANEELNAME
import com.truckintransit.user.constants.AppConstants.NOTIFACTIONBOOKINGAPPROVEDID
import com.truckintransit.user.dataprefence.DataManager
import com.truckintransit.user.pojo.notificationAccept.ResponseNotificationBookingConfirmed


class FirebaseMessagingServiceApp: FirebaseMessagingService() {
    lateinit var dataManager: DataManager
    lateinit var mNotificationHelper: NotificationHelper
    override fun onNewToken(newToken: String?) {
        super.onNewToken(newToken)

        Log.e("firebasetoken",newToken)



   }

    override fun onMessageReceived(remoteMessage : RemoteMessage?) {
        super.onMessageReceived(remoteMessage )
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        mNotificationHelper = BaseApplication.baseApplicationInstance.getNotificationHelper()

        if (remoteMessage!!.data.isNotEmpty()) {

            val paramsbody = remoteMessage.data["alldata"]
            dataManager.setUserDetails(paramsbody!!)

            val notification = Gson().fromJson(dataManager.getUserDetails(), ResponseNotificationBookingConfirmed::class.java)

            val pushNotification = Intent(INTENTBOOKINGCONFIRMED)
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)
            mNotificationHelper.registerNotificationChannelChannel(NOTIFACTIONBOOKINGAPPROVEDCHANEELID,
                NOTIFACTIONBOOKINGAPPROVEDCHANEELNAME,NOTIFACTIONBOOKINGAPPROVEDCHANEELDESCRIPTION
            )
            mNotificationHelper.normalNotification(NOTIFACTIONBOOKINGAPPROVEDID,NOTIFACTIONBOOKINGAPPROVEDCHANEELID,notification.title,notification.body)




        }


    }


}