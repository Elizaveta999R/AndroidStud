package ru.tpu.lab2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Lab2Activity extends AppCompatActivity {

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab2Activity.class);
    }
    private static final String STATE_VIEWS_COUNT = "views_count";
    private EditText PI;
    private EditText RI;

    private Lab2ViewsContainer lab2ViewsContainer;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab2_activity);

        setTitle(getString(R.string.lab2_title, getClass().getSimpleName()));

        // findViewById - generic метод https://docs.oracle.com/javase/tutorial/extra/generics/methods.html,
        // который автоматически кастит (class cast) View в указанный класс.
        // Тип вью, в которую происходит каст, не проверяется, поэтому если здесь указать View,
        // отличную от View в XML, то приложение крашнется на вызове этого метода.
        Button b = findViewById(R.id.btn_add_view);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lab2ViewsContainer = findViewById(R.id.container);
                /*  PI = findViewById(R.id.edittext1) != null ? findViewById(R.id.edittext1).toString()  : "jjj";*/
                PI = findViewById(R.id.edittext1);
                String nnn  = PI.getText().length() == 0? "ggggg": PI.getText().toString();
                RI =findViewById(R.id.edittext2);
                String mmm  = RI.getText().length() == 0? "7": RI.getText().toString();
                Double mmm1 = Double.parseDouble(mmm);
               /* if (nnn.length() == 0)
                {nnn="ggggg";}
                if (mmm.length() == 0)
                {mmm="8";}*/
               /* nnn = nnn != null ? nnn : "dddd";
                mmm = mmm != null ? mmm : "7";*/
               lab2ViewsContainer.incrementViews(nnn,mmm1);
            }
        });



        // Восстанавливаем состояние нашего View, добавляя заново все View
        if (savedInstanceState != null) {
            lab2ViewsContainer.setViewsCount(savedInstanceState.getInt(STATE_VIEWS_COUNT));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_VIEWS_COUNT, lab2ViewsContainer.getViewsCount());
    }
}

