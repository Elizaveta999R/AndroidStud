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

import java.util.ArrayList;

public class Lab2Activity extends AppCompatActivity {

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab2Activity.class);
    }
    private static final String STATE_VIEWS_COUNT = "views_count";
    private EditText title;
    private EditText score;

    private Lab2ViewsContainer lab2ViewsContainer;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lab2_activity);

        setTitle(getString(R.string.lab2_title, getClass().getSimpleName()));
        lab2ViewsContainer = findViewById(R.id.container);
        Button b = findViewById(R.id.btn_add_view);
        b.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View view) {


                title = findViewById(R.id.title);
                String titleText  = title.getText().length() == 0? "7.5": title.getText().toString();
                score =findViewById(R.id.score);
                String scoreText  = score.getText().length() == 0? "Звук": score.getText().toString();

               lab2ViewsContainer.incrementViews(titleText,scoreText);
            }
        });



        // Восстанавливаем состояние нашего View, добавляя заново все View
        if (savedInstanceState != null) {

            ArrayList<ProgressBar> pgList = savedInstanceState.getParcelableArrayList(STATE_VIEWS_COUNT);
            for (int i = 0; i < pgList.size(); i++) {
                ProgressBar pg = pgList.get(i);
               lab2ViewsContainer.incrementViews(pg.title, pg.score);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_VIEWS_COUNT, lab2ViewsContainer.getProgressBars());
    }
}

