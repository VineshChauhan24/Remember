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
                // d[0] = dayName;
            } else if (day[i] == Calendar.MONDAY) {
                dayName += context.getString(R.string.monday) + ". ";
                //  d[1] = dayName;
            } else if (day[i] == Calendar.TUESDAY) {
                dayName += context.getString(R.string.tuesday) + ". ";
                //  d[2] = dayName;
            } else if (day[i] == Calendar.WEDNESDAY) {
                dayName += context.getString(R.string.wednesday) + ". ";
                // d[3] = dayName;
            } else if (day[i] == Calendar.THURSDAY) {
                dayName += context.getString(R.string.thursday) + ". ";
                // d[4] = dayName;
            } else if (day[i] == Calendar.FRIDAY) {
                dayName += context.getString(R.string.friday) + ". ";
                // d[5] = dayName;
            } else if (day[i] == Calendar.SATURDAY) {
                dayName += context.getString(R.string.saturday) + ". ";
                // d[6] = dayName;
            }
        }
        return dayName;
    }

    // TODO: 18.12.2015
//    public static ArrayList<String> getDayName(TextView tvDayName, int dayNameOfCalendar) {
//        ArrayList<String> dayName = new ArrayList<>();
//        dayName.add(R.string.sunday + ". ");
//        dayName.add(R.string.monday + ". ");
//        dayName.add(R.string.tuesday + ". ");
//        dayName.add(R.string.wednesday + ". ");
//        dayName.add(R.string.thursday + ". ");
//        dayName.add(R.string.friday + ". ");
//        dayName.add(R.string.saturday + ". ");
//
//        switch (dayNameOfCalendar) {
//            case Calendar.SUNDAY:
//                tvDayName.setText(dayName.get(0));
//                break;
//            case Calendar.MONDAY:
//                tvDayName.setText(dayName.get(1));
//                break;
//            case Calendar.TUESDAY:
//                tvDayName.setText(dayName.get(2));
//                break;
//            case Calendar.WEDNESDAY:
//                tvDayName.setText(dayName.get(3));
//                break;
//            case Calendar.THURSDAY:
//                tvDayName.setText(dayName.get(4));
//                break;
//            case Calendar.FRIDAY:
//                tvDayName.setText(dayName.get(5));
//                break;
//            case Calendar.SATURDAY:
//                tvDayName.setText(dayName.get(6));
//                break;
//
//        }
//
////        if (dayNameOfCalendar == Calendar.SUNDAY) {
////            tvDayName.setText(dayName.get(0);
////        }else if (dayNameOfCalendar == Calendar.SUNDAY){
////
////        }
//        return dayName;
//    }
}