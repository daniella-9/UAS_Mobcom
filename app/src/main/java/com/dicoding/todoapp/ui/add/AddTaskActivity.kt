package com.dicoding.todoapp.ui.add

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.databinding.ActivityAddTaskBinding
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.utils.DateTimePickerFragment
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class AddTaskActivity : AppCompatActivity(), DateTimePickerFragment.DateTimeListener {
    private var dueDateMillis: Long = System.currentTimeMillis()
    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var viewModel: AddTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.add_task)

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(AddTaskViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                val title = binding.addEdTitle.text.toString().trim()
                val description = binding.addEdDescription.text.toString().trim()
                val task = Task(0, title, description, dueDateMillis, false)
                viewModel.addTask(task)
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showDateTimePicker(view: View) {
        val dialogFragment = DateTimePickerFragment()
        dialogFragment.setListener(this) // Set the listener here
        dialogFragment.show(supportFragmentManager, "dateTimePicker")
    }



    override fun onDateTimeSet(year: Int, month: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance().apply {
            set(year, month, dayOfMonth, hourOfDay, minute)
        }
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        binding.addTvDueDate.text = formattedDate
        dueDateMillis = calendar.timeInMillis
        Log.d("DateTimeSet", "Year: $year, Month: $month, Day: $dayOfMonth, Hour: $hourOfDay, Minute: $minute")
    }

}
