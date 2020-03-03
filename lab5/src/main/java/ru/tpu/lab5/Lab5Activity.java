package ru.tpu.lab5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Lab5Activity extends AppCompatActivity {

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab5Activity.class);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Метод setTitle используется для задания заголовка в тулбаре
        // Метод getString с параметрами достаёт строку из ресурсов и форматирует её по правилам
        // https://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html
        setTitle(getString(R.string.lab5_title, getClass().getSimpleName()));
    }
}
