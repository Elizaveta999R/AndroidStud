package ru.tpu.lab2;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Parcelable;
import android.renderscript.Sampler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;


import androidx.annotation.Px;
import androidx.annotation.RequiresApi;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Простейший пример самописного View. В данном случае мы просто наследуемся от LinearLayout-а и
 * добавляем свою логику, но также есть возможность отнаследоваться от {@link android.view.ViewGroup},
 * если необходимо реализовать контейнер для View полностью с нуля, либо отнаследоваться от {@link android.view.View}.
 * <p/>
 * Здесь можно было бы добавить автоматическое сохранение и восстановление состояния, переопределив методы
 * {@link #onSaveInstanceState()} и {@link #onRestoreInstanceState(Parcelable)}.
 */
public class Lab2ViewsContainer extends LinearLayout {

    private int minViewsCount;
    private int viewsCount;
    private int CC=0;
    private int prov1=0;
    private String PI;
    private String RI;
    private int Rt;
    Map<Integer, Integer> states = new HashMap<Integer, Integer>();


 /*   List<LinearLayout> LLL= new ArrayList<LinearLayout>();
    List<Double> ln= new ArrayList<Double>();
    List<Integer> IdL = new ArrayList<Integer>();*/

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

    /**
     * Конструктор, вызывается при инфлейте View, когда у View указан дополнительный стиль.
     * Почитать про стили можно здесь https://developer.android.com/guide/topics/ui/look-and-feel/themes
     *
     * @param attrs атрибуты, указанные в XML. Стандартные android атрибуты обрабатываются внутри родительского класса.
     *              Здесь необходимо только обработать наши атрибуты.
     */
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

        setViewsCount(minViewsCount);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void incrementViews(String OOO, Double PPP) {


        LinearLayout.LayoutParams lParams1;
        LinearLayout.LayoutParams lParams2;
        LinearLayout.LayoutParams lParams3;

     /*   name = findViewById(R.id.edittext1);
        lastName = findViewById(R.id.edittext2);*/
        PI =OOO;
        RI = PPP.toString();


        //Rt= Integer.parseInt(RI) ;

        LinearLayout linearLayout1 = new LinearLayout(getContext());
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        linearLayout1.setId(CC++);

// Зададим цвета для фона и самого индикатора
        Drawable background = new ColorDrawable(0xFF373737);
        Drawable progress = new ColorDrawable(0xFF00B51C);
        ClipDrawable clipProgress = new ClipDrawable(progress, Gravity.LEFT,
                ClipDrawable.HORIZONTAL);

// Слои для индикатора
        LayerDrawable layerlist = new LayerDrawable(new Drawable[] {
                background, clipProgress });
        layerlist.setId(0, android.R.id.background);
        layerlist.setId(1, android.R.id.progress);

        ProgressBar progressBar = new ProgressBar(getContext(), null,
                android.R.attr.progressBarStyleHorizontal);

        int tempI = (int) (PPP*10.0) ;
        states.put(linearLayout1.getId(), tempI);


        //SortedSet<Integer> values = new TreeSet<Integer>(states.values());
       // states.entrySet().stream().sorted((n1,n2)->n1.getValue().compareTo(n2.getValue())).forEach(n->System.out.println(n));


        progressBar.setProgressDrawable(layerlist);
        progressBar.setProgress(tempI);



        TextView textView1 = new TextView(getContext());
        textView1.setTextSize(16);
        textView1.setText(OOO);
       // textView1.setId(viewsCount++);


        TextView textView3 = new TextView(getContext());
        textView3.setTextSize(16);
        textView3.setText(RI)
        ;
        LayoutParams poi = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,2.0f);
        LayoutParams poi2 = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,2.0f);
        LayoutParams poi3 = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1.0f);
        textView1.setLayoutParams(poi);
         progressBar.setLayoutParams(poi2);
        textView3.setLayoutParams(poi3);

        linearLayout1.addView(textView1);
        linearLayout1.addView(progressBar);
        linearLayout1.addView(textView3);



        addView(linearLayout1);

        List<Integer> mapValues = new ArrayList<Integer>(states.values());
        Collections.sort(mapValues);
        Collections.reverse(mapValues);

        for(Integer element : mapValues) {
            for (Map.Entry<Integer, Integer> entry :states.entrySet()) {
              if (entry.getValue()==element)
              {
                  int uuu = entry.getKey();
               LinearLayout nnn= findViewById(uuu);

               removeView(findViewById(uuu));
                  addView(nnn);

              }
            }


        }



// do stuff


    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setViewsCount(int viewsCount) {
        if (this.viewsCount == viewsCount) {
            return;
        }
        viewsCount = viewsCount < minViewsCount ? minViewsCount : viewsCount;

        removeAllViews();
        this.viewsCount = 0;

        for (int i = 0; i < viewsCount; i++) {
            incrementViews("jjj",(7.1+(double)prov1++) );
        }
    }

    public int getViewsCount() {
        return viewsCount;
    }

    /**
     * Метод трансформирует указанные dp в пиксели, используя density экрана.
     */
    @Px
    public int dpToPx(float dp) {
        if (dp == 0) {
            return 0;
        }
        float density = getResources().getDisplayMetrics().density;
        return (int) Math.ceil(density * dp);
    }
}

