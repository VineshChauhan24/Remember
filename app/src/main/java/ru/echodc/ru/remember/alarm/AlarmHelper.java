package ru.echodc.ru.remember.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import ru.echodc.ru.remember.model.ModelTask;

public class AlarmHelper {
    private static AlarmHelper instance;
    private Context context;
    private AlarmManager alarmManager;
    Calendar c;
    PendingIntent pendingIntent;


    //    Создаем AlarmHelper в случае его отсутствия
    public static AlarmHelper getInstance() {
        if (instance == null) {
            instance = new AlarmHelper();
        }
        return instance;
    }

    //    Метод для инициализации
    public void init(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
    }

    //    Метод создает новый ресивер и передет ему данные
    public void setAlarm(ModelTask task) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("title", task.getTitle());
        intent.putExtra("time_stamp", task.getTimeStamp());
        intent.putExtra("color", task.getPriorityColor());

//        Если  PendingIntent существует, то новый не создаем, а используем текущий, но с обновленными данными
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),
                (int) task.getTimeStamp(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        Передаем время пробуджения устройства
//        alarmManager.set(AlarmManager.RTC_WAKEUP, task.getDate(), pendingIntent);
        if (task.getDate() != 0) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, task.getDate(), pendingIntent);
        } else {
            for (int i = 0; i < task.getDay().length; i++) {

                if (task.getDay()[i] == Calendar.SUNDAY) {
                    forDay(Calendar.SUNDAY, task);
                   /* c = Calendar.getInstance();
                    c.set(Calendar.HOUR_OF_DAY, task.getHour());
                    c.set(Calendar.MINUTE, task.getMinute());
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(), 1 * 60 * 60 * 1000, pendingIntent);*/

                } else if (task.getDay()[i] == Calendar.MONDAY) {
                    forDay(Calendar.SUNDAY, task);
                   /* c = Calendar.getInstance();
                    c.set(Calendar.HOUR_OF_DAY, task.getHour());
                    c.set(Calendar.MINUTE, task.getMinute());
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 1 * 60 * 60 * 1000, pendingIntent);*/

                } else if (task.getDay()[i] == Calendar.TUESDAY) {
                    forDay(Calendar.SUNDAY, task);
                    /*c = Calendar.getInstance();
                    c.set(Calendar.HOUR_OF_DAY, task.getHour());
                    c.set(Calendar.MINUTE, task.getMinute());
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(), 1 * 60 * 60 * 1000, pendingIntent);*/

                } else if (task.getDay()[i] == Calendar.WEDNESDAY) {
                    forDay(Calendar.SUNDAY, task);
                   /* c = Calendar.getInstance();
                    c.set(Calendar.HOUR_OF_DAY, task.getHour());
                    c.set(Calendar.MINUTE, task.getMinute());
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(), 1 * 60 * 60 * 1000, pendingIntent);*/

                } else if (task.getDay()[i] == Calendar.THURSDAY) {
                    forDay(Calendar.SUNDAY, task);
                    /*c = Calendar.getInstance();
                    c.set(Calendar.HOUR_OF_DAY, task.getHour());
                    c.set(Calendar.MINUTE, task.getMinute());
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 1 * 60 * 60 * 1000, pendingIntent);*/

                } else if (task.getDay()[i] == Calendar.FRIDAY) {
                    forDay(Calendar.SUNDAY, task);
                    /*c = Calendar.getInstance();
                    c.set(Calendar.HOUR_OF_DAY, task.getHour());
                    c.set(Calendar.MINUTE, task.getMinute());
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 1 * 60 * 60 * 1000, pendingIntent);*/

                } else if (task.getDay()[i] == Calendar.SATURDAY) {
                    forDay(Calendar.SUNDAY, task);
                    /*c = Calendar.getInstance();
                    c.set(Calendar.HOUR_OF_DAY, task.getHour());
                    c.set(Calendar.MINUTE, task.getMinute());
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 1 * 60 * 60 * 1000, pendingIntent);*/
                }

            }

        }


    }

    public void forDay(int day, ModelTask task) {
        c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, day);
//        c.set(Calendar.HOUR_OF_DAY, task.getHour());
//        c.set(Calendar.MINUTE, task.getMinute());
        c.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                c.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
    }


    //    Метод для удаления задачи по timeStamp
    public void removeAlarm(long taskTimeStamp) {
        Intent intent = new Intent(context, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                (int) taskTimeStamp, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }
}
