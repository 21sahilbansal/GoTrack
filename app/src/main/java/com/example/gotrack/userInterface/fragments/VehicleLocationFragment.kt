package com.example.gotrack.userInterface.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
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
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.item_vehicle_detail.*
import kotlinx.android.synthetic.main.vehicle_location_fragment.*
import java.util.*


class VehicleLocationFragment : BaseFragment() {
    var googleMap: GoogleMap? = null
    var marker: Marker? = null
    var vehicleDetails: VehicleDetails? = null
    private var sheetBehavior: BottomSheetBehavior<*>?  = null
    var mapType = true

    override val fragmentLayoutId: Int
        get() = R.layout.vehicle_location_fragment


    override fun viewInitialization(view: View?) {
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet_layout)
        MainActivity.count = 1
        var position: Int? = arguments?.let { it[VEHICLE_POSITION] as Int }
        MainActivity.allVehicleObject?.let {
            var allVehicleObject = it as AllVehicle
            allVehicleObject.allVehicle?.let { it -> vehicleDetails = it[position!!] }
        }
        initMap()
        vehicleDetails?.let { addDataToBottomSheet() }

        (sheetBehavior as BottomSheetBehavior<CardView>?)?.setBottomSheetCallback(object : BottomSheetCallback(){
            override fun onSlide(p0: View, p1: Float) {

            }

            override fun onStateChanged(p0: View, p1: Int) {
            }

        })
    }

    private fun addDataToBottomSheet() {
        vehicle_Type_tv1.text = vehicleDetails?.vehicle
        vehicle_No_tv1.text = vehicleDetails?.vehicleId
        vehicle_Date_tv1.text = vehicleDetails?.date
        vehicle_location_tv1.text = vehicleDetails?.location
        if(vehicleDetails?.moving == "1"){
            vehicle_status_tv1.text = "Moving"
        }
        else{
            vehicle_status_tv1.text = "Idle"
        }
        vehicle_Extra_tv1.text = vehicleDetails?.extraInfo
    }


    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(onMapReadyCallback)
    }

    private val onMapReadyCallback = OnMapReadyCallback { googleMap: GoogleMap -> setupMaps(googleMap) }

    private fun setupMaps(googleMap: GoogleMap) {
        this.googleMap = googleMap
        var latitude = vehicleDetails?.lati
        var longtitude = vehicleDetails?.long
        var location = vehicleDetails?.location
        var latLng: LatLng? = null
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
        button_map_view.setOnClickListener{
            googleMap?.let {
                if (mapType) {
                    setSatelliteView(googleMap)
                    button_map_view.setImageResource(R.drawable.ic_map_view)
                    mapType = false
                } else  {
                    setNormalView(googleMap)
                    button_map_view.setImageResource(R.drawable.ic_satellite_view)
                    mapType = true
                }
            }
    }}
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
       // googleMap?.setMaxZoomPreference(20f)
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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