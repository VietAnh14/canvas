package vianh.nva.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomView extends View {

    private Paint paint;

    private void initPaint() {
        this.paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(10f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
    }

    public CustomView(Context context) {
        super(context);
        initPaint();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawHexagon(getWidth() / 2 , getHeight() / 2, 200, canvas, this.paint);
    }

    // draw a hexagon at x, y with radius of r
    private void drawHexagon(float x, float y, float r, Canvas canvas, Paint paint) {
        if (x < r || y < r) {
            throw new IllegalArgumentException("What the fuck?");
        }
        Path path = new Path();
        float height = (float) (r * Math.sqrt(3)/2);
        path.moveTo(x - r, y);
        path.lineTo(x - r/2, y - height);
        path.lineTo(x + r/2, y - height);
        path.lineTo(x + r, y);
        path.lineTo(x + r/2, y + height);
        path.lineTo(x - r/2, y + height);
        path.lineTo(x - r, y);
        path.close();
        canvas.drawPath(path, paint);
    }
}
