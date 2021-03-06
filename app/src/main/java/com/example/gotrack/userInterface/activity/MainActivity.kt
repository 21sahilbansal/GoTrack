package com.example.gotrack.userInterface.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.gotrack.R
import com.example.gotrack.base.BaseActivity
import com.example.gotrack.callBacks.FragCallBack
import com.example.gotrack.model.AllVehicle
import com.example.gotrack.userInterface.fragments.SplashFragment
import com.google.gson.Gson


class MainActivity : BaseActivity(), FragCallBack {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getCompleteVehicleData()
        openVehicleDetailFragment()
    }

    private fun getCompleteVehicleData() {
        val jsonString = resources.openRawResource(R.raw.all_vehicle)
                .bufferedReader().use { it.readText() }
        convertToGsonObjects(jsonString)
    }

    private fun convertToGsonObjects(jsonString: String) {
        var gson = Gson()
        allVehicleObject = gson.fromJson(jsonString, AllVehicle::class.java)
    }

    override val layoutRes: Int
        get() = R.layout.activity_main

    private fun openVehicleDetailFragment() {
        replaceFragment(R.id.container, SplashFragment.newInstance(), SplashFragment.TAG)
    }

    override fun onFragmentChange(fragment: Fragment, tag: String) {
        replaceFragment(R.id.container, fragment, tag, true)
    }

    companion object {
        var allVehicleObject: Any? = null
        var count = 0
    }
}