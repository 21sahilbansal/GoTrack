package com.example.gotrack.userInterface.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.gotrack.R
import com.example.gotrack.base.BaseFragment
import com.example.gotrack.model.AllVehicle
import com.example.gotrack.model.VehicleDetails
import com.example.gotrack.userInterface.activity.MainActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.vehicle_location_fragment.*


class VehicleLocationFragment : BaseFragment() {
    private var googleMap: GoogleMap? = null
    private var marker: Marker? = null
    var vehicleDetails: VehicleDetails? = null
    private var sheetBehavior: BottomSheetBehavior<*>? = null
    var mapType = true

    override val fragmentLayoutId: Int
        get() = R.layout.vehicle_location_fragment


    override fun viewInitialization(view: View?) {
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet_layout)
        MainActivity.count = 1
        val position: Int? = arguments?.let { it[VEHICLE_POSITION] as Int }
        MainActivity.allVehicleObject?.let {
            val allVehicleObject = it as AllVehicle
            allVehicleObject.allVehicle?.let { it -> vehicleDetails = it[position!!] }
        }
        initMap()
        vehicleDetails?.let { addDataToBottomSheet() }

    }

    private fun addDataToBottomSheet() {
        vehicle_Type_tv1.text = vehicleDetails?.vehicle
        vehicle_No_tv1.text = vehicleDetails?.vehicleId
        vehicle_Date_tv1.text = vehicleDetails?.date
        vehicle_location_tv1.text = vehicleDetails?.location
        if (vehicleDetails?.moving == "1") {
            vehicle_status_tv1.text = getString(R.string.vehicle_moving)
        } else {
            vehicle_status_tv1.text = getString(R.string.vehicle_idle)
        }
        vehicle_Extra_tv1.text = vehicleDetails?.extraInfo

        attBottomSheetListener()
    }

    private fun attBottomSheetListener() {
        bottom_sheet_opener_view.setOnClickListener {
            if (sheetBehavior?.state == BottomSheetBehavior.STATE_COLLAPSED)
                sheetBehavior?.setState(BottomSheetBehavior.STATE_EXPANDED)
            else if (sheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED)
                sheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }


    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(onMapReadyCallback)
    }

    private val onMapReadyCallback = OnMapReadyCallback { googleMap: GoogleMap -> setupMaps(googleMap) }

    private fun setupMaps(googleMap: GoogleMap) {
        this.googleMap = googleMap
        val latitude = vehicleDetails?.lati
        val longtitude = vehicleDetails?.long
        var latLng: LatLng?
        latitude?.let { it ->
            longtitude?.let { it1 ->
                latLng = LatLng(it, it1)
                marker = addMarker(latLng!!, googleMap)
                marker?.tag = "vehicle_tag"
                googleMap?.setMaxZoomPreference(17f)
                setAnimation(marker)
                setInfoWidow()
            }
        }
        setMapDisplayType()
    }

    private fun setMapDisplayType() {
        this.button_map_view.setOnClickListener {
            googleMap?.let {
                if (mapType) {
                    setSatelliteView(googleMap)
                    button_map_view.setImageResource(R.drawable.ic_map_view)
                    mapType = false
                } else {
                    setNormalView(googleMap)
                    button_map_view.setImageResource(R.drawable.ic_satellite_view)
                    mapType = true
                }
            }
        }
    }

    private fun setSatelliteView(googleMap: GoogleMap?) {
        googleMap?.let {
            it.mapType = GoogleMap.MAP_TYPE_HYBRID
        }
    }

    private fun setNormalView(googleMap: GoogleMap?) {
        googleMap?.let {
            it.mapType = GoogleMap.MAP_TYPE_NORMAL
        }
    }

    private fun setInfoWidow() {
        googleMap?.setOnMarkerClickListener { marker ->
            marker.tag?.let { it ->
                if (it.equals("vehicle_tag")) {
                    googleMap?.setInfoWindowAdapter(customInfoWindowAdapter())
                    marker.showInfoWindow()
                }
            }
            return@setOnMarkerClickListener true
        }
    }


    private fun customInfoWindowAdapter(): GoogleMap.InfoWindowAdapter {
        return object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? {
                val infoView = LayoutInflater.from(context).inflate(R.layout.dialog_location, null)
                val locationAddress: TextView = infoView.findViewById(R.id.location_tv)
                val vehicleType: TextView = infoView.findViewById(R.id.vehile_type_tv)
                val vehicleDate: TextView = infoView.findViewById(R.id.date_tv)
                locationAddress.text = vehicleDetails?.location
                vehicleType.text = vehicleDetails?.vehicle
                vehicleDate.text = vehicleDetails?.date
                return infoView
            }
            override fun getInfoContents(marker: Marker): View? {
                return null
            }
        }
    }


    private fun setAnimation(marker: Marker?) {
        val builder = LatLngBounds.Builder()
        //the include method will calculate the min and max bound.
        //the include method will calculate the min and max bound.
        builder.include(marker?.getPosition())
        val bounds = builder.build()
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        val padding = (height * 0.20).toInt() // offset from edges of the map 10% of screen
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
        googleMap?.animateCamera(cu)
    }

    fun addMarker(latLng: LatLng, googleMap: GoogleMap?): Marker? {
        googleMap?.let {
            val icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_car_icon)
            val markerOptions = MarkerOptions().position(latLng).icon(icon)
            val marker = googleMap.addMarker(markerOptions)
            marker.setAnchor(0.5f, 0.5f)
            return marker
        } ?: return null
    }

    override fun showLoadingState(loading: Boolean) {
    }

    companion object {
        val TAG = VehicleLocationFragment::class.java.name
        const val VEHICLE_POSITION = "Vehicle_Position"
        fun getInstance(position: Int): VehicleLocationFragment {
            val fragment = VehicleLocationFragment()
            val arg = Bundle()
            arg.putInt(VEHICLE_POSITION, position)
            fragment.arguments = arg
            return fragment
        }
    }
}