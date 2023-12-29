package com.dicoding.todoapp.ui.detail

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.databinding.ActivityTaskDetailBinding
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.utils.TASK_ID
import java.util.Calendar

@Suppress("DEPRECATION")
class DetailTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskDetailBinding
    private lateinit var viewModel: DetailTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        )[DetailTaskViewModel::class.java]

        binding.btnUpdate.setOnClickListener {
            updateTaskDetails()
        }

        binding.btnDeleteTask.setOnClickListener {
            viewModel.deleteTask()
            onBackPressed()
        }

        viewModel.setTaskId(intent.getIntExtra(TASK_ID, 0))

        viewModel.task.observe(this) { task ->
            if (task != null) {
                binding.detailEdTitle.setText(task.title)
                binding.detailEdDescription.setText(task.description)
                binding.detailEdDueDate.setText(DateConverter.convertMillisToString(task.dueDateMillis))

                binding.detailEdTitle.setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        val updatedTitle = binding.detailEdTitle.text.toString()
                        viewModel.updateTitle(updatedTitle)

                        // Update the EditText content without changing focus
                        val selectionStart = binding.detailEdTitle.selectionStart
                        val selectionEnd = binding.detailEdTitle.selectionEnd
                        binding.detailEdTitle.text?.replace(0, updatedTitle.length, updatedTitle)
                        binding.detailEdTitle.setSelection(selectionStart, selectionEnd)
                    }
                }

                binding.detailEdDescription.setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        val updatedDescription = binding.detailEdDescription.text.toString()
                        viewModel.updateDescription(updatedDescription)

                        // Update the EditText content without changing focus
                        val selectionStart = binding.detailEdDescription.selectionStart
                        val selectionEnd = binding.detailEdDescription.selectionEnd
                        binding.detailEdDescription.text?.replace(0, updatedDescription.length, updatedDescription)
                        binding.detailEdDescription.setSelection(selectionStart, selectionEnd)
                    }
                }

                binding.detailEdDueDate.setOnClickListener {
                    showDateTimePicker()
                }
            }
        }
    }

    private fun updateTaskDetails() {
        val updatedTitle = binding.detailEdTitle.text.toString()
        val updatedDescription = binding.detailEdDescription.text.toString()
        val updatedDueDate =
            DateConverter.convertStringToMillis(binding.detailEdDueDate.text.toString())

        viewModel.updateTaskDetails(updatedTitle, updatedDescription, updatedDueDate)
        onBackPressed()
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val timePicker = TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        // Combine date and time here and update your view model
                        val selectedCalendar = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth, hourOfDay, minute)
                        }
                        viewModel.updateDueDate(selectedCalendar.timeInMillis)
                        binding.detailEdDueDate.setText(
                            DateConverter.convertMillisToString(selectedCalendar.timeInMillis)
                        )
                    },
                    currentHour,
                    currentMinute,
                    false
                )
                timePicker.show()
            },
            currentYear,
            currentMonth,
            currentDay
        )

        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
        datePicker.show()
    }
}
