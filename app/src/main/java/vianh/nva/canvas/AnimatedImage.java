package vianh.nva.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class AnimatedImage extends AppCompatImageView {
    private Path halfCirclePath;
    private Paint paint;
    public AnimatedImage(Context context) {
        super(context);
    }

    public AnimatedImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            canvas.clipOutPath(halfCirclePath);
//        } else {
//            canvas.clipPath(halfCirclePath, Region.Op.DIFFERENCE);
//        }
        canvas.clipPath(halfCirclePath);
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        halfCirclePath = calculatePath(width, height);
        invalidate();
    }

    private Path calculatePath(int width, int height) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        Path path = new Path();
        path.addCircle(width/2, height/2, Math.min(width/2, height/2), Path.Direction.CW);
//        path.moveTo(width, height);
//        path.arcTo(0, 0, width, height, 0, 360, true);
        return path;
    }
}
