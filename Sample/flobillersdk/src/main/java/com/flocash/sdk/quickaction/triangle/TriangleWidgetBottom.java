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
public class TriangleWidgetBottom extends TriangleBase {

    public TriangleWidgetBottom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //constructor
    public TriangleWidgetBottom(Context context) {
        super(context);
    }

    //what I want to draw is here
    protected void onDraw(Canvas canvas) {
        mCanvas = canvas;
        super.onDraw(mCanvas);
        canvas.drawColor(Color.TRANSPARENT);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        Point a = new Point(0, 0);
        Point b = new Point(30, 0);
        Point c = new Point(15, 20);
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);
        path.close();
        canvas.drawPath(path, mPaint);
    }

}
