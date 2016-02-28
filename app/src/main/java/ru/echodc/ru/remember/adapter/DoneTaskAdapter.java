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

import de.hdodenhof.circleimageview.CircleImageView;
import ru.echodc.ru.remember.R;
import ru.echodc.ru.remember.Utils;
import ru.echodc.ru.remember.fragment.DoneTaskFragment;
import ru.echodc.ru.remember.model.Item;
import ru.echodc.ru.remember.model.ModelTask;

public class DoneTaskAdapter extends TaskAdapter {


    public DoneTaskAdapter(DoneTaskFragment taskFragment) {
        super(taskFragment);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.model_task, viewGroup, false);
        TextView title = (TextView) v.findViewById(R.id.tvTaskTitle);
        TextView date = (TextView) v.findViewById(R.id.tvTaskDate);
        CircleImageView priority = (CircleImageView) v.findViewById(R.id.cvTaskPriority);
        TextView day = (TextView) v.findViewById(R.id.tvDays);

        return new TaskViewHolder(v, title, date, priority, day);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Item item = items.get(position);
//        Если элемент является задачей
        if (item.isTask()) {
//            Активируем возможность нажатия на задачу
            viewHolder.itemView.setEnabled(true);
            final ModelTask task = (ModelTask) item;
            final TaskViewHolder taskViewHolder = (TaskViewHolder) viewHolder;

//            Для удобства манипулирования идентификаторами вью элементов
            final View itemView = taskViewHolder.itemView;
            final Resources resources = itemView.getResources();

//            Установим дни недели
            taskViewHolder.day.setText(Utils.getDayName(context, task.getDay()));

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
//                  в уже существующие яцчейки списка, которые уже не видны пользователю
                taskViewHolder.date.setText(null);
            }

            itemView.setVisibility(View.VISIBLE);

//            Добавим возможность нажатия на задачу только один раз
            taskViewHolder.priority.setEnabled(true);

//            itemView.setBackgroundColor(resources.getColor(R.color.gray_200));//цвет текста выполненных задач

//            Установим цвет по умолчанию для заголовка задачи
            taskViewHolder.title.setTextColor(resources.getColor(R.color.primary_text_disabled_material_light));

//            Установим цвет текста для даты
            taskViewHolder.date.setTextColor(resources.getColor(R.color.secondary_text_disabled_material_light));

//            Установим цвет текста для дней недели
            taskViewHolder.day.setTextColor(resources.getColor(R.color.secondary_text_disabled_material_light));

            taskViewHolder.priority.setColorFilter(resources.getColor(task.getPriorityColor()));

//            Установим иконку для пункта списка
            taskViewHolder.priority.setImageResource(R.drawable.ic_check_circle_white_48dp);

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
                    task.setStatus(ModelTask.STATUS_CURRENT);

//                    Обновляем статус
                    getTaskFragment()
                            .activity
                            .dbHelper
                            .update()
                            .status(task.getTimeStamp(), ModelTask.STATUS_CURRENT);

//                    itemView.setBackgroundColor(resources.getColor(R.color.gray_50));

//            Установим цвет по умолчанию для заголовка задачи
                    taskViewHolder.title.setTextColor(resources.getColor(R.color.primary_text_default_material_light));
//            Установим цвет текста для даты
                    taskViewHolder.date.setTextColor(resources.getColor(R.color.secondary_text_default_material_light));
                    taskViewHolder.priority.setColorFilter(resources.getColor(task.getPriorityColor()));

//                    Добавим анимацию
//                    Поворот картинки по вертикальной оси
                    ObjectAnimator flipIn = ObjectAnimator.ofFloat(taskViewHolder.priority, View.ROTATION_Y, 180f, 0f);
//                    ObjectAnimator flipIn = ObjectAnimator.ofFloat(taskViewHolder.priority, "rotationY", 180f, 0f);
//                    Ставим анимацию по клику, а не по окончанию
                    taskViewHolder.priority.setImageResource(R.drawable.ic_checkbox_blank_circle_white_48dp);
//                    Установим слушатель на аниматора
                    flipIn.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (task.getStatus() != ModelTask.STATUS_DONE) {

//                                Перемещаем элемент списка в другую сторону на расстояние равной его длине/ширине
                                ObjectAnimator translationX = ObjectAnimator.ofFloat(itemView,
                                        View.ROTATION_Y, 0f, -itemView.getWidth());
//                              ObjectAnimator translationX = ObjectAnimator.ofFloat(itemView,
//                                        "translationX", 0f, -itemView.getWidth());

//                                Возвращаем элемент списка в исходное состояние
                                ObjectAnimator translationXBack = ObjectAnimator.ofFloat(itemView,
                                        View.TRANSLATION_X, -itemView.getWidth(), 0f);

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
//                                translationSet.play(translationX).before(translationXBack);
                                translationSet.start();
                            }
                            Toast.makeText(getTaskFragment().getActivity(), R.string.moved_to_current, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    flipIn.start();
                }
            });
        }
    }
}
