package ru.echodc.ru.remember.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import ru.echodc.ru.remember.model.ModelTask;

public class DBUpdateManager {

    SQLiteDatabase database;

    //    Конструктор
    DBUpdateManager(SQLiteDatabase database) {
        this.database = database;
    }

    //    Методы для обновления столбцов
    public void title(long timeStamp, String title) {
        update(DBHelper.TASK_TITLE_COLUMN, timeStamp, title);
    }

    public void date(long timeStamp, long date) {
        update(DBHelper.TASK_DATE_COLUMN, timeStamp, date);
    }

    public void priority(long timeStamp, int priority) {
        update(DBHelper.TASK_PRIORITY_COLUMN, timeStamp, priority);
    }

    public void status(long timeStamp, int status) {
        update(DBHelper.TASK_STATUS_COLUMN, timeStamp, status);
    }

    //    Обновление записей в базе времени для дней недели
    public void daySelected(long timeStamp, int[] days) {
        update(DBHelper.TASK_DAY_COLUMN, timeStamp, days);
    }

    public void timeOfDay(long timeStamp, long date) {
        update(DBHelper.TASK_TIME_DAY_COLUMN, timeStamp, date);
    }

    //    Метод обновления общего таска
    public void task(ModelTask task) {
        title(task.getTimeStamp(), task.getTitle());
        date(task.getTimeStamp(), task.getDate());
        priority(task.getTimeStamp(), task.getPriority());
        status(task.getTimeStamp(), task.getStatus());
        daySelected(task.getTimeStamp(), task.getDay());
        timeOfDay(task.getTimeStamp(), task.getOnlyTime());
    }

    private void update(String column, long key, String value) {
        ContentValues cv = new ContentValues();
        cv.put(column, value);
        database.update(DBHelper.TASK_TABLE, cv,
                DBHelper.TASK_TIME_STAMP_COLUMN + " = " + key, null);
    }

    //    Перегрузим метод для других значений
    private void update(String column, long key, long value) {
        ContentValues cv = new ContentValues();
        cv.put(column, value);

        database.update(DBHelper.TASK_TABLE, cv,
                DBHelper.TASK_TIME_STAMP_COLUMN + " = " + key, null);
    }

    private void update(String column, long key, int[] days) {

        String dayTemp = "";

        for (int i = 0; i < days.length; i++) {
            dayTemp += days[i];
        }

//        for (int day : days) {
//            dayTemp += day;
//        }

        ContentValues cv = new ContentValues();
        cv.put(column, dayTemp);

        database.update(DBHelper.TASK_TABLE, cv,
                DBHelper.TASK_TIME_STAMP_COLUMN + " = " + key, null);
    }
}
