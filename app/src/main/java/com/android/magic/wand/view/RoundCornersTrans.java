package com.android.magic.wand.view;

import android.content.Context;
import android.graphics.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class RoundCornersTrans extends BitmapTransformation {

    private static final String ID = "com.qiku.android.filebrowser.resource.bitmap.RoundCornersTrans";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
    private Context context;
    private float leftTop;
    private float rightTop;
    private float leftBottom;
    private float rightBottom;

    public RoundCornersTrans(Context context, float leftTop, float rightTop, float leftBottom, float rightBottom){
        this.context = context;
        this.leftTop = dip2px(leftTop);
        this.rightTop = dip2px(rightTop);
        this.leftBottom = dip2px(leftBottom);
        this.rightBottom = dip2px(rightBottom);
    }

    private int dip2px(float px) {
        float var2 = context.getResources().getDisplayMetrics().density;
        return (int)(px * var2 + 0.5f);
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();
        Bitmap bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setHasAlpha(true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        RectF rect = new RectF(0f, 0f, width, height);
        float[] radii = {leftTop, leftTop, rightTop, rightTop, rightBottom, rightBottom, leftBottom, leftBottom};
        Path path = new Path();
        path.addRoundRect(rect, radii, Path.Direction.CW);
        canvas.drawPath(path, paint);
        return bitmap;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof RoundCornersTrans) {
            RoundCornersTrans o = (RoundCornersTrans)obj;
            if (o.leftTop == leftTop && o.leftBottom == leftBottom && o.rightBottom == rightBottom && o.rightTop == rightTop) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + (int)(leftTop + rightTop + leftBottom + rightBottom);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}
