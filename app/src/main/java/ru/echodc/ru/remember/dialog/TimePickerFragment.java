package ru.echodc.ru.remember.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
//        Инициализируем переменные для времени
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

//        Возвращаем объект OnTimeSetListener
//        getActivity() - получаем Активити
//        this - ссылка на OnTimeSetListener call back
//        hour, minute, - часы, минуты
//        DateFormat.is24HourFormat(...) - класс, с помощью которого можно форматировать и анализировать показания даты и времени
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}
