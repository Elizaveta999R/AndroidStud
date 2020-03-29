package ru.tpu.lab3.adapter;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.tpu.lab3.Student;


/**
 * Задача Адаптера - управление View, которые содержатся в RecyclerView, с учётом его жизненного цикла.
 * Адаптер работает не с View, а с {@link RecyclerView.ViewHolder}. Этот класс содержит не только
 * View, которая будет показана на экране, но и дополнительную информацию, вроде позиции элемента
 * в списке.
 * <p>
 * Сначала мы переопределяем метод {@link #getItemCount()}. В нём необходимо вернуть количество
 * элементов в списке. В нашем случае это количество студентов помноженное на 2, т.к. на каждого
 * студента идёт 2 отдельных View. Одна - с номером студента, другая - с его ФИО.
 * <p>
 * Т.к. у нас идёт 2 разных типа View, то мы переопределяем метод {@link #getItemViewType(int)},
 * в котором должны вернуть номер типа View для переданной в методе позиции списка.
 * <p>
 * В методе {@link #onCreateViewHolder(ViewGroup, int)} мы создаём ViewHolder для
 * соответствующего типа View. Здесь мы производим инфлейт View из XML и ищем нужные нам View
 * в их иерархии.
 * <p>
 * В методе {@link #onBindViewHolder(RecyclerView.ViewHolder, int)} мы описываем заполнение
 * ViewHolder-а данными, соответствующими переданной нам позиции.
 * <p>
 * Когда мы только вызвали {@link RecyclerView#setAdapter(RecyclerView.Adapter)}, согласно алгоритму
 * лэйаута описанному в LayoutManager, RecyclerView начинает вызывать методы адаптера,
 * чтобы расположить столько элементов, сколько помещается на экране.
 * Для каждого такого элемента вызывается сначала getItemViewType, с полученным itemViewType
 * вызывается метод onCreateViewHolder и созданный viewHolder передаётся в onBindViewHolder для заполнения
 * данными. После этого измеряются размеры элемента и добавляются в RecyclerView. Как только мы вышли
 * за пределы размеров RecyclerView, процесс останавливается. При скролле списка вниз, верхние
 * ViewHolder, которые стали не видны, открепляются от RecyclerView и добавляются в
 * {@link RecyclerView.RecycledViewPool}. Снизу же, когда появляется пустое пространство, в
 * RecyclerViewPool ищется ViewHolder для viewType следующего элемента. Если такой найден, то для него
 * вызывается onBindViewHolder и ViewHolder добавляется снизу.
 * <p>
 * Для того, чтобы сказать RecyclerView, что список был обновлён, используются методы
 * {@link #notifyDataSetChanged()}, {@link #notifyItemInserted(int)} и т.д. notifyDataSetChanged
 * обновляет весь список, а остальные методы notify... говорят об изменении конкретного элемента и
 * что с ним произошло, что позволяет делать анимированные изменения в списке. При этом RecyclerView
 * всё также будет работать с теми же закэшированными ViewHolder и не будет пересоздавать все View.
 */
public class StudentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    public static final int TYPE_NUMBER = 0;
    public static final int TYPE_STUDENT = 1;

    //private List<Student> students = new ArrayList<>();
    private List<SpannableString> studentNames = new ArrayList<>();
    private List<String> studentNameSearch;
   /* public StudentsAdapter(List<Student> students) {
        studentNames.clear();
        for (Student student : students)
        {
            studentNames.add(student.lastName + " " + student.firstName + " " + student.secondName);
        }
        studentNameSearch.addAll(studentNames);
    }*/
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
                // Высчитыванием номер студента начиная с 1
                numberHolder.bind((position + 1) / 2);
                break;
            case TYPE_STUDENT:
                StudentHolder studentHolder = (StudentHolder) holder;
               // Student student = students.get(position / 2);
                SpannableString studentName = studentNames.get(position / 2);
               // studentNames.add(student.lastName + " " + student.firstName + " " + student.secondName);
                studentHolder.student.setText(
                        //student.lastName + " " + student.firstName + " " + student.secondName
                        studentName
                );
                break;
        }
    }

    @Override
    public int getItemCount() {
        return studentNames.size()*2;//students.size() * 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? TYPE_NUMBER : TYPE_STUDENT;
    }

    public void setStudents(List<Student> students) {
       // this.students = students;
        studentNames = new ArrayList<>();
        studentNameSearch = new ArrayList<>();
        for (Student student : students)
        {
            String name = student.lastName + " " + student.firstName + " " + student.secondName;
            studentNames.add(new SpannableString(name));
            studentNameSearch.add(name);
        }

    }

    @Override
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
