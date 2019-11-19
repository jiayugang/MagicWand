package com.android.magic.wand.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import com.android.magic.wand.R;
import com.android.magic.wand.model.BackgroundColor;
import com.android.magic.wand.model.BubbleColor;
import com.android.magic.wand.utils.UpdateLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BubblePop extends View {
    private static final String TAG = "BubblePop";
    private int mBubbleMaxRadius = 100;          // 气泡最大半径 px
    private int mBubbleMinRadius = 20;           // 气泡最小半径 px
    private int mBubbleMaxSize = 20;            // 气泡数量
    private int mBubbleRefreshTime = 15;        // 刷新间隔
    private int mSpeed = 10;
    private int mBubbleAlpha = 255;             // 气泡画笔

    private Paint mWaterPaint;                  // 水画笔
    private Paint mBubblePaint;                 // 气泡画笔

    private BubbleColor mBubbleColor;           //气泡颜色
    private BackgroundColor mBackgroundColor;   //背景颜色

    private boolean isSameSpeed = false;

    private class Bubble {
        int radius;     // 气泡半径
        float speedY;   // 上升速度
        float speedX;   // 平移速度
        float x;        // 气泡x坐标
        float y;        // 气泡y坐标
        boolean isBlur; //是否模糊
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

        mBubbleColor = new BubbleColor();
        mBackgroundColor = new BackgroundColor();

        mBubbleColor.setTop(getResources().getColor(R.color.clean_bubble_low_top));
        mBubbleColor.setBottom(getResources().getColor(R.color.clean_bubble_low_bottom));

        mBackgroundColor.setTop(getResources().getColor(android.R.color.white));
        mBackgroundColor.setBottom(getResources().getColor(R.color.clean_bg_low_bottom));

        initBubble();
    }

    public void setBubbleMaxSize(int size){
        mBubbleMaxSize = size;
    }

    public void setSpeed(int speed, boolean isSameSpeed){
        this.isSameSpeed = isSameSpeed;
        if (speed == 0) {
            mSpeed = 10;
        } else {
            mSpeed = speed;
        }
    }

    public void setBubbleColor(BubbleColor bubbleColor) {
        mBubbleColor = bubbleColor;
    }

    public void setBackgroundColor(BackgroundColor backgroundColor) {
        mBackgroundColor = backgroundColor;
        createBg();
        postInvalidate();
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
        createBg();
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

    private void createBg(){
        Shader gradient = new LinearGradient(0, 0, 0, getHeight(), new int[] {
                mBackgroundColor.getTop(),
                mBackgroundColor.getBottom() },
                null, Shader.TileMode.CLAMP);
        mWaterPaint.setShader(gradient);
    }

    // 初始化气泡
    private void initBubble() {
        mBubblePaint = new Paint();
        mBubblePaint.setStyle(Paint.Style.FILL);
    }

    private void resetBubble(Bubble bubble){
        LinearGradient shader = new LinearGradient(bubble.x + bubble.radius, bubble.y - bubble.radius, // 渐变区域,上下
                bubble.x - bubble.radius, bubble.y + bubble.radius + bubble.radius, new int[] {
                mBubbleColor.getTop(),
                mBubbleColor.getBottom()}
                , null, Shader.TileMode.MIRROR);

        mBubblePaint.setShader(shader);
        if (bubble.isBlur) {
            mBubblePaint.setAlpha(85);
        } else {
            mBubblePaint.setAlpha(mBubbleAlpha);
        }
    }

    // 开始气泡线程
    private void startBubbleSync() {
        UpdateLog.d(TAG,"startBubbleSync");
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
                        e.printStackTrace();
                        break;
                    }
                }
            }
        };
        mBubbleThread.start();
    }

    // 停止气泡线程
    private void stopBubbleSync() {
        UpdateLog.d(TAG,"stopBubbleSync");
        if (null == mBubbleThread) return;
        mBubbleThread.interrupt();
        mBubbleThread = null;
    }

    // 绘制气泡
    private void drawBubble(Canvas canvas) {
        List<Bubble> list = new ArrayList<>(mBubbles);
        for (Bubble bubble : list) {
            if (null == bubble) continue;
            resetBubble(bubble);
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
        bubble.radius = radius;

        if (isSameSpeed) {
            bubble.speedY = mSpeed;
            if (bubble.speedY <= 0) {
                bubble.speedY = 20;
            }
        } else {
            if (bubble.radius >= 20 && bubble.radius <= 30) {
                bubble.speedY = mSpeed;
            } else if (bubble.radius > 30 && bubble.radius <= 40) {
                bubble.speedY = mSpeed * 3 / 4;
            } else if (bubble.radius > 40 && bubble.radius <= 80) {
                bubble.speedY = mSpeed / 2;
            } else {
                bubble.speedY = mSpeed / 4;
            }

            if (bubble.speedY <= 0) {
                bubble.speedY = 5;
            }
        }

        float xx = random.nextFloat();
        bubble.x = xx > 0 ? xx * getWidth() : getWidth();
        bubble.y = getHeight() - radius;
        float speedX = random.nextFloat() - 0.5f;
        while (speedX == 0) {
            speedX = random.nextFloat() - 0.5f;
        }
        bubble.speedX = speedX * 2;
        int blur = random.nextInt(10);
        if (blur >= 3 && blur < 5) {
            bubble.isBlur = true;
        } else {
            bubble.isBlur = false;
        }
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
