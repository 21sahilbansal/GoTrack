package com.example.gotrack.model

import com.google.gson.annotations.SerializedName

class VehicleDetails {
    @SerializedName("Vehicle")
    var vehicle: String? = null
    @SerializedName("Lati")
    var lati: Double? = null
    @SerializedName("Long")
    var long: Double? = null
    @SerializedName("Date")
    var date: String? = null
    @SerializedName("Imei")
    var imei: String? = null
    @SerializedName("VehicleId")
    var vehicleId: String? = null
    @SerializedName("Location")
    var location: String? = null
    @SerializedName("Moving")
    var moving: String? = null
    @SerializedName("NoDate")
    var noDate: String? = null
    @SerializedName("CompanyId")
    var companyId: String? = null
    @SerializedName("TrackNum")
    var trackNum: String? = null
    @SerializedName("ExtraInfo")
    var extraInfo: String? = null
    @SerializedName("DriverName")
    var driverName: String? = null
    @SerializedName("DriverMobile")
    var driverMobile: String? = null
    @SerializedName("Mobile")
    var mobile: String? = null
    @SerializedName("Device")
    var device: String? = null

}