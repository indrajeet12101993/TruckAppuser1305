package com.truckintransit.user.activity


import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.truckintransit.user.R
import com.truckintransit.user.base.BaseActivity
import com.truckintransit.user.base.BaseApplication
import com.truckintransit.user.constants.AppConstants.SPLASH_DELAY
import com.truckintransit.user.dataprefence.DataManager

class SplashActivity : BaseActivity() {
    private var mDelayHandler: Handler? = null
    lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
    }

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {

            if(dataManager.getLoggedIn()){

                launchActivityWithFinish<DashBoardActvity>()

            }else{
                launchActivityWithFinish<WelcomeActivity>()
            }



        }
    }

    override fun onDestroy() {
        mDelayHandler?.removeCallbacks(mRunnable)
        super.onDestroy()
    }

    override fun onBackPressed() {
        mDelayHandler?.removeCallbacks(mRunnable)
        super.onBackPressed()
    }
}
