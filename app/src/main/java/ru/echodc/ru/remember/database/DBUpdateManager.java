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

    //    Метода для обновления столбцов
    public void title(long timeStamp, String title) {
        update(DBHelper.TASK_TITLE_COLUMN, timeStamp, title);
    }

    public void date(long timeStamp, long date) {
        update(DBHelper.TASK_DATE_COLUMN, timeStamp, date);
    }

    //    Обновление записей в базе времени для дней недели
    public void timeOfDay(long timeStamp, long date) {
        update(DBHelper.TASK_TIME_DAY_COLUMN, timeStamp, date);
    }

    public void priority(long timeStamp, int priority) {
        update(DBHelper.TASK_PRIORITY_COLUMN, timeStamp, priority);
    }

    public void status(long timeStamp, int status) {
        update(DBHelper.TASK_STATUS_COLUMN, timeStamp, status);
    }


    //    Метод обновления общего таска
    public void task(ModelTask task) {
        title(task.getTimeStamp(), task.getTitle());
        date(task.getTimeStamp(), task.getDate());
        priority(task.getTimeStamp(), task.getPriority());
        status(task.getTimeStamp(), task.getStatus());
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

}
