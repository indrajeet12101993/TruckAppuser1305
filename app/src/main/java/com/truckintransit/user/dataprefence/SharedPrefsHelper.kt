package com.truckintransit.user.dataprefence

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.truckintransit.user.constants.AppConstants.PREFILENAME
import com.truckintransit.user.constants.AppConstants.PREF_KEY_IS_LOGGED_IN
import com.truckintransit.user.constants.AppConstants.PREF_KEY_USER_DETAILS
import com.truckintransit.user.constants.AppConstants.PREF_KEY_USER_Id


class SharedPrefsHelper(context: Context) {
    internal var mSharedPreferences: SharedPreferences

    init {
        mSharedPreferences = context.getSharedPreferences(PREFILENAME, MODE_PRIVATE)
    }


    var loggedInMode: Boolean
        get() = mSharedPreferences.getBoolean(PREF_KEY_IS_LOGGED_IN, false)
        set(loggedInMode) = with(mSharedPreferences.edit()) {
            putBoolean(PREF_KEY_IS_LOGGED_IN, loggedInMode)
            apply()
        }

    var userDetails: String?
        get() = mSharedPreferences.getString(PREF_KEY_USER_DETAILS,null)
        set(userDetails) = with(mSharedPreferences.edit()) {
            putString(PREF_KEY_USER_DETAILS, userDetails)
            apply()
        }

    var userId: String?
        get() = mSharedPreferences.getString(PREF_KEY_USER_Id,null)
        set(userId) = with(mSharedPreferences.edit()) {
            putString(PREF_KEY_USER_Id, userId)
            apply()
        }
    fun clear() {
        with(mSharedPreferences.edit()) {
            clear()
            apply()
        }


    }

}