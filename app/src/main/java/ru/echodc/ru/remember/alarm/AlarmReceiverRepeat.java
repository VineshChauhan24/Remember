package ru.echodc.ru.remember.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

import ru.echodc.ru.remember.MainActivity;
import ru.echodc.ru.remember.MyApplication;
import ru.echodc.ru.remember.R;
import ru.echodc.ru.remember.Utils;

public class AlarmReceiverRepeat extends AlarmReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

//        Формируем данные для передачи их ресиверу
        String title = intent.getStringExtra("title");//заголовок
        String onlyTime = intent.getStringExtra("onlyTime");//заголовок
        String nameDay = intent.getStringExtra("nameDay");//заголовок
        long timeStamp = intent.getLongExtra("time_stamp", 0);//время создания, по умолчанию 0
        int color = intent.getIntExtra("color", 0);//цвет приоритета задачи, по умолчанию 0
        long fullDate = intent.getLongExtra("fullDate", 0);//время создания, по умолчанию 0
        int numberDay = intent.getIntExtra("numberDay", 0);//цвет приоритета задачи, по умолчанию 0

//        Toast.makeText(context, nameDay + " " + numberDay, Toast.LENGTH_LONG).show();

        Calendar cal = Calendar.getInstance();

//        Интент запускает главное Активити, при нажатии на уведомление
        Intent repeatResultIntent = new Intent(context, MainActivity.class);

//        Если Активити уже запущено, то передаем его состояние в resultIntent из intent
//        поступившего на вход, чтобы оно не пересоздавалось вновь
        if (MyApplication.isActivityVisible()) {
            repeatResultIntent = intent;
        }
//        Если приложение закрыто, то стартуем его
        repeatResultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

//        PendingIntent - позволяет запустить интент, хранящийся в нем,
//          от имени того приложения, с  теми же полномочиями, которым он и создавался
        PendingIntent pendingIntentRepeat = PendingIntent.getActivity(context, (int) timeStamp + 10000,
                repeatResultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

//        Обратимся к ресурсам для удобства
        Resources res = context.getResources();

//        Создаем строитель уведомлений
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(res.getString(R.string.app_name));//используем название приложения
        builder.setContentText(title);//В тексте ипользуем, пришедший заголовок
        builder.setColor(context.getResources().getColor(color));//цвет иконки задачи
        builder.setSmallIcon(R.drawable.ic_check_white_48dp);

//        Указываем что быдет использованно от системы по умолчанию
        builder.setDefaults(Notification.DEFAULT_ALL);

//        В Контент отдаем pendingIntent
        builder.setContentIntent(pendingIntentRepeat);

        boolean equals = Utils.getFullDate(fullDate).equals(Utils.getFullDate(cal.getTimeInMillis()));

        if (equals) {
            Notification notification = builder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;//при нажатии пользователем, флаг удаляется

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify((int) timeStamp, notification);
        }
//        Создаем уведомление
        onRepeat(context, title, timeStamp, color, nameDay, numberDay, onlyTime, intent);
    }

    public void onRepeat(Context context, String title, long timeStamp, int color, String nameDay,
                         int numberDay, String onlyTime, Intent intent) {

        String[] output = onlyTime.split(":");
        int hour = Integer.parseInt(output[0]);
        int minutes = Integer.parseInt(output[1]);

        Calendar c = Calendar.getInstance();
        System.out.println("номер недели   " + c.get(Calendar.WEEK_OF_YEAR));
        c.set(Calendar.YEAR, c.get(Calendar.YEAR));
        c.set(Calendar.MONTH, c.get(Calendar.MONTH));
        c.set(Calendar.WEEK_OF_MONTH, c.get(Calendar.WEEK_OF_MONTH) + 1);
        c.set(Calendar.DAY_OF_WEEK, numberDay);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minutes);

        intent.putExtra("fullDate", c.getTimeInMillis());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                (int) timeStamp + numberDay + 10000, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }
}
