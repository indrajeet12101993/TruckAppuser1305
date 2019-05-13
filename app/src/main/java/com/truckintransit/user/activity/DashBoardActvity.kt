package com.truckintransit.user.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.NonNull
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.truckintransit.user.R
import com.truckintransit.user.base.BaseActivity
import kotlinx.android.synthetic.main.activity_dash_board_actvity.*
import kotlinx.android.synthetic.main.app_bar_dash_board_actvity.*
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.*
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.truckintransit.user.adapter.*
import com.truckintransit.user.backgroundJobs.FetchAddressTask
import com.truckintransit.user.base.BaseApplication
import com.truckintransit.user.callbackInterface.*
import com.truckintransit.user.constants.AppConstants
import com.truckintransit.user.constants.AppConstants.INTENTBOOKINGCONFIRMED
import com.truckintransit.user.constants.AppConstants.PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP1
import com.truckintransit.user.constants.AppConstants.PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP2
import com.truckintransit.user.constants.AppConstants.REQUEST_LOCATION_PERMISSION
import com.truckintransit.user.constants.AppConstants.REQUEST_LOCATION_PERMISSION_LOCATION
import com.truckintransit.user.constants.UserBooking.MSselectVehcicle_Id
import com.truckintransit.user.constants.UserBooking.MUserDropPlace1
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace1
import com.truckintransit.user.constants.UserBooking.MplaceName
import com.truckintransit.user.constants.UserBooking.Mplace_id
import com.truckintransit.user.constants.UserBooking.MSselectVehcicle_name
import com.truckintransit.user.constants.UserBooking.MUserDropPlace1lat
import com.truckintransit.user.constants.UserBooking.MUserDropPlace1long
import com.truckintransit.user.constants.UserBooking.MUserOptions
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace1lat
import com.truckintransit.user.constants.UserBooking.MUserSelectPlace1long
import com.truckintransit.user.constants.UserBooking.Mbody_id
import com.truckintransit.user.constants.UserBooking.Mbody_name
import com.truckintransit.user.constants.UserBooking.Mfuel_id
import com.truckintransit.user.constants.UserBooking.Mfuel_name
import com.truckintransit.user.constants.UserBooking.mvehicle_name_type
import com.truckintransit.user.constants.UserBooking.mvehicle_name_typeid
import com.truckintransit.user.dataprefence.DataManager
import com.truckintransit.user.newtworking.ApiRequestClient
import com.truckintransit.user.pojo.ResponseFromServerGeneric
import com.truckintransit.user.pojo.service.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.bottom_sheet_for_user_pickup.*
import kotlinx.android.synthetic.main.city_list.*


class DashBoardActvity : BaseActivity(), ListnerForNaviagtionItem, ListnerForUserSelectPlace, OnMapReadyCallback,
    OnTaskCompleted, InterfaceCustomDialogCatogoryListner, InterfaceCustomDialogFuelType,
    InterfaceCustomDialogVehicleType, ListnerForUserSelectVehicle {
    override fun itemSelectVehicle(position: Int) {
        selectVehcicle_id = mList_Vehicle[position].id
        selectVehcicle_name = mList_Vehicle[position].description
        MSselectVehcicle_name = mList_Vehicle[position].description
        MSselectVehcicle_Id = mList_Vehicle[position].id
        linearselect.visibility = View.VISIBLE
        for (i in mList_Vehicle.indices) {
            mList_Vehicle[i].isCloured = i == position
        }
        mAdapter_Vehicles!!.notifyDataSetChanged()

        if (position == 0) {
            linearservices.visibility = View.GONE
            iv_uparrow.visibility = View.VISIBLE
        } else {
            linearservices.visibility = View.VISIBLE
            iv_uparrow.visibility = View.VISIBLE
        }


    }

    override fun funOnVehicleType(result: Vehiclebodytype) {
        dialog_custom.dismiss()
        body_name = result.name
        body_id = result.id
        Mbody_name = result.name
        Mbody_id = result.id
        tv_body_type.setText(result.name)
    }

    override fun funOnFuelType(result: Vehiclefueltype) {
        dialog_custom.dismiss()
        fuel_name = result.name
        fuel_id = result.id
        Mfuel_name = result.name
        Mfuel_id = result.id
        tv_fuel_type.setText(result.name)
    }

    override fun funOnItemClick(result: Vehicletype) {
        dialog_custom.dismiss()
        vehicle_name = result.name
        vehicle_id = result.id
        mvehicle_name_type = result.name
        mvehicle_name_typeid = result.id
        tv_vehicle_type.setText(result.name)


    }

    override fun itemSelectPlace(position: Int) {
        place_id = mList_UserBook[position].id
        placeName = mList_UserBook[position].name

        Mplace_id = mList_UserBook[position].id
        MplaceName = mList_UserBook[position].name
        for (i in mList_UserBook.indices) {
            mList_UserBook[i].isCloured = i == position
        }
        mAdapter_user_Book!!.notifyDataSetChanged()

        if (place_id == "2") {
            tv_vehicle_type.visibility = View.GONE

        } else {
            tv_vehicle_type.visibility = View.VISIBLE
        }

    }

    override fun onTaskCompleted(result: String) {
        diplaylocation(result)

    }


    lateinit var mList: ArrayList<String>
    lateinit var mList_UserBook: ArrayList<Service>
    lateinit var mList_Vehicle: ArrayList<Truckloaddetail>
    lateinit var mList_VehicleType: ArrayList<Vehicletype>
    lateinit var mList_VehicleFuelType: ArrayList<Vehiclefueltype>
    lateinit var mList_VehicleBodyType: ArrayList<Vehiclebodytype>
    lateinit var mList_OnlineDriver: ArrayList<Livedriver>
    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mMarker: Marker
    private var mTrackingLocation: Boolean = true
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationCallback: LocationCallback? = null
    private var mLastLocation: Location? = null
    lateinit var dataManager: DataManager
    private var mCompositeDisposable: CompositeDisposable? = null
    private var mAdapter_user_Book: UserSelectPlaceAdapter? = null
    private var mAdapter_Vehicles: VehicleListAdapter? = null
    private lateinit var dialog_custom: Dialog
    private var body_name: String = ""
    private var body_id: String = ""
    private var fuel_name: String = ""
    private var fuel_id: String = ""
    private var vehicle_name: String = ""
    private var vehicle_id: String = ""
    private var place_id: String = ""
    private var placeName: String = ""
    private var selectVehcicle_id: String = ""
    private var selectVehcicle_name: String = ""
    private var mUserSelectPlace1: String = ""
    private var mUserDropPlace1: String = ""
    private var mUserOptions: String = "Helper"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board_actvity)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        dataManager.setLoggedIn(true)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        initCustomDialog()
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initialize the FusedLocationClient.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Toast.makeText(activity, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show()
        } else {
            showSnackBar("GPS is disabled in your device")


        }
        // Initialize the location callbacks.
        mLocationCallback = object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult?) {
                mLastLocation = locationResult!!.lastLocation
                FetchAddressTask(this@DashBoardActvity, this@DashBoardActvity).execute(locationResult.lastLocation)


            }
        }

        //list of recyclerview naviagtion
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        rv_menu_items.layoutManager = layoutManager
        mList = ArrayList<String>()
        mList.add("Track Live Trips")
        mList.add("My Trips")
        mList.add("Paytm Wallet")
        mList.add("Rewards And Offers")
        mList.add("Invite Friends")
        mList.add("Contact Us")
        mList.add("About Us")
        val mAdapter = NaviagtionDrawerRecyclerAdapter(this, mList, this)
        rv_menu_items.adapter = mAdapter


        //list of recyclerview userSelect
        val layoutManager_horizontal: RecyclerView.LayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_user_book.layoutManager = layoutManager_horizontal
        mList_UserBook = ArrayList<Service>()
        mAdapter_user_Book = UserSelectPlaceAdapter(this, mList_UserBook, this)
        rv_user_book.adapter = mAdapter_user_Book

        //list of recyclerview vehicleList
        val layoutManager_horizontal1: RecyclerView.LayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_vehicle.layoutManager = layoutManager_horizontal1
        mList_Vehicle = ArrayList<Truckloaddetail>()
        mAdapter_Vehicles = VehicleListAdapter(this, mList_Vehicle, this)
        rv_vehicle.adapter = mAdapter_Vehicles

        mList_VehicleType = ArrayList()
        mList_VehicleBodyType = ArrayList()
        mList_VehicleFuelType = ArrayList()
        tv_book.setOnClickListener {

            launchActivity<RiderBookDropOffActivity>()
        }

        relative_pickup.setOnClickListener {

            try {

                val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this)
                startActivityForResult(intent, AppConstants.PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP1)
            } catch (e: GooglePlayServicesRepairableException) {

            } catch (e: GooglePlayServicesNotAvailableException) {

            }

        }

        relative_dropoff.setOnClickListener {

            try {

                val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this)
                startActivityForResult(intent, AppConstants.PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP2)
            } catch (e: GooglePlayServicesRepairableException) {

            } catch (e: GooglePlayServicesNotAvailableException) {

            }

        }


        tv_body_type.setOnClickListener {
            dialog_custom.setTitle(getString(R.string.select_body))

            if (mList_VehicleBodyType.isNotEmpty()) {
                val mCityAdapter = CustomAdapterForVehicleType(mList_VehicleBodyType, this)
                dialog_custom.recycler_view.adapter = mCityAdapter
                dialog_custom.show()


            }

        }
        tv_fuel_type.setOnClickListener {
            dialog_custom.setTitle(getString(R.string.select_fuel_type))

            if (mList_VehicleFuelType.isNotEmpty()) {
                val mCityAdapter = CustomAdapterForFuelType(mList_VehicleFuelType, this)
                dialog_custom.recycler_view.adapter = mCityAdapter
                dialog_custom.show()


            }

        }
        tv_vehicle_type.setOnClickListener {
            dialog_custom.setTitle(getString(R.string.select_vehicle_type))

            if (mList_VehicleType.isNotEmpty()) {
                val mAdapter = CustomAdapterForCustomDialogCatogary(mList_VehicleType, this)
                dialog_custom.recycler_view.adapter = mAdapter
                dialog_custom.show()


            }

        }


        cb_helper.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mUserOptions = "Helper"
                MUserOptions = "Helper"
            } else {
                // show toast , check box is not checked
            }
        }
        cb_labour.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mUserOptions = "Labour"
                MUserOptions = "Labour"
            } else {
                // show toast , check box is not checked
            }
        }


        startTrackingLocation()


    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun itemSelcectPosition(position: Int) {

        if (position == 0) {

            // replaceFragment(DashBoardFragment(),mList.get(position))
        }
        if (position == 1) {

            // launchActivity<YourProfileActivity>()


        }

        if (position == 2) {

            launchActivity<PaytmActvity>()


        }
        if (position == 4) {

            //launchActivity<AddVehicleActivity>()


        }
        if (position == 5) {

            //  launchActivity<YourTotalEarningsActivity>()


        }
        if (position == 7) {

            // launchActivity<ReferAndEarnActivity>()


        }
        if (position == 8) {

            // launchActivity<SupportActivity>()


        }
        drawer_layout.closeDrawer(GravityCompat.START)
    }


    // api call for post notification
    private fun postServerForDriver() {

        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerNotification()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseOtp, this::handleErrorOtp)
        )


    }

    // handle sucess response of api
    private fun handleResponseOtp(responseFromServerOtpVerify: ResponseFromServerGeneric) {
        hideDialogLoading()
        mCompositeDisposable?.clear()

        showSnackBar("Waiting for find Driver!!")


    }


    // handle failure response of api call otp verify
    private fun handleErrorOtp(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }


    // get all services
    private fun getServicesForUser(lat: String, longg: String) {

        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .getServiceList(lat, longg)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseServices, this::handleErrorServices)
        )


    }


    private fun handleResponseServices(response: ResponseFromServerForServiceList) {
        hideDialogLoading()
        mCompositeDisposable?.clear()
        mTrackingLocation = true


        linearservices.visibility = View.GONE
        iv_uparrow.visibility = View.GONE



        if (response.services.isNotEmpty()) {
            mList_UserBook.clear()
            mList_UserBook.addAll(response.services)
            for (i in mList_UserBook.indices) {
                mList_UserBook[i].isCloured = i == 0
            }

            place_id = mList_UserBook[0].id
            placeName = mList_UserBook[0].name
            mAdapter_user_Book!!.notifyDataSetChanged()
        }
        if (response.truckloaddetail.isNotEmpty()) {
            mList_Vehicle.clear()
            mList_Vehicle.addAll(response.truckloaddetail)
            for (i in mList_Vehicle.indices) {
                mList_Vehicle[i].isCloured = i == 0
            }
            selectVehcicle_id = mList_Vehicle[0].id
            selectVehcicle_name = mList_Vehicle[0].description
            MSselectVehcicle_name = mList_Vehicle[0].description
            mAdapter_Vehicles!!.notifyDataSetChanged()
        }
        if (response.vehicletype.isNotEmpty()) {
            mList_VehicleType = response.vehicletype as ArrayList<Vehicletype>

        }
        if (response.vehiclefueltype.isNotEmpty()) {
            mList_VehicleFuelType = response.vehiclefueltype as ArrayList<Vehiclefueltype>

        }
        if (response.vehiclebodytype.isNotEmpty()) {
            mList_VehicleBodyType = response.vehiclebodytype as ArrayList<Vehiclebodytype>

        }
        if (response.Livedriver.isNotEmpty()) {
            mList_OnlineDriver = response.Livedriver as ArrayList<Livedriver>
            showonlineDrivers()

        }


    }


    private fun handleErrorServices(error: Throwable) {
        hideDialogLoading()
        mTrackingLocation = true
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }

    private fun showonlineDrivers() {

        for (i in mList_OnlineDriver.indices) {
            val home = LatLng(mList_OnlineDriver[i].lat.toDouble(), mList_OnlineDriver[i].longg.toDouble())
            mMarker = mMap.addMarker(
                MarkerOptions().position(home).title(mList_OnlineDriver[i].name).icon(
                    bitmapDescriptorFromVector(
                        this,
                        R.drawable.ic_trucking
                    )
                )
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP1) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(this, data!!)
                tv_pickup_adress.text = place.name

                MUserSelectPlace1lat = place.latLng.latitude.toString()
                MUserSelectPlace1long = place.latLng.longitude.toString()
                mUserSelectPlace1 = tv_pickup_adress.text.toString()
                MUserSelectPlace1 = tv_pickup_adress.text.toString()
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(this, data!!)


            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP2) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(this, data!!)
                //Log.i(FragmentActivity.TAG, "Place: " + place.name)
                tv_dropoff_adress.text = place.name
                MUserDropPlace1lat = place.latLng.latitude.toString()
                MUserDropPlace1long = place.latLng.longitude.toString()
                mUserDropPlace1 = tv_dropoff_adress.text.toString()
                MUserDropPlace1 = tv_dropoff_adress.text.toString()
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(this, data!!)

            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }


    }

    private fun diplaylocation(result: String) {
        val latitude = mLastLocation!!.latitude
        val longitude = mLastLocation!!.longitude
        val home = LatLng(latitude, longitude)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(home, 13f))

        // mMarker = mMap.addMarker(
        //   MarkerOptions().position(home).title(result))
        //  mMarker.showInfoWindow()

        // mMap.uiSettings.isMyLocationButtonEnabled = false


        mMap.setOnPoiClickListener {
            val poiMarker: Marker = mMap.addMarker(
                MarkerOptions()
                    .position(it.latLng)
                    .title(it.name)
            )
            poiMarker.showInfoWindow()
        }
        enableMyLocation()
        if (mTrackingLocation) {
            mTrackingLocation = false
            getServicesForUser(latitude.toString(), longitude.toString())
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

    }


    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation()

            }
            REQUEST_LOCATION_PERMISSION_LOCATION -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startTrackingLocation()

            } else {
                showSnackBar("permission denied")
            }


        }
    }


    private fun startTrackingLocation() {


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this!!,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION_LOCATION
            )
        } else {
            mFusedLocationClient!!.requestLocationUpdates(getLocationRequest(), mLocationCallback, null)
        }


    }


    private fun getLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest()
        locationRequest.interval = 100000
        locationRequest.fastestInterval = 50000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        return locationRequest
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroy() {
        super.onDestroy()

        stopTrackingLocation()
    }


    private fun stopTrackingLocation() {

        mMap.clear()
        mFusedLocationClient!!.removeLocationUpdates(mLocationCallback)


    }


    private fun bitmapDescriptorFromVector(context: Context, vectorDrawableResourceId: Int): BitmapDescriptor {
        val background: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_trucking)!!
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight())
        val vectorDrawable: Drawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)!!
        val bitmap: Bitmap = Bitmap.createBitmap(
            background.getIntrinsicWidth(),
            background.getIntrinsicHeight(),
            Bitmap.Config.ARGB_8888
        );
        val canvas: Canvas = Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


    // initiliaze custom dialog
    private fun initCustomDialog() {
        dialog_custom = Dialog(this)
        dialog_custom.setContentView(R.layout.city_list)
        dialog_custom.setCanceledOnTouchOutside(false)
        dialog_custom.setCancelable(true)
        dialog_custom.recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val itemDecor = DividerItemDecoration(this, LinearLayout.HORIZONTAL)
        dialog_custom.recycler_view.addItemDecoration(itemDecor)


    }

}
