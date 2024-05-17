package ir.ahmadno

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ir.ahmadno.datepickerdialog.CalendarType
import ir.ahmadno.datepickerdialog.DatePickerDialog
import ir.ahmadno.datepickerdialog.api.DatePickerListener
import ir.ahmadno.datepickerdialog.api.IDatePicker
import ir.ahmadno.datepickerdialog.sample.R
import ir.ahmadno.datepickerdialog.sample.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private var calendarType = CalendarType.GREGORIAN


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.toggleBtnCalendarType.check(if (calendarType == CalendarType.JALALI) binding.btnJalali.id else binding.btnGregorian.id)
        binding.toggleBtnCalendarType.addOnButtonCheckedListener { group, checkedId, isChecked ->

            if (isChecked) {
                when (checkedId) {
                    R.id.btn_jalali -> {
                        calendarType = CalendarType.JALALI
                    }

                    R.id.btn_gregorian -> {
                        calendarType = CalendarType.GREGORIAN
                    }
                }
            }
        }

        binding.btnShowAsDialog.setOnClickListener {
            DatePickerDialog.Builder(this@MainActivity)
                .setCalendarType(calendarType)
                .setMaxYear(DatePickerDialog.THIS_YEAR)
                .setMaxMonth(DatePickerDialog.THIS_MONTH)
                .setMaxDay(DatePickerDialog.THIS_DAY)
                .setCancelable(false)
                .setMinYear(1400)
                .setShowInBottomSheet(false)
                .setListener(object : DatePickerListener {
                    override fun onDateSelected(datePicker: IDatePicker) {
                        Log.d(TAG, "onDateSelected: " + datePicker.getTimeStamp())
                        Log.d(TAG, "onDateSelected: " + datePicker.getGregorianDate())

                        Toast.makeText(this@MainActivity, "${datePicker.getJalaliYear()}/${datePicker.getJalaliMonth()}/${datePicker.getJalaliDay()} ", Toast.LENGTH_SHORT).show()
                    }

                    override fun onDismiss() {
                        Toast.makeText(this@MainActivity, "dismissed", Toast.LENGTH_SHORT).show()
                    }

                })
                .build().show()
        }

        binding.btnShowAsBottomSheet.setOnClickListener {
            DatePickerDialog.Builder(this@MainActivity)
                .setCalendarType(calendarType)
                .setMaxYear(DatePickerDialog.THIS_YEAR)
                .setMaxMonth(DatePickerDialog.THIS_MONTH)
                .setMaxDay(DatePickerDialog.THIS_DAY)
                .setPositiveButtonString(R.string.ok)
                .setNegativeButtonString(R.string.cancel)
                .setCancelable(false)
                .setShowInBottomSheet(true)
                .setListener(object : DatePickerListener {
                    override fun onDateSelected(datePicker: IDatePicker) {
                        Log.d(TAG, "onDateSelected: " + datePicker.getTimeStamp())
                        Log.d(TAG, "onDateSelected: " + datePicker.getGregorianDate())
                        Log.d(TAG, "onDateSelected: " + datePicker.getJalaliLongDate())
                        Log.d(TAG, "onDateSelected: " + datePicker.getJalaliMonthName())

                        Toast.makeText(this@MainActivity, "${datePicker.getJalaliYear()}/${datePicker.getJalaliMonth()}/${datePicker.getJalaliDay()} ", Toast.LENGTH_SHORT).show()
                    }

                    override fun onDismiss() {
                        Toast.makeText(this@MainActivity, "dismissed", Toast.LENGTH_SHORT).show()
                    }

                })
                .build().show()
        }

    }
}