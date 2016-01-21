package ru.echodc.ru.remember.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.echodc.ru.remember.R;
import ru.echodc.ru.remember.adapter.DoneTaskAdapter;
import ru.echodc.ru.remember.database.DBHelper;
import ru.echodc.ru.remember.model.ModelTask;

public class DoneTaskFragment extends TaskFragment {

    public DoneTaskFragment() {
        // Required empty public constructor
    }

    OnTaskRestoreListener onTaskRestoreListener;

    public interface OnTaskRestoreListener {
        void onTaskRestore(ModelTask task);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onTaskRestoreListener = (OnTaskRestoreListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTaskRestoreListener");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_done_task, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvDoneTasks);

        layoutManager = new LinearLayoutManager(getActivity());

//        Присвоим LayoutManager новому RecyclerView
        recyclerView.setLayoutManager(layoutManager);

//        Определим адаптер
        adapter = new DoneTaskAdapter(this);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    //    Поиск задач
    @Override
    public void findTasks(String title) {
        checkAdapter();
        adapter.removeAllItems();//для удаления всех элементов
        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(activity.dbHelper.query().getTasks(DBHelper.SELECTION_LIKE_TITLE + " AND "
                + DBHelper.SELECTION_STATUS, new String[]{"%" + title + "%",
                Integer.toString(ModelTask.STATUS_DONE)}, DBHelper.TASK_DATE_COLUMN));

        for (int i = 0; i < tasks.size(); i++) {

            addTask(tasks.get(i), false);//false - запрос на сохранение
        }
    }

    //    Если Адаптер пустой, пересоздадим его
    @Override
    public void checkAdapter() {
        if (adapter == null) {
            adapter = new DoneTaskAdapter(this);
            addTaskFromDB();
        }
    }

    //    Добавление задачи из Базы
    @Override
    public void addTaskFromDB() {
        checkAdapter();
        adapter.removeAllItems();
        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(activity.dbHelper.query().getTasks(DBHelper.SELECTION_STATUS,
                new String[]{Integer.toString(ModelTask.STATUS_DONE)}, DBHelper.TASK_DATE_COLUMN));

        for (int i = 0; i < tasks.size(); i++) {
            addTask(tasks.get(i), false);//false - запрос на сохранение
        }
    }

    //    Для добавления задач
    @Override
    public void addTask(ModelTask newTask, boolean saveToDB) {
        int position = -1;
        checkAdapter();
//        Для добавления элелментов по дате
        for (int i = 0; i < adapter.getItemCount(); i++) {
//            Если значение нового эелемента списка будет больше, чем значение любого желемента
//            списка, то новый элемент будет добавляться на позицию выше
            if (adapter.getItem(i).isTask()) {
                ModelTask task = (ModelTask) adapter.getItem(i);
                if (newTask.getDate() < task.getDate()) {
                    position = i;
//                    Прерывание цикла при нахождении элемета с большей датой
                    break;
                }
            }
        }
//        Если позиция не равна -1, т.е. ни один эелемент из списка не больше нового,
//          то новый элемент будет добавлятся на позицию ниже
        if (position != -1) {
            adapter.addItem(position, newTask);
        } else {
            adapter.addItem(newTask);
        }

//        Если сохрание удалось, скажем Активити об этом
        if (saveToDB) {
            activity.dbHelper.saveTask(newTask);
        }
    }


    @Override
    public void moveTask(ModelTask task) {
//        Устанавливаем оповещение
        if (task.getDate() != 0) {
            alarmHelper.setAlarm(task);
        } else if (task.getOnlyTime() != 0) {
            alarmHelper.setAlarm(task);
        }
//        Вызовем слушателя на восстановление задачи
        onTaskRestoreListener.onTaskRestore(task);
    }
}
