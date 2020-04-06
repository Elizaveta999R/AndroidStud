package ru.tpu.lab5.Task;

import androidx.annotation.WorkerThread;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import ru.tpu.lab5.Repo;

public class SearchTask extends Task<List<Repo>> {

    String searcheString;
    private static OkHttpClient httpClient;

    public static OkHttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (SearchTask.class) {
                if (httpClient == null) {
                    // Логирование запросов в logcat
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BASIC);
                    httpClient = new OkHttpClient.Builder()
                            .addInterceptor(loggingInterceptor)
                            .build();
                }
            }
        }
        return httpClient;
    }

    public SearchTask(Observer<List<Repo>> observer, String repoString) {
        super(observer);
        this.searcheString = repoString;
    }

    @Override
    @WorkerThread
    protected List<Repo> executeInBackground() throws Exception {

            String response = search("https://api.github.com/search/repositories?q="
                    + searcheString);
            return parseSearch(response);
    }

    private String search(String url) throws Exception {

        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = getHttpClient().newCall(request).execute();
        if (response.code() != 200) {
            throw new Exception("api returned unexpected http code: " + response.code());
        }
        return response.body().string();
    }

    private List<Repo> parseSearch(String response) throws Exception {

        List<Repo> repos = new ArrayList<>();
        JSONObject responseRepo = new JSONObject(response);

        JSONArray items = responseRepo.getJSONArray("items");
        String url;
        for (int i = 0; i < items.length(); i++) {
            JSONObject repoJson = items.getJSONObject(i);
            String fullName = repoJson.getString("full_name");
            String description = repoJson.getString("description");
            url = repoJson.getString("url");
            Repo repo = new Repo(fullName, description, url);
            repos.add(repo);
        }
        return repos;
    }


}

