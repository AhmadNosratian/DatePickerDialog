package ir.ahmadno.datepickerdialog.date

import ir.ahmadno.datepickerdialog.api.IDatePicker
import saman.zamani.persiandate.PersianDate
import java.util.Date

class DatePickerImpl(private val persianDate: PersianDate) :
    IDatePicker {

    override fun setDate(date: Date) {
        // TODO: not implemented
    }

    override fun setJalaliDate(year: Int, month: Int, day: Int) {
        try{
            persianDate.shYear = year
            persianDate.shMonth = month
            persianDate.shDay = day
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
    override fun setGregorianDate(year: Int, month: Int, day: Int) {
        try{
            persianDate.grgYear = year
            persianDate.grgMonth = month
            persianDate.grgDay = day
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    override fun getJalaliYear(): Int = persianDate.shYear

    override fun getJalaliMonth(): Int = persianDate.shMonth

    override fun getJalaliDay(): Int = persianDate.shDay
    override fun getJalaliDayName(): String = persianDate.dayName()

    override fun getJalaliMonthLength(year: Int,month: Int): Int = persianDate.getMonthLength(year,month)

    override fun getGregorianYear(): Int = persianDate.grgYear

    override fun getGregorianMonth(): Int = persianDate.grgMonth

    override fun getGregorianDay(): Int = persianDate.grgDay
    override fun getGregorianDayName(): String = persianDate.dayEnglishName()

    override fun getJalaliMonthName(): String = persianDate.monthName

    override fun getJalaliLongDate(): String {
        return getJalaliDayName() + "  " + getJalaliDay() + "  " + getJalaliMonthName() + "  " + getJalaliYear()
    }

    override fun getGregorianMonthName(): String = persianDate.getGrgMonthName(persianDate.grgMonth)

    override fun getGregorianDate(): Date = persianDate.toDate()

    override fun getGregorianMonthLength(year: Int,month: Int): Int = persianDate.getGrgMonthLength(year,month)

    override fun getTimeStamp(): Long = persianDate.time

    override fun isLeapYear(): Boolean {
        return persianDate.isLeap
    }
    override fun isLeapYear(year: Int): Boolean {
        return persianDate.isLeap(year)
    }
}