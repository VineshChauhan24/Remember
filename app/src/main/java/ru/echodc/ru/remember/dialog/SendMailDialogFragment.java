package ru.echodc.ru.remember.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.echodc.ru.remember.R;

public class SendMailDialogFragment extends DialogFragment {
    Intent intent;

    public static SendMailDialogFragment newInstance(String email) {

        SendMailDialogFragment sendMailDialogFragment = new SendMailDialogFragment();

        Bundle args = new Bundle();
        args.putString("email", email);
        sendMailDialogFragment.setArguments(args);
        return sendMailDialogFragment;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View container = inflater.inflate(R.layout.dialog_send_mail, null);

        String email = getArguments().getString("email");
        TextView tvDialogTitle = (TextView) container.findViewById(R.id.tvDialogMailTitle);
        tvDialogTitle.setText(R.string.dialog_send_mail_title);

        //        Получаем элементы EditText через TextInputLayout
        final TextInputLayout tilMailRecipient = (TextInputLayout) container.findViewById(R.id.tilDialogMailRecipient);
        final EditText etMailRecipient = tilMailRecipient.getEditText();

        final TextInputLayout tilMailTitle = (TextInputLayout) container.findViewById(R.id.tilDialogMailTitle);
        final EditText etMailTitle = tilMailTitle.getEditText();

        final TextInputLayout tilMailMessage = (TextInputLayout) container.findViewById(R.id.tilDialogMailMessage);
        final EditText etMailMessage = tilMailMessage.getEditText();

//        Присвоим подсказки
        tilMailRecipient.setHint(getResources().getString(R.string.dialog_mail_recipient));
        tilMailTitle.setHint(getResources().getString(R.string.dialog_mail_title));
        tilMailMessage.setHint(getResources().getString(R.string.dialog_mail_message));
        intent = getActivity().getIntent();
        final String incomingRecipient = intent.getStringExtra("email");
        assert etMailRecipient != null;
        etMailRecipient.setText(email);

//        Передаем все данные в билдер для формирования диалога
        builder.setView(container);

//        присвоим слушатели полям ввода
        assert etMailTitle != null;
        etMailTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                Чтобы избежать накладывания анимации, добавим пробел по нажатию на поле ввода
//                Создадим проверку на длину текста в поле
                if (etMailTitle.length() == 0) {
                    etMailTitle.setText(" ");
                }
            }
        });

        assert etMailMessage != null;
        etMailMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Чтобы избежать накладывания анимации, добавим пробел по нажатию на поле ввода
//                Создадим проверку на длину текста в поле
                if (etMailMessage.length() == 0) {
                    etMailMessage.setText(" ");
                }
            }
        });
        builder.setPositiveButton(R.string.dialog_OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//                Формируем данные для отправки письма
                System.out.println("=========================                               " + etMailRecipient);
//                Формируем данные для вставки в заголовки отправки письма
                System.out.println("Send email >>>>>>>>>>>>>>>>>>>>>>>>>> max");
                String[] TO = {etMailRecipient.getText().toString()};
                String[] CC = {""};
                System.out.println(etMailRecipient.getText().toString());
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                //emailIntent.setData(Uri.fromParts("mailto: ", incomingRecipient, null));
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{etMailRecipient.getText().toString()});
                // emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, etMailTitle.getText().toString());
                emailIntent.putExtra(Intent.EXTRA_TEXT, etMailMessage.getText().toString());

                // startActivity(emailIntent);
                startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.dialog_Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Отменяем диалог
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
                if (etMailTitle.length() == 0) {
//                Заблокируем кнопку ОК для предотвращения создания пустых сообщений
                    positiveButton.setEnabled(false);
                    tilMailTitle.setError(getResources().getString(R.string.dialog_error_empty_mail_title));
                }
                //                Добавим слушатель на событие изменения текста
                etMailTitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        Проверим длину текста
                        if (s.length() == 0) {
//                            Если ноль, блокируем кнопку ОК и отображаем ошибку
                            positiveButton.setEnabled(false);
                            tilMailTitle.setError(getResources().getString(R.string.dialog_error_empty_mail_title));
                        } else {
//                            Если не ноль, активируем кнопку и убираем сообщение об ошибке
                            positiveButton.setEnabled(true);
                            tilMailTitle.setErrorEnabled(false);
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