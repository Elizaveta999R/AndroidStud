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

public class SearchCommitsIssues extends Task<List<List<String>>> {
    private String url;

    public SearchCommitsIssues(Observer<List<List<String>>> observer, String url) {
        super(observer);
        this.url = url;
    }

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

    @Override
    @WorkerThread
    protected List<List<String>> executeInBackground() throws Exception {
        String responseRepo = search(url);
        String responseCommits = search(url + "/commits");
        String responseIssues = search(url + "/issues");
        List<String> parseCommits = parseCommits(responseCommits);
        List<String> parseIssues = parseIssues(responseIssues);
        List<String> parseRepo = parseRepo(responseRepo);
        List<List<String>> c = new ArrayList<>();
        c.add(parseCommits);
        c.add(parseIssues);
        c.add(parseRepo);

        return c;
    }

    private List<String> parseCommits(String arr) throws Exception {
        JSONArray Commits = new JSONArray(arr);
        List<String> RepoCommits = new ArrayList<>();
        for (int j = 0; j < Commits.length(); j++) {
            JSONObject commitJson = Commits.getJSONObject(j);
            RepoCommits.add(commitJson.getJSONObject("commit").getString("message"));
        }
        return RepoCommits;
    }

    private List<String> parseIssues(String arr) throws Exception {
        JSONArray Issues = new JSONArray(arr);
        List<String> RepoIssues = new ArrayList<>();
        for (int j = 0; j < Issues.length(); j++) {
            JSONObject issueJson = Issues.getJSONObject(j);
            RepoIssues.add(issueJson.getString("title"));
        }
        return RepoIssues;
    }

    private List<String> parseRepo(String response) throws Exception {

        List<String> repo = new ArrayList<>();
        JSONObject Repo = new JSONObject(response);

        String fullName = Repo.getString("full_name");
        String description = Repo.getString("description");

        repo.add(fullName);
        repo.add(description);
        return repo;
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
}
