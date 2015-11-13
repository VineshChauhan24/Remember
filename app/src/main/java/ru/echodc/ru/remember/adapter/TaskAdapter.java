package ru.echodc.ru.remember.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.echodc.ru.remember.fragment.TaskFragment;
import ru.echodc.ru.remember.model.Item;
import ru.echodc.ru.remember.model.ModelSeparator;
import ru.echodc.ru.remember.model.ModelTask;

/**
 * Объединяет CurrentTaskAdapter и DoneTaskAdapter
 */
public abstract class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;

    List<Item> items;

    TaskFragment taskFragment;

    public boolean containsSeparatorOverdue;
    public boolean containsSeparatorToday;
    public boolean containsSeparatorTomorrow;
    public boolean containsSeparatorFuture;
    public boolean containsSeparatorRepeat;

    //    Инициализируем массив в конструкторе
    public TaskAdapter(TaskFragment taskFragment) {
        items = new ArrayList<>();
        this.taskFragment = taskFragment;
        this.context = getTaskFragment().getActivity().getApplicationContext();
    }

    //    Методы для работы со списком
//    getItem(...) для работы с элеметом по позициям
    public Item getItem(int position) {
        return items.get(position);
    }

    //    addItem(...) метод добавляет пункты списка
    public void addItem(Item item) {
        items.add(item);
//        Сообщаем о добавлении нового элемента в список
//        notifyDataSetChanged(...) метод делает тоже самое, но без анимации
        notifyItemInserted(getItemCount() - 1);
    }

    //    addItem(...) перегружаем метод для других параметров
//    Теперь он будет добавлять элементы не в конец списка, а в определенную позицию
    public void addItem(int location, Item item) {
        items.add(location, item);
//        Сообщаем о добавлении нового элемента в список
//        notifyDataSetChanged(...) метод делает тоже самое, но без анимации
        notifyItemInserted(location);
    }

    //    Для обновления базы
    public void updateTask(ModelTask newTask) {
        for (int i = 0; i < getItemCount(); i++) {
            if (getItem(i).isTask()) {
                ModelTask task = (ModelTask) getItem(i);
                if (newTask.getTimeStamp() == task.getTimeStamp()) {
                    removeItem(i);
                    getTaskFragment().addTask(newTask, false);
                }
            }
        }
    }

    //    Метод для удаления элементов
    public void removeItem(int location) {
//       Элемент будет удалятся, если позиция в аргументе больше или равна нулю,
//          но не меньше текущего кол-ва элементов -1 позиция,
//          т.к. элементы считаются с нуля
        if (location >= 0 && location <= getItemCount() - 1) {
            items.remove(location);
            notifyItemRemoved(location);
//            Для удаления разделителей
            if (location - 1 >= 0 && location <= getItemCount() - 1) {
                if (!getItem(location).isTask() && !getItem(location - 1).isTask()) {
                    ModelSeparator separator = (ModelSeparator) getItem(location - 1);
                    checkSeparators(separator.getType());
                    items.remove(location - 1);
                    notifyItemRemoved(location - 1);
                }
            } else if (getItemCount() - 1 >= 0 && !getItem(getItemCount() - 1).isTask()) {
                ModelSeparator separator = (ModelSeparator) getItem(getItemCount() - 1);
                checkSeparators(separator.getType());

//                Независящая переменная от itemCount, после удаления item
                int locationTemp = getItemCount() - 1;
                items.remove(locationTemp);
                notifyItemRemoved(locationTemp);
            }
        }
    }

    //    Проверка разделителей по типу
    public void checkSeparators(int type) {
        switch (type) {
            case ModelSeparator.TYPE_OVERDUE:
                containsSeparatorOverdue = false;
                break;
            case ModelSeparator.TYPE_TODAY:
                containsSeparatorToday = false;
                break;
            case ModelSeparator.TYPE_TOMORROW:
                containsSeparatorTomorrow = false;
                break;
            case ModelSeparator.TYPE_FUTURE:
                containsSeparatorFuture = false;
                break;
            case ModelSeparator.TYPE_REPEAT:
                containsSeparatorRepeat = false;
                break;
        }
    }

    //    Удаление всех элементов
    public void removeAllItems() {
        if (getItemCount() != 0) {
            items = new ArrayList<>();
            notifyDataSetChanged();
//            Присвоим false всем разделителям
            containsSeparatorOverdue = false;
            containsSeparatorToday = false;
            containsSeparatorTomorrow = false;
            containsSeparatorFuture = false;
            containsSeparatorRepeat = false;
        }
    }

    //    Возвращаем размер массива эелементов списка
    @Override
    public int getItemCount() {
        return items.size();
    }

    //    ViewHolder - будет виден всем классам в пакете, т.к. он protected
    protected class TaskViewHolder extends RecyclerView.ViewHolder {

        //        Создадим поля для заголовка и даты
//        К ним можно получить доступ из классов наследников
        protected TextView title;
        protected TextView date;
        protected CircleImageView priority;
        //        ******************************************************************************************
        protected TextView day;
//        ******************************************************************************************

        //        Конструктор
        public TaskViewHolder(View itemView, TextView title, TextView date, CircleImageView priority, TextView day) {
            super(itemView);
            this.title = title;
            this.date = date;
            this.priority = priority;
//            **************************************************************************************
            this.day = day;
//            **************************************************************************************
        }
    }

    //    Для определения наличия разделителей
    protected class SeparatorViewHolder extends RecyclerView.ViewHolder {

        protected TextView type;


        //        Конструктор
        public SeparatorViewHolder(View itemView, TextView type) {
            super(itemView);
            this.type = type;
        }
    }

    public TaskFragment getTaskFragment() {
        return taskFragment;
    }

}
