package com.truckintransit.user.activity


import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.truckintransit.user.R
import com.truckintransit.user.base.BaseActivity
import com.truckintransit.user.fragment.login.FragmentLoginNumber

class UserEnterOtpActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_enter_otp)
        addNumberFragment()
    }

    private fun addNumberFragment() {

        val homeFragment = FragmentLoginNumber()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.place_holder_for_fragment, homeFragment)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit()
    }
}
