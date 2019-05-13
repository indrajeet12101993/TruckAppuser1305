package com.truckintransit.user.activity

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.truckintransit.user.R
import com.truckintransit.user.base.BaseActivity
import kotlinx.android.synthetic.main.activity_rider_book.*
import android.content.Intent
import androidx.core.content.ContextCompat

import com.truckintransit.user.constants.AppConstants.PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP1
import com.truckintransit.user.constants.AppConstants.PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP2
import com.truckintransit.user.constants.AppConstants.PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP3
import com.truckintransit.user.constants.UserBooking.MSselectVehcicle_name
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace1
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace1lat
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace1long
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace2
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace2lat
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace2long
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace3
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace3lat
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace3long


class RiderBookActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rider_book)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        toolbar.title = MSselectVehcicle_name
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        if (MUserSelectPlace1 == "") {
            tv_pickup1.text = "1. Enter your first pickup adress"
        } else {
            tv_pickup1.text = MUserSelectPlace1
        }

        tv_pickup1.setOnClickListener {
            try {
                //MODE_OVERLAY-- for dialogview
                //MODE_FULLSCREEN-- for fullscreen
                val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this)
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP1)
            } catch (e: GooglePlayServicesRepairableException) {
                // TODO: Handle the error.
            } catch (e: GooglePlayServicesNotAvailableException) {
                // TODO: Handle the error.
            }
        }
        tv_pickup2.setOnClickListener {
            try {
                //MODE_OVERLAY-- for dialogview
                //MODE_FULLSCREEN-- for fullscreen
                val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this)
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP2)
            } catch (e: GooglePlayServicesRepairableException) {
                // TODO: Handle the error.
            } catch (e: GooglePlayServicesNotAvailableException) {
                // TODO: Handle the error.
            }
        }
        tv_pickup3.setOnClickListener {
            try {
                //MODE_OVERLAY-- for dialogview
                //MODE_FULLSCREEN-- for fullscreen
                val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this)
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP3)
            } catch (e: GooglePlayServicesRepairableException) {
                // TODO: Handle the error.
            } catch (e: GooglePlayServicesNotAvailableException) {
                // TODO: Handle the error.
            }
        }
        card_view_confirm.setOnClickListener {
            launchActivity<RiderBookDropOffActivity>()
        }


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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP1) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(this, data!!)
                tv_pickup1.text = place.name
                MUserSelectPlace1 = place.name.toString()
                MUserSelectPlace1lat = place.latLng.latitude.toString()
                MUserSelectPlace1long = place.latLng.longitude.toString()
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(this, data!!)


            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP2) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(this, data!!)

                tv_pickup2.text = place.name
                MUserSelectPlace2 = place.name.toString()
                MUserSelectPlace2lat = place.latLng.latitude.toString()
                MUserSelectPlace2long = place.latLng.longitude.toString()
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(this, data!!)


            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP3) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(this, data!!)
                tv_pickup3.text = place.name
                MUserSelectPlace3 = place.name.toString()
                MUserSelectPlace3lat = place.latLng.latitude.toString()
                MUserSelectPlace3long = place.latLng.longitude.toString()
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(this, data!!)

            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }
}
