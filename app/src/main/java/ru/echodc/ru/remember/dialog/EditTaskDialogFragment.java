package ru.echodc.ru.remember.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import ru.echodc.ru.remember.R;
import ru.echodc.ru.remember.Utils;
import ru.echodc.ru.remember.alarm.AlarmHelper;
import ru.echodc.ru.remember.model.ModelTask;

public class EditTaskDialogFragment extends DialogFragment {

    int[] day;

    public static EditTaskDialogFragment newInstance(ModelTask task) {
        EditTaskDialogFragment editTaskDialogFragment = new EditTaskDialogFragment();

//        Формируем данные для передачи из диалога
        Bundle args = new Bundle();
        args.putString("title", task.getTitle());
        args.putLong("date", task.getDate());
        args.putInt("priority", task.getPriority());
        args.putLong("time_stamp", task.getTimeStamp());

        String dayArray = "";
        for (int i = 0; i < task.getDay().length; i++) {
            dayArray += task.getDay()[i];
        }
        args.putString("day", dayArray);
        args.putLong("onlyTime", task.getOnlyTime());
        editTaskDialogFragment.setArguments(args);
        return editTaskDialogFragment;
    }

    private EditingTaskListener editingTaskListener;

    public interface EditingTaskListener {
        void onTaskEdited(ModelTask updateTask);
    }

    //    Присвоим слушатель главной Активити
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            editingTaskListener = (EditingTaskListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement EditingTaskListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Создадим эекземпляр Контекста
        final Context context = getActivity().getApplicationContext();

//        Создаем диалог, наполняя его данными из Bundle
        Bundle args = getArguments();
        String title = args.getString("title");
        long date = args.getLong("date", 0);
        int priority = args.getInt("priority", 0);
        long timeStamp = args.getLong("time_stamp", 0);
        //int day = args.getInt("day", 0);

        long onlyTime = args.getLong("onlyTime", 0);

        String dayArray = args.getString("day");
        day = new int[7];

        assert dayArray != null;
        for (int i = 0; i < dayArray.length(); i++) {
            day[i] = Integer.parseInt(String.valueOf(dayArray.charAt(i)));
            System.out.println(Integer.parseInt(String.valueOf(dayArray.charAt(i))));//номера дней в консоли
        }

        final ModelTask task = new ModelTask(title, date, priority, 0, timeStamp, day, onlyTime);

//        Создаем экземпляр от AlertDialog.Builder
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

//        builder.setTitle(R.string.dialog_editing_title);

//        Для работы с макетом диалога
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_task, null);
        TextView tvDialogTitle = (TextView) container.findViewById(R.id.tvDialogTitle);
        tvDialogTitle.setText(R.string.dialog_editing_title);

//        Получаем элементы EditText через TextInputLayout
        final TextInputLayout tilTitle = (TextInputLayout) container.findViewById(R.id.tilDialogTaskTitle);
        final EditText etTitle = tilTitle.getEditText();

        final TextInputLayout tilDate = (TextInputLayout) container.findViewById(R.id.tilDialogTaskDate);
        final EditText etDate = tilDate.getEditText();

        final TextInputLayout tilTime = (TextInputLayout) container.findViewById(R.id.tilDialogTaskTime);
        final EditText etTime = tilTime.getEditText();

        final TextInputLayout tilWeekTime = (TextInputLayout) container.findViewById(R.id.tilDialogTaskTimeWeekDays);
        final EditText etTimeWeekDays = tilWeekTime.getEditText();

//        Инициализируем переключатель для выбора дней
        final SwitchCompat mSwitch = (SwitchCompat) container.findViewById(R.id.switchRepeat);
        final TableLayout tblLayout = (TableLayout) container.findViewById(R.id.tblLayoutDays);
        tblLayout.setVisibility(View.GONE);

        final CheckBox su = (CheckBox) container.findViewById(R.id.chbSunday);
        final CheckBox mo = (CheckBox) container.findViewById(R.id.chbMonday);
        final CheckBox tu = (CheckBox) container.findViewById(R.id.chbTuesday);
        final CheckBox we = (CheckBox) container.findViewById(R.id.chbWednesday);
        final CheckBox th = (CheckBox) container.findViewById(R.id.chbThursday);
        final CheckBox fr = (CheckBox) container.findViewById(R.id.chbFriday);
        final CheckBox sa = (CheckBox) container.findViewById(R.id.chbSaturday);

        final TextView tvSu = (TextView) container.findViewById(R.id.tvSunday);
        final TextView tvMo = (TextView) container.findViewById(R.id.tvMonday);
        final TextView tvTu = (TextView) container.findViewById(R.id.tvTuesday);
        final TextView tvWe = (TextView) container.findViewById(R.id.tvWednesday);
        final TextView tvTh = (TextView) container.findViewById(R.id.tvThursday);
        final TextView tvFr = (TextView) container.findViewById(R.id.tvFriday);
        final TextView tvSa = (TextView) container.findViewById(R.id.tvSaturday);

//        Переключатель для показа выбора дней
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tblLayout.setVisibility(View.VISIBLE);
                    tilDate.setVisibility(View.GONE);
                    tilTime.setVisibility(View.GONE);

                    for (int i = 0; i < day.length; i++) {
                        if (day[i] == 1) {
                            su.setChecked(true);
                            tvSu.setTextColor(getActivity().getResources().getColor(R.color.white));
                        }
                        if (day[i] == 2) {
                            mo.setChecked(true);
                            tvMo.setTextColor(getActivity().getResources().getColor(R.color.white));
                        }
                        if (day[i] == 3) {
                            tu.setChecked(true);
                            tvTu.setTextColor(getActivity().getResources().getColor(R.color.white));
                        }
                        if (day[i] == 4) {
                            we.setChecked(true);
                            tvWe.setTextColor(getActivity().getResources().getColor(R.color.white));
                        }
                        if (day[i] == 5) {
                            th.setChecked(true);
                            tvTh.setTextColor(getActivity().getResources().getColor(R.color.white));
                        }
                        if (day[i] == 6) {
                            fr.setChecked(true);
                            tvFr.setTextColor(getActivity().getResources().getColor(R.color.white));
                        }
                        if (day[i] == 7) {
                            sa.setChecked(true);
                            tvSa.setTextColor(getActivity().getResources().getColor(R.color.white));
                        }
                    }
//        Перебор вариантов для отметки нужных дней
                    CompoundButton.OnCheckedChangeListener ochl = new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            switch (buttonView.getId()) {
                                case R.id.chbSunday:
                                    if (isChecked) {
                                        su.setChecked(true);
                                        tvSu.setTextColor(getActivity().getResources().getColor(R.color.white));

                                    } else {
                                        tvSu.setTextColor(getActivity().getResources().getColor(R.color.accent));
                                    }
                                    break;
                                case R.id.chbMonday:
                                    if (isChecked) {
                                        mo.setChecked(true);
                                        tvMo.setTextColor(getActivity().getResources().getColor(R.color.white));
//                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                                    } else {
                                        tvMo.setTextColor(getActivity().getResources().getColor(R.color.accent));
                                    }
                                    break;
                                case R.id.chbTuesday:
                                    if (isChecked) {
                                        tu.setChecked(true);
                                        tvTu.setTextColor(getActivity().getResources().getColor(R.color.white));
//                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                                    } else {
                                        tvTu.setTextColor(getActivity().getResources().getColor(R.color.accent));
                                    }
                                    break;
                                case R.id.chbWednesday:
                                    if (isChecked) {
                                        we.setChecked(true);
                                        tvWe.setTextColor(getActivity().getResources().getColor(R.color.white));
//                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                                    } else {
                                        tvWe.setTextColor(getActivity().getResources().getColor(R.color.accent));
                                    }
                                    break;
                                case R.id.chbThursday:
                                    if (isChecked) {
                                        th.setChecked(true);
                                        tvTh.setTextColor(getActivity().getResources().getColor(R.color.white));
//                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                                    } else {
                                        tvTh.setTextColor(getActivity().getResources().getColor(R.color.accent));
                                    }
                                    break;
                                case R.id.chbFriday:
                                    if (isChecked) {
                                        fr.setChecked(true);
                                        tvFr.setTextColor(getActivity().getResources().getColor(R.color.white));
//                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                                    } else {
                                        tvFr.setTextColor(getActivity().getResources().getColor(R.color.accent));
                                    }
                                    break;
                                case R.id.chbSaturday:
                                    if (isChecked) {
                                        sa.setChecked(true);
                                        tvSa.setTextColor(getActivity().getResources().getColor(R.color.white));
//                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                                    } else {
                                        tvSa.setTextColor(getActivity().getResources().getColor(R.color.accent));
                                    }
                                    break;
                            }
                        }
                    };

//        Вешаем слушателя на состояние отмеченных дней
                    su.setOnCheckedChangeListener(ochl);
                    mo.setOnCheckedChangeListener(ochl);
                    tu.setOnCheckedChangeListener(ochl);
                    we.setOnCheckedChangeListener(ochl);
                    th.setOnCheckedChangeListener(ochl);
                    fr.setOnCheckedChangeListener(ochl);
                    sa.setOnCheckedChangeListener(ochl);
                } else {
                    tblLayout.setVisibility(View.GONE);
                    tilDate.setVisibility(View.VISIBLE);
                    tilTime.setVisibility(View.VISIBLE);
                }
            }
        });

//        Инициализируем Спиннер
        Spinner spPriority = (Spinner) container.findViewById(R.id.spDialogTaskPriority);

        etTitle.setText(task.getTitle());
//        Ставим курсос в конец текста заголовка
        etTitle.setSelection(etTitle.length());
//        Если дата не равна нулю, то установим ее
        if (task.getDate() != 0) {
            etDate.setText(Utils.getDate(task.getDate()));
            etTime.setText(Utils.getTime(task.getDate()));
            // Иначе ставим только время
        } else if (task.getOnlyTime() != 0) {
            etTimeWeekDays.setText(Utils.getTime(task.getOnlyTime()));
        }

//        Присвоим подсказки
        tilTitle.setHint(getResources().getString(R.string.dialog_task_title));
        tilDate.setHint(getResources().getString(R.string.dialog_task_date));
        tilTime.setHint(getResources().getString(R.string.dialog_task_time));
        tilWeekTime.setHintAnimationEnabled(true);
        tilWeekTime.setHint(getResources().getString(R.string.dialog_task_time));

//        Передаем все данные в билдер для формирования диалога
        builder.setView(container);

//        Создадиим Адаптер для Спиннера
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.priority));

//        Создадиим Адаптер для Спиннера
//        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(getActivity(),
//                android.R.layout.simple_spinner_dropdown_item, ModelTask.PRIORITY_LEVELS );

//        Присвоим Адаптер Спиннеру и установим ему слушатель
        spPriority.setAdapter(priorityAdapter);

        spPriority.setSelection(task.getPriority());

        spPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Устанавливаем приоритеты, используя позицию элемента
                task.setPriority(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        Объект Calendar будет возвращать текущее время
        final Calendar calendar = Calendar.getInstance();
//        Добавим 1 час ко времени, если указана только дата, при создании задачи
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);
//        Если дата не изменилась, то вернем какая была на момент  редактирования
        if (etDate.length() != 0 || etTime.length() != 0) {
            calendar.setTimeInMillis(task.getDate());

        }

//        Присвоим слушателей для TimePickerDialog и DatePickerDialog
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Чтобы избежать накладывания анимации, добавим пробел по нажатию на поле ввода
//                Создадим проверку на длину текста в поле
                if (etDate.length() == 0) {
                    etDate.setText(" ");
                }
//                Создадим объет datePickerFragment и переопределим его метод onDateSet
                DialogFragment datePickerFragment = new DatePickerFragment() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        Установим значения года, месяца, дня в календарь
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        etDate.setText(Utils.getDate(calendar.getTimeInMillis()));
                    }

//                    Чтобы при нажатии кнопки Cancel текст не устанавливался
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        etDate.setText(null);
                    }
                };
//                Покажем DatePicker диалог
                datePickerFragment.show(getFragmentManager(), "DatePickerFragment");
            }
        });

        assert etTime != null;
        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                При нажатии, записываем пробел в поле ввода
                if (etTime.length() == 0) {
                    etTime.setText(" ");
                }
                DialogFragment timePickerFragment = new TimePickerFragment() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        Установим значения часа, минут, а секунды будут 0
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);

//                        Нельзя задать только время, нужны все параметры
//                        год, меся и день задаим нулями 0,0,0
                        etTime.setText(Utils.getTime(calendar.getTimeInMillis()));
                    }

//                    Чтобы при нажатии кнопки Cancel текст не устанавливался
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        etTime.setText(null);
                    }
                };
//                Показываем TimePicker диалог
                timePickerFragment.show(getFragmentManager(), "TimePickerFragment");
            }
        });

        assert etTimeWeekDays != null;
        etTimeWeekDays.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                При нажатии, записываем пробел в поле ввода
                if (etTimeWeekDays.length() == 0) {
                    etTimeWeekDays.setText(" ");
                }

                DialogFragment timePickerFragment = new TimePickerFragment() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        Установим значения часа, минут, а секунды будут 0
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);

//                        Нельзя задать только время, нужны все параметры
//                        год, месяц и день задаим нулями 0,0,0
                        etTimeWeekDays.setText(Utils.getTime(calendar.getTimeInMillis()));
                    }

//                    Чтобы при нажатии кнопки Cancel текст не устанавливался
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        etTimeWeekDays.setText(null);
                    }
                };
//                Показываем TimePicker диалог
                timePickerFragment.show(getFragmentManager(), "TimePickerFragment");
            }
        });

//        Установим кнопки подтверждения и отмены
        builder.setPositiveButton(R.string.dialog_OK, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlarmHelper alarmHelper = AlarmHelper.getInstance();

                if (mSwitch.isChecked()) {
                    task.setDate(0);
                    etDate.setText(null);
                    etTime.setText(null);
                    alarmHelper.removeAlarm(task.getTimeStamp(), task);
                    task.setOnlyTime(calendar.getTimeInMillis());

//                Установим отмеченные дни
                    if (su.isChecked()) {
                        task.setDay(0, 1);
                        System.out.println("=========================                               setDay(1,1)");
                    }
                    if (mo.isChecked()) {
                        System.out.println("=========================                               setDay(1,2)");
                        task.setDay(1, 2);
                    }
                    if (tu.isChecked()) {
                        System.out.println("=========================                               setDay(1,3)");
                        task.setDay(2, 3);
                    }
                    if (we.isChecked()) {
                        System.out.println("=========================                               setDay(1,4)");
                        task.setDay(3, 4);
                    }
                    if (th.isChecked()) {
                        System.out.println("=========================                               setDay(1,5)");
                        task.setDay(4, 5);
                    }
                    if (fr.isChecked()) {
                        System.out.println("=========================                               setDay(1,6)");
                        task.setDay(5, 6);
                    }
                    if (sa.isChecked()) {
                        System.out.println("=========================                               setDay(1,7)");
                        task.setDay(6, 7);
                    }

                    if (!su.isChecked() && !mo.isChecked() && !tu.isChecked() && !we.isChecked() && !th.isChecked() && !fr.isChecked() && !sa.isChecked()) {
                        task.setDay(0, 1);
                        task.setDay(1, 2);
                        task.setDay(2, 3);
                        task.setDay(3, 4);
                        task.setDay(4, 5);
                        task.setDay(5, 6);
                        task.setDay(6, 7);

                        Toast.makeText(context, R.string.toast_forgot_to_choose_a_day, Toast.LENGTH_LONG).show();
                    }
                }

                //если не выбран ни один день, дни простввятся автоматом
                else {
                    etTimeWeekDays.setText(null);
                    alarmHelper.removeAlarm(task.getTimeStamp(), task);
                    su.setChecked(false);
                    task.setDay(0, 0);
                    mo.setChecked(false);
                    task.setDay(1, 0);
                    tu.setChecked(false);
                    task.setDay(2, 0);
                    we.setChecked(false);
                    task.setDay(3, 0);
                    th.setChecked(false);
                    task.setDay(4, 0);
                    fr.setChecked(false);
                    task.setDay(5, 0);
                    sa.setChecked(false);
                    task.setDay(6, 0);
                    task.setOnlyTime(0);
                    task.setDate(calendar.getTimeInMillis());
                }

//                Присвоим заголовок задаче из поля ввода
                task.setTitle(etTitle.getText().toString());
                task.setStatus(ModelTask.STATUS_CURRENT);//установим статус задачи

//                Создадим новый объект AlarmHelper
                alarmHelper.setAlarm(task);

//                Установим задаче статус Текущая
                task.setStatus(ModelTask.STATUS_CURRENT);
                editingTaskListener.onTaskEdited(task);

                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.dialog_Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

//        Обращатся к кнопкам можно после показа диалога
//        Установим слушатель на события отображения диалога
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                if (etTitle.length() == 0) {
//                Заблокируем кнопку ОК для предотвращения создания пустых Task
                    positiveButton.setEnabled(false);
                    tilTitle.setError(getResources().getString(R.string.dialog_error_empty_title));
                }

//                Добавим слушатель на событие изменения текста
                etTitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

//                        Проверим длину текста
                        if (s.length() == 0) {
//                            Если ноль, блокируем кнопку ОК и отображаем ошибку
                            positiveButton.setEnabled(false);
                            tilTitle.setError(getResources().getString(R.string.dialog_error_empty_title));
                        } else {
//                            Если не ноль, активируем кнопку и убираем сообщение об ошибке
                            positiveButton.setEnabled(true);
                            tilTitle.setErrorEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        });

        return alertDialog;
    }
}