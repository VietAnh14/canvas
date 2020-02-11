package vianh.nva.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.appcompat.widget.AppCompatImageView;

public class CustomImage extends AppCompatImageView {
    private static int INVALID_POINTER_ID = -1;

    // active pointer id
    private int activePointerId = INVALID_POINTER_ID;
    private Paint paint;
    private float x;
    private float y;
    private float r;
    private float recWidth;
    private float recHeight;
    private boolean isRectangle = false;
    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1f;

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
        x = getWidth() / 2;
        y = getHeight() / 2;
        this.r = 100;
        recHeight = 100;
        recWidth = 200;
    }

    public void setR(float r) {
        this.r = r;
        invalidate();
    }

    public String getCenter() {
        return "center: " + x + " " + y;
    }

    public void setRecWidth(float recWidth) {
        this.recWidth = recWidth;
        invalidate();
    }

    public void setRecHeight(float recHeight) {
        this.recHeight = recHeight;
        invalidate();
    }

    public void setIsRectangle(boolean isRectangle) {
        this.isRectangle = isRectangle;
        invalidate();
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(10f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    public CustomImage(Context context) {
        super(context);
        init(context);
    }

    public CustomImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isRectangle) {
            drawRectangle(x, y, recHeight, recWidth, canvas, paint);
        } else {
            drawHexagon(x, y, r, canvas, paint);
        }
    }

    public float getScaleFactor() {
        return scaleFactor;
    }

    // draw a hexagon at x, y with radius of r
    private void drawHexagon(float x, float y, float r, Canvas canvas, Paint paint) {
//        if (x < r || y < r) {
//            Log.e("draw", "x: " + x + " y: " + y + " r: " + r);
//            throw new IllegalArgumentException("What the fuck?");
//        }
        Path path = new Path();
        float height = (float) (r * Math.sqrt(3) / 2);
        path.moveTo(x - r, y);
        path.lineTo(x - r / 2, y - height);
        path.lineTo(x + r / 2, y - height);
        path.lineTo(x + r, y);
        path.lineTo(x + r / 2, y + height);
        path.lineTo(x - r / 2, y + height);
        path.lineTo(x - r, y);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRectangle(float x, float y, float height, float width, Canvas canvas, Paint paint) {
//        if (x < (width / 2) || y < (height / 2)) {
//            Log.e("Draw rectangle", "x: " + x + " y: " + y + " width: " + width + "height" + height);
//            throw new IllegalArgumentException("What the fuck?");
//        }
        Path path = new Path();
        path.moveTo(x - width / 2, y - height / 2);
        path.lineTo(x + width / 2, y - height / 2);
        path.lineTo(x + width / 2, y + height / 2);
        path.lineTo(x - width / 2, y + height / 2);
        path.close();
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        handleTouch(event);
        invalidate();
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // when zoom in, the scale should > 1 or when zoom out it should < 1
            float lastScaleFactor = scaleFactor;
            scaleFactor *= detector.getScaleFactor();
            if (lastScaleFactor > scaleFactor) {
                scaleFactor = 1 - (lastScaleFactor - scaleFactor);
            } else {
                scaleFactor = 1 + (scaleFactor - lastScaleFactor);
            }

            // the shape should not be too big or too small
            r = Math.max(100, Math.min(getWidth() / 2, r * scaleFactor));
            recHeight *= scaleFactor;
            recWidth *= scaleFactor;
            invalidate();
            return true;
        }
    }

    private void handleTouch(MotionEvent event) {
        final int ACTION = event.getActionMasked();
        scaleGestureDetector.onTouchEvent(event);

        switch (ACTION) {
            case MotionEvent.ACTION_DOWN:
                // get the first pointer id
                activePointerId = event.getPointerId(0);
                Log.d("DEBUG: ", "ACTION DOWN");
            case MotionEvent.ACTION_MOVE:
                paint.setColor(Color.GREEN);
                final int pointerIndex = event.findPointerIndex(activePointerId);
                if (!scaleGestureDetector.isInProgress()) {
                    Log.d("DEBUG: ", "ACTION MOVE " + pointerIndex);
                    calCenterPoint(event.getX(pointerIndex), event.getY(pointerIndex));
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                paint.setColor(Color.GRAY);
                activePointerId = INVALID_POINTER_ID;
                Log.d("DEBUG: ", "ACTION UP OR CANCEL");
                break;

            case MotionEvent.ACTION_POINTER_UP:
                // the first pointer have left the screen
                final int pointerIndexUp = event.getActionIndex();
                if (activePointerId == event.getPointerId(pointerIndexUp)) {
                    final int newPointerIndex = pointerIndexUp == 0 ? 1 : 0;
                    activePointerId = event.getPointerId(newPointerIndex);
                    calCenterPoint(event.getX(newPointerIndex), event.getY(newPointerIndex));
                    Log.d("DEBUG: ", "POINTER UP: INDEX UP: " + pointerIndexUp);
                }
                break;

            default:
                Log.d("DEBUG: ", "DEFAULT " + ACTION);
                break;
        }
    }

    private void calCenterPoint(float centerX, float centerY) {
         //keep the shape inside view
        if (isRectangle) {
            if (centerX < recWidth / 2)
                centerX = recWidth / 2;
            if (centerX > getWidth() - recWidth / 2)
                centerX = getWidth() - recWidth / 2;
            if (centerY < recHeight / 2)
                centerY = recHeight / 2;
            if (centerY > getHeight() - recHeight / 2)
                centerY = getHeight() - recHeight / 2;
        } else {
            if (centerX < r) {
                centerX = r;
            }
            if (centerX > getWidth() - r) {
                centerX = getWidth() - r;
            }
            if (centerY < r) {
                centerY = r;
            }

            if (centerY > getHeight() - r) {
                centerY = getHeight() - r;
            }
        }

        x = centerX;
        y = centerY;
    }
}
