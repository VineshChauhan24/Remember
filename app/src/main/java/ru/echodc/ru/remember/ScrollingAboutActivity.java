package ru.echodc.ru.remember;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

public class ScrollingAboutActivity extends MainActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    ImageView vklinkMax;
    ImageView vklinkYarik;
    ImageView fblinkMax;
    ImageView fblinkYarik;

    CheckBox checkBoxMax;
    CheckBox checkBoxYarik;


//    Bundle args;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        vklinkMax = (ImageView) findViewById(R.id.vklinkMax);
        vklinkYarik = (ImageView) findViewById(R.id.vklinkYarik);
        fblinkMax = (ImageView) findViewById(R.id.fblinkMax);
        fblinkYarik = (ImageView) findViewById(R.id.fblinkYarik);

        vklinkMax.setOnClickListener(this);
        vklinkYarik.setOnClickListener(this);
        fblinkMax.setOnClickListener(this);
        fblinkYarik.setOnClickListener(this);

        checkBoxMax = (CheckBox) findViewById(R.id.chbMax);
        checkBoxYarik = (CheckBox) findViewById(R.id.chbYarik);

        checkBoxMax.setOnCheckedChangeListener(this);
        checkBoxYarik.setOnCheckedChangeListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabMail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subject = "";
                String body = "";
                if (checkBoxMax.isChecked()) {
                    String recipient = getApplicationContext().getString(R.string.max_email);
                    sendEmail(recipient, subject, body);
//                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  email MAX");
//                    startMailDialog(getApplicationContext().getString(R.string.max_email));

                } else if (checkBoxYarik.isChecked()) {
                    String recipient = getApplicationContext().getString(R.string.yarik_email);
                    sendEmail(recipient, subject, body);
//                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> email Yarik");
//                    startMailDialog(getApplicationContext().getString(R.string.yarik_email));

                } else {
                    Snackbar.make(view, R.string.select_the_recipient, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    //    Стартуем диалог для отправки письма
//    public void startMailDialog(String email) {
//        DialogFragment sendMailDialogFragment = SendMailDialogFragment.newInstance(email);
//        sendMailDialogFragment.show(mFragmentManager, "SendMailDialogFragment");
//    }

    //    При клике по социальным значкам, переходим по адресу
    @Override
    public void onClick(View v) {
        Intent intent;
        Uri address;
        switch (v.getId()) {
            case R.id.vklinkMax:
                address = Uri.parse("https://vk.com/ciklodol");
                intent = new Intent(Intent.ACTION_VIEW, address);
                startActivity(intent);
                break;
            case R.id.vklinkYarik:
                address = Uri.parse("https://vk.com/id11633052");
                intent = new Intent(Intent.ACTION_VIEW, address);
                startActivity(intent);
                break;
            case R.id.fblinkMax:
                address = Uri.parse("https://www.facebook.com/bagryanetzz");
                intent = new Intent(Intent.ACTION_VIEW, address);
                startActivity(intent);
                break;
            case R.id.fblinkYarik:
                address = Uri.parse("https://www.facebook.com/profile.php?id=100006148275102");
                intent = new Intent(Intent.ACTION_VIEW, address);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

//    Метод для посылки письма через выбор почтового клиента
    public void sendEmail(String recipient, String subject, String body) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, body);
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, getResources().getText(R.string.select_email_client)));
    }
}
