package ru.echodc.ru.remember.model;

import ru.echodc.ru.remember.R;

public class ModelSeparator implements Item {

    public static final int TYPE_OVERDUE = R.string.separator_overdue;
    public static final int TYPE_TODAY = R.string.separator_today;
    public static final int TYPE_TOMORROW = R.string.separator_tomorrow;
    public static final int TYPE_FUTURE = R.string.separator_future;
    public static final int TYPE_REPEAT = R.string.separator_repeat;
    private int type;

    //    Конструктор для определения типа разделителя
    public ModelSeparator(int type) {
        this.type = type;
    }

    //    Возвращаем false, т.к. это не задача
    @Override
    public boolean isTask() {
        return false;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
