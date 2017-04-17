package org.stepic.droid.core.presenters

import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import org.stepic.droid.concurrency.MainHandler
import org.stepic.droid.core.presenters.contracts.InstructorsView
import org.stepic.droid.di.course.CourseAndSectionsScope
import org.stepic.droid.model.User
import org.stepic.droid.web.Api
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@CourseAndSectionsScope
class InstructorsPresenter
@Inject constructor(
        private val threadPoolExecutor: ThreadPoolExecutor,
        private val mainHandler: MainHandler,
        private val api: Api) : PresenterBase<InstructorsView>() {

    private val isHandling = AtomicBoolean(false)
    private val instructorsCache = ArrayList<User>()

    @MainThread
    fun fetchInstructors(instructorIds: LongArray) {
        if (instructorIds.isEmpty()) {
            return
        }
        if (instructorsCache.isNotEmpty()) {
            view?.showInstructors(instructorsCache)
            return
        }

        if (isHandling.compareAndSet(false, true)) {
            threadPoolExecutor.execute {
                try {
                    fetchInstructorsDirectly(instructorIds)
                } finally {
                    isHandling.set(false)
                }
            }
        }
    }

    @WorkerThread
    private fun fetchInstructorsDirectly(instructorIds: LongArray) {
        try {
            val instructors = api.getUsers(instructorIds).execute().body()?.users!!
            if (instructors.isNotEmpty()) {

                mainHandler.post {
                    instructorsCache.clear()
                    instructorsCache.addAll(instructors)
                    view?.showInstructors(instructorsCache)
                }
            }
        } catch (ex: Exception) {
            //we do not handle it by design, just do not change state
        }

    }

}
