package ru.echodc.ru.remember.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.echodc.ru.remember.R;
import ru.echodc.ru.remember.adapter.CurrentTaskAdapter;
import ru.echodc.ru.remember.database.DBHelper;
import ru.echodc.ru.remember.model.ModelSeparator;
import ru.echodc.ru.remember.model.ModelTask;


public class CurrentTaskFragment extends TaskFragment {

    public CurrentTaskFragment() {

    }

    OnTaskDoneListener onTaskDoneListener;

    //    Реализуем интерфейс
    public interface OnTaskDoneListener {
        void onTaskDone(ModelTask task);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onTaskDoneListener = (OnTaskDoneListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTaskDoneListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_task, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvCurrentTasks);

        layoutManager = new LinearLayoutManager(getActivity());

//        Присвоим LayoutManager новому RecyclerView
        recyclerView.setLayoutManager(layoutManager);

//        Проинициализируем Адаптер и установим его к RecyclerView
        adapter = new CurrentTaskAdapter(this);//ссылаемся на текущий фрагмент
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
                        + DBHelper.SELECTION_STATUS + " OR " + DBHelper.SELECTION_STATUS,
//                Поиск по всем буквосочетаниям в заголовках "%" + title + "%"
                new String[]{"%" + title + "%", Integer.toString(ModelTask.STATUS_CURRENT),
                        Integer.toString(ModelTask.STATUS_OVERDUE)}, DBHelper.TASK_DATE_COLUMN));

        for (int i = 0; i < tasks.size(); i++) {
            Log.d("data", "Title = " + tasks.get(i).getTitle());
            addTask(tasks.get(i), false);//false - запрос на сохранение
        }
    }

    //    Если Адаптер пустой, пересоздадим его
    @Override
    public void checkAdapter() {
        if (adapter == null) {
            adapter = new CurrentTaskAdapter(this);
            addTaskFromDB();
        }
    }

    //    Добавление задачи из Базы
    @Override
    public void addTaskFromDB() {
        checkAdapter();
        adapter.removeAllItems();//для удаления всех элементов
        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(activity.dbHelper.query().getTasks(DBHelper.SELECTION_STATUS + " OR "
                + DBHelper.SELECTION_STATUS, new String[]{Integer.toString(ModelTask.STATUS_CURRENT),
                Integer.toString(ModelTask.STATUS_OVERDUE)}, DBHelper.TASK_DATE_COLUMN));

        for (int i = 0; i < tasks.size(); i++) {
            addTask(tasks.get(i), false);//false - запрос на сохранение
        }
    }

    //    Для добавления задач
    @Override
    public void addTask(ModelTask newTask, boolean saveToDB) {
        int position = -1;

//        Присвоим значение null разделителю
        ModelSeparator separator = null;
        checkAdapter();

        //        Для добавления элементов по дате
        for (int i = 0; i < adapter.getItemCount(); i++) {
//            Если значение нового элемента списка будет больше, чем значение любого элемента
//            списка, то новый элемент будет добавляться на позицию выше
            if (adapter.getItem(i).isTask()) {
                ModelTask task = (ModelTask) adapter.getItem(i);
                if (newTask.getDate() < task.getDate()) {
                    position = i;
//                    Прерывание цикла при нахождении элемента с большей датой
                    break;
                }
            }
        }

//        Определяем варианты создания разделителей

        if (newTask.getDate() != 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(newTask.getDate());
            if (calendar.get(Calendar.YEAR) < Calendar.getInstance().get(Calendar.YEAR)) {
                newTask.setDateStatus(ModelSeparator.TYPE_OVERDUE);
                if (!adapter.containsSeparatorOverdue) {
                    adapter.containsSeparatorOverdue = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_OVERDUE);
                }
            } else if (calendar.get(Calendar.YEAR) > Calendar.getInstance().get(Calendar.YEAR)) {
                newTask.setDateStatus(ModelSeparator.TYPE_FUTURE);
                if (!adapter.containsSeparatorFuture) {
                    adapter.containsSeparatorFuture = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_FUTURE);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) < Calendar.getInstance().get(Calendar.DAY_OF_YEAR) &&
                    calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
                newTask.setDateStatus(ModelSeparator.TYPE_OVERDUE);
                if (!adapter.containsSeparatorOverdue) {
                    adapter.containsSeparatorOverdue = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_OVERDUE);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) &&
                    calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
                newTask.setDateStatus(ModelSeparator.TYPE_TODAY);
                if (!adapter.containsSeparatorToday) {
                    adapter.containsSeparatorToday = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_TODAY);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1 &&
                    calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
                newTask.setDateStatus(ModelSeparator.TYPE_TOMORROW);
                if (!adapter.containsSeparatorTomorrow) {
                    adapter.containsSeparatorTomorrow = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_TOMORROW);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) > Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1 &&
                    calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
                newTask.setDateStatus(ModelSeparator.TYPE_FUTURE);
                if (!adapter.containsSeparatorFuture) {
                    adapter.containsSeparatorFuture = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_FUTURE);
                }
            }
        } else {
            newTask.setDateStatus(ModelSeparator.TYPE_REPEAT);
            if (!adapter.containsSeparatorRepeat) {
                adapter.containsSeparatorRepeat = true;
                separator = new ModelSeparator(ModelSeparator.TYPE_REPEAT);
            }

        }

//        Если позиция не равна -1, т.е. ни один элемент из списка не больше нового,
//          то новый элемент будет добавляться на позицию ниже
        if (position != -1) {

//            Для предотвращения размещения некорректных задач в разделителях
            if (!adapter.getItem(position - 1).isTask()) {
                if (position - 2 >= 0 && adapter.getItem(position - 2).isTask()) {
                    ModelTask task = (ModelTask) adapter.getItem(position - 2);
                    if (task.getDateStatus() == newTask.getDateStatus()) {
                        position -= 1;
                    }
                } else if (position - 2 < 0 && newTask.getDate() == 0) {
                    position -= 1;
                }
            }

            if (separator != null) {
                adapter.addItem(position - 1, separator);
            }
            adapter.addItem(position, newTask);
        } else {
            if (separator != null) {
                adapter.addItem(separator);
            }
            adapter.addItem(newTask);
        }

//        Если сохранение удалось, скажем Активити об этом
        if (saveToDB) {
            activity.dbHelper.saveTask(newTask);
        }
    }

    @Override
    public void moveTask(ModelTask task) {

//        Удаляем Оповещение
        alarmHelper.removeAlarm(task.getTimeStamp());

//        Вызываем слушателя на выполненные задачи
        onTaskDoneListener.onTaskDone(task);
    }
}
