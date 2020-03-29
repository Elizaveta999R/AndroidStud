package ru.tpu.lab4.adapter;

import android.graphics.Color;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.tpu.lab4.db.Student;

public class StudentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    public static final int TYPE_NUMBER = 0;
    public static final int TYPE_STUDENT = 1;

    private List<Student> students = new ArrayList<>();

    private List<SpannableString> studentNames = new ArrayList<>();
    private List<String> studentNameSearch = new ArrayList<>();;

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_NUMBER:
                return new NumberHolder(parent);
            case TYPE_STUDENT:
                return new StudentHolder(parent);
        }
        throw new IllegalArgumentException("unknown viewType = " + viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_NUMBER:
                NumberHolder numberHolder = (NumberHolder) holder;
                numberHolder.bind((position + 1) / 2);
                break;
            case TYPE_STUDENT:
                StudentHolder studentHolder = (StudentHolder) holder;
                Student student = students.get(position / 2);

                studentHolder.student.setText(student.lastName + " " + student.firstName
                        + " " + student.secondName);


                break;
        }
    }

    @Override
    public int getItemCount() {
        return students.size() * 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? TYPE_NUMBER : TYPE_STUDENT;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
        for (Student student : students)
        {
            String name = student.lastName + " " + student.firstName + " " + student.secondName;
            studentNames.add(new SpannableString(name));
            studentNameSearch.add(name);
        }
    }





    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<SpannableString> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                for (String s : studentNameSearch)
                {
                    filteredList.add(new SpannableString(s));
                }

            } else {

                for (String name: studentNameSearch) {
                    if (name.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        SpannableString coloredName = new SpannableString(name);
                        int i = 0;
                        int lendth = charSequence.toString().length();
                        while (name.toLowerCase().contains(charSequence.toString().toLowerCase()))
                        {
                            int startIndx = name.toLowerCase().indexOf(charSequence.toString().toLowerCase());


                            BackgroundColorSpan fcs = new BackgroundColorSpan(Color.YELLOW);
                            coloredName.setSpan(fcs,startIndx + i, lendth+startIndx + i, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

                            name = name.toLowerCase().replaceFirst(charSequence.toString().toLowerCase(), "");
                            i+=lendth;
                        }
                        filteredList.add(coloredName);

                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            studentNames.clear();
            studentNames.addAll((Collection<? extends SpannableString>) filterResults.values);
            notifyDataSetChanged();
        }
    };
}

