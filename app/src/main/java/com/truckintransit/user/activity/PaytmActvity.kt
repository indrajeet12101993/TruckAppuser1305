package com.truckintransit.user.activity


import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPGService
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
import com.truckintransit.user.R
import com.truckintransit.user.base.BaseActivity
import com.truckintransit.user.constants.AppConstants.IS_PATM_STAGIN
import com.truckintransit.user.constants.AppConstants.PAYTM_CALLBACK_URL
import com.truckintransit.user.newtworking.ApiRequestClient
import com.truckintransit.user.pojo.ResponseFromServerGeneric
import com.truckintransit.user.pojo.paytm.ResponseFromPaytmChecksum
import com.truckintransit.user.utils.UtiliyMethods
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.app_bar_dash_board_actvity.*
import java.util.HashMap

class PaytmActvity : BaseActivity() {
    private var mCompositeDisposable: CompositeDisposable? = null
    val paramMap = HashMap<String, String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_driver_actvity)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        paytmCheckSum()

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
    //geeting  number for user
    private fun paytmCheckSum() {


        showDialogLoading()

        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postPaytm("ORDER1","CUST00001","Retail","WAP","1.00")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    // handle sucess response of api call of get transport
    private fun handleResponse(responseFromSerevr: ResponseFromPaytmChecksum) {
        hideDialogLoading()
        mCompositeDisposable?.clear()
        paramMap["CALLBACK_URL"] = String.format(PAYTM_CALLBACK_URL,"ORDER1")
        paramMap["CHANNEL_ID"] = "WAP"
        paramMap["CUST_ID"] = "CUST00001"
        paramMap["INDUSTRY_TYPE_ID"] ="Retail"
        paramMap["MID"] = "EHLppL89494955023458"
        paramMap["WEBSITE"] = "WEBSTAGING"
        paramMap["ORDER_ID"] = "ORDER1"
        paramMap["TXN_AMOUNT"] = "1.00"
        paramMap["MOBILE_NO"] = "9999999999"
        paramMap["EMAIL"]= "abc@gmail.com"
        paramMap["CHECKSUMHASH"] = responseFromSerevr.CHECKSUMHASH
        placeOrder()

    }


    // handle failure response of api call of get transport
    private fun handleError(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }

    fun placeOrder() {
       // setStatus(R.string.msg_redirecting_to_paytm)

        // choosing between PayTM staging and production
        val pgService = if (IS_PATM_STAGIN) PaytmPGService.getStagingService() else PaytmPGService.getProductionService()

        val Order = PaytmOrder(paramMap)

        pgService.initialize(Order, null)

        pgService.startPaymentTransaction(this, true, true,
            object : PaytmPaymentTransactionCallback {
                override fun someUIErrorOccurred(inErrorMessage: String) {
                   // Timber.e("someUIErrorOccurred: %s", inErrorMessage)
                    finish()
                    // Some UI Error Occurred in Payment Gateway Activity.
                    // // This may be due to initialization of views in
                    // Payment Gateway Activity or may be due to //
                    // initialization of webview. // Error Message details
                    // the error occurred.
                }

                override fun onTransactionResponse(inResponse: Bundle) {
                   // Timber.d("PayTM Transaction Response: %s", inResponse.toString())
                    val orderId = inResponse.getString("ORDERID")
                  // verifyTransactionStatus(orderId)
                }

                override fun networkNotAvailable() { // If network is not
                   // Timber.e("networkNotAvailable")
                    finish()
                    // available, then this
                    // method gets called.
                }

                override fun clientAuthenticationFailed(inErrorMessage: String) {
                   // Timber.e("clientAuthenticationFailed: %s", inErrorMessage)
                    finish()
                    // This method gets called if client authentication
                    // failed. // Failure may be due to following reasons //
                    // 1. Server error or downtime. // 2. Server unable to
                    // generate checksum or checksum response is not in
                    // proper format. // 3. Server failed to authenticate
                    // that client. That is value of payt_STATUS is 2. //
                    // Error Message describes the reason for failure.
                }

                override fun onErrorLoadingWebPage(
                    iniErrorCode: Int,
                    inErrorMessage: String, inFailingUrl: String
                ) {
                  //  Timber.e("onErrorLoadingWebPage: %d | %s | %s", iniErrorCode, inErrorMessage, inFailingUrl)
                    finish()
                }

                override fun onBackPressedCancelTransaction() {
                  //  Toast.makeText(this@PayTMActivity, "Back pressed. Transaction cancelled", Toast.LENGTH_LONG).show()
                    finish()
                }

                override fun onTransactionCancel(inErrorMessage: String, inResponse: Bundle) {
                  //  Timber.e("onTransactionCancel: %s | %s", inErrorMessage, inResponse)
                    finish()
                }
            })
    }






}
