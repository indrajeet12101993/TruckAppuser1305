package com.truckintransit.user.base

import android.app.Application
import android.util.Log

import com.truckintransit.user.dataprefence.DataManager
import com.truckintransit.user.dataprefence.SharedPrefsHelper
import com.truckintransit.user.pushNotifications.NotificationHelper
import com.truckintransit.user.pushNotifications.NotificationUtils


class BaseApplication :Application(){

    companion object {
        lateinit var baseApplicationInstance: BaseApplication
            private set
    }
    lateinit var dataManager: DataManager
    private lateinit var notificationHelper: NotificationHelper

    override fun onCreate() {
        super.onCreate()
        baseApplicationInstance= this
        val sharedPrefsHelper = SharedPrefsHelper(applicationContext)
        dataManager = DataManager(sharedPrefsHelper)
        Log.i("AppApplication","appSignatures = ${AppSignatureHelper(this).getAppSignatures()}")
        val notificationUtils = NotificationUtils(applicationContext)
        notificationHelper = NotificationHelper(notificationUtils)


    }

    fun getdatamanger(): DataManager {

        return dataManager
    }

    fun getNotificationHelper(): NotificationHelper {

        return notificationHelper
    }


}