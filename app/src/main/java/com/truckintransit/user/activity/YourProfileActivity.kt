package com.truckintransit.user.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.truckintransit.user.R
import com.truckintransit.user.base.BaseActivity
import kotlinx.android.synthetic.main.activity_your_profile.*

class YourProfileActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_profile)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setTitle("Your Profile")
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.yellow))

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {

            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
