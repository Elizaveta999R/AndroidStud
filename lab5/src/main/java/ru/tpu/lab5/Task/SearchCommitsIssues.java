package ru.tpu.lab5.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchCommitsIssues extends Task<List<List<String>>> {
    private String url;
    public SearchCommitsIssues(Observer<List<List<String>>> observer, String url) {
        super(observer);
        this.url = url;
    }
    @Override
    protected List<List<String>> executeInBackground() throws Exception {
        //Формируем ссылки для получения Ишьюз и комитов и передаем их
        String responseRepo = search(url);
        String responseCommits = search(url+"/commits");
        String responseIssues = search(url+"/issues");
        //преобразуем ответ из формата JSON в лист из объектов Repo
        List<String> parseCommits = parseCommits(responseCommits);
        List<String> parseIssues = parseIssues(responseIssues);
        List<String> parseRepo = parseRepo(responseRepo);
        //Список из списков. Будет содержать 3 списка - ишьюз, коммитов и инфа (название, описание)
        List<List<String>> c = new ArrayList<>();
        c.add(parseCommits);
        c.add(parseIssues);
        c.add(parseRepo);

        return c;
    }
    private List<String> parseCommits(String arr)throws Exception{
        JSONArray Commits = new JSONArray(arr);
        List<String> RepoCommits = new ArrayList<>();
        for (int j = 0; j < Commits.length(); j++) {
            JSONObject commitJson = Commits.getJSONObject(j);
            //Вытаскиваем из полученного объекта сообщения комитов и добавляем в лист
            RepoCommits.add(commitJson.getJSONObject("commit").getString("message"));
        }
        return RepoCommits;
    }
    private List<String> parseIssues(String arr) throws Exception {
        JSONArray Issues = new JSONArray(arr);
        List<String> RepoIssues = new ArrayList<>();
        for (int j = 0; j < Issues.length(); j++) {
            JSONObject issueJson = Issues.getJSONObject(j);
            //Вытаскиваем из полученного объекта заголовки ишьюз и добавляем в лист
            RepoIssues.add(issueJson.getString("title"));
        }
        return RepoIssues;
    }
    private List<String> parseRepo(String response) throws Exception {

        List<String> repo = new ArrayList<>();
        JSONObject Repo = new JSONObject(response);
        //получаем массив элементов

        //Из полученного JSON-объекта вычленяем имя репозитория и его описпние
        String fullName = Repo.getString("full_name");
        String description = Repo.getString("description");
        //в полученом JSON-объекте указана ссылки на него.
        repo.add(fullName);
        repo.add(description);
        return repo;
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
}
