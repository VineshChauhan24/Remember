package ru.echodc.ru.remember.model;

import java.util.Date;

import ru.echodc.ru.remember.R;


public class ModelTask implements Item {
    //    Константы для приоритетов и статусов задач
    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_NORMAL = 1;
    public static final int PRIORITY_HIGH = 2;

    //    public static final String[] PRIORITY_LEVELS = {"Low priority", "Normal priority", "High priority"};//Спиннер теперь формируется из строковых ресурсов

    public static final int STATUS_OVERDUE = 0;
    public static final int STATUS_CURRENT = 1;
    public static final int STATUS_DONE = 2;

    private String title;
    private long date;
    private int priority;
    private int status;
    private long timeStamp;
    private int dateStatus;

    //    Новые поля для определения дней и времени
    private int[] day;
    private long onlyTime;


    //    Пустой конструктор
    public ModelTask() {
//        По умолчанию int = 0, присвоим -1, чтобы он не создавался с нулевым значением
        this.status = -1;
        this.timeStamp = new Date().getTime();//создаем новую дату и забираем из нее время
        this.day = new int[7];//
    }

    //    Конструктор с параметрами
    public ModelTask(String title, long date, int priority, int status, long timeStamp, int[] day) {
        this.title = title;
        this.date = date;
        this.priority = priority;
        this.status = status;
        this.timeStamp = timeStamp;
        this.day = day;
    }

    //Конструктор для повторения напоминаний
    public ModelTask(String title, int priority, int status, int[] day, long onlyTime) {
        this.title = title;
        this.priority = priority;
        this.status = status;
        this.day = day;
        this.onlyTime = onlyTime;
    }
//    Конструктор для получения только времени
    public ModelTask(int[] day, long onlyTime) {
        this.day = day;
        this.onlyTime = onlyTime;
    }

    //    public ModelTask(String title, long date, int priority, int status, long timeStamp, int[] day, int hour, int minute, long timeDay) {
    public ModelTask(String title, long date, int priority, int status, long timeStamp, int[] day, long timeDay) {
        this.title = title;
        this.date = date;
        this.priority = priority;
        this.status = status;
        this.timeStamp = timeStamp;
        this.day = day;
        this.onlyTime = timeDay;
    }


    public int getPriorityColor() {
//        Возвращаем идентификатор цвета, в зависимости от состояния
        switch (getPriority()) {
            case PRIORITY_HIGH:
                if (getStatus() == STATUS_CURRENT || getStatus() == STATUS_OVERDUE) {
                    return R.color.priority_high;
                } else {
                    return R.color.priority_high_selected;
                }
            case PRIORITY_NORMAL:
                if (getStatus() == STATUS_CURRENT || getStatus() == STATUS_OVERDUE) {
                    return R.color.priority_normal;
                } else {
                    return R.color.priority_normal_selected;
                }
            case PRIORITY_LOW:
                if (getStatus() == STATUS_CURRENT || getStatus() == STATUS_OVERDUE) {
                    return R.color.priority_low;
                } else {
                    return R.color.priority_low_selected;
                }

            default:
                return 0;
        }
    }

    //    Метод задачи возвращает true
    @Override
    public boolean isTask() {
        return true;
    }

    //    Геттеры с сеттеры для доступа к переменным из других классов
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getDateStatus() {
        return dateStatus;
    }

    public void setDateStatus(int dateStatus) {
        this.dateStatus = dateStatus;
    }
//    **********************************************************************************************

    public int[] getDay() {
        return day;
    }

    public void setDay(int position, int day) {
        this.day[position] = day;
    }


    public long getOnlyTime() {
        return onlyTime;
    }

    public void setOnlyTime(long onlyTime) {
        this.onlyTime = onlyTime;
    }
}
