package vianh.nva.canvas;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class CustomProgressbar extends View {
    Paint paint, trackPaint;
    int chunkSize, width, height, padding, max;
    float lineSpacing;
    int startColor = 0;
    int endColor = 0;
    int currentValue;
    int progressLineCount = 50;
    private OnProgressChange listener;

    // gradient color
    private ArgbEvaluator argbEvaluator;

    public CustomProgressbar(Context context) {
        super(context);
        initPaint();
    }

    public CustomProgressbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        initPaint();
    }

    public CustomProgressbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressbar);
        startColor = a.getColor(R.styleable.CustomProgressbar_startColor, ContextCompat.getColor(context, R.color.startColor));
        endColor = a.getColor(R.styleable.CustomProgressbar_endColor, ContextCompat.getColor(context, R.color.endColor));
        max = a.getInt(R.styleable.CustomProgressbar_max, 100);
        currentValue = a.getInt(R.styleable.CustomProgressbar_currentProgress, 0);
//        setCurrentValue(value);
        initPaint();
        a.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void drawLine(Canvas canvas) {
//        int progressLineCount = (int) ((float) mProgress / (float) mMax * mLineCount);
        lineSpacing = ((float) (width - chunkSize)/(progressLineCount - 1));
        float startY = height - padding * 3;
        float endY = padding * 3;
        int currentProgress = (int) (((float)currentValue/max) * (progressLineCount - 1));

        // draw seekbar
        for (int i = 0; i < progressLineCount; i++) {
            float x = ((float) chunkSize / 2) + i * lineSpacing;
            if (i > currentProgress) {
                canvas.drawLine(x, startY, x, endY, trackPaint);
            } else if (currentProgress == i) {
                float originalStartY = height - 2*padding;
                float originalEndY = 2*padding;
                int color = currentProgress == 0 ? startColor : getColor(i);
                paint.setColor(color);
                canvas.drawLine(x, originalStartY, x, originalEndY, paint);
            } else {
                int color = getColor(i);
//                Log.d("Color", String.valueOf(color));
                paint.setColor(color);
//                Log.d("Color of paint", String.valueOf(paint.getColor()));
                canvas.drawLine(x, startY, x, endY, paint);
            }

        }
    }

    private void initPaint() {
        argbEvaluator = new ArgbEvaluator();
//        if (startColor*endColor == 0) {
//            startColor = ContextCompat.getColor(getContext(), R.color.startColor);
//            endColor = ContextCompat.getColor(getContext(), R.color.endColor);
//        }

        padding = getResources().getDimensionPixelSize(R.dimen.padding);
        chunkSize = getContext().getResources().getDimensionPixelSize(R.dimen.chunk_size);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(chunkSize);
        paint.setColor(getResources().getColor(R.color.colorAccent));

        // track paint
        trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        trackPaint.setStrokeCap(Paint.Cap.ROUND);
        trackPaint.setStyle(Paint.Style.STROKE);
        trackPaint.setStrokeWidth(chunkSize);
        trackPaint.setColor(getResources().getColor(R.color.gray));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
//        Log.d("Action: ", String.valueOf(event.getAction()));
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            calculateProgressValue(event);
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void calculateProgressValue(MotionEvent event) {
        float x = event.getX();
        x = Math.max(0, Math.min(x, width));
        int newValue = (int)(x /(width - chunkSize) * max);
        newValue = Math.max(0, Math.min(newValue, max));
        if (newValue != currentValue) {
            currentValue = newValue;
            if (listener != null) {
//                int newProgress = (int) (x/lineSpacing);
//                currentProgress = Math.max(0, Math.min(newProgress, progressLineCount - 1));
                listener.onProgressChange(currentValue);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(0, 0);
        drawLine(canvas);
        canvas.restore();
    }

    public void setOnProgressChangeListener(OnProgressChange listener) {
        this.listener = listener;
    }

    private int getColor(int progress) {
        float currentProgress = ((float)currentValue/max) * progressLineCount;
        return (int) argbEvaluator.evaluate((float) progress/currentProgress, startColor, endColor);
    }

    public int getCurrentValue() {
//        return (int) ((float)max/(progressLineCount - 1)*currentProgress);
        return currentValue;
    }

    public void setCurrentValue(int value) {
//        currentProgress = (int)(value/((float)max/progressLineCount));
        currentValue = value;
        invalidate();
    }

    public interface OnProgressChange {
        void onProgressChange(int progress);
    }
}
