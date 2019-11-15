package com.android.magic.wand.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import com.android.magic.wand.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BubblePop extends View {
    private static final String TAG = "BubblePop";
    private int mBubbleMaxRadius = 80;          // 气泡最大半径 px
    private int mBubbleMinRadius = 20;           // 气泡最小半径 px
    private int mBubbleMaxSize = 30;            // 气泡数量
    private int mBubbleRefreshTime = 20;        // 刷新间隔
    private int mBubbleMaxSpeedY = 30;           // 气泡速度
    private int mSpeed = -1;
    private int mBubbleAlpha = 186;             // 气泡画笔

    private Paint mWaterPaint;                  // 水画笔
    private Paint mBubblePaint;                 // 气泡画笔

    private class Bubble {
        int radius;     // 气泡半径
        float speedY;   // 上升速度
        float speedX;   // 平移速度
        float x;        // 气泡x坐标
        float y;        // 气泡y坐标
    }

    public class BubbleColor {
        int start, center, end;
    }

    public class BackgroundColor {
        int top, center, bottom;
    }

    private ArrayList<Bubble> mBubbles = new ArrayList<>();

    private Random random = new Random();
    private Thread mBubbleThread;

    public BubblePop(Context context) {
        this(context, null);
    }

    public BubblePop(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubblePop(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mWaterPaint = new Paint();
        mWaterPaint.setAntiAlias(true);

        initBubble();
    }

    public void setSpeed(int speed){
        if (speed > mBubbleMaxSpeedY) {
            mSpeed = mBubbleMaxSpeedY;
        } else if (speed == 0) {
            mSpeed = 10;
        } else {
            mSpeed = speed;
        }
    }

    public void setBubbleColor(BubbleColor bubbleColor) {

    }

    public void setBackgroundColor(BackgroundColor backgroundColor) {

    }

    public void start() {
        startBubbleSync();
    }

    public void stop() {
        stopBubbleSync();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mWaterPaint);
        drawBubble(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Shader gradient = new LinearGradient(0, 0, 0, getHeight(), new int[] {
                getResources().getColor(android.R.color.white),
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_blue_dark) },
                null, Shader.TileMode.CLAMP);
        mWaterPaint.setShader(gradient);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startBubbleSync();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopBubbleSync();
    }

    // 初始化气泡
    private void initBubble() {
        mBubblePaint = new Paint();
        mBubblePaint.setColor(Color.WHITE);
        mBubblePaint.setStyle(Paint.Style.FILL);
        mBubblePaint.setAlpha(mBubbleAlpha);
    }

    // 开始气泡线程
    private void startBubbleSync() {
        stopBubbleSync();
        mBubbleThread = new Thread() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(mBubbleRefreshTime);
                        tryCreateBubble();
                        refreshBubbles();
                        postInvalidate();
                    } catch (InterruptedException e) {
                        System.out.println("Bubble线程结束");
                        break;
                    }
                }
            }
        };
        mBubbleThread.start();
    }

    // 停止气泡线程
    private void stopBubbleSync() {
        if (null == mBubbleThread) return;
        mBubbleThread.interrupt();
        mBubbleThread = null;
    }

    // 绘制气泡
    private void drawBubble(Canvas canvas) {
        List<Bubble> list = new ArrayList<>(mBubbles);
        for (Bubble bubble : list) {
            if (null == bubble) continue;
            LinearGradient shader = new LinearGradient(bubble.x, bubble.y - bubble.radius, // 渐变区域,上下
                    bubble.x, bubble.y + bubble.radius, new int[] {
                    getResources().getColor(android.R.color.holo_orange_light),
                    getResources().getColor(R.color.orange),
                    getResources().getColor(android.R.color.holo_orange_dark) }
                    , null, Shader.TileMode.REPEAT);
            mBubblePaint.setShader(shader);
            canvas.drawCircle(bubble.x, bubble.y,
                    bubble.radius, mBubblePaint);
        }
    }

    // 尝试创建气泡
    private void tryCreateBubble() {
        if (mBubbles.size() >= mBubbleMaxSize) {
            return;
        }
        if (random.nextFloat() < 0.95) {
            return;
        }
        Bubble bubble = new Bubble();
        int radius = random.nextInt(mBubbleMaxRadius - mBubbleMinRadius);
        radius += mBubbleMinRadius;
        if (mSpeed > 0) {
            bubble.speedY = mSpeed;
        } else {
            float speedY = random.nextFloat() * mBubbleMaxSpeedY;
            while (speedY < 1) {
                speedY = random.nextFloat() * mBubbleMaxSpeedY;
            }
            bubble.speedY = speedY;
        }

        bubble.radius = radius;

        float xx = random.nextFloat();
        bubble.x = xx > 0 ? xx * getWidth() : getWidth();
        bubble.y = getHeight() - radius;
        float speedX = random.nextFloat() - 0.5f;
        while (speedX == 0) {
            speedX = random.nextFloat() - 0.5f;
        }
        bubble.speedX = speedX * 2;
        mBubbles.add(bubble);
    }

    // 刷新气泡位置，对于超出区域的气泡进行移除
    private void refreshBubbles() {
        List<Bubble> list = new ArrayList<>(mBubbles);
        for (Bubble bubble : list) {
            if (bubble.y - bubble.speedY <= bubble.radius) {
                mBubbles.remove(bubble);
            } else {
                int i = mBubbles.indexOf(bubble);
                if (bubble.x + bubble.speedX <= bubble.radius) {
                    bubble.x = bubble.radius;
                } else if (bubble.x + bubble.speedX >= getWidth() - bubble.radius) {
                    bubble.x = getWidth() - bubble.radius;
                } else {
                    bubble.x = bubble.x + bubble.speedX;
                }
                bubble.y = bubble.y - bubble.speedY;
                mBubbles.set(i, bubble);
            }
        }
    }
}
