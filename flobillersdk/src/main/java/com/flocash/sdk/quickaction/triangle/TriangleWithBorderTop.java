
/**
 * Created by binhp_000 on 3/10/2015.
 */
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
public class TriangleWithBorderTop extends TriangleBase {

    public Paint mPaintStroke, mPaintInside;
    private int mColor;
    public static Canvas mCanvas;
    private int mPivotX = 0;
    private int mPivotY = 0;
    private int radius = 60;

    public TriangleWithBorderTop(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaintStroke = new Paint();
        mPaintInside = new Paint();
    }

    //constructor
    public TriangleWithBorderTop(Context context) {
        super(context);
        mPaintStroke = new Paint();
        mPaintInside = new Paint();
    }

    //what I want to draw is here
    protected void onDraw(Canvas canvas) {
        mCanvas = canvas;
        super.onDraw(mCanvas);
        canvas.drawColor(Color.TRANSPARENT);
        mPaintStroke.setColor(Color.WHITE);
        mPaintStroke.setStyle(Paint.Style.FILL);
        mPaintStroke.setAntiAlias(true);

        Point a = new Point(0, 24);
        Point b = new Point(36, 24);
        Point c = new Point(18, 0);
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);
        path.close();
        canvas.drawPath(path, mPaintStroke);

        Point a1, b1, c1;
        mPaintInside.setColor(mColor);
        mPaintInside.setStyle(Paint.Style.FILL);
        mPaintInside.setAntiAlias(true);
        a1 = new Point(5, 24);
        b1 = new Point(31, 24);
        c1 = new Point(18, 8);
        path.moveTo(a1.x, a1.y);
        path.lineTo(b1.x, b1.y);
        path.lineTo(c1.x, c1.y);
        path.lineTo(a1.x, a1.y);
        canvas.drawPath(path, mPaintInside);
    }

}

