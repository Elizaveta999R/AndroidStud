package ru.tpu.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import ru.tpu.laba1.Laba1Activity;
import ru.tpu.lab2.Lab2Activity;
import ru.tpu.lab3.Lab3Activity;
import ru.tpu.lab4.Lab4Activity;
import ru.tpu.lab5.Lab5Activity;
import ru.tpu.lab6.Lab6Activity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Переопределяя методы жизненного цикла Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.laba1).setOnClickListener((v) -> startActivity(Laba1Activity.newIntent(this)));
        findViewById(R.id.lab2).setOnClickListener((v) -> startActivity(Lab2Activity.newIntent(this)));
        findViewById(R.id.lab3).setOnClickListener((v) -> startActivity(Lab3Activity.newIntent(this)));
        findViewById(R.id.lab4).setOnClickListener((v) -> startActivity(Lab4Activity.newIntent(this)));
        findViewById(R.id.lab5).setOnClickListener((v) -> startActivity(Lab5Activity.newIntent(this)));
        findViewById(R.id.lab6).setOnClickListener((v) -> startActivity(Lab6Activity.newIntent(this)));
    }
}
