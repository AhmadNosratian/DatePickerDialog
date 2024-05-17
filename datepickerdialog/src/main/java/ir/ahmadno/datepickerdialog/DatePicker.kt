package ir.ahmadno.datepickerdialog

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import ir.ahmadno.datepickerdialog.api.IDatePicker
import ir.ahmadno.datepickerdialog.databinding.LayoutDatePickerBinding
import ir.ahmadno.datepickerdialog.date.DatePickerImpl
import ir.ahmadno.datepickerdialog.utils.toEnglishNumber
import ir.ahmadno.datepickerdialog.utils.toPersianNumber
import saman.zamani.persiandate.PersianDate
import kotlin.math.max


class DatePicker(context: Context, attr: AttributeSet?) : LinearLayout(context, attr) {

    private var yearRange = 0
    private var minYear = -1
    private var maxYear = -1
    private var maxMonth = 0
    private var maxDay = -1

    private var displayMonthNames = false

    private var mListener: OnDateChangedListener? = null

    private var selectedMonth = 0
    private var selectedYear = 0
    private var selectedDay = 0
    private var calendarType = CalendarType.GREGORIAN

    private var persianDate: IDatePicker

    private var binding: LayoutDatePickerBinding

    constructor(context: Context) : this(context, null)

    init {
        val inflater = LayoutInflater.from(context)
        binding = LayoutDatePickerBinding.inflate(inflater, this, true)

        setNumberPickerFormatter()

        persianDate = DatePickerImpl(PersianDate())

        // update variables from xml
        updateVariablesFromXml(context, attr)

        // update view
        updateViewData()
    }


    private fun dateChanged() {
        val currentYear = binding.yearPicker.value
        val currentMonth = binding.monthPicker.value
        val currentDay = binding.dayPicker.value

        binding.dayPicker.minValue = 1

        val maxMonth = getMaxMonthBySelectedYear(currentYear)
        if (currentMonth > maxMonth){
            binding.monthPicker.value = maxMonth
        }
        setMonthNumberPickerMaxValue(maxMonth)


        val maxDay = getMaxDayByCalendarType(currentYear,currentMonth)
        if (currentDay > maxDay){
            binding.dayPicker.value = maxDay
        }
        setDayNumberPickerMaxValue(maxDay)

        setDate(
            binding.yearPicker.value,
            binding.monthPicker.value,
            binding.dayPicker.value
        )

        mListener?.onDateChanged( binding.yearPicker.value,
            binding.monthPicker.value,
            binding.dayPicker.value)
    }

    private fun getMaxMonthBySelectedYear(selectedYear: Int): Int {
        if (selectedYear == maxYear){
            return maxMonth
        }else{
            return 12
        }
    }

    private fun setDate(year : Int, month : Int, day : Int){
        when(calendarType){
            CalendarType.JALALI -> {
                persianDate.setJalaliDate(year,month,day)
            }
            CalendarType.GREGORIAN -> {
                persianDate.setGregorianDate(year,month,day)
            }
        }
    }

    private fun updateViewData() {
        /*
         * initialing yearNumberPicker
         */
        Log.d(TAG, "initializing yearNumberPicker")
        binding.yearPicker.minValue = minYear
        binding.yearPicker.maxValue = maxYear
        Log.d(TAG, "yearNumberPicker minValue $minYear, maxValue $maxYear ")


        Log.d(TAG, "yearNumberPicker selectedYear $selectedYear ")
        binding.yearPicker.value = selectedYear
        binding.yearPicker.setOnValueChangedListener { picker, oldValue, newValue -> dateChanged() }


        /*
         * initialing monthNumberPicker
         */
        Log.d(TAG, "initializing monthNumberPicker")
        binding.monthPicker.minValue = 1
        binding.monthPicker.maxValue = if (maxMonth > 0) maxMonth else 12
        Log.d(
            TAG,
            "monthNumberPicker minValue ${binding.monthPicker.minValue}, maxValue ${binding.monthPicker.maxValue} "
        )

        require(!(selectedMonth < 1 || selectedMonth > 12)) {
            String.format("Selected month (%d) must be between 1 and 12", selectedMonth)
        }

        binding.monthPicker.value = selectedMonth
        Log.d(TAG, "monthNumberPicker selectedMonth $selectedMonth ")
        binding.monthPicker.setOnValueChangedListener { picker, oldValue, newValue -> dateChanged() }

        /*
         * initializing dayNumberPicker
         */
        Log.d(TAG, "initializing dayNumberPicker")
        binding.dayPicker.minValue = 1
        setDayNumberPickerMaxValue(getMaxDayByCalendarType(selectedYear,selectedMonth))
        Log.d(
            TAG,
            "dayNumberPicker minValue ${binding.dayPicker.minValue}, maxValue ${binding.dayPicker.maxValue} "
        )

        require(!(selectedDay > 31 || selectedDay < 1)) {
            String.format("Selected day (%d) must be between 1 and 31", selectedDay)
        }

        binding.dayPicker.value = selectedDay
        Log.d(TAG, "dayNumberPicker selectedDay $selectedDay ")
        binding.dayPicker.setOnValueChangedListener { picker, oldValue, newValue -> dateChanged() }

    }

    private fun getMaxDayByCalendarType(year : Int, month: Int): Int {
        return when (calendarType) {
            CalendarType.JALALI -> {
                persianDate.getJalaliMonthLength(year,month)
            }

            CalendarType.GREGORIAN -> {
                persianDate.getGregorianMonthLength(year,month)
            }
        }
    }



    private fun setMonthNumberPickerMaxValue(value: Int) {
        binding.monthPicker.maxValue = value
      /*  if (binding.monthPicker.value == maxMonth) {
            if (maxMonth > 0) {
                binding.monthPicker.maxValue = maxDay
            } else {
                binding.monthPicker.maxValue = value
            }
        } else {
            binding.monthPicker.maxValue = value
        }*/
    }

    private fun setDayNumberPickerMaxValue(value: Int) {
        if (binding.monthPicker.value == maxMonth) {
            Log.d(TAG, "setDayNumberPickerMaxValue in maxMonth $maxMonth")
            if (maxDay > 0) {
                Log.d(TAG, "dayPicker maxValue set to $maxDay")
                binding.dayPicker.maxValue = maxDay
            } else {
                Log.d(TAG, "dayPicker maxValue set to $value")
                binding.dayPicker.maxValue = value
            }
        } else {
            Log.d(TAG, "set dayPicker maxValue to $value")
            binding.dayPicker.maxValue = value
        }
    }


    private fun updateVariablesFromXml(context: Context, attr: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attr, R.styleable.DatePicker)
        yearRange = typedArray.getInteger(R.styleable.DatePicker_yearRange, DEFAULT_YEAR_RANGE)

        val calType = typedArray.getInt(R.styleable.DatePicker_calendarType, 0)
        calendarType = when (calType) {
            1 -> CalendarType.GREGORIAN
            else -> CalendarType.JALALI
        }

        minYear = typedArray.getInt(R.styleable.DatePicker_minYear, getYear() - DEFAULT_YEAR_RANGE)
        maxYear = typedArray.getInt(R.styleable.DatePicker_maxYear, getYear() + DEFAULT_YEAR_RANGE)
        displayMonthNames = typedArray.getBoolean(R.styleable.DatePicker_displayMonthNames, false)

        selectedDay = typedArray.getInt(R.styleable.DatePicker_selectedDay, getDay())
        selectedMonth = typedArray.getInt(R.styleable.DatePicker_selectedMonth, getMonth())
        selectedYear = typedArray.getInt(R.styleable.DatePicker_selectedYear, getYear())


        // if you pass selected year before min year, then we need to push min year to before that
        if (minYear > selectedYear) {
            minYear = selectedYear - yearRange
        }

        if (maxYear < selectedYear) {
            maxYear = selectedYear + yearRange
        }

        typedArray.recycle()
    }

    private fun onCalendarTypeChange(){
        minYear = getYear() - DEFAULT_YEAR_RANGE
        maxYear = getYear() + DEFAULT_YEAR_RANGE

        selectedDay = getDay()
        selectedMonth = getMonth()
        selectedYear = getYear()

        if (minYear > selectedYear) {
            minYear = selectedYear - yearRange
        }

        if (maxYear < selectedYear) {
            maxYear = selectedYear + yearRange
        }
    }

     fun getDay(): Int {
        return when (calendarType) {
            CalendarType.JALALI -> {
                persianDate.getJalaliDay()
            }

            CalendarType.GREGORIAN -> {
                persianDate.getGregorianDay()
            }
        }
    }

     fun getMonth(): Int {
        return when (calendarType) {
            CalendarType.JALALI -> {
                persianDate.getJalaliMonth()
            }

            CalendarType.GREGORIAN -> {
                persianDate.getGregorianMonth()
            }
        }
    }

     fun getYear(): Int {
        return when (calendarType) {
            CalendarType.JALALI -> {
                persianDate.getJalaliYear()
            }

            CalendarType.GREGORIAN -> {
                persianDate.getGregorianYear()
            }
        }
    }

    fun getDatePicker() : IDatePicker {
        return persianDate
    }

    fun setMaxYear(maxYear : Int){
        this.maxYear = maxYear
        updateViewData()
    }

    fun setMinYear(minYear : Int){
        this.minYear = minYear
        updateViewData()
    }

    fun setMaxMonth(maxMonth : Int){
        this.maxMonth = maxMonth
        updateViewData()
    }

    fun setMaxDay(maxDay : Int){
        this.maxDay = maxDay
        updateViewData()
    }

    fun setCalendarType(calendarType: CalendarType){
        this.calendarType = calendarType
        onCalendarTypeChange()
        updateViewData()
        setNumberPickerFormatter()
    }


    fun setOnDateChangedListener(onDateChangedListener: OnDateChangedListener) {
        this.mListener = onDateChangedListener
    }


    private fun setNumberPickerFormatter() {
        when (calendarType) {
            CalendarType.JALALI -> {
                binding.yearPicker.setFormatter { it.toString().toPersianNumber() }
                binding.monthPicker.setFormatter { it.toString().toPersianNumber() }
                binding.dayPicker.setFormatter { it.toString().toPersianNumber() }
            }

            CalendarType.GREGORIAN -> {
                binding.yearPicker.setFormatter { it.toString().toEnglishNumber() }
                binding.monthPicker.setFormatter { it.toString().toEnglishNumber() }
                binding.dayPicker.setFormatter { it.toString().toEnglishNumber() }
            }
        }

    }


    interface OnDateChangedListener {
        fun onDateChanged(newYear: Int, newMonth: Int, newDay: Int)
    }


}
