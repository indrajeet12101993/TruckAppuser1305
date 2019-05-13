package com.truckintransit.user.activity

import android.os.Bundle
import android.view.MenuItem
import com.truckintransit.user.R
import com.truckintransit.user.base.BaseActivity
import kotlinx.android.synthetic.main.app_bar_dash_board_actvity.*

class ReferAndEarnActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refer_and_earn)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
