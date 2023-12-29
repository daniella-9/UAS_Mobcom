package com.dicoding.todoapp.setting

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.Data
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.dicoding.todoapp.R
import com.dicoding.todoapp.notification.NotificationWorker
import java.util.concurrent.TimeUnit

class SettingsActivity : AppCompatActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Notifications permission granted")
            } else {
                showToast("Notifications will not show without permission")
            }
        }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        requestNotificationPermission()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        val permission = Manifest.permission.POST_NOTIFICATIONS
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), PERMISSION_REQUEST_CODE)
        } else {
            showToast("Notifications permission granted")
        }
    }

    private fun scheduleDailyReminder(channelName: String) {
        val data = Data.Builder()
            .putString(NotificationWorker.NOTIFICATION_CHANNEL_ID, channelName)
            .build()

        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(NotificationWorker::class.java,1, TimeUnit.DAYS)
                .setInputData(data)
                .build()

        val workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueue(periodicWorkRequest)
    }

    private fun cancelDailyReminder() {
        val workManager = WorkManager.getInstance(applicationContext)
        workManager.cancelAllWorkByTag(NotificationWorker::class.java.simpleName)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private lateinit var workManager: WorkManager

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val prefNotification = findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
            prefNotification?.setOnPreferenceChangeListener { _, newValue ->
                val channelName = getString(R.string.notify_channel_name)

                if (newValue as Boolean) {
                    scheduleDailyReminder(channelName)
                } else {
                    cancelDailyReminder()
                }
                true
            }

            workManager = WorkManager.getInstance(requireContext())
        }

        private fun scheduleDailyReminder(channelName: String) {
            val settingsActivity = activity as? SettingsActivity
            settingsActivity?.scheduleDailyReminder(channelName)
        }

        private fun cancelDailyReminder() {
            val settingsActivity = activity as? SettingsActivity
            settingsActivity?.cancelDailyReminder()
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }
}
