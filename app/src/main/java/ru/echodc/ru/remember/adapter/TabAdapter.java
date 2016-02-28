package ru.echodc.ru.remember.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import ru.echodc.ru.remember.fragment.CurrentTaskFragment;
import ru.echodc.ru.remember.fragment.DoneTaskFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

    //    Для получения экземпляра существующего фрагмента в Активити
    public static final int CURRENT_TASK_FRAGMENT_POSITION = 0;
    public static final int DONE_TASK_FRAGMENT_POSITION = 1;

    //    Чтобы объекты не создавались при каждом вызове метода getItem(...)
    private CurrentTaskFragment currentTaskFragment;
    private DoneTaskFragment doneTaskFragment;

    //    Хранит количество таб-вкладок
    private int numberOfTabs;

    public TabAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
        currentTaskFragment = new CurrentTaskFragment();
        doneTaskFragment = new DoneTaskFragment();
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return currentTaskFragment;
            case 1:
                return doneTaskFragment;
//            Если ни одно значение не подходит, по умолчанию вернется null
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
