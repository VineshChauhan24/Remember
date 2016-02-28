package ru.echodc.ru.remember.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import ru.echodc.ru.remember.Utils;
import ru.echodc.ru.remember.model.ModelTask;

public class AlarmHelper {
    private static AlarmHelper instance;
    private Context context;
    private AlarmManager alarmManager;
    PendingIntent pendingIntent;

    private boolean sunday;
    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private boolean saturday;

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
        intent.putExtra("onlyTime", task.getOnlyTime());
//        intent.putExtra("day", task.getDay());

//        Если  PendingIntent существует, то новый не создаем, а используем текущий, но с обновленными данными
        pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),
                (int) task.getTimeStamp(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        Передаем время пробуджения устройства
        if (task.getDate() != 0) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, task.getDate(), pendingIntent);
        } else {
//          Аларм приходит из метода addAlarm(...)
            for (int i = 0; i < task.getDay().length; i++) {
                System.out.println(task.getDay()[i]);

                if (task.getDay()[i] == Calendar.MONDAY) {
                    addAlarm(task, Calendar.MONDAY);
                    System.out.println("==========================================     Установлен ПН");
                }
                if (task.getDay()[i] == Calendar.TUESDAY) {
                    addAlarm(task, Calendar.TUESDAY);
                    System.out.println("==========================================     Установлен ВТ");
                }
                if (task.getDay()[i] == Calendar.WEDNESDAY) {
                    addAlarm(task, Calendar.WEDNESDAY);
                    System.out.println("==========================================     Установлен СР");
                }
                if (task.getDay()[i] == Calendar.THURSDAY) {
                    addAlarm(task, Calendar.THURSDAY);
                    System.out.println("==========================================     Установлен ЧТ");
                }
                if (task.getDay()[i] == Calendar.FRIDAY) {
                    addAlarm(task, Calendar.FRIDAY);
                    System.out.println("==========================================     Установлен ПТ");
                }
                if (task.getDay()[i] == Calendar.SATURDAY) {
                    addAlarm(task, Calendar.SATURDAY);
                    System.out.println("==========================================     Установлен СУБ");
                }
                if (task.getDay()[i] == Calendar.SUNDAY) {
                    addAlarm(task, Calendar.SUNDAY);
                    System.out.println("==========================================     Установлен ВС");
                }
            }
        }
    }

    //    Метод для установки алармов
    public void addAlarm(ModelTask task, int numberDay) {

        String getOnlyTime = Utils.getTime(task.getOnlyTime());
        Intent nameDay = new Intent(context, AlarmReceiverRepeat.class);
        nameDay.putExtra("title", task.getTitle());
        nameDay.putExtra("time_stamp", task.getTimeStamp());
        nameDay.putExtra("color", task.getPriorityColor());
        nameDay.putExtra("nameDay", " ... ");
//        nameDay.putExtra("numberDay", Calendar.MONDAY);
        nameDay.putExtra("numberDay", numberDay);
        nameDay.putExtra("onlyTime", getOnlyTime);

        String[] output = getOnlyTime.split(":");
        int hour = Integer.parseInt(output[0]);
        int minutes = Integer.parseInt(output[1]);

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, c.get(Calendar.YEAR));
        c.set(Calendar.MONTH, c.get(Calendar.MONTH));
        c.set(Calendar.WEEK_OF_MONTH, c.get(Calendar.WEEK_OF_MONTH));
        System.out.println("номер недели   " + c.get(Calendar.WEEK_OF_YEAR));
        System.out.println("проверка   " + task.getDate());

        c.set(Calendar.DAY_OF_WEEK, numberDay);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minutes);

        long fullDate = c.getTimeInMillis();
        nameDay.putExtra("fullDate", c.getTimeInMillis());

        System.out.println(" fullDate " + fullDate);

        pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),
                (int) task.getTimeStamp() + numberDay + 10000, nameDay, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    //    Метод для удаления задачи по timeStamp
    public void removeAlarm(long taskTimeStamp, ModelTask task) {

        System.out.println("зашло в removeAlarm");
        if (task.getDate() != 0) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    (int) taskTimeStamp, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
            System.out.println("зашло в task.getDate()==0");
        } else {
            System.out.println("зашло в else");
            for (int i = 0; i < task.getDay().length; i++) {
                if (task.getDay()[i] != 0) {
                    System.out.println("task.getDay()[i]!=0");
                    Intent intent = new Intent(context, AlarmReceiverRepeat.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                            (int) taskTimeStamp + task.getDay()[i] + 10000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(pendingIntent);
                    System.out.println("отменило  " + task.getDay()[i]);
                }
            }
        }
    }
}
