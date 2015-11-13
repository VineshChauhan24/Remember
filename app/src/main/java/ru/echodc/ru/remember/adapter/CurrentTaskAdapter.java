package ru.echodc.ru.remember.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.echodc.ru.remember.R;
import ru.echodc.ru.remember.Utils;
import ru.echodc.ru.remember.fragment.CurrentTaskFragment;
import ru.echodc.ru.remember.model.Item;
import ru.echodc.ru.remember.model.ModelSeparator;
import ru.echodc.ru.remember.model.ModelTask;


// Элементами Адаптера будут ViewHolder
public class CurrentTaskAdapter extends TaskAdapter {

    public static final int TYPE_TASK = 0;
    public static final int TYPE_SEPARATOR = 1;

    //    Конструктор
    public CurrentTaskAdapter(CurrentTaskFragment taskFragment) {
        super(taskFragment);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case TYPE_TASK:
                View v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.model_task, viewGroup, false);
                TextView title = (TextView) v.findViewById(R.id.tvTaskTitle);
                TextView date = (TextView) v.findViewById(R.id.tvTaskDate);
                CircleImageView priority = (CircleImageView) v.findViewById(R.id.cvTaskPriority);
//                **********************************************************************************
                TextView day = (TextView) v.findViewById(R.id.tvDays);
//                **********************************************************************************

                return new TaskViewHolder(v, title, date, priority, day);
            case TYPE_SEPARATOR:
                View separator = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.model_separator, viewGroup, false);
                TextView type = (TextView) separator.findViewById(R.id.tvSeparatorName);
//                Возвращаем готовый разделитель
                return new SeparatorViewHolder(separator, type);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        Item item = items.get(position);
//            Для удобства манипулирования идентификаторами вью элементов
        final Resources resources = viewHolder.itemView.getResources();

//        Если элемент является задачей
        if (item.isTask()) {
//            Активируем возможность нажатия на задачу
            viewHolder.itemView.setEnabled(true);
            final ModelTask task = (ModelTask) item;
            final TaskViewHolder taskViewHolder = (TaskViewHolder) viewHolder;

//            Для удобства манипулирования идентификаторами вью элементов
            final View itemView = taskViewHolder.itemView;

//            Установим дни недели из базы
//            **************************************************************************************
            taskViewHolder.day.setText(Utils.getDayName(context, task.getDay()));
//            **************************************************************************************
//            Установим заголовок через обращение к taskViewHolder
            taskViewHolder.title.setText(task.getTitle());
//            Заполняем поля даты и времени
//            Если дата и время не установлены, то ничего не пишем
            if (task.getDate() != 0) {
                taskViewHolder.date.setText(Utils.getFullDate(task.getDate()));
            } else if (task.getOnlyTime() != 0) {
                taskViewHolder.date.setText(Utils.getTime(task.getOnlyTime()));
            } else {
//                Обнуляем текст в поле, для перезаписывания новых значений
// в уже существующие ячейки списка, которые уже не видны пользователю
                taskViewHolder.date.setText(null);
            }
            itemView.setVisibility(View.VISIBLE);

//            Добавим возможность нажатия на задачу только один раз
            taskViewHolder.priority.setEnabled(true);

//            Условие для подсветки просроченых задач
            if (task.getDate() != 0 && task.getDate() < Calendar.getInstance().getTimeInMillis()) {
                itemView.setBackgroundColor(resources.getColor(R.color.gray_200));
            } else {
                itemView.setBackgroundColor(resources.getColor(R.color.gray_50));
            }

//            Установим цвет по умолчанию для заголовка задачи
            taskViewHolder.title.setTextColor(resources.getColor(R.color.primary_text_default_material_light));
//            Установим цвет текста для даты
            taskViewHolder.date.setTextColor(resources.getColor(R.color.secondary_text_default_material_light));
//            Установим цвет текста для дней недели
            taskViewHolder.day.setTextColor(resources.getColor(R.color.secondary_text_default_material_light));
            taskViewHolder.priority.setColorFilter(resources.getColor(task.getPriorityColor()));
//            Установим иконку для пункта списка
            taskViewHolder.priority.setImageResource(R.drawable.ic_checkbox_blank_circle_white_48dp);

//            Cлушатель диалога по клику на задачу в списке
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getTaskFragment().showTaskEditDialog(task);
                }
            });


            //            Добавим слушатель на долгое нажатие
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                    Создадим экземпляр Хэндлера, для срабатывания RippleAnimation до вызова диалога
                    Handler handler = new Handler();
//                    Реализуем задержку
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getTaskFragment().removeTaskDialog(taskViewHolder.getLayoutPosition());
                        }
                    }, 1000);

                    return true;
                }
            });

//            Установим слушатель, при клике на картинку, будет меняться статус задачи
            taskViewHolder.priority.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Чтобы заблокировать многократные нажатия
                    taskViewHolder.priority.setEnabled(false);
                    task.setStatus(ModelTask.STATUS_DONE);

//                    Обновляем статус
                    getTaskFragment()
                            .activity
                            .dbHelper
                            .update()
                            .status(task.getTimeStamp(), ModelTask.STATUS_DONE);

//                    itemView.setBackgroundColor(resources.getColor(R.color.gray_200));

//            Установим цвет по умолчанию для заголовка задачи
                    taskViewHolder.title.setTextColor(resources.getColor(R.color.primary_text_disabled_material_light));
//            Установим цвет текста для даты
                    taskViewHolder.date.setTextColor(resources.getColor(R.color.secondary_text_disabled_material_light));
                    taskViewHolder.priority.setColorFilter(resources.getColor(task.getPriorityColor()));

//                    Добавим анимацию
//                    Поворот картинки по вертикальной оси


                    ObjectAnimator rotationY = ObjectAnimator.ofFloat(taskViewHolder.priority, View.ROTATION_Y, -180f, 0f);

//                    ObjectAnimator flipIn = ObjectAnimator.ofFloat(taskViewHolder.priority, "rotationY", -180f, 0f);
//                    Установим слушатель на аниматора
                    rotationY.addListener(new Animator.AnimatorListener() {
//                    flipIn.addListener(new Animator.AnimatorListener() {


                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (task.getStatus() == ModelTask.STATUS_DONE) {
                                taskViewHolder.priority.setImageResource(R.drawable.ic_check_circle_white_48dp);

//                                Перемещаем элемент списка в сторону на расстояние равной его длине/ширине
                                ObjectAnimator translationX = ObjectAnimator.ofFloat(itemView,
                                        View.ROTATION_X, 0f, itemView.getWidth());
// ObjectAnimator translationX = ObjectAnimator.ofFloat(itemView,
//                                        "translationX", 0f, itemView.getWidth());

//                                Возвращаем элемент списка в исходное состояние
                                ObjectAnimator translationXBack = ObjectAnimator.ofFloat(itemView,
                                        View.TRANSLATION_X, itemView.getWidth(), 0f);
//                                ObjectAnimator translationXBack = ObjectAnimator.ofFloat(itemView,
//                                        "translationX", itemView.getWidth(), 0f);
                                translationX.setDuration(1000);//Задержка анимации вращения

//                                Установим слушателя для translationX
                                translationX.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
//                                        Скрываем удаленный элемент списка
                                        itemView.setVisibility(View.GONE);

                                        getTaskFragment().moveTask(task);
//                                        removeItem(taskViewHolder.getAdapterPosition());//до сравнения
                                        removeItem(taskViewHolder.getLayoutPosition());
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });

//                                Зададим последовательность для анимаций
                                AnimatorSet translationSet = new AnimatorSet();
                                translationSet.play(translationX).before(translationXBack);
                                translationSet.start();
                            }
                            Toast.makeText(getTaskFragment().getActivity(), R.string.moved_to_done, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    rotationY.start();
//                    flipIn.start();

                }
            });
        } else {//когда эелмент не является задачей и значит он разделитель
            ModelSeparator separator = (ModelSeparator) item;
            SeparatorViewHolder separatorViewHolder = (SeparatorViewHolder) viewHolder;
            separatorViewHolder.type.setText(resources.getString(separator.getType()));
        }
    }

    //    В зависимости от позиции возвращает задачу или разделитель
    // (TYPE || SEPARATOR)
    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isTask()) {
            return TYPE_TASK;
        } else {
            return TYPE_SEPARATOR;
        }
    }
}
