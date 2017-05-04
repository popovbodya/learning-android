package ru.dimasokol.currencies.diagram;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;

import java.util.ArrayList;
import java.util.List;

/**
 * Drawable для отображения диаграммы. Всю диаграмму рисует при level = 10000, при меньших значениях
 * рисует лишь часть, что легко использовать для анимации.
 *
 * @author Дмитрий Соколов <DPSokolov.SBT@sberbank.ru>
 */
public class DiagramDrawable extends Drawable {

    private List<Point> mPoints = new ArrayList<>();
    private int mMinValueIndex;
    private int mMaxValueIndex;
    private float mFinalTextSize = 16f;
    private float mLineWidth = 8f;

    private int mLineColor = Color.RED;
    private float mShadowOffset = 2f;

    private TextPaint mCaptionsPaint;
    private Paint mLinePaint;

    /**
     * Конструктор принимает список точек
     * @param points Точки для графика
     */
    public DiagramDrawable(@NonNull List<Point> points) {
        mPoints = points;

        if (points.size() > 1) {
            computeRanges();
            preparePaints();
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mPoints.size() < 2) {
            return;
        }

        for (int i = 1; i < mPoints.size(); i++) {
            float stepProgress = getStepProgress(i);
            drawStep(canvas, i, stepProgress);
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        mLineWidth = Math.max(8f, bounds.height() / 24);
        float textSize = Math.max(16f, bounds.height() / 12);
        mFinalTextSize = textSize;

        preparePaints();
        invalidateSelf();
    }

    @Override
    protected boolean onLevelChange(int level) {
        invalidateSelf();
        return mPoints.size() > 1;
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void setLineColor(int lineColor) {
        mLineColor = lineColor;
        preparePaints();
        invalidateSelf();
    }

    /**
     * Вычислим диапазоны значений
     */
    private void computeRanges() {
        double minValue = Double.MAX_VALUE;
        double maxValue = Double.MIN_VALUE;

        for (int i = 0; i < mPoints.size(); i++) {
            Point point = mPoints.get(i);

            if (point.getValue() < minValue) {
                mMinValueIndex = i;
                minValue = point.getValue();
            }

            if (point.getValue() > maxValue) {
                mMaxValueIndex = i;
                maxValue = point.getValue();
            }
        }
    }

    /**
     * Подготовим рисование
     */
    private void preparePaints() {
        mCaptionsPaint = new TextPaint();
        mCaptionsPaint.setColor(Color.BLACK);
        mCaptionsPaint.setAntiAlias(true);
        mCaptionsPaint.setSubpixelText(true);
        mCaptionsPaint.setTextSize(mFinalTextSize);

        mLinePaint = new Paint();
        mLinePaint.setColor(mLineColor);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(mLineWidth);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);
    }

    /**
     * Вычислим прогресс отрисовки одного «колена» графика по текущему level-у.
     * @param step Номер шага (прямой на графике)
     * @return Числов диапазоне [0..1] ([не начат..отрисован])
     */
    private float getStepProgress(int step) {
        float stepSize = 10_000f / (mPoints.size() - 1);
        float minStep = stepSize * (step - 1);
        float maxStep = stepSize * step;

        if (getLevel() >= maxStep) {
            return 1f;
        }

        if (getLevel() <= minStep) {
            return 0f;
        }

        return ((float) getLevel() - minStep) / stepSize;
    }

    /**
     * Рисует один шаг
     * @param canvas Канва
     * @param step Номер шага
     * @param progress Его вычисленный прогресс
     */
    private void drawStep(Canvas canvas, int step, float progress) {
        // Пропускаем нулевые и околонулевые шаги
        if (progress < 0.001f) {
            return;
        }

        // Находим свои границы
        Rect bounds = copyBounds();
        if (bounds.isEmpty()) {
            bounds = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        }

        // Слегка подвинем их для красоты рисования
        bounds.inset((int) mLineWidth / 2, (int) mLineWidth / 2);

        double minValue = mPoints.get(mMinValueIndex).getValue();
        double maxValue = mPoints.get(mMaxValueIndex).getValue();
        double valueRange = maxValue - minValue;

        Point startPoint = mPoints.get(step - 1);
        Point endPoint = mPoints.get(step);

        // По Х всё просто
        int stepSize = (bounds.width() / (mPoints.size() - 1));
        float startX = stepSize * (step - 1);
        float endX = startX + stepSize;

        // По Y смотрим на диапазоны, приводим в [0..1], умножаем на пиксели
        float startY = 1f - Double.valueOf((startPoint.getValue() - minValue) / valueRange).floatValue();
        startY *= bounds.height();
        float endY = 1f - Double.valueOf((endPoint.getValue() - minValue) / valueRange).floatValue();
        endY *= bounds.height();

        // Рисование неполной прямой (пропустим, если она уже полная)
        if (progress < 1f) {
            float c1 = endX - startX;
            float c2 = endY - startY;
            float length = (float) Math.sqrt((c1 * c1) + (c2 * c2));
            float required = length * progress;

            float t = required / length;
            endX = (1f - t) * startX + t * endX;
            endY = (1f - t) * startY + t * endY;
        }

        // Добавим смещения рамок
        startX += bounds.left;
        endX += bounds.left;
        startY += bounds.top;
        endY += bounds.top;

        // Нарисуем линейку
        canvas.drawLine(startX, startY, endX, endY, mLinePaint);

        // Нарисуем тексты с тенями, текст прошлого шага рисуем чтобы он оказался поверх линии
        // На прошлом шаге текст всегда полного размера
        PointF startText = getCaptionPosition(bounds, step - 1, mCaptionsPaint);
        mCaptionsPaint.setTextSize(mFinalTextSize);
        mCaptionsPaint.setColor(Color.WHITE);
        canvas.drawText(startPoint.getCaption(), startText.x + mShadowOffset, startText.y + mShadowOffset, mCaptionsPaint);
        mCaptionsPaint.setColor(Color.BLACK);
        canvas.drawText(startPoint.getCaption(), startText.x, startText.y, mCaptionsPaint);

        PointF endText = getCaptionPosition(bounds, step, mCaptionsPaint);
        mCaptionsPaint.setTextSize(progress * mFinalTextSize);
        mCaptionsPaint.setColor(Color.WHITE);
        canvas.drawText(endPoint.getCaption(), endText.x + mShadowOffset, endText.y + mShadowOffset, mCaptionsPaint);
        mCaptionsPaint.setColor(Color.BLACK);
        canvas.drawText(endPoint.getCaption(), endText.x, endText.y, mCaptionsPaint);
    }

    /**
     * Находит позицию подписи точки на графике
     *
     * @param bounds
     * @param pointIndex
     * @param setAlignment Установит здесь выравнивание текста
     * @return
     */
    private PointF getCaptionPosition(Rect bounds, int pointIndex, TextPaint setAlignment) {
        Point point = mPoints.get(pointIndex);
        setAlignment.setTextAlign((pointIndex > mPoints.size() / 2)? Paint.Align.RIGHT :
                Paint.Align.LEFT);

        double minValue = mPoints.get(mMinValueIndex).getValue();
        double maxValue = mPoints.get(mMaxValueIndex).getValue();
        double valueRange = maxValue - minValue;

        float x = bounds.left + (bounds.width() / (mPoints.size() - 1)) * pointIndex;
        float y = 1f - Double.valueOf((point.getValue() - minValue) / valueRange).floatValue();
        y *= bounds.height();
        y += bounds.top;

        double half = (maxValue - minValue) / 2;
        if (point.getValue() > minValue + half) {
            y += mFinalTextSize;
        } else {
            y -= mFinalTextSize;
        }

        return new PointF(x, y);
    }

    public static class Point {
        private final String mCaption;
        private final double mValue;

        public Point(String caption, double value) {
            mCaption = caption;
            mValue = value;
        }

        public String getCaption() {
            return mCaption;
        }

        public double getValue() {
            return mValue;
        }
    }
}
