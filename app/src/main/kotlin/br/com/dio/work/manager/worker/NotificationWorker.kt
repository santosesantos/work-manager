package br.com.dio.work.manager.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import br.com.dio.work.manager.data.datasource.VideosDataSource
import br.com.dio.work.manager.ui.extensions.showBigPictureNotification
import java.util.concurrent.TimeUnit

class NotificationWorker(
    private val context: Context,
    workerParams: WorkerParameters
): Worker(context, workerParams) {
    override fun doWork(): Result {
        val video = VideosDataSource.getRandomVideo()
        context.showBigPictureNotification(video)

        return Result.success()
    }

    companion object {
        private const val TAG = "NotificationWorker"
        private const val WORKER_NAME = "WORKER_NAME"

        fun start(context: Context) {
            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    WORKER_NAME,
                    ExistingWorkPolicy.KEEP,
                    createRequest()
                )
        }

        private fun createRequest() =
            OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(2, TimeUnit.MINUTES)
                .build()

        fun startPeriodic(context: Context) {
            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    WORKER_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    createPeriodicRequest()
                )
        }

        private fun createPeriodicRequest() =
            PeriodicWorkRequestBuilder<NotificationWorker>(
                15,
                TimeUnit.MINUTES
            ).build()
    }
}