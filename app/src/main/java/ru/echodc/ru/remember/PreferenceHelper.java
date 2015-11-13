package ru.echodc.ru.remember;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
    public static final String SPLASH_IS_INVISIBLE = "splash_is_invisible";
    private static PreferenceHelper instance;
    private SharedPreferences mPreferences;
    private Context context;

    //    Конструктор
    private PreferenceHelper() {

    }

    public static PreferenceHelper getInstance() {
//        Получаем объект instance, если его нет, то создаем новый
        if (instance == null) {
            instance = new PreferenceHelper();
        }
        return instance;
    }

    public void init(Context context) {
//        "preferences" - файл настроек
//        MODE_PRIVATE - режим работы, доступ будет имтеть только это приложение
        this.context = context;
        mPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = mPreferences.edit();//получаем объект Edit, для внесения изменений
        editor.putBoolean(key, value);//изменяем нужный объект
        editor.apply();//вызываем apply, чтобы изменения вступили в силу
    }

    public boolean getBoolean(String key) {
        return mPreferences.getBoolean(key, false);//ключ поля с сохраненной настройкой
    }
}
