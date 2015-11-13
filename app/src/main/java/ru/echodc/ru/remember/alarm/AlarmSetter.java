package ru.echodc.ru.remember.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import ru.echodc.ru.remember.database.DBHelper;
import ru.echodc.ru.remember.model.ModelTask;

/**
 * Устанавливает ресивер после перезагрузки устройства
 */
public class AlarmSetter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DBHelper dbHelper = new DBHelper(context);

        AlarmHelper.getInstance().init(context);
        AlarmHelper alarmHelper = AlarmHelper.getInstance();
        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(dbHelper.query().getTasks(DBHelper.SELECTION_STATUS + " OR "
                + DBHelper.SELECTION_STATUS, new String[]{Integer.toString(ModelTask.STATUS_CURRENT),
                Integer.toString(ModelTask.STATUS_OVERDUE)}, DBHelper.TASK_DATE_COLUMN));

//        Устанавливаем напоминание через цикл foreach
        for (ModelTask task : tasks) {
            if (task.getDate() != 0) {
                alarmHelper.setAlarm(task);
            }
        }

//        ******************************************************************************************
        ///*
        // правильно ли я запрос делаю в базу? Обращаюсь к Хелперу, забираю строку чтобы был текущий стаус
        // потом в колонку с днями
        // если запрос не NULL , через цикл ставим аларм
        // */
        //
//        Если в запросе к базе в задаче существует стобик с днями, то установим напоминание через цикл
//
//        if (dbHelper.query().getTasks(DBHelper.SELECTION_STATUS, new String[]{Integer.toString(ModelTask.STATUS_CURRENT)}, DBHelper.TASK_DAY_COLUMN) != null) {
//            for (ModelTask task: tasks) {
//                    alarmHelper.setAlarm(task);
//            }
//        }
//        ******************************************************************************************
    }
}
