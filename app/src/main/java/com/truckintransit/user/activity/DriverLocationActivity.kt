package com.truckintransit.user.activity

import android.Manifest
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.truckintransit.user.R
import android.content.Context
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Handler
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import com.truckintransit.user.base.BaseActivity
import com.truckintransit.user.constants.AppConstants.REQUEST_LOCATION_PERMISSION
import android.os.SystemClock
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.*
import com.truckintransit.user.backgroundJobs.FetchAddressTask
import com.truckintransit.user.callbackInterface.OnTaskCompleted
import com.truckintransit.user.constants.AppConstants.REQUEST_LOCATION_PERMISSION_LOCATION
import kotlinx.android.synthetic.main.activity_driver_location.*


class DriverLocationActivity : BaseActivity(), OnMapReadyCallback, OnTaskCompleted {
    override fun onTaskCompleted(result: String) {
        // hideDialogLoading()
     //   aniamtion.visibility = View.GONE

        diplaylocation(result)
        // mMarker= mMap.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_trucking)).position(home).title(result))
    }

    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mMarker: Marker


    // Location classes
    private var mTrackingLocation: Boolean = false
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationCallback: LocationCallback? = null
    private var mLastLocation: Location? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_location)


        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this@DriverLocationActivity)

        // Initialize the FusedLocationClient.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)






        switch_toogle.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {

                tv_stautus.text = getString(R.string.online)
                showSnackBar(getString(R.string.online))
                startTrackingLocation()

            } else {
                tv_stautus.text = getString(R.string.offfline)
                showSnackBar(getString(R.string.offfline))
                stopTrackingLocation()
            }
        }

        // Initialize the location callbacks.
        mLocationCallback = object : LocationCallback() {
            /**
             * This is the callback that is triggered when the
             * FusedLocationClient updates your location.
             * @param locationResult The result containing the device location.
             */
            override fun onLocationResult(locationResult: LocationResult?) {


                mLastLocation = locationResult!!.lastLocation
                FetchAddressTask(this@DriverLocationActivity, this@DriverLocationActivity)
                    .execute(locationResult.lastLocation)


            }
        }


    }

    private fun diplaylocation(result: String) {
        val latitude = mLastLocation!!.latitude
        val longitude = mLastLocation!!.longitude
        val home = LatLng(latitude, longitude)

        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney),10)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(home, 10f))
        mMarker = mMap.addMarker(
            MarkerOptions().position(home).title(result).icon(
                bitmapDescriptorFromVector(
                    this,
                    R.drawable.ic_trucking
                )
            )
        )
        rotateMarker(mMarker, -360f)
        enableMyLocation()
        mMap.setOnPoiClickListener {
            val poiMarker: Marker = mMap.addMarker(
                MarkerOptions()
                    .position(it.latLng)
                    .title(it.name)
            )
            poiMarker.showInfoWindow()
        }

    }


    override fun onMapReady(googleMap: GoogleMap) {
        // hideDialogLoading()
        mMap = googleMap

//        1: World
//        5: Landmass/continent
//        10: City
//        15: Streets
//        20: Buildings


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

    /**
     * Starts tracking the device. Checks for
     * permissions, and requests them if they aren't present. If they are,
     * requests periodic location updates, sets a loading text and starts the
     * animation.
     */
    private fun startTrackingLocation() {
        // showDialogLoading()
      //  aniamtion.visibility = View.VISIBLE
      //  val myAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.bounce)
     //   myAnim.cancel()
     //   aniamtion.startAnimation(myAnim)


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION_LOCATION
            )
        } else {


            mFusedLocationClient!!.requestLocationUpdates(getLocationRequest(), mLocationCallback, null)


        }


    }

    /**
     * Sets up the location request.
     *
     * @return The LocationRequest object containing the desired parameters.
     */
    private fun getLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        return locationRequest
    }

    override fun onPause() {
        if (mTrackingLocation) {
            stopTrackingLocation()
            mTrackingLocation = true
        }
        super.onPause()
    }

    override fun onResume() {
        if (mTrackingLocation) {
            startTrackingLocation()
        }
        super.onResume()
    }

    override fun onDestroy() {

        stopTrackingLocation()

        super.onDestroy()
    }

    /**
     * Stops tracking the device. Removes the location
     * updates, stops the animation, and resets the UI.
     */
    private fun stopTrackingLocation() {

        if(mMarker!=null){
            mMarker.remove()
        }
        if(mMap!=null){

            mMap.clear()
        }
       if(mFusedLocationClient!=null)
           mFusedLocationClient!!.removeLocationUpdates(mLocationCallback)


    }


    fun rotateMarker(marker: Marker, toRotation: Float) {

        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val startRotation = marker.rotation
        val duration = 8000

        val interpolator: Interpolator = LinearInterpolator()



        handler.post {

            val elapsed = SystemClock.uptimeMillis() - start
            val t = interpolator.getInterpolation(elapsed.toFloat() / duration)
            val rot = t * toRotation + (1 - t) * startRotation
            val bearing = if (-rot > 180) rot / 2 else rot

            marker.rotation = bearing

            if (t < 1.0) {
                // Post again 16ms later.
                handler.postDelayed({}, 40)
            } else {
                // isMarkerRotating = false;
            }

        }

    }


    private fun bitmapDescriptorFromVector(context: Context, vectorDrawableResourceId: Int): BitmapDescriptor {
        val background: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_trucking)!!
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight())
        val vectorDrawable: Drawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)!!
//       // vectorDrawable.setBounds(
//            40,
//            20,
//            vectorDrawable.getIntrinsicWidth() + 40,
//            vectorDrawable.getIntrinsicHeight() + 20
//        );
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


}
