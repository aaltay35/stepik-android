package org.stepic.droid.ui.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_course_syllabus_short.*
import org.stepic.droid.R
import org.stepic.droid.base.App
import org.stepic.droid.base.FragmentBase
import org.stepic.droid.core.presenters.SectionsPresenter
import org.stepic.droid.core.presenters.contracts.SectionsView
import org.stepic.droid.model.Course
import org.stepic.droid.model.Section
import org.stepic.droid.ui.adapters.SmallSectionAdapter
import javax.inject.Inject

class CourseShortSyllabus : FragmentBase(), SectionsView {
    companion object {
        val courseKey = "courseKey"

        fun newInstance(course: Course): CourseShortSyllabus {
            val bundle = Bundle()
            bundle.putParcelable(courseKey, course)
            val fragment = CourseShortSyllabus()
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var sectionPresenter: SectionsPresenter

    private var smallSectionAdapter: SmallSectionAdapter? = null

    override fun injectComponent() {
        App
                .component()
                .courseComponentBuilder()
                .build()
                .inject(this)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_course_syllabus_short, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        courseSyllabusRecyclerView.layoutManager = LinearLayoutManager(context)

        sectionPresenter.attachView(this)
        sectionPresenter.showSections(arguments.getParcelable<Course>(courseKey), false)
    }

    override fun onDestroyView() {
        sectionPresenter.detachView(this)
        super.onDestroyView()
    }


    override fun onConnectionProblem() {
        if (smallSectionAdapter?.isEmpty() ?: true) {
            reportConnectionProblemSections.visibility = View.VISIBLE
            progressBarSections.visibility = View.GONE
            reportEmptySections.visibility = View.GONE
            courseSyllabusRecyclerView.visibility = View.GONE
        }
    }

    override fun onNeedShowSections(sectionList: List<Section>) {
        reportConnectionProblemSections.visibility = View.GONE
        progressBarSections.visibility = View.GONE
        reportEmptySections.visibility = View.GONE
        courseSyllabusRecyclerView.visibility = View.VISIBLE
        smallSectionAdapter = SmallSectionAdapter(context, sectionList)
        courseSyllabusRecyclerView.adapter = smallSectionAdapter
    }

    override fun onLoading() {
        reportConnectionProblemSections.visibility = View.GONE
        progressBarSections.visibility = View.VISIBLE
        reportEmptySections.visibility = View.GONE
        courseSyllabusRecyclerView.visibility = View.GONE
    }

    override fun updatePosition(position: Int) {
//        do nothing
    }

    override fun onEmptySections() {
        reportConnectionProblemSections.visibility = View.GONE
        progressBarSections.visibility = View.GONE
        reportEmptySections.visibility = View.VISIBLE
        courseSyllabusRecyclerView.visibility = View.GONE
    }

}
