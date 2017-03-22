package com.flocash.sdk.quickaction.triangle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by hiccup on 23/07/2015.
 */
public abstract class TriangleBase extends ImageView {
    public Paint mPaint;
    public static Canvas mCanvas;
    protected int color;

    public TriangleBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        color = Color.BLACK;
    }

    //constructor
    public TriangleBase(Context context) {
        super(context);
        mPaint = new Paint();
    }

    public void setColor(int color) {
        this.color = color;
    }
}
