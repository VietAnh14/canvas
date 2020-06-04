package vianh.nva.canvas;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class RoundProgressBar extends View {
    int startAngle, sweepRadius, size, lineWidth, lineSize, padding;
    int currentProgress, max;
    int startColor, endColor, trackColor;
    float centerX, centerY;
    ArgbEvaluator argbEvaluator;
    Paint paint;
    private int lineCount;

    public RoundProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
        final int DEFAULT_START_ANGLE = 0;
        final int DEFAULT_SWEEP_RADIUS = 180;
        final int DEFAULT_MAX = 500;
        final int DEFAULT_PROGRESS = 0;
        final int DEFAULT_LINE_COUNT = 50;

        startColor = typedArray.getColor(R.styleable.RoundProgressBar_startColor,
                ContextCompat.getColor(context, R.color.startColor));
        endColor = typedArray.getColor(R.styleable.RoundProgressBar_endColor,
                ContextCompat.getColor(context, R.color.endColor));
        trackColor = typedArray.getColor(R.styleable.RoundProgressBar_trackColor,
                ContextCompat.getColor(context, R.color.darkBlue));
        lineSize = typedArray.getDimensionPixelSize(R.styleable.RoundProgressBar_lineSize,
                context.getResources().getDimensionPixelSize(R.dimen.lineSize));
        lineWidth = typedArray.getDimensionPixelSize(R.styleable.RoundProgressBar_lineWidth,
                context.getResources().getDimensionPixelSize(R.dimen.lineWidth));
        lineCount = typedArray.getInt(R.styleable.RoundProgressBar_lineCount, DEFAULT_LINE_COUNT);

        max = typedArray.getInt(R.styleable.RoundProgressBar_max, DEFAULT_MAX);
        currentProgress = typedArray.getInt(R.styleable.RoundProgressBar_currentProgress, DEFAULT_PROGRESS);
        startAngle = typedArray.getInt(R.styleable.RoundProgressBar_startAngle, DEFAULT_START_ANGLE);
        sweepRadius = typedArray.getInt(R.styleable.RoundProgressBar_sweepRadius, DEFAULT_SWEEP_RADIUS);
        initPaint();
        argbEvaluator = new ArgbEvaluator();
        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        size = Math.min(w, h);
        centerX = (float) w / 2;
        centerY = (float) h / 2;
        super.onSizeChanged(size, size, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        size = Math.min(width, height);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.rotate(startAngle, centerX, centerY);
        drawLine(canvas);
        canvas.restore();
    }

    protected void initPaint() {
        padding = 2 * lineSize;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineSize);
    }


    private void drawLine(Canvas canvas) {
        float radiant = (float) (sweepRadius / 180f * Math.PI);
        float spacing = radiant / lineCount;
        int progress = Math.round((float) currentProgress / max * lineCount);
        int outerRadius = size / 2 - padding;
        int innerRadius = outerRadius - lineWidth;
        for (int i = 0; i <= lineCount; ++i) {
            if (i > progress) {
                paint.setColor(trackColor);
            } else {
                int color = getColor(((float)i) /progress);
                paint.setColor(color);
            }
            float alpha = -spacing * i;
            float startX = centerX - (float) (Math.cos(alpha) * innerRadius);
            float startY = centerY + (float) (Math.sin(alpha) * innerRadius);
            float endX = centerX - (float) (Math.cos(alpha) * outerRadius);
            float endY = centerY + (float) (Math.sin(alpha) * outerRadius);
            canvas.drawLine(startX, startY, endX, endY, paint);
        }
    }

    private int getColor(float faction) {
        return (int) argbEvaluator.evaluate(faction, startColor, endColor);
    }


    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
        invalidate();
    }

    public void animateToCurrent(long duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, currentProgress).setDuration(duration);
        valueAnimator.addUpdateListener(animation -> {
            int progress = (int) animation.getAnimatedValue();
            setCurrentProgress(progress);
        });
        valueAnimator.start();
    }
}
