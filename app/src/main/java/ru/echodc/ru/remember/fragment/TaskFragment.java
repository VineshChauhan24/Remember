package ru.echodc.ru.remember.fragment;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ru.echodc.ru.remember.MainActivity;
import ru.echodc.ru.remember.R;
import ru.echodc.ru.remember.adapter.TaskAdapter;
import ru.echodc.ru.remember.alarm.AlarmHelper;
import ru.echodc.ru.remember.dialog.EditTaskDialogFragment;
import ru.echodc.ru.remember.model.Item;
import ru.echodc.ru.remember.model.ModelTask;

public abstract class TaskFragment extends Fragment {
    //    Для отображения списков с стиле RecyclerView
    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;

    protected TaskAdapter adapter;

    public MainActivity activity;

    public AlarmHelper alarmHelper;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            activity = (MainActivity) getActivity();
        }

//        Инициализируем AlarmHelper
        alarmHelper = AlarmHelper.getInstance();

//        Вызывам 1 раз, вмето многих вызвов во фрагментах
        addTaskFromDB();
    }

    //    Для добавления задач
    public abstract void addTask(ModelTask newTask, boolean saveToDB);

    //    Реализация обновления задачи
    public void updateTask(ModelTask task) {
        adapter.updateTask(task);
    }

    //    Реализация диалога удаления задачи
    public void removeTaskDialog(final int location) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setMessage(R.string.dialog_removing_message);

        Item item = adapter.getItem(location);

        if (item.isTask()) {

            ModelTask removingTask = (ModelTask) item;

            final long timeStamp = removingTask.getTimeStamp();
            final boolean[] isRemoved = {false};

//            Кнопка ОК
            dialogBuilder.setPositiveButton(R.string.dialog_OK, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.removeItem(location);
                    isRemoved[0] = true;
//                    Инициализируем SnackBar
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinator),
                            R.string.removed, Snackbar.LENGTH_LONG);
                    snackbar.setAction(R.string.dialog_Cancel, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            По нажатию на SnackBar задача будет восстанавливаться из базы данных
                            addTask(activity.dbHelper.query().getTask(timeStamp), false);
                            isRemoved[0] = false;

                        }
                    });
//                    Ставим слушатель на SnackBar
                    snackbar.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                        //                        SnackBar появился
                        @Override
                        public void onViewAttachedToWindow(View v) {

                        }

                        //                        SnackBar изчез
                        @Override
                        public void onViewDetachedFromWindow(View v) {
//                            Если не было нажатие на кнопку удаления SnackBar,
//                              то задача окончательно удаляется из базы. Определяется по временной метке
                            if (isRemoved[0]) {
//                                Метод для уделения уведомления
                                alarmHelper.removeAlarm(timeStamp);


                                activity.dbHelper.removeTask(timeStamp);
                            }
                        }
                    });

//                    Покажем SnackBar
                    snackbar.show();
                    dialog.dismiss();

                }
            });
//            Кнопка Cancel
            dialogBuilder.setNegativeButton(R.string.dialog_Cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

        }
//        Покажем диалог
        dialogBuilder.show();
    }

//    Метод вызова диалога
    public void showTaskEditDialog(ModelTask task){
        DialogFragment editingTaskDialog = EditTaskDialogFragment.newInstance(task);
        editingTaskDialog.show(getActivity().getFragmentManager(),"EditTaskDialogFragment");
    }

//    public void removeAllTasks() {
//        adapter.removeAllItems();
//    }

    public abstract void findTasks(String title);

    public abstract void checkAdapter();

    public abstract void addTaskFromDB();

    public abstract void moveTask(ModelTask task);
}
