package com.dicoding.todoapp.ui.username

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.dicoding.todoapp.R // Ganti dengan nama package Anda
import com.dicoding.todoapp.ui.list.TaskActivity

class EnterUsernameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val username: String? = sharedPreferences.getString("username", null)

        if (username != null && username.isNotEmpty()) {
            // Jika username sudah ada, langsung arahkan ke TaskActivity
            startActivity(Intent(this@EnterUsernameActivity, TaskActivity::class.java))
            finish()
        } else {
            setContentView(R.layout.activity_enter_username)

            val editTextUsername: EditText = findViewById(R.id.editTextUsername)
            val buttonSave: Button = findViewById(R.id.buttonSave)

            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            buttonSave.setOnClickListener {
                val newUsername: String = editTextUsername.text.toString()
                editor.putString("username", newUsername)
                editor.apply()

                // Redirect to TaskActivity after saving username
                startActivity(Intent(this@EnterUsernameActivity, TaskActivity::class.java))
                finish()
            }
        }
    }
}
