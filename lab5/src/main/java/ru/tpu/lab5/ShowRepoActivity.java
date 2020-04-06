package ru.tpu.lab5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ru.tpu.lab5.Task.Observer;
import ru.tpu.lab5.Task.SearchCommitsIssues;
import ru.tpu.lab5.Task.Task;

public class ShowRepoActivity extends AppCompatActivity {

    public static Intent newIntent(@NonNull Context context) {

        return new Intent(context, ShowRepoActivity.class);
    }

    private SearchCommitsIssues task;
    private Repo repo;
    private TextView descriptionView;
    private TextView descrLabel;
    private TextView comLabel;
    private TextView issLabel;
    private LinearLayout commitsView;
    private LinearLayout issuesView;
    private ProgressBar progressBar;
    private Button button;
    private TextView tw;
    private SwipeRefreshLayout swipeContainer;


    private static Executor threadExecutor = Executors.newCachedThreadPool();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab5_activity_show_repo);
        descriptionView = findViewById(R.id.lab5_repoDescription);
        commitsView = findViewById(R.id.repoCommits);
        issuesView = findViewById(R.id.repoIssues);
        descrLabel = findViewById(R.id.lab5_descriptionLabel);
        comLabel = findViewById(R.id.lab5_commitsLabel);
        issLabel = findViewById(R.id.lab5_issuesLabel);
        progressBar = findViewById(R.id.loading);
        button = findViewById(R.id.RefreshButton);

        Bundle arguments = getIntent().getExtras();
        tw = new TextView(this);
        if (arguments != null) {

            repo = (Repo) arguments.get("Repo");

            setTitle(repo.fullName);
            task = new SearchCommitsIssues(searchObserver, repo.url);
            threadExecutor.execute(task);
        }

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                task = new SearchCommitsIssues(searchObserver, repo.url);
                threadExecutor.execute(task);
                swipeContainer.setRefreshing(false);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task = new SearchCommitsIssues(searchObserver, repo.url);

                threadExecutor.execute(task);
            }
        });

    }

    private Observer<List<List<String>>> searchObserver = new Observer<List<List<String>>>() {

        @Override
        public void onLoading(Task<List<List<String>>> task) {
            progressBar.setVisibility(View.VISIBLE);
            button.setVisibility(View.INVISIBLE);
            descrLabel.setText("");
            comLabel.setText("");
            issLabel.setText("");
            descriptionView.setText("");
            issuesView.removeAllViews();
            commitsView.removeAllViews();
        }


        @Override

        public void onSuccess(Task<List<List<String>>> task, List<List<String>> data) {

            for (String commit : data.get(0)) {
                TextView textView = new TextView(tw.getContext());
                textView.setText(commit);

                commitsView.addView(textView);
            }
            for (String issue : data.get(1)) {
                TextView textView = new TextView(tw.getContext());
                textView.setText(issue);

                issuesView.addView(textView);
            }
            setTitle(data.get(2).get(0));
            descriptionView.setText(data.get(2).get(1));
            descrLabel.setText("descripton:");
            if (data.get(0).size() > 0) comLabel.setText("commits:");
            if (data.get(1).size() > 0) issLabel.setText("issues:");
            progressBar.setVisibility(View.INVISIBLE);


        }

        @Override
        public void onError(Task<List<List<String>>> task, Exception e) {
            progressBar.setVisibility(View.INVISIBLE);
            button.setVisibility(View.VISIBLE);
        }

    };
}
