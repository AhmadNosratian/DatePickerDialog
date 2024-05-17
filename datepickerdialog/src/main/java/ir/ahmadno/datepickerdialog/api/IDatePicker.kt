package ir.ahmadno.datepickerdialog.api

import java.util.Date

interface IDatePicker {

    fun setDate(date: Date)
    fun setJalaliDate(year : Int, month: Int, day : Int)
    fun setGregorianDate(year : Int, month: Int, day : Int)

    fun getJalaliYear(): Int
    fun getJalaliMonth(): Int
    fun getJalaliDay(): Int
    fun getJalaliDayName(): String
    fun getJalaliMonthLength(year: Int,month: Int) : Int

    fun getGregorianYear(): Int
    fun getGregorianMonth(): Int
    fun getGregorianDay(): Int
    fun getGregorianDayName(): String

    fun getJalaliMonthName(): String
    fun getJalaliLongDate() : String
    fun getGregorianMonthName(): String
    fun getGregorianMonthLength(year: Int,month: Int) : Int

    fun getGregorianDate(): Date
    fun getTimeStamp(): Long
    fun isLeapYear() : Boolean
    fun isLeapYear(year : Int) : Boolean

}