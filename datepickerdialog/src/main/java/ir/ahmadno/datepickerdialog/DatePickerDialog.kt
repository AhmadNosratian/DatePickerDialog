package ir.ahmadno.datepickerdialog

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import ir.ahmadno.datepickerdialog.api.DatePickerListener
import ir.ahmadno.datepickerdialog.databinding.LayoutDatePickerDialogBinding
import ir.ahmadno.datepickerdialog.date.DatePickerImpl
import ir.ahmadno.datepickerdialog.utils.toEnglishNumber
import ir.ahmadno.datepickerdialog.utils.toPersianNumber
import saman.zamani.persiandate.PersianDate

class DatePickerDialog(private val context: Context) {

    private var cancelable = true
    private var showInBottomSheet = true
    private var calendarType = CalendarType.GREGORIAN

    private var maxYear = 0
    private var minYear = 0

    private var maxMonth = 0
    private var maxDay = 0

    private lateinit var binding: LayoutDatePickerDialogBinding

    private var listener: DatePickerListener? = null

    private var positiveButtonString: String = context.getString(R.string.ok)
    private var negativeButtonString: String = context.getString(R.string.cancel)

    fun show() {
        Log.d(TAG, "Initializing view to show dialog...")
        val persianDate = PersianDate()
        val datePickerImpl = DatePickerImpl(persianDate)

        binding = LayoutDatePickerDialogBinding.inflate(LayoutInflater.from(context))

        binding.datePicker.setCalendarType(calendarType)

        if (maxYear > 0) {
            binding.datePicker.setMaxYear(maxYear)
        } else if (maxYear == THIS_YEAR) {
            maxYear = binding.datePicker.getYear()
            binding.datePicker.setMaxYear(maxYear)
        }

        if (maxMonth > 0) {
            binding.datePicker.setMaxMonth(maxMonth)
        } else if (maxMonth == THIS_MONTH) {
            maxMonth = binding.datePicker.getMonth()
            binding.datePicker.setMaxMonth(maxMonth)
        }

        if (maxDay > 0) {
            binding.datePicker.setMaxDay(maxDay)
        } else if (maxDay == THIS_DAY) {
            maxDay = binding.datePicker.getDay()
            binding.datePicker.setMaxDay(maxDay)
        }

        if (minYear > 0) {
            binding.datePicker.setMinYear(minYear)
        } else if (minYear == THIS_YEAR) {
            minYear = binding.datePicker.getYear()
            binding.datePicker.setMinYear(minYear)
        }

        binding.btnOk.text = positiveButtonString
        binding.btnCancel.text = negativeButtonString

        updateView()

        binding.datePicker.setOnDateChangedListener(object : DatePicker.OnDateChangedListener {
            override fun onDateChanged(newYear: Int, newMonth: Int, newDay: Int) {
                Log.d(TAG, "Date changed $newYear/$newMonth/$newDay")
                updateView()
            }
        })


        val dialog: AppCompatDialog
        if (showInBottomSheet) {
            dialog = BottomSheetDialog(context)
            dialog.setContentView(binding.root)
            dialog.setCancelable(cancelable)
        } else {
            dialog = AlertDialog.Builder(context)
                .setView(binding.root)
                .setCancelable(cancelable)
                .create()
        }


        binding.btnCancel.setOnClickListener {
            listener?.onDismiss()
            dialog.dismiss()
        }

        binding.btnOk.setOnClickListener {
            listener?.onDateSelected(binding.datePicker.getDatePicker())
            dialog.dismiss()
        }


        dialog.show()
    }

    private fun updateView() {
        val datePicker = binding.datePicker.getDatePicker()
        val date = when(calendarType) {
            CalendarType.JALALI -> {
                (datePicker.getJalaliDayName() + " " +  datePicker.getJalaliDay().toString() + " " + datePicker.getJalaliMonthName() + " " + datePicker.getJalaliYear()).toPersianNumber()
            }
            CalendarType.GREGORIAN -> {
                (datePicker.getGregorianDayName() + " " + datePicker.getGregorianDay() + " " + datePicker.getGregorianMonthName() + " " + datePicker.getGregorianYear()).toEnglishNumber()
            }
        }
        binding.txtViewDate.text = date
    }


    class Builder(private val context: Context) {
        private val datePickerDialog = DatePickerDialog(context)

        fun setMaxYear(maxYear: Int): Builder {
            datePickerDialog.maxYear = maxYear
            return this
        }

        fun setMinYear(minYear: Int): Builder {
            datePickerDialog.minYear = minYear
            return this
        }

        fun setMaxMonth(maxMonth: Int): Builder {
            if (maxMonth > 12) {
                throw RuntimeException("max month is not valid")
            }
            datePickerDialog.maxMonth = maxMonth
            return this
        }

        fun setMaxDay(maxDay: Int): Builder {
            if (maxDay > 31) {
                throw RuntimeException("max day is not valid")
            }
            datePickerDialog.maxDay = maxDay
            return this
        }

        fun setCalendarType(calendarType: CalendarType): Builder {
            datePickerDialog.calendarType = calendarType
            return this
        }

        fun setPositiveButtonString(@StringRes resValue: Int): Builder {
            datePickerDialog.positiveButtonString = context.getString(resValue)
            return this
        }

        fun setNegativeButtonString(@StringRes resValue: Int): Builder {
            datePickerDialog.negativeButtonString = context.getString(resValue)
            return this
        }

        fun setListener(listener: DatePickerListener): Builder {
            datePickerDialog.listener = listener
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            datePickerDialog.cancelable = cancelable
            return this
        }

        fun setShowInBottomSheet(showInBottomSheet: Boolean): Builder {
            datePickerDialog.showInBottomSheet = showInBottomSheet
            return this
        }

        fun build(): DatePickerDialog {
            return datePickerDialog
        }
    }


    companion object {
        const val THIS_YEAR = -1
        const val THIS_MONTH = -2
        const val THIS_DAY = -3
    }

}