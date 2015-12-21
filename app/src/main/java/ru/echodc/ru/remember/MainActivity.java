package ru.echodc.ru.remember;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import ru.echodc.ru.remember.adapter.TabAdapter;
import ru.echodc.ru.remember.alarm.AlarmHelper;
import ru.echodc.ru.remember.database.DBHelper;
import ru.echodc.ru.remember.dialog.AddingTaskDialogFragment;
import ru.echodc.ru.remember.dialog.EditTaskDialogFragment;
import ru.echodc.ru.remember.fragment.CurrentTaskFragment;
import ru.echodc.ru.remember.fragment.DoneTaskFragment;
import ru.echodc.ru.remember.fragment.SplashFragment;
import ru.echodc.ru.remember.fragment.TaskFragment;
import ru.echodc.ru.remember.model.ModelTask;

public class MainActivity extends AppCompatActivity implements AddingTaskDialogFragment.AddingTaskListener, CurrentTaskFragment.OnTaskDoneListener, DoneTaskFragment.OnTaskRestoreListener, EditTaskDialogFragment.EditingTaskListener {
    FragmentManager mFragmentManager;

    PreferenceHelper mPreferenceHelper;
    TabAdapter tabAdapter;

    TaskFragment mCurrentTaskFragment;
    TaskFragment mDoneTaskFragment;

    SearchView searchView;

    public DBHelper dbHelper;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Покажем баннер
//        Ads.showBanner(this);

//        Инициализируем AlarmHelper
        AlarmHelper.getInstance().init(getApplicationContext());

//        Инициализируем новый DBHelper
        dbHelper = new DBHelper(getApplicationContext());

//        Инициализация PreferenceHelper методом init
        PreferenceHelper.getInstance().init(getApplicationContext());
//        Получаем экземпляр класса PreferenceHelper
        mPreferenceHelper = PreferenceHelper.getInstance();
//        Инициализация Фрагмент менеджера
        mFragmentManager = getFragmentManager();

        runSplash();

        setUI();
    }

    //    Чтобы приложение не вылетало сразу, после нажатия кнопки Назад
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    }

    //   Состояния Активити
    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.activityPaused();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem splashItem = menu.findItem(R.id.action_splash);
        splashItem.setChecked(mPreferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case R.id.action_splash:
                item.setChecked(!item.isChecked());
//            Сохраняем состояние флага в PreferenceHelper
                mPreferenceHelper.putBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE, item.isChecked());
                return true;
            case R.id.action_authors:
                intent = new Intent(getApplicationContext(), ScrollingAboutActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void runSplash() {
//          Проверяем сначала состояние флага в объекте mPreferenceHelper
        if (!mPreferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE)) {
            SplashFragment splashFragment = new SplashFragment();

//            Загружаем фрагмент со Сплэшскрином
            mFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, splashFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    //    Метод отвечает за пользовательский интерфейс
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            setSupportActionBar(toolbar);
        }

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.current_task));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.done_task));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
//        В адаптер передаем Менеджер фрагментов и кол-во вкладок
        tabAdapter = new TabAdapter(mFragmentManager, 2);

//        Присвоим адаптер viewPager
        viewPager.setAdapter(tabAdapter);
//        Ставим слушатель на событие смены вкладок
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //            Определяет какой Таб выбран
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            //            Определяет, что Таб более не выбран
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            //            Определяет, что выбран ранее выбранный Таб
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        //        Инициализируем фрагменты
        mCurrentTaskFragment = (CurrentTaskFragment) tabAdapter.getItem(TabAdapter.CURRENT_TASK_FRAGMENT_POSITION);
        mDoneTaskFragment = (DoneTaskFragment) tabAdapter.getItem(TabAdapter.DONE_TASK_FRAGMENT_POSITION);

        //        Инициализируем поиск
        searchView = (SearchView) findViewById(R.id.search_view);

        //        Установим слушателя на поле поиска
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mCurrentTaskFragment.findTasks(newText);
                mDoneTaskFragment.findTasks(newText);
                return false;
            }
        });


//            Инициализируем плавающую кнопку, вешаем слушателя
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment addingTaskDialogFragment = new AddingTaskDialogFragment();
                addingTaskDialogFragment.show(mFragmentManager, "AddingTaskDialogFragment");
            }
        });
    }

    //    Методы для обратной связи
    @Override
    public void onTaskAdded(ModelTask newTask) {
        mCurrentTaskFragment.addTask(newTask, true);//если задача новая

        Toast.makeText(this, R.string.task_added, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTaskAddingCancel() {
        Toast.makeText(this, R.string.task_adding_cancel, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTaskDone(ModelTask task) {
        mDoneTaskFragment.addTask(task, false);//если задача переносится
    }

    @Override
    public void onTaskRestore(ModelTask task) {
        mCurrentTaskFragment.addTask(task, false);//чтобы не создавался еще 1 экземпляр
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }

    //    Обновляем базу после редактирования
    @Override
    public void onTaskEdited(ModelTask updateTask) {
        mCurrentTaskFragment.updateTask(updateTask);
        dbHelper.update().task(updateTask);
    }


}
