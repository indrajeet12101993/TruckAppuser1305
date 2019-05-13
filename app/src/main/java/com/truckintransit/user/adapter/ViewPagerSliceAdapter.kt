package com.truckintransit.user.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.truckintransit.user.fragment.welcome.FragmentInroOne
import com.truckintransit.user.fragment.welcome.FragmentInteoFour
import com.truckintransit.user.fragment.welcome.FragmentIntroThree
import com.truckintransit.user.fragment.welcome.FragmentIntroTwo

class ViewPagerSliceAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {


    override fun getItem(position: Int): Fragment {


        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FragmentIntroTwo()
            1 -> fragment = FragmentIntroThree()
            2 -> fragment = FragmentInteoFour()
            3 -> fragment = FragmentInroOne()
            4 -> fragment = FragmentInteoFour()
            5 -> fragment = FragmentIntroTwo()


        }

        return fragment!!


    }

    override fun getCount(): Int {
        return 4
    }


}