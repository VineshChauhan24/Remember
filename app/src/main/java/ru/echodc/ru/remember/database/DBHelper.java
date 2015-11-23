package ru.echodc.ru.remember.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import ru.echodc.ru.remember.model.ModelTask;

public class DBHelper extends SQLiteOpenHelper {

    //    Версия базы
//    public static final int DATABASE_VERSION = 1;

    public static final int DATABASE_VERSION = 2;

    //    Имя базы данных
    public static final String DATABASE_NAME = "reminder_db";

    //    Имя таблицы в базе
    public static final String TASK_TABLE = "task_table";

    //    Колонки в таблице
    public static final String TASK_TITLE_COLUMN = "task_title";
    public static final String TASK_DATE_COLUMN = "task_date";
    public static final String TASK_PRIORITY_COLUMN = "task_priority";
    public static final String TASK_STATUS_COLUMN = "task_status";
    public static final String TASK_TIME_STAMP_COLUMN = "task_time_stamp";

    //    Новые колонки для данных дней недели
// TODO: 12.11.15
    public static final String ALTER_TABLE_ID_DAY_COLUMN = "ALTER TABLE task_table ADD COLUMN day_id INTEGER ";
    public static final String TASK_TABLE_DAY_NAME = "task_day_table";
    public static final String TASK_DAY_COLUMN = "task_selected_day";
    public static final String TASK_HOUR_COLUMN = "task_hour";
    public static final String TASK_MINUTE_COLUMN = "task_minute";
    public static final String TASK_TIME_DAY_COLUMN = "task_time";

    // TODO: 12.11.15
//    Скрипт создания таблицы дней недели
    public static final String TASK_TABLE_DAY_CREATE_SCRIPT = "CREATE TABLE "
            + TASK_TABLE_DAY_NAME + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TASK_DAY_COLUMN + " TEXT DEFAULT NULL, "
            + TASK_HOUR_COLUMN + " INTEGER, "
            + TASK_MINUTE_COLUMN + " INTEGER, "
            + TASK_TIME_DAY_COLUMN + " LONG); ";


    //    Константа для создания таблицы
    private static final String TASKS_TABLE_CREATE_SCRIPT = "CREATE TABLE "
            + TASK_TABLE + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TASK_TITLE_COLUMN + " TEXT NOT NULL, "
            + TASK_DATE_COLUMN + " LONG, "
            + TASK_PRIORITY_COLUMN + " INTEGER, "
            + TASK_STATUS_COLUMN + " INTEGER, "
            + TASK_TIME_STAMP_COLUMN + " LONG); ";


    //    Константы для выборки значений
    public static final String SELECTION_STATUS = DBHelper.TASK_STATUS_COLUMN + " = ?";
    public static final String SELECTION_TIME_STAMP = TASK_TIME_STAMP_COLUMN + " = ?";
    public static final String SELECTION_LIKE_TITLE = TASK_TITLE_COLUMN + " LIKE ?";

    //    Версия нового запроса, после обновления версии базы данных:
//    private static final String TASKS_NEW_TABLE_CREATE_SCRIPT = "CREATE TABLE "
//            + TASK_TABLE
//            + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//            + TASK_TITLE_COLUMN + " TEXT NOT NULL, "
//            + TASK_DATE_COLUMN + " LONG, "
//            + TASK_PRIORITY_COLUMN + " INTEGER, "
//            + TASK_STATUS_COLUMN + " INTEGER, "
//            + TASK_TIME_STAMP_COLUMN + " LONG, "
//            + TASK_DAY_COLUMN + " TEXT, "
//            + TASK_HOUR_COLUMN + " INTEGER, "
//            + TASK_MINUTE_COLUMN + " INTEGER, "
//            + TASK_TIME_DAY_COLUMN + " LONG); ";

    //    Пробная версия запроса:
//    private static final String TASKS_ALTER_TABLE_CREATE_SCRIPT = "ALTER TABLE "
//            + TASK_TABLE + " ADD COLUMN " + TASK_DAY_COLUMN + " TEXT, " + " ADD COLUMN " + TASK_HOUR_COLUMN + " INTEGER, " + " ADD COLUMN " + TASK_MINUTE_COLUMN + " INTEGER, " + " ADD COLUMN " + TASK_TIME_DAY_COLUMN + " LONG; ";

//    public static final String ALTER_TABLE_TASK_DAY_COLUMN = "ALTER TABLE task_table ADD COLUMN task_selected_day TEXT DEFAULT NULL ";
//    public static final String ALTER_TABLE_TASK_HOUR_COLUMN = "ALTER TABLE task_table ADD COLUMN task_hour INTEGER DEFAULT 0 ";
//    public static final String ALTER_TABLE_TASK_MINUTE_COLUMN = "ALTER TABLE task_table ADD COLUMN task_minute INTEGER DEFAULT 0 ";
//    public static final String ALTER_TABLE_TASK_TIME_DAY_COLUMN = "ALTER TABLE task_table ADD COLUMN task_time LONG DEFAULT 0 ";

    //    для получения доступа из фрагментов
    private DBQueryManager queryManager;
    private DBUpdateManager updateManager;


    //    Конструктор Базы данных
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

//        Инициализируем менеджеры
        queryManager = new DBQueryManager(getReadableDatabase());//для чтения из базы
        updateManager = new DBUpdateManager(getWritableDatabase());//для записи в базу
    }

    //    Создаем таблицу в базе
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TASKS_TABLE_CREATE_SCRIPT);
    }

    //    Обновляем, удаляя записи в базе
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// TODO: 12.11.15

//        Так было изначально
//        db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE);
//        onCreate(db);//пересоздаем таблицу

//        Если версия Базы данных выше текущей, то добавим новую колонку для идентификации дней недели
//        СОздадим новую талицу с колонками _id, название дней, часы, минуты, время
        if (newVersion > 1) {

// TODO: 12.11.15
            db.beginTransaction();
            try {
//                Скрипт создания временной таблицы
                String TASKS_TEMP_TABLE_CREATE_SCRIPT = "CREATE TABLE "
                        + "temp_task_table "
                        + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + TASK_TITLE_COLUMN + " TEXT NOT NULL, "
                        + TASK_DATE_COLUMN + " LONG, "
                        + TASK_PRIORITY_COLUMN + " INTEGER, "
                        + TASK_STATUS_COLUMN + " INTEGER, "
                        + TASK_TIME_STAMP_COLUMN + " LONG); ";

//                Созадем временную таблицу
                db.execSQL(TASKS_TEMP_TABLE_CREATE_SCRIPT);

//                Вставляем данные во временную таблицу из таблицы task_table
                db.execSQL("INSERT INTO temp_task_table (task_title, task_date, task_priority, task_status, task_time_stamp)" +
                        " SELECT task_title, task_date, task_priority, task_status, task_time_stamp FROM task_table ");

//                Удаляем таблицу task_table если она существует
                db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE);

//                Создаем ее заново
//                db.execSQL(TASKS_TABLE_CREATE_SCRIPT);
                onCreate(db);//пересоздаем таблицу

//                Добавляем колонку для идентификации дней недели из таблицы task_day_table
                db.execSQL(ALTER_TABLE_ID_DAY_COLUMN);

//                Вставляем данные пользователя из временной таблицы в новую таблицу task_table
                db.execSQL("INSERT INTO task_table (task_title, task_date, task_priority, task_status, task_time_stamp)" +
                        " SELECT task_title, task_date, task_priority, task_status, task_time_stamp FROM temp_task_table ");

//                Удаляем временную талицу
                db.execSQL("DROP TABLE IF EXISTS temp_task_table ");

//                Создаем таблицу идентификации дней недели task_day_table
                db.execSQL(TASK_TABLE_DAY_CREATE_SCRIPT);
                db.setTransactionSuccessful();
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> Создана новая таблица дней недели!");

            } finally {
                db.endTransaction();
            }

//            db.beginTransaction();
//            try {
//                String TASKS_TEMP_TABLE_CREATE_SCRIPT = "CREATE TABLE "
//                        + "temp_task_table "
//                        + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                        + TASK_TITLE_COLUMN + " TEXT NOT NULL, "
//                        + TASK_DATE_COLUMN + " LONG, "
//                        + TASK_PRIORITY_COLUMN + " INTEGER, "
//                        + TASK_STATUS_COLUMN + " INTEGER, "
//                        + TASK_TIME_STAMP_COLUMN + " LONG, "
//                        + TASK_DAY_COLUMN + " TEXT DEFAULT NULL, "
//                        + TASK_HOUR_COLUMN + " INTEGER, "
//                        + TASK_MINUTE_COLUMN + " INTEGER, "
//                        + TASK_TIME_DAY_COLUMN + " LONG); ";
//
//                db.execSQL(TASKS_TEMP_TABLE_CREATE_SCRIPT);
//
//                db.execSQL("INSERT INTO temp_task_table (task_title, task_date, task_priority, task_status, task_time_stamp) SELECT task_title, task_date, task_priority, task_status, task_time_stamp FROM task_table ");
//                db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE);
//
//                db.execSQL(TASKS_NEW_TABLE_CREATE_SCRIPT);
//
//                db.execSQL("INSERT INTO task_table (task_title, task_date, task_priority, task_status, task_time_stamp, task_selected_day, task_hour, task_minute, task_time) SELECT task_title, task_date, task_priority, task_status, task_time_stamp, task_selected_day, task_hour, task_minute, task_time FROM temp_task_table ");
//                db.execSQL("DROP TABLE IF EXISTS temp_task_table ");
//
//                db.setTransactionSuccessful();
//            } finally {
//                db.endTransaction();
//            }
        }
    }

    //    Сохраняем задачи
    public void saveTask(ModelTask task) {
//        Формируем данных для вставки
        ContentValues newValues = new ContentValues();

        newValues.put(TASK_TITLE_COLUMN, task.getTitle());
        newValues.put(TASK_DATE_COLUMN, task.getDate());
        newValues.put(TASK_STATUS_COLUMN, task.getStatus());
        newValues.put(TASK_PRIORITY_COLUMN, task.getPriority());
        newValues.put(TASK_TIME_STAMP_COLUMN, task.getTimeStamp());

        newValues.put(TASK_HOUR_COLUMN, task.getHour());
        newValues.put(TASK_MINUTE_COLUMN, task.getMinute());

        String dayTemp = "";

        for (int i = 0; i < task.getDay().length; i++) {
            dayTemp += task.getDay()[i];
        }

        newValues.put(TASK_DAY_COLUMN, dayTemp);

        newValues.put(TASK_TIME_DAY_COLUMN, task.getOnlyTime());

//        Втавляем данных в таблицу
        getWritableDatabase().insert(TASK_TABLE, null, newValues);
    }

    //   Методы для получения доступа к менеджерам из фрагментов
    public DBQueryManager query() {
        return queryManager;
    }

    public DBUpdateManager update() {
        return updateManager;
    }

    //    Добавляем в базу данных запрос на удаление задач
    public void removeTask(long timeStamp) {
        getWritableDatabase().delete(TASK_TABLE, SELECTION_TIME_STAMP, new String[]{Long.toString(timeStamp)});
    }

}
