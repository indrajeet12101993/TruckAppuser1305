package com.truckintransit.user.fragment.registration


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.textfield.TextInputEditText
import com.truckintransit.user.R
import com.truckintransit.user.activity.DashBoardActvity
import com.truckintransit.user.base.BaseApplication
import com.truckintransit.user.base.BaseFragment
import com.truckintransit.user.callbackInterface.ListnerForDialog
import com.truckintransit.user.dataprefence.DataManager
import com.truckintransit.user.newtworking.ApiRequestClient
import com.truckintransit.user.pojo.ResponseFromServerGeneric
import com.truckintransit.user.utils.UtiliyMethods
import com.truckintransit.user.utils.Validation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_transport.view.*


class UserDetailFragment : BaseFragment() ,ListnerForDialog{


    private lateinit var mName: String
    private lateinit var mEmail: String
    private lateinit var mRefreral: String
    private lateinit var et_name: TextInputEditText
    private lateinit var et_email: TextInputEditText
    private lateinit var et_referal: TextInputEditText
    private var mCompositeDisposable: CompositeDisposable? = null
    private lateinit var dataManager: DataManager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_transport, container, false)
        et_name = view.et_name
        et_email = view.et_email
        et_referal = view.et_referal
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        view.tv_next.setOnClickListener {
            if(!validationForUser()){
               startUser()
            }

        }

        return view
    }
    override fun selctok() {
       launchActivityWithFinish(DashBoardActvity())
    }

    //geeting  number for user
    private fun startUser() {


        showDialogLoading()

        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postUser(dataManager.getUserId()!!,mName,mEmail,mRefreral,"true")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    // handle sucess response of api call of get transport
    private fun handleResponse(responseFromSerevr: ResponseFromServerGeneric) {
        hideDialogLoading()
        UtiliyMethods.showDialogwithMessage(activity!!,responseFromSerevr.response_message,this )
        mCompositeDisposable?.clear()


    }


    // handle failure response of api call of get transport
    private fun handleError(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }

    private fun validationForUser():Boolean {

        mName = et_name.text.toString()
        mEmail = et_email.text.toString()
        mRefreral = et_referal.text.toString()



        if(Validation.isEmptyField(mName)){
            et_name.error = getString(R.string.error_no_text)
            et_name.requestFocus()
            return true

        }


      return false
    }

    private fun replaceFragment(fragment: Fragment) {

        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.place_holder_for_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}
