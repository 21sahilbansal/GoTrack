package com.example.gotrack.userInterface.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrack.R
import com.example.gotrack.callBacks.AdpterCallBack
import com.example.gotrack.model.VehicleDetails
import kotlinx.android.synthetic.main.item_vehicle_detail.view.*
import java.util.*
import kotlin.collections.ArrayList


class AllVehicleAdapter(vehicleDetailList: ArrayList<VehicleDetails>, private val listener: AdpterCallBack) : RecyclerView.Adapter<AllVehicleAdapter.VehicleViewHolder>() {

    var newVehicleList = ArrayList<VehicleDetails>()

    init {
        newVehicleList.addAll(vehicleDetailList)
    }

    fun updateList(sortedList: ArrayList<VehicleDetails>) {
        newVehicleList.clear()
        newVehicleList.addAll(sortedList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vehicle_detail, parent, false)
        return VehicleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return newVehicleList.size
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val rnd = Random()
        val currentColor: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        holder.itemView.item_card_view.setCardBackgroundColor(currentColor)
        holder.bind(newVehicleList[position], position)
    }

    inner class VehicleViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(vehicleDetails: VehicleDetails, position: Int) {
            view.vehicle_location.text = vehicleDetails.location
            view.vehicle_no.text = vehicleDetails.vehicleId
            view.vehicle_type.text = vehicleDetails.vehicle
            if (vehicleDetails.moving.equals("1")) {
                view.vehicle_status.text = "Moving"
            } else {
                view.vehicle_status.text = "Idle"
            }
            view.date_tv.text = vehicleDetails.date
            view.setOnClickListener {
                listener.onVehicleItemClick(position)
            }
        }
    }
}