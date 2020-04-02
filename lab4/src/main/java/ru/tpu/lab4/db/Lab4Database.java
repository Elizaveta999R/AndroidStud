package ru.tpu.lab4.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Student.class, version = 1, exportSchema = false)
public abstract class Lab4Database extends RoomDatabase {

    private static Lab4Database db;

    /**
     * Синглетон, в котором происходит инициализация и настройка самой БД. Используем синглетон,
     * т.к. инстанс класса БД должен быть только один (т.ё. в нём происходит управление
     * соединением к БД).
     */
    @NonNull
    public static Lab4Database getInstance(@NonNull Context context) {
        if (db == null) {
            synchronized (Lab4Database.class) {
                if (db == null) {
                    db = Room.databaseBuilder(
                            context.getApplicationContext(),
                            Lab4Database.class,
                            "lab4_database"
                    ).allowMainThreadQueries()
                            .build();
                }
            }
        }
        return db;
    }

    public abstract StudentDao studentDao();
}
