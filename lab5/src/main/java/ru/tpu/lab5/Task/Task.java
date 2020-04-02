package ru.tpu.lab5.Task;

import android.os.Handler;
import android.os.Looper;
import android.os.Process;

public abstract class Task<T> implements Runnable {



    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private Observer<T> observer;



    public Task(Observer<T> observer) {

        this.observer = observer;

    }


//Поток стартовал
    @Override
    public final void run() {

        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
//Передаем в главный поток данные
        mainHandler.post(new Runnable() {

            @Override

            public void run() {

                if (observer != null) {
//Здесь передаются данные в метод onLoading интерфейса Observer,
// а получаем данные через этот интерфейс в главном потоке в Lab5Activity
// searchObserver метод onLoading
                    observer.onLoading(Task.this);

                }

            }

        });

        try {
//Пытаемся получить данные с помощью функции executeInBackground()
            //Реализация этой функции находится в SearchTask
            final T data = executeInBackground();

            mainHandler.post(new Runnable() {

                @Override

                public void run() {

                    if (observer != null) {
//Здесь передаются данные в метод onSuccess интерфейса Observer,
// а получаем данные через этот интерфейс в главном потоке в Lab5Activity
// searchObserver метод onSuccess
                        observer.onSuccess(Task.this, data);

                    }

                }

            });
//Отслеживаем ошибку и передаем её в метод onError
        } catch (final Exception e) {

            mainHandler.post(new Runnable() {

                @Override

                public void run() {

                    if (observer != null) {

                        observer.onError(Task.this, e);

                    }

                }

            });

        }

    }



    protected abstract T executeInBackground() throws Exception;



    public void unregisterObserver() {

        observer = null;

    }

}

