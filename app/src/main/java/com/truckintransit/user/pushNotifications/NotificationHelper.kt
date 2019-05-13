package com.truckintransit.user.pushNotifications

class NotificationHelper( var notificationUtils: NotificationUtils)  {

    fun registerNotificationChannelChannel(channelId: String, channelName: String, channelDescription: String) {

        notificationUtils.registerNotificationChannelChannel(channelId,channelName,channelDescription)
    }

    fun normalNotification(notificationId: Int,channelId: String,title: String,description:String){


        notificationUtils.normalNotification(notificationId,channelId,title,description)

    }
}