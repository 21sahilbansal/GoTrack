package com.example.gotrack.userInterface.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gotrack.R
import com.example.gotrack.base.BaseFragment
import com.example.gotrack.callBacks.AdpterCallBack
import com.example.gotrack.callBacks.FragCallBack
import com.example.gotrack.model.AllVehicle
import com.example.gotrack.model.VehicleDetails
import com.example.gotrack.userInterface.activity.MainActivity
import com.example.gotrack.userInterface.adapter.AllVehicleAdapter
import kotlinx.android.synthetic.main.vehicle_detail_fragment.*


class AllVehicleFragment : BaseFragment(), AdpterCallBack {

    private lateinit var fragChangeListener: FragCallBack
    val allVehcileData = ArrayList<VehicleDetails>()
    var movingVehcileList: ArrayList<VehicleDetails> = ArrayList()
    var idleVehicleList: ArrayList<VehicleDetails> = ArrayList()
    private var listSorted: Boolean = false


    private val vehicleDataAdapter by lazy {
        AllVehicleAdapter(allVehcileData, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivity.allVehicleObject?.let {
            var allVehicleObject = it as AllVehicle
            allVehicleObject.allVehicle?.let { it1 -> allVehcileData.addAll(it1) }
        }
        fragChangeListener = context as FragCallBack
    }

    override fun viewInitialization(view: View?) {
        if(MainActivity.count == 0){
            showLoadingState(true)
            Handler().postDelayed({
                showLoadingState(false)
                setUpView(view)
                vehicleSorter()
            }, 1000)}
        else{
            showLoadingState(false)
            setUpView(view)
            vehicleSorter()
        }

    }

    private fun vehicleSorter() {
        running_button.setOnClickListener {
            vehicleDataAdapter.updateList(getOnlyMovingVehicleList(true))
        }
        idle_button.setOnClickListener {
            vehicleDataAdapter.updateList(getOnlyMovingVehicleList(false))
        }
        all_vehicle_button.setOnClickListener {
            vehicleDataAdapter.updateList(allVehcileData)
        }
    }


    private fun getOnlyMovingVehicleList(boolean: Boolean): ArrayList<VehicleDetails> {
        if (!listSorted) {
            for (vehicleDetail in allVehcileData) {
                if (vehicleDetail.moving.equals("1")) {
                    movingVehcileList.add(vehicleDetail)
                } else {
                    idleVehicleList.add(vehicleDetail)
                }
            }
        }
        listSorted = true
        if (boolean) {
            return movingVehcileList
        } else {
            return idleVehicleList
        }
    }

    private fun setUpView(view: View?) {
        linear_layout.visibility = View.VISIBLE
        initAdapter()
    }

    private fun initAdapter() {
        var linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        parent_recycler.layoutManager = linearLayoutManager
        parent_recycler.adapter = vehicleDataAdapter
    }

    override val fragmentLayoutId: Int
        protected get() = R.layout.vehicle_detail_fragment

    override fun showLoadingState(loading: Boolean) {
        if (loading)
            shimmer_view_container.startShimmerAnimation()
        else {
            shimmer_view_container.stopShimmerAnimation()
            shimmer_view_container.visibility = View.GONE
        }
    }


    override fun onVehicleItemClick(itemPosition: Int) {
        fragChangeListener.onFragmentChange(VehicleLocationFragment.getInstance(itemPosition), VehicleLocationFragment.TAG)
    }


    companion object {
        val TAG = AllVehicleFragment::class.java.name
        fun newInstance(): AllVehicleFragment {
            return AllVehicleFragment()
        }
    }

}