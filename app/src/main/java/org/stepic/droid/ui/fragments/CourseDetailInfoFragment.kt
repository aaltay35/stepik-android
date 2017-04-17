package org.stepic.droid.ui.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_course_info_detailed.*
import org.stepic.droid.R
import org.stepic.droid.base.FragmentBase
import org.stepic.droid.ui.adapters.CourseDetailAdapter
import timber.log.Timber

class CourseDetailInfoFragment : FragmentBase() {
    companion object {
        fun newInstance(): CourseDetailInfoFragment {
            val fragment = CourseDetailInfoFragment()
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_course_info_detailed, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        courseInfoDetailedRecyclerView.adapter = CourseDetailAdapter(context, textResolver, null)
        courseInfoDetailedRecyclerView.layoutManager = LinearLayoutManager(context)

        courseInfoDetailedRecyclerView.onFlingListener = object : RecyclerView.OnFlingListener() {
            override fun onFling(velocityX: Int, velocityY: Int): Boolean {
                Timber.d("Fling $velocityX, $velocityY")
                return false
            }
        }

        courseInfoDetailedRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                Timber.d("onScrollStateChanged $newState")
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Timber.d("onScrolled $dx, $dy")
            }
        })

    }


}
