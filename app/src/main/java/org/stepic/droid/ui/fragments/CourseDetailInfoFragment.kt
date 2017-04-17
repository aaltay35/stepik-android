package org.stepic.droid.ui.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_course_info_detailed.*
import org.stepic.droid.R
import org.stepic.droid.base.App
import org.stepic.droid.base.FragmentBase
import org.stepic.droid.core.presenters.InstructorsPresenter
import org.stepic.droid.core.presenters.contracts.InstructorsView
import org.stepic.droid.model.Course
import org.stepic.droid.model.User
import org.stepic.droid.ui.adapters.CourseDetailAdapter
import javax.inject.Inject

class CourseDetailInfoFragment : FragmentBase(), InstructorsView {
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

    @Inject
    lateinit var instructorsPresenter: InstructorsPresenter

    var courseDetailAdapter: CourseDetailAdapter? = null

    override fun injectComponent() {
        super.injectComponent()
        App
                .component()
                .courseComponentBuilder()
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_course_info_detailed, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val course = arguments.getParcelable<Course>(courseKey)
        val coursePropertyList = coursePropertyResolver.getSortedPropertyList(course)
        courseDetailAdapter = CourseDetailAdapter(context, textResolver, coursePropertyList)
        courseInfoDetailedRecyclerView.adapter = courseDetailAdapter
        courseInfoDetailedRecyclerView.layoutManager = LinearLayoutManager(context)
        instructorsPresenter.attachView(this)
        instructorsPresenter.fetchInstructors(course.instructors)
    }

    override fun onDestroyView() {
        instructorsPresenter.detachView(this)
        super.onDestroyView()
    }

    override fun showInstructors(instructors: List<User>) {
        courseDetailAdapter?.setInstructors(instructors, activity)
    }

}
