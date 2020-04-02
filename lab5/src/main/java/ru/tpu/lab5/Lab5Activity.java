package ru.tpu.lab5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import ru.tpu.lab5.Task.Observer;
import ru.tpu.lab5.Task.SearchTask;
import ru.tpu.lab5.Task.Task;
import ru.tpu.lab5.adapter.RepoAdapter;

//Все, что выполняется здесь, выполняется в главном потоке
//Запросы мы делаем отдельным потоком
//Между потоками нет связи. Мы делаем эту связь с помощью паттерна наблюдатель
//Главный поток как бы подписывается на методы фонового потока, указаные в классе "Observer"
//В AndroidManifest добавлена строчка для разрешения подключения к интернету
//В файле buld.gradle добавлены несколько зависимостей
public class Lab5Activity extends AppCompatActivity {

    private static final String TAG = Lab5Activity.class.getSimpleName();
    private ProgressBar progressBar;
    private RepoAdapter repoAdapter;
    private Button button;

    private RecyclerView list;
    boolean isLoading = false;
    LinearLayoutManager layoutManager;
    private SearchTask task;
    private String CurrString = "";
   // List<Repo> reposList = new ArrayList<>();

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab5Activity.class);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab5_activity);
        // Метод setTitle используется для задания заголовка в тулбаре
        // Метод getString с параметрами достаёт строку из ресурсов и форматирует её по правилам
        // https://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html
        setTitle(getString(R.string.lab5_title, getClass().getSimpleName()));

        list = findViewById(R.id.ReposList);

        progressBar = findViewById(R.id.loading);
        layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        button = findViewById(R.id.RefreshButton);
        //Востановление состояния recyclerView, если есть, что востанавливать)
        if(savedInstanceState != null)
        {
            list.setAdapter(repoAdapter = new RepoAdapter(savedInstanceState.getParcelableArrayList("repos")));
            list.scrollToPosition(savedInstanceState.getInt("position"));
        }

        //обработка кнопки, которая отображается при ошибке. Просто так её не видно
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //формируем данные для передачи в поток - экземпляр класса SearchTask,
                //в который передаем полученную текущую строку поиска
                task = new SearchTask(searchObserver, CurrString);
                //создаем поток и передаем в него даные
                //после создания потока выполняется код в классе Task.
                new Thread(task).start();
            }});

    }
    @Override

    protected void onDestroy() {
        super.onDestroy();
        if(task!=null) {
            task.unregisterObserver();
        }
    }
    //сохранение состояния: сохраняем данные листа и позицию скрола
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("repos", new ArrayList<>(repoAdapter.getItems()));
        outState.putInt("position", layoutManager.findFirstVisibleItemPosition());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lab5_search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_repos);
        SearchView searchView = (SearchView) menuItem.getActionView();
        //обработка searchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            //если текст был изменен
            @Override
            public boolean onQueryTextChange(final String s) {

                if (s.length() > 2) {
                    CurrString = s;
                    Log.d(TAG, "textChanged");
                    progressBar.setVisibility(View.VISIBLE);

                    task = new SearchTask(searchObserver, s);
                    //создаем поток и передаем в него данные
                    new Thread(task).start();
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    //реализация интерфейся Observer
    private Observer<List<Repo>> searchObserver = new Observer<List<Repo>>() {

        @Override
        public void onLoading(Task<List<Repo>> task) {

            //Логирование, чтобы можно было без отладки посмотреть
            //что происходит в приложении на вкадке Logcat
            Log.d(TAG, "onLoading");
        }



        @Override

        public void onSuccess(Task<List<Repo>> task, List<Repo> data) {
//получаем данные из фонового потока, который выполнял запрос и получал ответ
            Log.d(TAG, "onSuccess");

            list.setAdapter(repoAdapter = new RepoAdapter(data));

            progressBar.setVisibility(View.INVISIBLE);


        }

        @Override
        public void onError(Task<List<Repo>> task, Exception e) {

            Log.d(TAG, "onError");
            progressBar.setVisibility(View.INVISIBLE);
            button.setVisibility(View.VISIBLE);
        }

    };

}


