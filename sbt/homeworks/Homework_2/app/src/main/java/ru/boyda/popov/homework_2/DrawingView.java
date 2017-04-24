package ru.boyda.popov.homework_2;

import android.content.Context;
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

    protected Bitmap mBitmap;
    protected Canvas mCanvas;

    protected boolean isDrawing = false;

    protected float startX;
    protected float startY;
    protected float currentX;
    protected float currentY;

    private Paint mainPaint;
    private Paint mEditModePaint = new Paint();
    private SparseArray<PointF> mLastPoints = new SparseArray<>(10);
    private SparseArray<Paint> mPaints = new SparseArray<>(10);


    public void setColorID(int colorID) {
        Log.i("SetColor", "colorID = " + colorID);
        this.colorID = colorID;
        if (colorID == 0) {
            mainPaint.setStyle(Paint.Style.STROKE);
        } else {
            mainPaint.setColor(this.colorID);
            mainPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        }
    }

    public void setShapeID(int shapeID) {
        this.shapeID = shapeID;
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

    public void clear() {
        mCanvas.drawColor(Color.WHITE);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        currentX = event.getX();
        currentY = event.getY();
        switch (shapeID) {
            case ToolsBundle.RECTANGLE:
                onTouchEventRectangle(event);
                break;
            case ToolsBundle.SQUARE:
                onTouchEventSquare(event);
                break;
            case ToolsBundle.CIRCLE:
                onTouchEventCircle(event);
                break;
            case ToolsBundle.LINE:
                onTouchEventLine(event);
                break;
            case ToolsBundle.CURVE_LINE:
                onTouchEventCurveLine(event);
                break;
        }
        return true;
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
            mainPaint = new Paint(Paint.DITHER_FLAG);
            mainPaint.setAntiAlias(true);
            mainPaint.setDither(true);
            mainPaint.setColor(colorID);
            mainPaint.setStrokeCap(Paint.Cap.ROUND);
            mainPaint.setStrokeJoin(Paint.Join.ROUND);
            mainPaint.setStrokeWidth(getResources().getDimension(R.dimen.default_paint_width));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (isInEditMode()) {
            canvas.drawRect(0, 0, getRight(), getBottom(), mainPaint);
        }
        canvas.drawBitmap(mBitmap, 0, 0, null);

        if (isDrawing) {
            switch (shapeID) {
                case ToolsBundle.LINE:
                    onDrawLine(canvas);
                    break;
                case ToolsBundle.RECTANGLE:
                    drawRect(canvas, mainPaint);
                    break;
                case ToolsBundle.SQUARE:
                    drawRect(canvas, mainPaint);
                    break;
                case ToolsBundle.CIRCLE:
                    drawCircle(canvas);
                    break;
                default:
                    canvas.drawBitmap(mBitmap, 0, 0, null);

            }
        }
    }

    private void onTouchEventCurveLine(MotionEvent event) {
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
                defaultConfig();
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                Log.i("ACTION_UP", "action up");
                isDrawing = false;
                drawRect(mCanvas, mainPaint);
                invalidate();
                break;
        }
    }


    private void onTouchEventCircle(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                defaultConfig();
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isDrawing = false;
                mCanvas.drawCircle(startX, startY, calculateRadius(startX, startY, currentX, currentY), mainPaint);
                invalidate();
                break;
        }
    }

    private void onTouchEventLine(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                defaultConfig();
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isDrawing = false;
                mCanvas.drawLine(startX, startY, currentX, currentY, mainPaint);
                invalidate();
                break;
        }
    }

    private void onTouchEventSquare(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                defaultConfig();
                break;
            case MotionEvent.ACTION_MOVE:
                findSquare(currentX, currentY);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:

                isDrawing = false;
                findSquare(currentX, currentY);
                drawRect(mCanvas, mainPaint);
                invalidate();
                break;
        }
    }


    private void drawRect(Canvas canvas, Paint paint) {
        float right = startX > currentX ? startX : currentX;
        float left = startX > currentX ? currentX : startX;
        float bottom = startY > currentY ? startY : currentY;
        float top = startY > currentY ? currentY : startY;
        canvas.drawRect(left, top, right, bottom, paint);
    }


    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(startX, startY, calculateRadius(startX, startY, currentX, currentY), mainPaint);
    }


    private void onDrawLine(Canvas canvas) {
        canvas.drawLine(startX, startY, currentX, currentY, mainPaint);
    }


    protected float calculateRadius(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    protected void findSquare(float x, float y) {
        float deltaX = Math.abs(startX - x);
        float deltaY = Math.abs(startY - y);
        float max = Math.max(deltaX, deltaY);
        currentX = startX - x < 0 ? startX + max : startX - max;
        currentY = startY - y < 0 ? startY + max : startY - max;
    }

    private void defaultConfig() {
        isDrawing = true;
        startX = currentX;
        startY = currentY;
        invalidate();
    }



}
