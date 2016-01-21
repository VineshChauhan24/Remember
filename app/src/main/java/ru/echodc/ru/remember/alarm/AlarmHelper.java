package ru.echodc.ru.remember.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import java.util.Calendar;

import ru.echodc.ru.remember.model.ModelTask;

public class AlarmHelper {
    private static AlarmHelper instance;
    private Context context;
    private AlarmManager alarmManager;
    Calendar c;
    PendingIntent pendingIntent;
    //    PendingIntent pendingIntentRepeat;
    private boolean sunday;
    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private boolean saturday;
//    private ModelTask task;

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
//        intent.putExtra("onlyTime", task.getOnlyTime());
//        intent.putExtra("day", task.getDay());

//        Если  PendingIntent существует, то новый не создаем, а используем текущий, но с обновленными данными
        pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),
                (int) task.getTimeStamp(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        pendingIntentRepeat = PendingIntent.getBroadcast(context.getApplicationContext(),
//                (int) task.getOnlyTime(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (task.getDate() != 0) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, task.getDate(), pendingIntent);
        } else {
//          Иниализация boolean значений
            checkDay(task);
//          Аларм приходит из метода forDay(...)
            for (int i = 0; i < task.getDay().length; i++) {
                System.out.println(task.getDay()[i]);
//                Ловим Воскресенье
                if (task.getDay()[i] == Calendar.SUNDAY) {
                    System.out.println("==========================================     Установлен ВС");
                    if (monday) {
                        forDay(1, task, 1);
                        break;
                    } else if (tuesday) {
                        forDay(1, task, 2);
                        break;
                    } else if (wednesday) {
                        forDay(1, task, 3);
                        break;
                    } else if (thursday) {
                        forDay(1, task, 4);
                        break;
                    } else if (friday) {
                        forDay(1, task, 5);
                        break;
                    } else if (saturday) {
                        forDay(1, task, 6);
                        break;
                    } else {
                        forDay(1, task, 7);
                    }
                }
//                Ловим Понедельник
                if (task.getDay()[i] == Calendar.MONDAY) {
                    System.out.println("==========================================     Установлен ПН");
                    if (tuesday) {
                        forDay(2, task, 1);
                        break;
                    } else if (wednesday) {
                        forDay(2, task, 2);
                        break;
                    } else if (thursday) {
                        forDay(2, task, 3);
                        break;
                    } else if (friday) {
                        forDay(2, task, 4);
                        break;
                    } else if (saturday) {
                        forDay(2, task, 5);
                        break;
                    } else if (sunday) {
                        forDay(2, task, 6);
                        break;
                    } else {
                        forDay(2, task, 7);
                    }
                }
//                Ловим Вторник
                if (task.getDay()[i] == Calendar.TUESDAY) {
                    System.out.println("==========================================     Установлен ВТ");
                    if (wednesday) {
                        forDay(3, task, 1);
                        break;
                    } else if (thursday) {
                        forDay(3, task, 2);
                        break;
                    } else if (friday) {
                        forDay(3, task, 3);
                        break;
                    } else if (saturday) {
                        forDay(3, task, 4);
                        break;
                    } else if (sunday) {
                        forDay(3, task, 5);
                        break;
                    } else if (monday) {
                        forDay(3, task, 6);
                        break;
                    } else {
                        forDay(3, task, 7);
                    }
                }
//                Ловим Среду
                if (task.getDay()[i] == Calendar.WEDNESDAY) {
                    System.out.println("==========================================     Установлен СР");
                    if (thursday) {
                        forDay(4, task, 1);
                        break;
                    } else if (friday) {
                        forDay(4, task, 2);
                        break;
                    } else if (saturday) {
                        forDay(4, task, 3);
                        break;
                    } else if (sunday) {
                        forDay(4, task, 4);
                        break;
                    } else if (monday) {
                        forDay(4, task, 5);
                        break;
                    } else if (tuesday) {
                        forDay(4, task, 6);
                        break;
                    } else {
                        forDay(4, task, 7);
                    }
                }
//                Ловим Четверг
                if (task.getDay()[i] == Calendar.THURSDAY) {
                    System.out.println("==========================================     Установлен ЧТ");
                    if (friday) {
                        forDay(5, task, 1);
                        break;
                    } else if (saturday) {
                        forDay(5, task, 2);
                        break;
                    } else if (sunday) {
                        forDay(5, task, 3);
                        break;
                    } else if (monday) {
                        forDay(5, task, 4);
                        break;
                    } else if (tuesday) {
                        forDay(5, task, 5);
                        break;
                    } else if (wednesday) {
                        forDay(5, task, 6);
                        break;
                    } else {
                        forDay(5, task, 7);
                    }
                }
//                Ловим Пятницу
                if (task.getDay()[i] == Calendar.FRIDAY) {
                    System.out.println("==========================================     Установлен ПТ");
                    if (saturday) {
                        forDay(6, task, 1);
                        break;
                    } else if (sunday) {
                        forDay(6, task, 2);
                        break;
                    } else if (monday) {
                        forDay(6, task, 3);
                        break;
                    } else if (tuesday) {
                        forDay(6, task, 4);
                        break;
                    } else if (wednesday) {
                        forDay(6, task, 5);
                        break;
                    } else if (thursday) {
                        forDay(6, task, 6);
                        break;
                    } else {
                        forDay(6, task, 7);
                    }
                }
//                Ловим Субботу
                if (task.getDay()[i] == Calendar.SATURDAY) {
                    System.out.println("==========================================     Установлен СУБ");
                    if (sunday) {
                        forDay(7, task, 1);
                        break;
                    } else if (monday) {
                        forDay(7, task, 2);
                        break;
                    } else if (tuesday) {
                        forDay(7, task, 3);
                        break;
                    } else if (wednesday) {
                        forDay(7, task, 4);
                        break;
                    } else if (thursday) {
                        forDay(7, task, 5);
                        break;
                    } else if (friday) {
                        forDay(7, task, 6);
                        break;
                    } else {
                        forDay(7, task, 7);
                    }
                }
            }
        }
    }

    private void forDay(int day, ModelTask task, int interval) {
// TODO: 21.01.2016

//        Intent intent = new Intent(context, AlarmReceiver.class);
//        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        pendingIntent = PendingIntent.getService(context.getApplicationContext(),
//                day, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR, 0);
//        c.set(Calendar.AM_PM, Calendar.AM);
//        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.DAY_OF_WEEK, day);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("title", task.getTitle());
//        intent.putExtra("time_stamp", task.getTimeStamp());
        intent.putExtra("color", task.getPriorityColor());
        intent.putExtra("onlyTime", task.getOnlyTime());
        intent.putExtra("day", task.getDay());

        pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),
                day, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        pendingIntent = PendingIntent.getService(context.getApplicationContext(),
//                day, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        Формируем напоминание
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
//                c.getTimeInMillis(), AlarmManager.INTERVAL_DAY + interval, pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_DAY + interval, pendingIntent);
    }

    private void checkDay(ModelTask task) {


        if (task.getDay()[0] != 0) {
            sunday = true;
        }
        if (task.getDay()[1] != 0) {
            monday = true;
        }
        if (task.getDay()[2] != 0) {
            tuesday = true;
        }
        if (task.getDay()[3] != 0) {
            wednesday = true;
        }
        if (task.getDay()[4] != 0) {
            thursday = true;
        }
        if (task.getDay()[5] != 0) {
            friday = true;
        }
        if (task.getDay()[6] != 0) {
            saturday = true;
        }

    }

    //    Метод для удаления задачи по timeStamp
    public void removeAlarm(long taskTimeStamp) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                (int) taskTimeStamp, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }
}
