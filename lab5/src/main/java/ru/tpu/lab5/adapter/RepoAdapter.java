package ru.tpu.lab5.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.tpu.lab5.Repo;
import ru.tpu.lab5.ShowRepoActivity;


public class RepoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Repo> repos;

    public RepoAdapter(List<Repo> repos) {

        this.repos = new ArrayList<Repo>();
        this.repos.addAll(repos);
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RepoHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RepoHolder repoHolder = (RepoHolder) holder;
        Repo repo = repos.get(position);
        repoHolder.repo.setText(repo.fullName);
        repoHolder.description.setText(repo.description);
        //отследивание щелчка на RepoHolder
        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                    //По щелчку переходим в новое активити ShowRepoActivity
                                                   Intent intent = new Intent(view.getContext(), ShowRepoActivity.class);
                                                   //Передаем данные о репозитории в активити с ключом Repo
                                                   intent.putExtra("Repo", repo);
                                                   view.getContext().startActivity(intent);

                                               }
                                           }

        );
    }

    @Override
    public int getItemCount() {
        return repos.size();
    }

    public List<Repo> getItems(){
        return repos;
    }



}

