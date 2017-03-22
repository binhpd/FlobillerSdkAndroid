package com.flocash.sdk.quickaction.triangle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;

/**
 * Created by binhp_000 on 3/1/2015.
 */
public class TriangleWithBorderBottom extends TriangleBase {

    public Paint mPaint;
    public static Canvas mCanvas;
    private int radius = 60;
    private Paint mPaint2;

    public TriangleWithBorderBottom(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint2 = new Paint();
    }

    //constructor
    public TriangleWithBorderBottom(Context context) {
        super(context);
        mPaint = new Paint();
        mPaint2 = new Paint();
    }

    //what I want to draw is here
    protected void onDraw(Canvas canvas) {
        mCanvas = canvas;
        super.onDraw(mCanvas);
        canvas.drawColor(Color.TRANSPARENT);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        Point a = new Point(0, 0);
        Point b = new Point(36, 0);
        Point c = new Point(18, 24);
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);
        path.close();
        canvas.drawPath(path, mPaint);

        Point a1, b1, c1;
        mPaint2.setColor(color);
        mPaint2.setStyle(Paint.Style.FILL);
        mPaint2.setAntiAlias(true);
        a1 = new Point(5, 0);
        b1 = new Point(31, 0);
        c1 = new Point(18, 17);
        path.moveTo(a1.x, a1.y);
        path.lineTo(b1.x, b1.y);
        path.lineTo(c1.x, c1.y);
        path.lineTo(a1.x, a1.y);
        canvas.drawPath(path, mPaint2);
    }

}

