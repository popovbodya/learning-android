package ru.boyda.popov.homework_2;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;


public class DrawingView extends View {

    private int shapeID;
    private int colorID;

    public static final float TOUCH_TOLERANCE = 4;

    public static final int CURVE_LINE = 1;
    public static final int LINE = 2;
    public static final int RECTANGLE = 3;
    public static final int SQUARE = 4;
    public static final int CIRCLE = 5;

    protected Bitmap mBitmap;
    protected Canvas mCanvas;

    protected boolean isDrawing = false;


    protected float mx;
    protected float my;

    protected float mStartX;
    protected float mStartY;

    private Paint mainPaint;
    private Paint mEditModePaint = new Paint();
    private SparseArray<PointF> mLastPoints = new SparseArray<>(10);
    private SparseArray<Paint> mPaints = new SparseArray<>(10);


    public void setColorID(int colorID) {
        Log.i("SetColor", "colorID = " + colorID);
        this.colorID = colorID;
        mainPaint.setColor(this.colorID);
    }

    public void setShapeID(int shapeID) {
        this.shapeID = shapeID;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);

            if (mBitmap != null) {
                canvas.drawBitmap(mBitmap, 0, 0, null);
                mBitmap.recycle();
            }

            mBitmap = bitmap;
            mCanvas = canvas;
        }
    }

    protected void init() {

        shapeID = ToolsBundle.CURVE_LINE;
        colorID = getResources().getColor(R.color.green);

        if (getRootView().isInEditMode()) {
            mEditModePaint.setColor(Color.TRANSPARENT);
        } else {
            mainPaint = new Paint();
            mainPaint.setAntiAlias(true);
            mainPaint.setColor(colorID);
            mainPaint.setStrokeCap(Paint.Cap.ROUND);
            mainPaint.setStrokeJoin(Paint.Join.ROUND);
            mainPaint.setStrokeWidth(getResources().getDimension(R.dimen.default_paint_width));
        }
    }

    public DrawingView(Context context) {
        super(context);
        init();
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DrawingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mx = event.getX();
        my = event.getY();
        switch (shapeID) {
            case RECTANGLE:
                onTouchEventRectangle(event);
                break;
            case SQUARE:
                onTouchEventSquare(event);
                break;
            case CIRCLE:
                onTouchEventCircle(event);
                break;
            case LINE:
                onTouchEventLine(event);
                break;
            case CURVE_LINE:
                onTouchEventSmoothLine(event);
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (isInEditMode()) {
            canvas.drawRect(0, 0, getRight(), getBottom(), mainPaint);
        }

        canvas.drawBitmap(mBitmap, 0, 0, null);

        if (isDrawing) {
            switch (shapeID) {
                case LINE:
                    onDrawLine(canvas);
                    break;
                case RECTANGLE:
                    onDrawRectangle(canvas);
                    break;
                case SQUARE:
                    onDrawRectangle(canvas);
                    break;
                case CIRCLE:
                    onDrawCircle(canvas);
                    break;

            }
        }


    }

    private void onTouchEventSmoothLine(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                int pointerId = event.getPointerId(event.getActionIndex());
                mLastPoints.put(pointerId, new PointF(event.getX(event.getActionIndex()), event.getY(event.getActionIndex())));
                mPaints.put(pointerId, mainPaint);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < event.getPointerCount(); i++) {
                    PointF last = mLastPoints.get(event.getPointerId(i));
                    Paint paint = mPaints.get(event.getPointerId(i));

                    if (last != null) {
                        float x = event.getX(i);
                        float y = event.getY(i);

                        mCanvas.drawLine(last.x, last.y, x, y, paint);
                        last.x = x;
                        last.y = y;
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mLastPoints.clear();
                invalidate();
                break;
        }

    }


    private void onTouchEventRectangle(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = true;
                mStartX = mx;
                mStartY = my;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                Log.i("ACTION_UP", "action up");
                isDrawing = false;
                drawRectangle(mCanvas, mainPaint);
                invalidate();
                break;
        }
    }

    private void onTouchEventSquare(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = true;
                mStartX = mx;
                mStartY = my;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                adjustSquare(mx, my);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:

                isDrawing = false;
                adjustSquare(mx, my);
                drawRectangle(mCanvas, mainPaint);
                invalidate();
                break;
        }
    }

    protected void adjustSquare(float x, float y) {
        float deltaX = Math.abs(mStartX - x);
        float deltaY = Math.abs(mStartY - y);

        float max = Math.max(deltaX, deltaY);

        mx = mStartX - x < 0 ? mStartX + max : mStartX - max;
        my = mStartY - y < 0 ? mStartY + max : mStartY - max;
    }

    private void onDrawRectangle(Canvas canvas) {
        drawRectangle(canvas, mainPaint);
    }

    private void drawRectangle(Canvas canvas, Paint paint) {
        float right = mStartX > mx ? mStartX : mx;
        float left = mStartX > mx ? mx : mStartX;
        float bottom = mStartY > my ? mStartY : my;
        float top = mStartY > my ? my : mStartY;
        canvas.drawRect(left, top, right, bottom, paint);
    }

    private void onDrawCircle(Canvas canvas) {
        canvas.drawCircle(mStartX, mStartY, calculateRadius(mStartX, mStartY, mx, my), mainPaint);
    }

    private void onTouchEventCircle(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = true;
                mStartX = mx;
                mStartY = my;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isDrawing = false;
                mCanvas.drawCircle(mStartX, mStartY,
                        calculateRadius(mStartX, mStartY, mx, my), mainPaint);
                invalidate();
                break;
        }
    }


    protected float calculateRadius(float x1, float y1, float x2, float y2) {

        return (float) Math.sqrt(
                Math.pow(x1 - x2, 2) +
                        Math.pow(y1 - y2, 2)
        );
    }

    private void onDrawLine(Canvas canvas) {

        float dx = Math.abs(mx - mStartX);
        float dy = Math.abs(my - mStartY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            canvas.drawLine(mStartX, mStartY, mx, my, mainPaint);
        }
    }

    private void onTouchEventLine(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = true;
                mStartX = mx;
                mStartY = my;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isDrawing = false;
                mCanvas.drawLine(mStartX, mStartY, mx, my, mainPaint);
                invalidate();
                break;
        }
    }

    public void clear() {
        mCanvas.drawColor(Color.WHITE);
        invalidate();
    }

}
