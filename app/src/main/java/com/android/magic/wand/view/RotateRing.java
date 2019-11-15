package com.android.magic.wand.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class RotateRing extends View {
    // 颜色数组，定义渐变色；
    private int[] colors;

    public RotateRing(Context context) {
        this(context, null);
    }

    public RotateRing(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotateRing(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        colors = new int[] {// 渐变色数组 7色
                0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00, 0xFFFFFF00,
                0xFFFF0000 };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float radius = 210;	//圆半径
        int cX = getWidth() / 2;	//圆心
        int cY = getHeight() / 2;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL); // 设置空心
        paint.setStrokeWidth(2); // 设置圆环的宽度

        LinearGradient shader = new LinearGradient(cX - radius, cY - radius, // 渐变区域,左上右下
                cX + radius, cY + radius, colors, null, Shader.TileMode.REPEAT);

        paint.setShader(shader);
        canvas.drawCircle(cX, cY, radius, paint);// 画圆
        //rotateArray(colors);
    }

    /**
     * 改变颜色数组内颜色值位置，实现颜色转动
     * @param arr 颜色数组
     */
    public void rotateArray(int[] arr) {
        int tmp = arr[0];
        for (int i = 0; i < arr.length - 1; i++) {
            arr[i] = arr[i + 1];
        }
        arr[arr.length - 1] = tmp;
        invalidate();	//重绘
    }
}
