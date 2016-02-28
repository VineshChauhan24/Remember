package ru.echodc.ru.remember;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.util.Calendar.SUNDAY;

/**
 * Используется для получения даты из календаря в текстовом виде
 */
public class Utils {

    //    Для обращения к методам из любого места программы, без создания экземпляра, сделаем их статичными
    public static String getDate(long date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        return dateFormat.format(date);
    }

    public static String getTime(long time) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(time);
    }

    //    Для получения полной даты
    public static String getFullDate(long date) {
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd.MM.yy HH:mm");
        return fullDateFormat.format(date);
    }



    //    Для получения названия дней недели из числа
    public static String getDayName(Context context, int day[]) {

        String dayName = "";

        for (int i = 0; i < day.length; i++) {
            if (day[i] == SUNDAY) {
                dayName += context.getString(R.string.sunday) + ". ";
            } else if (day[i] == Calendar.MONDAY) {
                dayName += context.getString(R.string.monday) + ". ";
            } else if (day[i] == Calendar.TUESDAY) {
                dayName += context.getString(R.string.tuesday) + ". ";
            } else if (day[i] == Calendar.WEDNESDAY) {
                dayName += context.getString(R.string.wednesday) + ". ";
            } else if (day[i] == Calendar.THURSDAY) {
                dayName += context.getString(R.string.thursday) + ". ";
            } else if (day[i] == Calendar.FRIDAY) {
                dayName += context.getString(R.string.friday) + ". ";
            } else if (day[i] == Calendar.SATURDAY) {
                dayName += context.getString(R.string.saturday) + ". ";
            }
        }
        return dayName;
    }
}