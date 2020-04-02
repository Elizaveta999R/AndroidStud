
package ru.tpu.lab5.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ru.tpu.lab5.R;

public class RepoHolder extends RecyclerView.ViewHolder {

    public final TextView repo;
    public final TextView description;

    public RepoHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.lab5_item_repo, parent, false));
        repo = itemView.findViewById(R.id.repoName);
        description = itemView.findViewById(R.id.repoDescription);

    }
}

