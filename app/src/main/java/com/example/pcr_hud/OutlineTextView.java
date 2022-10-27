package com.example.pcr_hud;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class OutlineTextView extends AppCompatTextView {

    private boolean stroke = false;

    private float strokeWidth = 0.0f;

    private int strokeColor;


    public OutlineTextView(Context context) {

        super(context);

    }


    public OutlineTextView(Context context, AttributeSet attrs) {

        super(context, attrs);


        initView(context, attrs);

    }


    public OutlineTextView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);


        initView(context, attrs);

    }


    private void initView(Context context, AttributeSet attrs) {

        TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.OutlineTextView);


        stroke = type.getBoolean(R.styleable.OutlineTextView_textStroke, false); // 외곽선 유무

        strokeWidth = type.getFloat(R.styleable.OutlineTextView_textStrokeWidth, 0.0f); // 외곽선 두께

        strokeColor = type.getColor(R.styleable.OutlineTextView_textStrokeColor, 0xffffffff); // 외곽선

    }


    @Override

    protected void onDraw(Canvas canvas) {

        if (stroke) {

            ColorStateList states = getTextColors();

            getPaint().setStyle(Paint.Style.STROKE);

            getPaint().setStrokeWidth(strokeWidth);

            setTextColor(strokeColor);

            super.onDraw(canvas);


            getPaint().setStyle(Paint.Style.FILL);

            setTextColor(states);

        }


        super.onDraw(canvas);

    }
}