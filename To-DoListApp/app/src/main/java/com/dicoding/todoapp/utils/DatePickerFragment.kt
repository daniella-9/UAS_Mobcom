package com.dicoding.todoapp.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class DateTimePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private var mListener: DateTimeListener? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Mengatur listener sebelum menampilkan dialog DatePicker
        mListener?.let {
            val datePickerDialog = DatePickerDialog(requireContext(), this, year, month, dayOfMonth)
            datePickerDialog.setOnDateSetListener { _, y, m, d ->
                val timePickerDialog = TimePickerDialog(
                    requireContext(),
                    { _, h, min ->
                        it.onDateTimeSet(y, m, d, h, min)
                    },
                    hourOfDay,
                    minute,
                    true
                )
                timePickerDialog.show()
            }
            return datePickerDialog
        } ?: run {
            // Jika listener belum diatur, kembalikan dialog kosong
            return Dialog(requireContext())
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as? DateTimeListener
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
    fun setListener(listener: DateTimeListener) {
        mListener = listener
    }

    interface DateTimeListener {
        fun onDateTimeSet(year: Int, month: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int)

    }
}
