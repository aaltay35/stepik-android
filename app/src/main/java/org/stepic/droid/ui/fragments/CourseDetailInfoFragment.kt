package org.stepic.droid.ui.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_course_info_detailed.*
import org.stepic.droid.R
import org.stepic.droid.base.FragmentBase
import org.stepic.droid.model.Course
import org.stepic.droid.ui.adapters.CourseDetailAdapter

class CourseDetailInfoFragment : FragmentBase() {
    companion object {
        val courseKey = "courseKey"

        fun newInstance(course: Course): CourseDetailInfoFragment {
            val bundle = Bundle()
            bundle.putParcelable(courseKey, course)
            val fragment = CourseDetailInfoFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_course_info_detailed, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val course = arguments.getParcelable<Course>(courseKey)
        val coursePropertyList = coursePropertyResolver.getSortedPropertyList(course)
        courseInfoDetailedRecyclerView.adapter = CourseDetailAdapter(context, textResolver, coursePropertyList)
        courseInfoDetailedRecyclerView.layoutManager = LinearLayoutManager(context)
    }


}
