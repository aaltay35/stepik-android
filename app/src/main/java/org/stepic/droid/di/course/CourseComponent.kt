package org.stepic.droid.di.course

import dagger.Subcomponent
import org.stepic.droid.ui.fragments.CourseDetailFragment
import org.stepic.droid.ui.fragments.CourseDetailInfoFragment
import org.stepic.droid.ui.fragments.CourseShortSyllabus
import org.stepic.droid.ui.fragments.SectionsFragment


@CourseAndSectionsScope
@Subcomponent
interface CourseComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): CourseComponent
    }

    fun inject(courseDetailFragment: CourseDetailFragment)

    fun inject(sectionsFragment: SectionsFragment)

    fun inject(courseInfo: CourseDetailInfoFragment)

    fun inject(courseShortSyllabus: CourseShortSyllabus)
}