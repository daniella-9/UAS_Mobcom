package com.dicoding.todoapp.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.username.EnterUsernameActivity


@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    private val SPLASH_DISPLAY_LENGTH = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val mainIntent = Intent(this@SplashActivity, EnterUsernameActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }
}
