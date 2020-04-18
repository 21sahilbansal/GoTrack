package com.example.gotrack.userInterface.fragments

import android.os.Bundle
import android.view.View
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
    private val allVehcileData by lazy { ArrayList<VehicleDetails>() }
    private val vehicleDataAdapter by lazy {
        AllVehicleAdapter(allVehcileData, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivity.allVehicleObject?.let {
            var allVehicleObject = it as AllVehicle
            allVehicleObject.allVehicle?.let { it1 -> allVehcileData.addAll(it1) }
        }
        showLoadingState(true)
        fragChangeListener = context as FragCallBack
    }

    override fun viewInitialization(view: View?) {
        showLoadingState(true)
        setUpView(view);
    }

    private fun setUpView(view: View?) {
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
        if (loading) { //            shimmer_view_container.startShimmerAnimation()
        } else { //     shimmer_view_container.stopShimmerAnimation()
//     shimmer_view_container.visibility = View.GONE
        }
    }


    override fun onVehicleItemClick(itemPosition: Int) {
        fragChangeListener.onFragmentChange(VehicleLocoMapFragment.getInstance(itemPosition), VehicleLocoMapFragment.TAG)
    }


    companion object {
        val TAG = AllVehicleFragment::class.java.name
        fun newInstance(): AllVehicleFragment {
            return AllVehicleFragment()
        }
    }

}