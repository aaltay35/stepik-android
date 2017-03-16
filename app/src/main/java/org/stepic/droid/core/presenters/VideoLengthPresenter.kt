package org.stepic.droid.core.presenters

import org.stepic.droid.concurrency.MainHandler
import org.stepic.droid.core.VideoLengthResolver
import org.stepic.droid.core.presenters.contracts.VideoLengthView
import org.stepic.droid.model.Step
import org.stepic.droid.model.Video
import org.stepic.droid.util.TimeUtil
import org.stepic.droid.util.resolvers.VideoResolver
import java.util.concurrent.ThreadPoolExecutor

class VideoLengthPresenter(private val threadPoolExecutor: ThreadPoolExecutor,
                           private val mainHandler: MainHandler,
                           private val videoResolver: VideoResolver,
                           private val videoLengthResolver: VideoLengthResolver) : PresenterBase<VideoLengthView>() {
    var cachedFormat: String? = null

    fun fetchLength(video: Video, step: Step) {
        cachedFormat?.let {
            view?.onVideoLengthDetermined(it)
            return
        }
        threadPoolExecutor.execute {
            val path = videoResolver.resolveVideoUrl(video, step)
            val millis = videoLengthResolver.determineLengthInMillis(path) ?: return@execute
            // if not determine millis -> do not form printable string

            val printable = TimeUtil.getFormattedVideoTime(millis)
            mainHandler.post {
                cachedFormat = printable
                view?.onVideoLengthDetermined(printable)
            }
        }
    }
}