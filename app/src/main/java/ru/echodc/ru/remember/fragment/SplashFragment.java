package ru.echodc.ru.remember.fragment;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.TimeUnit;

import ru.echodc.ru.remember.R;

public class SplashFragment extends Fragment {


    public SplashFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SplashTask splashTask = new SplashTask();
        splashTask.execute();

        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    class SplashTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
//            Задержка Сплэшскрина на экране на 2 сек.
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            Проверим на существование объекта Активити, чтобы приложение не вылетало
            if (getActivity() != null) {
//            Обратимся к Активити и найдем Фрагмент менеджер
                getActivity().getFragmentManager().popBackStack();
            }

            return null;
        }
    }
}
