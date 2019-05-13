package com.truckintransit.user.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {

    var inputNumber = MutableLiveData<String>()
}