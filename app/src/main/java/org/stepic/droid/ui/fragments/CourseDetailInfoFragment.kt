package org.stepic.droid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.stepic.droid.base.FragmentBase

class CourseDetailInfoFragment : FragmentBase() {
    companion object {
        fun newInstance(): CourseDetailInfoFragment {
            val fragment = CourseDetailInfoFragment()
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }


}
