package com.android.magic.wand.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;
import com.android.magic.wand.R;

public class RoundedImageView extends AppCompatImageView {
    private final static int CORNER = 0;
    private int leftTop;
    private int rightTop;
    private int leftBottom;
    private int rightBottom;
    private Paint mPaint;

    public RoundedImageView(Context context) {
        this(context, null);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView);

        int rippleColor = typedArray
                .getColor(R.styleable.RoundedImageView_bg_color, Color.WHITE);

        leftTop = typedArray.getInteger(R.styleable.RoundedImageView_left_top, CORNER);
        rightTop = typedArray.getInteger(R.styleable.RoundedImageView_right_top, CORNER);
        leftBottom = typedArray.getInteger(R.styleable.RoundedImageView_left_bottom, CORNER);
        rightBottom = typedArray.getInteger(R.styleable.RoundedImageView_right_bottom, CORNER);

        mPaint = new Paint();
        mPaint.setColor(rippleColor);
        mPaint.setAntiAlias(true);//消除锯齿
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (leftTop > 0) {
            drawLeftTop(canvas, leftTop);
        }
        if (rightTop > 0) {
            drawRightTop(canvas, rightTop);
        }
        if (leftBottom > 0) {
            drawLeftBottom(canvas, leftBottom);
        }
        if (rightBottom > 0) {
            drawRightBottom(canvas, rightBottom);
        }
    }

    private void drawLeftTop(Canvas canvas, int cornerSize) {
        Path path = new Path();
        path.moveTo(0, cornerSize);
        path.lineTo(0, 0);
        path.lineTo(cornerSize, 0);
        path.arcTo(new RectF(0, 0, cornerSize * 2, cornerSize * 2), -90, -90);
        path.close();
        canvas.drawPath(path, mPaint);
    }

    private void drawLeftBottom(Canvas canvas, int cornerSize) {
        Path path = new Path();
        path.moveTo(0, getHeight() - cornerSize);
        path.lineTo(0, getHeight());
        path.lineTo(cornerSize, getHeight());
        path.arcTo(new RectF(0, // x
                getHeight() - cornerSize * 2,// y
                cornerSize * 2,// x
                getHeight()// getWidth()// y

        ), 90, 90);
        path.close();
        canvas.drawPath(path, mPaint);
    }

    private void drawRightBottom(Canvas canvas, int cornerSize) {
        Path path = new Path();
        path.moveTo(getWidth() - cornerSize, getHeight());
        path.lineTo(getWidth(), getHeight());
        path.lineTo(getWidth(), getHeight() - cornerSize);
        RectF oval = new RectF(getWidth() - cornerSize * 2, getHeight()
                - cornerSize * 2, getWidth(), getHeight());
        path.arcTo(oval, 0, 90);
        path.close();
        canvas.drawPath(path, mPaint);
    }

    private void drawRightTop(Canvas canvas, int cornerSize) {
        Path path = new Path();
        path.moveTo(getWidth(), cornerSize);
        path.lineTo(getWidth(), 0);
        path.lineTo(getWidth() - cornerSize, 0);
        path.arcTo(new RectF(getWidth() - cornerSize * 2, 0, getWidth(),
                0 + cornerSize * 2), -90, 90);
        path.close();
        canvas.drawPath(path, mPaint);
    }
}
