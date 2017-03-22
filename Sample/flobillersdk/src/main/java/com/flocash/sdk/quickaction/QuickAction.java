package com.flocash.sdk.quickaction;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flocash.sdk.R;
import com.flocash.sdk.quickaction.listeners.OnDismissListener;
import com.flocash.sdk.quickaction.triangle.TriangleWidget;
import com.flocash.sdk.quickaction.triangle.TriangleWidgetBottom;
import com.flocash.sdk.quickaction.triangle.TriangleWithBorderBottom;
import com.flocash.sdk.quickaction.triangle.TriangleWithBorderTop;


/**
 * Author: Artemiy Garin
 * Date: 21.04.14
 */
public class QuickAction implements PopupWindow.OnDismissListener {

    private static final String PARAM_STATUS_BAR_HEIGHT = "status_bar_height";
    private static final String PARAM_DIMEN = "dimen";
    private static final String PARAM_ANDROID = "android";
    private final int rootY;

    private Context context;
    private int screenWidth;
    private int screenHeight;

    private OnDismissListener onDismissListener;
    private PopupWindow popupWindow;
    private WindowManager windowManager;
    private RelativeLayout rootLayout;

    private int mPositionTriangle;
    private int mPositionPopup; // up or down
    private int mColor;

    public QuickAction(Context context, int color, int animationStyle, RelativeLayout rootLayout, int rootY) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.context = context;
        this.rootLayout = rootLayout;
        this.rootY = rootY;
        this.mColor = color;
        this.mPositionPopup = MessagePopup.UP;
        initScreen();
        initPopupWindow(animationStyle);

    }

    @SuppressWarnings("deprecation")
    private void initScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
            screenHeight = size.y;
        } else {
            screenWidth = windowManager.getDefaultDisplay().getWidth();
            screenHeight = windowManager.getDefaultDisplay().getHeight();
        }

        // init color
        LayerDrawable bgDrawable;
        GradientDrawable shape;
        bgDrawable = (LayerDrawable)rootLayout.findViewById(R.id.lo_content_messsage).getBackground();
        shape = (GradientDrawable)   bgDrawable.findDrawableByLayerId(R.id.rectangle);
//        if (mTypeAction == MedicationConstants.BUTTON_DOCTOR_VISIT) {
//            shape.setColor(mColor);
//        } else {
            shape.setColor(Color.WHITE);
            shape.setStroke(5, mColor);
//            ((TextView)rootLayout.findViewById(R.id.tvContentMessage)).setTextColor(mColor);
//        }

    }

    private void initPopupWindow(int animationStyle) {
        popupWindow = new PopupWindow(context);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(rootLayout);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setAnimationStyle(animationStyle);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindow.dismiss();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @SuppressWarnings("UnusedDeclaration")
    public void dismiss() {
        popupWindow.dismiss();
    }

    public void show(View anchor) {
//        if (mTypeAction == MedicationConstants.BUTTON_DOCTOR_VISIT) {
//            showDoctorVisit(anchor);
//        } else {
            showTest(anchor);
//        }

    }

    private void showDoctorVisit(View anchor) {
        try {
            TriangleWidget triangleWidgetTop;
            TriangleWidgetBottom triangleWidgetBottom;
            triangleWidgetTop = (TriangleWidget) rootLayout.findViewById(R.id.triangle_message_up);
            triangleWidgetBottom = (TriangleWidgetBottom) rootLayout.findViewById(R.id.triangle_message_down);
            // set color
            triangleWidgetTop.setColor(mColor);
            triangleWidgetBottom.setColor(mColor);
            triangleWidgetTop.invalidate();
            triangleWidgetBottom.invalidate();
            int[] location = new int[2];
            anchor.getLocationOnScreen(location);

            Rect anchorRect = new Rect(location[0], location[1],
                    location[0] + anchor.getWidth(), location[1] + anchor.getHeight());

            if (rootLayout.getLayoutParams() == null) rootLayout.setLayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            rootLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            int rootHeight = rootLayout.getMeasuredHeight();
            int rootWidth = rootLayout.getMeasuredWidth();
            int offsetTop = anchorRect.top;
            int offsetBottom = screenHeight - anchorRect.bottom;
            boolean onTop = offsetTop > offsetBottom;

            int x = calculateHorizontalPosition(anchorRect, rootWidth, screenWidth);
            int y = calculateVerticalPosition(anchorRect, rootHeight, onTop, offsetTop);
            // set margin for triangle
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) triangleWidgetTop.getLayoutParams();
            RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) triangleWidgetBottom.getLayoutParams();
            lp.setMargins(mPositionTriangle, 0, 0, 0);
            lp1.setMargins(mPositionTriangle, -5, 0, 0);
            triangleWidgetTop.setLayoutParams(lp);
            triangleWidgetBottom.setLayoutParams(lp1);
            // set visiable triangle top or button of layout root
            if (mPositionPopup == MessagePopup.UP) {
                triangleWidgetTop.setVisibility(View.GONE);
            } else {
                triangleWidgetBottom.setVisibility(View.GONE);
            }
            popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, x, y);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showTest(View anchor) {
        try {
            // redraw color
            TriangleWithBorderTop triangleWidgetTop;
            TriangleWithBorderBottom triangleWidgetBottom;
            triangleWidgetTop = (TriangleWithBorderTop) rootLayout.findViewById(R.id.triangle_message_up);
            triangleWidgetBottom = (TriangleWithBorderBottom) rootLayout.findViewById(R.id.triangle_message_down);
            triangleWidgetTop.setColor(mColor);
            triangleWidgetBottom.setColor(mColor);
            triangleWidgetTop.invalidate();
            triangleWidgetBottom.invalidate();
            //
            int[] location = new int[2];
            anchor.getLocationOnScreen(location);

            Rect anchorRect = new Rect(location[0], location[1],
                    location[0] + anchor.getWidth(), location[1] + anchor.getHeight());

            if (rootLayout.getLayoutParams() == null) rootLayout.setLayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            rootLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            int rootHeight = rootLayout.getMeasuredHeight();
            int rootWidth = rootLayout.getMeasuredWidth();
            int offsetTop = anchorRect.top;
            int offsetBottom = screenHeight - anchorRect.bottom;
            boolean onTop = offsetTop > offsetBottom;

            int x = calculateHorizontalPosition(anchorRect, rootWidth, screenWidth);
            int y = calculateVerticalPosition(anchorRect, rootHeight, onTop, offsetTop);
            // set margin for triangle
            RelativeLayout.LayoutParams lpTop = (RelativeLayout.LayoutParams) triangleWidgetTop.getLayoutParams();
            RelativeLayout.LayoutParams lpBottom = (RelativeLayout.LayoutParams) triangleWidgetBottom.getLayoutParams();
            lpTop.setMargins(mPositionTriangle, 0, 0, 0);
            lpBottom.setMargins(mPositionTriangle, -5, 0, 0);
            triangleWidgetTop.setLayoutParams(lpTop);
            triangleWidgetBottom.setLayoutParams(lpBottom);
            // set visiable triangle top or button of layout root
            if (mPositionPopup == MessagePopup.UP) {
                triangleWidgetTop.setVisibility(View.GONE);
            } else {
                triangleWidgetBottom.setVisibility(View.GONE);
            }
            popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, x, y);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ConstantConditions")
    private int calculateVerticalPosition(Rect anchorRect, int rootHeight, boolean onTop, int offsetTop) {
        int y;
//        if (onTop) {
//            if (rootHeight > offsetTop) y = getStatusBarHeight();
//            else y = anchorRect.top - rootHeight;
//        } else y = anchorRect.bottom;

        if((offsetTop - rootHeight) < rootY) {
            y = anchorRect.bottom;
            mPositionPopup = MessagePopup.DOWN;
        } else {
            y = anchorRect.top - rootHeight;
            mPositionPopup = MessagePopup.UP;
        }
        return y;
    }

    private int calculateHorizontalPosition(Rect anchorRect, int rootWidth, int screenWidth) {
        int x;

        if ((anchorRect.left + rootWidth/2) > screenWidth) {
            x = screenWidth - rootWidth;
            mPositionTriangle = (anchorRect.left + anchorRect.width()/2 + rootWidth/2 - screenWidth);
        } else if((anchorRect.left - rootWidth/2) < 0){
            x = 0;
            mPositionTriangle = anchorRect.left + anchorRect.width()/2;
        } else {
            x = anchorRect.left - rootWidth/2 + anchorRect.width()/2;
            mPositionTriangle = rootWidth/2;
        }
        mPositionTriangle -= 15;
        return x;
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier(PARAM_STATUS_BAR_HEIGHT, PARAM_DIMEN, PARAM_ANDROID);
        if (resourceId > 0) result = context.getResources().getDimensionPixelSize(resourceId);

        return result;
    }

    @Override
    public void onDismiss() {
        if (onDismissListener != null) onDismissListener.onDismiss();
    }

    /**
     * Listeners
     */
    @SuppressWarnings("unused")
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public void setMaxHeightResource(int heightResource) {
        int maxHeight = context.getResources().getDimensionPixelSize(heightResource);
        popupWindow.setHeight(maxHeight);
    }

    public class MessagePopup {
        public static final int UP = 0;
        public static final int DOWN = 1;
    }
}