package com.example.gotrack.userInterface.fragments

import android.view.View
import androidx.fragment.app.Fragment
import com.example.gotrack.R
import com.example.gotrack.base.BaseFragment
import com.example.gotrack.callBacks.FragCallBack
import kotlinx.android.synthetic.main.splash_fragment.*

class SplashFragment : BaseFragment() {
    private lateinit var fragChangeListener: FragCallBack

    override fun viewInitialization(view: View?) {
        fragChangeListener = context as FragCallBack
        vehicle_status_button.setOnClickListener {
            fragChangeListener.onFragmentChange(AllVehicleFragment.newInstance(), AllVehicleFragment.TAG)

        }
    }

    override val fragmentLayoutId: Int
        get() = R.layout.splash_fragment

    override fun showLoadingState(loading: Boolean) {
    }

    companion object{
        val TAG = SplashFragment::class.java.name
        fun newInstance(): SplashFragment {
            return SplashFragment()
        }
    }


}