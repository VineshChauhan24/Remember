package ru.echodc.ru.remember.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;

import ru.echodc.ru.remember.MainActivity;
import ru.echodc.ru.remember.MyApplication;
import ru.echodc.ru.remember.R;

/**
 * Приемник широковещательных событий
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        Формируем данные для передачи их ресиверу
        String title = intent.getStringExtra("title");//заголовок
        long timeStamp = intent.getLongExtra("time_stamp", 0);//время создания, по умолчанию 0
        int color = intent.getIntExtra("color", 0);//цвет приоритета задачи, по умолчанию 0
//        ******************************************************************************************

//        String dayString = intent.getStringExtra("day");//день недели
//        int[] day = new int [7];
//
//        for(int i = 0;i<dayString.length();i++){
//            day[i] = Integer.parseInt(String.valueOf(dayString.charAt(i)));
//            System.out.println(Integer.parseInt(String.valueOf(dayString.charAt(i))));//номера дней в консоли
//        }
//        ******************************************************************************************

//        Интент запускает главное Активити, при нажатии на уведомление
        Intent resultIntent = new Intent(context, MainActivity.class);

//        Если Активити уже запущено, то передаем его состояние в resultIntent из intent
//        поступившего на вход, чтобы оно не пересоздавалось вновь
        if (MyApplication.isActivityVisible()) {
            resultIntent = intent;
        }
//        Если приложение закрыто, то стартуем его
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

//        PendingIntent - позволяет запустить интент, хранящийся в нем,
//          от имени того приложения, с  теми же полномочиями, которым он и создавался
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) timeStamp,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

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
        builder.setContentIntent(pendingIntent);

//        Создаем уведомление
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;//при нажатии пользователем, флаг удаляется

//        Для информирования о фоновых событиях
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) timeStamp, notification);

    }
}
