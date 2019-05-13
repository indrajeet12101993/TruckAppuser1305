package com.truckintransit.user.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.gson.Gson
import com.truckintransit.user.R
import com.truckintransit.user.base.BaseActivity
import com.truckintransit.user.base.BaseApplication
import com.truckintransit.user.constants.AppConstants
import com.truckintransit.user.constants.UserBooking.MSselectVehcicle_Id
import com.truckintransit.user.constants.UserBooking.MUserDropPlace1
import com.truckintransit.user.constants.UserBooking.MUserDropPlace1lat
import com.truckintransit.user.constants.UserBooking.MUserDropPlace1long
import com.truckintransit.user.constants.UserBooking.MUserDropPlace2
import com.truckintransit.user.constants.UserBooking.MUserDropPlace2lat
import com.truckintransit.user.constants.UserBooking.MUserDropPlace2long
import com.truckintransit.user.constants.UserBooking.MUserDropPlace3
import com.truckintransit.user.constants.UserBooking.MUserDropPlace3lat
import com.truckintransit.user.constants.UserBooking.MUserDropPlace3long
import com.truckintransit.user.constants.UserBooking.MUserOptions
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace1
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace1lat
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace1long
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace2
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace2lat
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace2long
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace3
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace3lat
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace3long
import com.truckintransit.user.constants.UserBooking.Mbody_id
import com.truckintransit.user.constants.UserBooking.Mfuel_id
import com.truckintransit.user.constants.UserBooking.Mplace_id
import com.truckintransit.user.constants.UserBooking.coupon
import com.truckintransit.user.constants.UserBooking.freightpayment
import com.truckintransit.user.constants.UserBooking.mmaterial_type
import com.truckintransit.user.constants.UserBooking.mvehicle_name_typeid
import com.truckintransit.user.constants.UserBooking.payment_mode
import com.truckintransit.user.constants.UserBooking.titcredits
import com.truckintransit.user.dataprefence.DataManager
import com.truckintransit.user.newtworking.ApiRequestClient
import com.truckintransit.user.pojo.ResponseFromServerForBookInitil
import com.truckintransit.user.pojo.notificationAccept.ResponseNotificationBookingConfirmed
import com.truckintransit.user.utils.UtiliyMethods

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_rider_book_drop_off.*

class RiderBookDropOffActivity : BaseActivity(){
    private var mCompositeDisposable: CompositeDisposable? = null
    lateinit var dialog1: ProgressDialog
    private lateinit var dataManager: DataManager
    private  lateinit var mBookingBroadcastReceiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rider_book_drop_off)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        toolbar.title = "Your Drop Location"
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black))

        if (MUserDropPlace1 == "") {
            tv_pickup1.text = "1. Enter your first DropOff adress"
        } else {
            tv_pickup1.text = MUserDropPlace1
        }
        tv_pickup1.setOnClickListener {
            try {
                //MODE_OVERLAY-- for dialogview
                //MODE_FULLSCREEN-- for fullscreen
                val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this)
                startActivityForResult(intent, AppConstants.PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP1)
            } catch (e: GooglePlayServicesRepairableException) {

            } catch (e: GooglePlayServicesNotAvailableException) {

            }
        }
        tv_pickup2.setOnClickListener {
            try {

                val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this)
                startActivityForResult(intent, AppConstants.PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP2)
            } catch (e: GooglePlayServicesRepairableException) {

            } catch (e: GooglePlayServicesNotAvailableException) {

            }
        }
        tv_pickup3.setOnClickListener {
            try {

                val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this)
                startActivityForResult(intent, AppConstants.PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP3)
            } catch (e: GooglePlayServicesRepairableException) {

            } catch (e: GooglePlayServicesNotAvailableException) {

            }
        }
        tv_book.setOnClickListener {

            mmaterial_type=  et_kg.text.toString()
            coupon=  et_coupon.text.toString()
            startRegistrationForVehicle()



        }

        cb_advance.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                freightpayment="Advance"

            }else{
                // show toast , check box is not checked
            }
        }
        cb_topay.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                freightpayment="To pay"
            }else{
                // show toast , check box is not checked
            }
        }
        cb_both.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                freightpayment="Both"
            }else{
                // show toast , check box is not checked
            }
        }

        cb_cash.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                payment_mode="Cash"
            }else{
                // show toast , check box is not checked
            }
        }
        cb_paytm.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                payment_mode="Paytm Wallet"
            }else{
                // show toast , check box is not checked
            }
        }
        cb_helper.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                titcredits="Use TIT Credits"
            }else{
                // show toast , check box is not checked
            }
        }
        tv_cancel.setOnClickListener {

          finish()


        }


        // broadcast recvier
        mBookingBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

                // checking for type intent filter
                if (intent.action == AppConstants.INTENTBOOKINGCONFIRMED) {

                    hideDialogLoading2()
                     launchActivity<RiderBookDeatialActivity>()


                }
            }
        }




    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(mBookingBroadcastReceiver!!,
            IntentFilter(AppConstants.INTENTBOOKINGCONFIRMED)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBookingBroadcastReceiver!!)


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
        if (requestCode == AppConstants.PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP1) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(this, data!!)
                tv_pickup1.text=place.name
                MUserDropPlace1=place.name.toString()
                MUserDropPlace1lat = place.latLng.latitude.toString()
                MUserDropPlace1long = place.latLng.longitude.toString()
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(this, data!!)

            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }

        if (requestCode == AppConstants.PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP2) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(this, data!!)

                tv_pickup2.text=place.name
                MUserDropPlace2=place.name.toString()
                MUserDropPlace2lat = place.latLng.latitude.toString()
                MUserDropPlace2long = place.latLng.longitude.toString()
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(this, data!!)


            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }

        if (requestCode == AppConstants.PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP3) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(this, data!!)

                tv_pickup3.text=place.name
                MUserDropPlace3=place.name.toString()
                MUserDropPlace3lat = place.latLng.latitude.toString()
                MUserDropPlace3long = place.latLng.longitude.toString()
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(this, data!!)

            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }


    //geeting  number for transport
    private fun startRegistrationForVehicle() {



        showDialogLoading1()

        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerBooking(dataManager.getUserId()!!,
                    Mplace_id,MUserSelectPlace1,MUserSelectPlace1lat,MUserSelectPlace1long,MUserSelectPlace2,
                    MUserSelectPlace2lat,MUserSelectPlace2long,MUserSelectPlace3,MUserSelectPlace3lat,MUserSelectPlace3long,
                    MUserDropPlace1,MUserDropPlace1lat,MUserDropPlace1long,MUserDropPlace2,MUserDropPlace2lat,MUserDropPlace2long,
                            MUserDropPlace3,MUserDropPlace3lat,MUserDropPlace3long,MSselectVehcicle_Id,MUserOptions,
                    Mbody_id,Mfuel_id,mvehicle_name_typeid,mmaterial_type,coupon,freightpayment,payment_mode,titcredits

                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    // handle sucess response of api call of get transport
    private fun handleResponse(responseFromSerevr: ResponseFromServerForBookInitil) {


        if(responseFromSerevr.response_code=="1"){
            hideDialogLoading1()
            UtiliyMethods.showDialogwithDismiss(this@RiderBookDropOffActivity,responseFromSerevr.response_message)


        }else{
            hideDialogLoading1()
            showDialogLoading2()
        }

        mCompositeDisposable?.clear()


    }


    // handle failure response of api call of get transport
    private fun handleError(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }

    fun showDialogLoading1() {

        dialog1 = ProgressDialog(this)
        dialog1.setMessage("Please wait...")
        dialog1.setTitle("Finidng Driver for You...")
        dialog1.setCanceledOnTouchOutside(false)
        dialog1.setCancelable(false)
        dialog1.isIndeterminate = true
        if(!dialog1.isShowing){
            dialog1.show()
        }

    }

    fun hideDialogLoading1() {


        if (dialog1 != null && dialog1.isShowing)
            dialog1.cancel()


    }

    fun showDialogLoading2() {

        dialog1 = ProgressDialog(this)
        dialog1.setMessage("Please wait...")
        dialog1.setTitle("Waiting for booking acccept  driver")
        dialog1.setCanceledOnTouchOutside(false)
        dialog1.setCancelable(false)
        dialog1.isIndeterminate = true
        if(!dialog1.isShowing){
            dialog1.show()
        }

    }

    fun hideDialogLoading2() {


        if (dialog1 != null && dialog1.isShowing)
            dialog1.cancel()


    }
}
