package com.example.gotrack.userInterface.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrack.R
import com.example.gotrack.callBacks.AdpterCallBack
import com.example.gotrack.model.VehicleDetails
import kotlinx.android.synthetic.main.item_vehicle_detail.view.*

class AllVehicleAdapter(private val vehicleDetailList: List<VehicleDetails>,private val  listener: AdpterCallBack) : RecyclerView.Adapter<AllVehicleAdapter.VehicleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vehicle_detail, parent, false)
        return VehicleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return vehicleDetailList.size
    }


    override fun onBindViewHolder(holder: AllVehicleAdapter.VehicleViewHolder, position: Int) {
        holder.bind(vehicleDetailList[position],position)

    }
    inner class VehicleViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(vehicleDetails: VehicleDetails,position: Int) {
            view.vehicle_location.text = vehicleDetails.location
                    view.vehicle_no.text =vehicleDetails.vehicleId
                    view.vehicle_type.text =vehicleDetails.vehicle
            if(vehicleDetails.moving.equals("1")){
                view.vehicle_status.text = "Moving"
            }
            else{
                view.vehicle_status.text = "Idle"
            }
                    view.date_tv.text = vehicleDetails.date
            view.setOnClickListener {
                listener.onVehicleItemClick(position)
            }
        }


    }

}