package ru.tpu.lab5.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.tpu.lab5.Repo;

public class SearchTask extends Task<List<Repo>> {

    String searcheString;
    public SearchTask(Observer<List<Repo>> observer, String repoString) {
        super(observer);
        this.searcheString = repoString;
    }

    @Override
    protected List<Repo> executeInBackground() throws Exception {
        //Запрашиваем данные о репозитотриях, названия которых содержат строку searchString
            String response = search("https://api.github.com/search/repositories?q=" + searcheString);
            return parseSearch(response);

    }

    private String search(String url) throws IOException {

        OkHttpClient client = new OkHttpClient();
        //запрос
        Request request = new Request.Builder()
                .url(url)
                .build();
        //ответ
        Response response = client.newCall(request).execute();
        //возращаем тело ответа
        return response.body().string();
    }

    //преобразуем ответ из формата JSON в лист из объектов Repo
    private List<Repo> parseSearch(String response) throws Exception {

        List<Repo> repos = new ArrayList<>();
        JSONObject responseRepo = new JSONObject(response);
        //получаем массив элементов
        JSONArray items = responseRepo.getJSONArray("items");
        String url;
        for (int i = 0; i < items.length(); i++) {
            JSONObject repoJson = items.getJSONObject(i);
            //Из полученного JSON-объекта вычленяем имя репозитория и его описпние
            String fullName = repoJson.getString("full_name");
            String description = repoJson.getString("description");
            //в полученом JSON-объекте указана ссылки на него.
            //Будем использовать её для получение коммитов и ишьюз в классе SearchCommitsIssues
            url = repoJson.getString("url");
            //Создем объект репозитория и сразу его запрлняем
            Repo repo = new Repo(fullName, description, url);
            repos.add(repo);
        }
        return repos;
    }


}

