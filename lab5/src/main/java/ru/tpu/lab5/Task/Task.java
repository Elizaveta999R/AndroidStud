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


    @Override
    public final void run() {

        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        mainHandler.post(new Runnable() {

            @Override

            public void run() {

                if (observer != null) {
                    observer.onLoading(Task.this);

                }

            }

        });

        try {
            final T data = executeInBackground();

            mainHandler.post(new Runnable() {

                @Override

                public void run() {

                    if (observer != null) {
                        observer.onSuccess(Task.this, data);

                    }

                }

            });
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

