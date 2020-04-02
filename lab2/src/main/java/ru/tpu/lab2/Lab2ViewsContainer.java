package ru.tpu.lab2;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class Lab2ViewsContainer extends RelativeLayout {

    private int minViewsCount;
    private int viewsCount;
    private int layoutId = 1;

    private static final int IDS_DIFFERENCE = 1000000;

    ArrayList<ru.tpu.lab2.ProgressBar> pgList = new ArrayList<>();


    /**
     * Этот конструктор используется при создании View в коде.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Lab2ViewsContainer(Context context) {
        this(context, null);
    }

    /**
     * Этот конструктор выдывается при создании View из XML.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Lab2ViewsContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Lab2ViewsContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // Свои атрибуты описываются в файле res/values/attrs.xml
        // Эта строчка объединяет возможные применённые к View стили
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Lab2ViewsContainer, defStyleAttr, 0);

        minViewsCount = a.getInt(R.styleable.Lab2ViewsContainer_lab2_minViewsCount, 0);
        if (minViewsCount < 0) {
            throw new IllegalArgumentException("minViewsCount can't be less than 0");
        }

        // Полученный TypedArray необходимо обязательно очистить.
        a.recycle();

        if (viewsCount < minViewsCount) {
            for (int i = 0; i < minViewsCount; i++) {
                incrementViews("Показатель", "7.3");
            }
        }

    }


    public void incrementViews(String title, String score) {
        if (viewsCount >= minViewsCount) {
            ru.tpu.lab2.ProgressBar pg = new ru.tpu.lab2.ProgressBar(title, score);
            pgList.add(pg);
        }

        viewsCount++;

        LinearLayout progressLayout = new LinearLayout(getContext());
        progressLayout.setOrientation(LinearLayout.HORIZONTAL);
        progressLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        progressLayout.setId(layoutId);


        ProgressBar progressBar = new ProgressBar(getContext(), null,
                android.R.attr.progressBarStyleHorizontal);

        int tempI = (int) (Double.parseDouble(score) * 10.0);


        progressBar.setProgress(tempI);


        TextView progressTitleView = new TextView(getContext());
        progressTitleView.setTextSize(16);
        progressTitleView.setText(title);


        TextView progressScoreView = new TextView(getContext());
        progressScoreView.setTextSize(16);
        progressScoreView.setText(score);
        progressScoreView.setId(layoutId + IDS_DIFFERENCE);
        LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 2.0f);
        LinearLayout.LayoutParams progressBarLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 2.0f);
        LinearLayout.LayoutParams ScoreLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
        ScoreLayoutParams.leftMargin = 70;

        progressTitleView.setLayoutParams(titleLayoutParams);
        progressBar.setLayoutParams(progressBarLayoutParams);
        progressScoreView.setLayoutParams(ScoreLayoutParams);

        progressLayout.addView(progressTitleView);
        progressLayout.addView(progressBar);
        progressLayout.addView(progressScoreView);

        LayoutParams relativeParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );

        TextView lessScoreTexView = null;
        TextView moreScoreTextView = null;
        double currLessScoreValue = 0;
        double currMoreScoreValue = 10;

        Double scoreValue = Double.parseDouble(score);
        for (int i = 1; i < layoutId; i++) {
            TextView currScoreView = findViewById(i + IDS_DIFFERENCE);
            CharSequence s = currScoreView.getText();
            Double currScoreValue = Double.parseDouble(s.toString());
            if (scoreValue > currScoreValue && currLessScoreValue < currScoreValue) {
                currLessScoreValue = currScoreValue;
                lessScoreTexView = currScoreView;
            }
            if (scoreValue <= currScoreValue && currMoreScoreValue >= currScoreValue) {
                currMoreScoreValue = currScoreValue;
                moreScoreTextView = currScoreView;
            }
        }
        LayoutParams relativeParams2 = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );
        if (moreScoreTextView != null)
            relativeParams.addRule(RelativeLayout.BELOW, moreScoreTextView.getId() - IDS_DIFFERENCE);


        addView(progressLayout, relativeParams);
        if (lessScoreTexView != null) {
            relativeParams2.addRule(RelativeLayout.BELOW, layoutId);
            findViewById(lessScoreTexView.getId() - IDS_DIFFERENCE).setLayoutParams(relativeParams2);
        }

        layoutId++;

    }

    public ArrayList<ru.tpu.lab2.ProgressBar> getProgressBars() {
        return pgList;
    }
}

