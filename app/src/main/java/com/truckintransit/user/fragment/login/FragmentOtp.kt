package com.truckintransit.user.fragment.login


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.truckintransit.user.R
import com.truckintransit.user.base.BaseFragment
import com.truckintransit.user.newtworking.ApiRequestClient
import com.truckintransit.user.pojo.ResponseFromServerGeneric
import com.truckintransit.user.viewmodel.SharedViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_fragment_otp.view.*
import android.content.IntentFilter
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.truckintransit.user.activity.DashBoardActvity
import com.truckintransit.user.base.BaseApplication
import com.truckintransit.user.broadcastRecevier.SMSBroadcastReceiver
import com.truckintransit.user.dataprefence.DataManager
import com.truckintransit.user.fragment.registration.UserDetailFragment
import com.truckintransit.user.pojo.otpVerify.ResponseFromServerOtpVerify
import com.truckintransit.user.utils.Validation


class FragmentOtp : BaseFragment() {
    private lateinit var mSharedViewModel: SharedViewModel
    private var mCompositeDisposable: CompositeDisposable? = null
    private val smsBroadcastReceiver by lazy { SMSBroadcastReceiver() }
    lateinit var dataManager: DataManager
    lateinit var mPhoneNumber: String
    private var mToken: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSharedViewModel = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid Activity")


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_fragment_otp, container, false)
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        mSharedViewModel.inputNumber.observe(this, Observer<String> { item ->
            transfernumber(item)


        }
        )

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result!!.token
                transferToken(token)

            })

        view.tv_resend_sms.setOnClickListener {
            startLoadingForPhoneNumber(mPhoneNumber)
        }

        view.relative_login.setOnClickListener {
            val otpnumber = view.txt_pin_entry.text.toString()
            if (!validationforOtp(otpnumber)) {

                // check for verify otp
                verifyOtp(otpnumber)
            } else {
                showDialogWithDismiss(getString(R.string.valid_otp))
            }


        }


        val client = SmsRetriever.getClient(activity!!)
        val retriever = client.startSmsRetriever()
        retriever.addOnSuccessListener {

            val listener = object : SMSBroadcastReceiver.Listener {
                override fun onSMSReceived(otp: String) {

                    val otpsplit = otp.split(":")
                    val otpsplit1 = otpsplit[1]
                    view.txt_pin_entry.setText(otpsplit1)


                }


                override fun onTimeOut() {


                }
            }
            smsBroadcastReceiver.injectListener(listener)
            activity!!.registerReceiver(smsBroadcastReceiver, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))
        }
        retriever.addOnFailureListener {
            //Problem to start listener
        }

        return view
    }

    private fun transferToken(token: String) {
        mToken = token
        startLoadingForPhoneNumber(mPhoneNumber)

    }

    private fun transfernumber(item: String?) {
        mPhoneNumber = item!!

    }

    private fun validationforOtp(otpnumber: String): Boolean {


        if (Validation.isEmptyField(otpnumber)) {
            return true
        }

        if (Validation.isLengthCheckForOtp(otpnumber)) {
            return true
        }

        return false


    }


    // api call for otp verify
    private fun verifyOtp(otp: String) {

        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerUserOtpVerify(otp)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseOtp, this::handleErrorOtp)
        )


    }

    // handle sucess response of api call of otp verify
    private fun handleResponseOtp(responseFromServerOtpVerify: ResponseFromServerOtpVerify) {
        hideDialogLoading()
        mCompositeDisposable?.clear()

        //false-- not fill data
        // true-- fill
        if (responseFromServerOtpVerify.response_code == "1") {
            showDialogWithDismiss(getString(R.string.valid_otp))
        } else {
            dataManager.setUserId(responseFromServerOtpVerify.result[0].id)
            if (responseFromServerOtpVerify.result[0].status.equals("true")) {
                launchActivityWithFinish(DashBoardActvity())
            } else {
                replaceFragment(UserDetailFragment())
            }


        }


    }

    // handle failure response of api call otp verify
    private fun handleErrorOtp(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }


    //geeting  number for otp api
    private fun startLoadingForPhoneNumber(phonenumber: String) {


        showDialogLoading()

        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerUserPhoneNumber(phonenumber, mToken!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    // handle sucess response of api call of get phone number
    private fun handleResponse(responseFromSerevrPhoneNumber: ResponseFromServerGeneric) {
        hideDialogLoading()
        mCompositeDisposable?.clear()


    }


    // handle failure response of api call of get phone number
    private fun handleError(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }


    override fun onDestroy() {
        super.onDestroy()
        activity!!.unregisterReceiver(smsBroadcastReceiver)
    }

    private fun replaceFragment(fragment: Fragment) {

        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.replace(R.id.place_holder_for_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}
