package ru.echodc.ru.remember.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ru.echodc.ru.remember.model.ModelTask;

public class DBQueryManager {
    private SQLiteDatabase database;

    //    Конструктор
    DBQueryManager(SQLiteDatabase database) {
        this.database = database;
    }

    public ModelTask getTask(long timeStamp) {
        ModelTask modelTask = null;
        Cursor cursor = database.query(DBHelper.TASK_TABLE, null, DBHelper.SELECTION_TIME_STAMP,
                new String[]{Long.toString(timeStamp)}, null, null, null);

        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex(DBHelper.TASK_TITLE_COLUMN));
            long date = cursor.getLong(cursor.getColumnIndex(DBHelper.TASK_DATE_COLUMN));
            int priority = cursor.getInt(cursor.getColumnIndex(DBHelper.TASK_PRIORITY_COLUMN));
            int status = cursor.getInt(cursor.getColumnIndex(DBHelper.TASK_STATUS_COLUMN));

            String dayString = cursor.getString(cursor.getColumnIndex(DBHelper.TASK_DAY_COLUMN));

            int[] day = new int[7];

            for (int i = 0; i < dayString.length(); i++) {
                day[i] = Integer.parseInt(String.valueOf(dayString.charAt(i)));
                System.out.println(Integer.parseInt(String.valueOf(dayString.charAt(i))));
            }

            long timeDay = cursor.getLong(cursor.getColumnIndex(DBHelper.TASK_TIME_DAY_COLUMN));

            modelTask = new ModelTask(title, date, priority, status, timeStamp, day, timeDay);
        }
//        Закрываем подключение к базе
        cursor.close();

//        Возвращаем заполненный modelTask
        return modelTask;
    }

    //    Возвращаем список задач
    public List<ModelTask> getTasks(String selection, String[] selectionArgs, String orderBy) {
        List<ModelTask> tasks = new ArrayList<>();

//        Находим значения в базе и формируем из них задачи
        Cursor c = database.query(DBHelper.TASK_TABLE, null, selection, selectionArgs, null, null, orderBy);// запрос

//        if (c.moveToFirst()) {
        c.moveToFirst();
        if (!c.isAfterLast()) {
            do {
                String title = c.getString(c.getColumnIndex(DBHelper.TASK_TITLE_COLUMN));
                long date = c.getLong(c.getColumnIndex(DBHelper.TASK_DATE_COLUMN));
                int priority = c.getInt(c.getColumnIndex(DBHelper.TASK_PRIORITY_COLUMN));
                int status = c.getInt(c.getColumnIndex(DBHelper.TASK_STATUS_COLUMN));
                long timeStamp = c.getLong(c.getColumnIndex(DBHelper.TASK_TIME_STAMP_COLUMN));
                String dayString = c.getString(c.getColumnIndex(DBHelper.TASK_DAY_COLUMN));

                int[] day = new int[7];

                if (dayString != null) {
                    for (int i = 0; i < dayString.length(); i++) {
                        day[i] = Integer.parseInt(String.valueOf(dayString.charAt(i)));
                        System.out.println(Integer.parseInt(String.valueOf(dayString.charAt(i))));
                    }
                }

                long timeDay = c.getLong(c.getColumnIndex(DBHelper.TASK_TIME_DAY_COLUMN));

                ModelTask modelTask = new ModelTask(title, date, priority, status, timeStamp, day, timeDay);
                tasks.add(modelTask);

            } while (c.moveToNext());
        }

        c.close();//закрываем подключение к базе
        return tasks;
    }


    //    Возвращаем время для повторения
//    public ModelTask getRepeatTime(long timeDay) {
//        ModelTask modelTask = null;
//        Cursor cursor = database.query(DBHelper.TASK_TABLE, null, DBHelper.SELECTION_TIME_FOR_DAY,
//                new String[]{Long.toString(timeDay)}, null, null, null);
//
//        if (cursor.moveToFirst()) {
////            String title = cursor.getString(cursor.getColumnIndex(DBHelper.TASK_TITLE_COLUMN));
////            int priority = cursor.getInt(cursor.getColumnIndex(DBHelper.TASK_PRIORITY_COLUMN));
////            int status = cursor.getInt(cursor.getColumnIndex(DBHelper.TASK_STATUS_COLUMN));
//            String dayString = cursor.getString(cursor.getColumnIndex(DBHelper.TASK_DAY_COLUMN));
//            int[] day = new int[7];
//            for (int i = 0; i < dayString.length(); i++) {
//                day[i] = Integer.parseInt(String.valueOf(dayString.charAt(i)));
//                System.out.println(Integer.parseInt(String.valueOf(dayString.charAt(i))));
//            }
//            timeDay = cursor.getLong(cursor.getColumnIndex(DBHelper.TASK_TIME_DAY_COLUMN));
////            modelTask = new ModelTask(title, priority, status, day, timeDay);
//            modelTask = new ModelTask(day, timeDay);
//        }
////        Закрываем подключение к базе
//        cursor.close();
//
////        Возвращаем заполненный modelTask
//        return modelTask;
//    }
}
