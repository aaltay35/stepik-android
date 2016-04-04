package org.stepic.droid.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Looper
import android.support.annotation.DrawableRes
import android.support.v4.app.NotificationCompat
import com.squareup.picasso.Picasso
import com.yandex.metrica.YandexMetrica
import org.stepic.droid.R
import org.stepic.droid.base.MainApplication
import org.stepic.droid.configuration.IConfig
import org.stepic.droid.model.Course
import org.stepic.droid.notifications.model.Notification
import org.stepic.droid.notifications.model.NotificationType
import org.stepic.droid.preferences.UserPreferences
import org.stepic.droid.store.operations.DatabaseFacade
import org.stepic.droid.util.ColorUtil
import org.stepic.droid.util.HtmlHelper
import org.stepic.droid.util.JsonHelper
import org.stepic.droid.view.activities.MainFeedActivity
import org.stepic.droid.web.IApi

class NotificationManagerImpl(val dbFacade: DatabaseFacade, val api: IApi, val configs: IConfig, val userPreferences: UserPreferences) : INotificationManager {

    override fun showNotification(notification: Notification) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw RuntimeException("Can't create notification on main thread")
        }
        if (userPreferences.isNotificationEnabled) {
            resolveAndSendNotification(notification)
        } else {
            YandexMetrica.reportEvent("Notification is disabled by user in app")
        }
    }

    private fun resolveAndSendNotification(notification: Notification) {
        if (notification.htmlText == null || notification.htmlText.isEmpty()) {
            YandexMetrica.reportEvent("notification html text was null", JsonHelper.toJson(notification))
        } else {
            //resolve which notification we should show
            when (notification.type) {
                NotificationType.learn -> sendLearnNotification(notification.htmlText)
                NotificationType.comments -> sendCommentNotification(notification.htmlText)
                else -> YandexMetrica.reportEvent("notification is not support: " + notification.type)
            }
        }
    }

    private fun sendCommentNotification(rawMessageHtml: String) {
        // just for test fixme: remove THIS!!! IMPLEMENT COMMENT
        YandexMetrica.reportEvent("notification comment is shown")
        sendLearnNotification(rawMessageHtml)
    }

    private fun sendLearnNotification(rawMessageHtml: String) {
        YandexMetrica.reportEvent("notification learn is shown")

        val intent = Intent(MainApplication.getAppContext(), MainFeedActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(MainApplication.getAppContext(), 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val largeIcon = getPictureByCourseId()
        val colorArgb = ColorUtil.getColorArgb(R.color.stepic_brand_primary)

        val justText: String = HtmlHelper.fromHtml(rawMessageHtml).toString()
        val notification = NotificationCompat.Builder(MainApplication.getAppContext())
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.ic_matching)
                .setContentTitle("I handle this notification")
                .setContentText(justText)
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(justText))
                .setColor(colorArgb)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        addVibrationIfNeed(notification)


        val notificationManager = MainApplication.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(0 /* ID of notification */, notification.build())
    }

    private fun getPictureByCourseId(courseId: Long = 67): Bitmap {
        var course: Course? = dbFacade.getCourseById(courseId, DatabaseFacade.Table.enrolled)
        if (course == null) {
            course = api.getCourse(courseId).execute()?.body()?.courses?.get(0)
        }

        val cover = course?.cover
        //FIXME create special icon for notification placeholder ?? dp in mdpi
        @DrawableRes val notificationPlaceholder = R.drawable.ic_course_placeholder

        if (cover == null) {
            return BitmapFactory.decodeResource(MainApplication.getAppContext().getResources(), notificationPlaceholder);
        } else {
            return Picasso.with(MainApplication.getAppContext())
                    .load(configs.baseUrl + cover)
                    .resize(200, 200) //pixels
                    .placeholder(notificationPlaceholder)
                    .error(notificationPlaceholder)
                    .get()
        }
    }

    private fun addVibrationIfNeed(builder: NotificationCompat.Builder) {
        if (userPreferences.isVibrateNotificationEnabled) {
            builder.setVibrate(longArrayOf(0, 100, 0, 100))
        }
    }
}