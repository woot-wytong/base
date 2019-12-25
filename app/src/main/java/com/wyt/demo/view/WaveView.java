package com.wyt.demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import com.wyt.demo.R;

import java.text.NumberFormat;

/**
 * 双波纹进度球，字体颜色分离。
 */
public class WaveView extends View {

    private Path aboveWavePath = new Path();

    private Path blowWavePath = new Path();

    private Paint aboveWavePaint = new Paint();

    private Paint blowWavePaint = new Paint();

    private int waveToTop;

    private int aboveWaveColor;

    private int blowWaveColor;

    private int progress;

    private PorterDuffXfermode xfermode_text = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);

    /**
     * wave length
     */
    private int x_zoom = 30;

    /**
     * wave crest
     */
    private int y_zoom = 20;// 控制上下起伏的高度

    /**
     * offset of X
     */
    private final float offset = 1f;

    private float max_right = x_zoom * offset;

    // wave animation
    private float aboveOffset = 0.0f;

    private float blowOffset = 4.0f;

    private Paint mRingPaint;

    private Paint mCirclePaint;

    private Paint flowPaint;

    private float num;

    private Path cPath;

    /**
     * offset of Y
     */
    private float animOffset = 0.2f;// 控制上下起伏的频率

    // refresh thread
    private RefreshProgressRunnable mRefreshProgressRunnable;

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.waveViewStyle);
        init();
    }

    public WaveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        progress = 0;
        setProgress(progress);
        initializePainters();
        init();
    }

    public void init() {
        // 外圈
        mRingPaint = new Paint();
        mRingPaint.setColor(aboveWaveColor);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setAntiAlias(true);
        mRingPaint.setStrokeWidth(4);

        // 内圈
        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.WHITE);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStrokeWidth(4);

        flowPaint = new Paint();
        flowPaint.setColor(Color.WHITE);
        flowPaint.setStyle(Paint.Style.FILL);
        flowPaint.setAntiAlias(true);

        aboveWaveColor = Color.rgb(21, 237, 244);
        blowWaveColor = Color.rgb(21, 158, 244);

        cPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {

            canvas.save();
            // 画一个空心圆
            canvas.drawCircle((float) ((getWidth() >> 1)), (float) (getHeight() >> 1),
                    ((getWidth() >> 1) - (mRingPaint.getStrokeWidth() / 2)), mRingPaint);

            canvas.drawCircle((float) ((getWidth() >> 1)), (float) (getHeight() >> 1),
                    ((getWidth() >> 1) - (mRingPaint.getStrokeWidth() / 2) - (mCirclePaint.getStrokeWidth() / 2)), mCirclePaint);
            canvas.restore();

            cPath.addCircle((float) ((getWidth() >> 1)), (float) (getHeight() >> 1),
                    ((getWidth() >> 1) - (mRingPaint.getStrokeWidth() / 2) - (mCirclePaint.getStrokeWidth() / 2)), Path.Direction.CCW);
            canvas.clipPath(cPath);
            canvas.drawPath(blowWavePath, blowWavePaint);
            flowPaint.setColor(aboveWaveColor);
            flowPaint.setTextSize(getWidth() >> 2);
            NumberFormat numberFormat = NumberFormat.getPercentInstance();
            numberFormat.setMinimumFractionDigits(1);
            PointF mPointF = new PointF(getWidth() >> 1, getHeight() >> 1);
            textCenter(new String[]{numberFormat.format(num)}, flowPaint,
                    canvas, mPointF, Paint.Align.CENTER);
            int flag = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
            flowPaint.setColor(Color.WHITE);
            canvas.drawPath(aboveWavePath, aboveWavePaint);
            flowPaint.setXfermode(xfermode_text);
            textCenter(new String[]{numberFormat.format(num)}, flowPaint,
                    canvas, mPointF, Paint.Align.CENTER);
            canvas.restoreToCount(flag);
            canvas.restore();
        } catch (Exception ignored) {

        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
    }

    private int measure(int measureSpec, boolean isWidth) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight();
            result += padding;
            if (mode == MeasureSpec.AT_MOST) {
                if (isWidth) {
                    result = Math.max(result, size);
                } else {
                    result = Math.min(result, size);
                }
            }
        }

        return result;
    }

    private void initializePainters() {
        int default_above_wave_alpha = 50;
        aboveWavePaint.setAlpha(default_above_wave_alpha);
        aboveWavePaint.setStyle(Paint.Style.FILL);
        aboveWavePaint.setAntiAlias(true);

        int default_blow_wave_alpha = 30;
        blowWavePaint.setAlpha(default_blow_wave_alpha);
        blowWavePaint.setStyle(Paint.Style.FILL);
        blowWavePaint.setAntiAlias(true);
    }

    /**
     * 计算波动轨迹
     */
    private void calculatePath() {
        aboveWavePath.reset();
        blowWavePath.reset();

        getWaveOffset();

        aboveWavePath.moveTo(0, getHeight());
        for (float i = 0; x_zoom * i <= getRight() + max_right; i += offset) {
            aboveWavePath.lineTo((x_zoom * i), (float) (y_zoom * Math.cos(i + aboveOffset)) + waveToTop);
        }
        aboveWavePath.lineTo(getRight(), getHeight());

        blowWavePath.moveTo(0, getHeight());
        for (float i = 0; x_zoom * i <= getRight() + max_right; i += offset) {
            blowWavePath.lineTo((x_zoom * i), (float) (y_zoom * Math.cos(i + blowOffset)) + waveToTop);
        }
        blowWavePath.lineTo(getRight(), getHeight());
    }

    /**
     * 根据设置的内存百分比不同，显示的颜色不同。大于70%红色，小于蓝色
     */
    public WaveView setProgress(int progress) {
        this.progress = progress > 100 ? 100 : progress;
        this.num = progress > 100 ? 1f : (float) progress / 100;
        return this;
    }

    public WaveView setAboveWaveColor(int aboveWaveColor) {
        this.aboveWaveColor = aboveWaveColor;
        aboveWavePaint.setColor(this.aboveWaveColor);
        mRingPaint.setColor(this.aboveWaveColor);
        return this;
    }

    public WaveView setBlowWaveColor(int blowWaveColor) {
        this.blowWaveColor = blowWaveColor;
        blowWavePaint.setColor(this.blowWaveColor);
        return this;
    }

    public WaveView setTextAttr(int size) {
        flowPaint.setTextSize(size);
        return this;
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mRefreshProgressRunnable = new RefreshProgressRunnable();
        post(mRefreshProgressRunnable);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(mRefreshProgressRunnable);
    }

    private void getWaveOffset() {
        if (blowOffset > Float.MAX_VALUE - 100) {
            blowOffset = 0;
        } else {
            blowOffset += animOffset;
        }

        if (aboveOffset > Float.MAX_VALUE - 100) {
            aboveOffset = 0;
        } else {
            aboveOffset += animOffset;
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        SavedState ss;
        Parcelable superState = super.onSaveInstanceState();
        ss = new SavedState(superState);
        ss.progress = progress;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setProgress(ss.progress);
    }

    private class RefreshProgressRunnable implements Runnable {
        public void run() {
            synchronized (WaveView.this) {
                waveToTop = (int) (getHeight() * (1f - progress / 100f));

                calculatePath();

                invalidate();

                postDelayed(this, 16);
            }
        }
    }

    private static class SavedState extends BaseSavedState {
        int progress;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            progress = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(progress);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    /**
     * 多行文本居中、居右、居左
     *
     * @param strings 文本字符串列表
     * @param paint   画笔
     * @param canvas  画布
     * @param point   点的坐标
     * @param align   居中、居右、居左
     */
    protected void textCenter(String[] strings, Paint paint, Canvas canvas,
                              PointF point, Paint.Align align) {
        paint.setTextAlign(align);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int length = strings.length;
        float total = (length - 1) * (-top + bottom)
                + (-fontMetrics.ascent + fontMetrics.descent);
        float offset = total / 2 - bottom;
        for (int i = 0; i < length; i++) {
            float yAxis = -(length - i - 1) * (-top + bottom) + offset;
            canvas.drawText(strings[i], point.x, point.y + yAxis, paint);
        }
    }


    /**
     * 设置波纹的大小
     *
     * @param x_zoom 单位波纹长度
     * @param y_zoom 波纹起伏高度
     * @return this
     */
    public WaveView setZoom(int x_zoom, int y_zoom) {
        this.x_zoom = x_zoom;
        this.y_zoom = y_zoom;
        return this;
    }

    /**
     * 设置上下起伏的频率
     * @param animOffset 频率
     * @return this
     */
    public WaveView setAnimOffset(float animOffset) {
        this.animOffset = animOffset;
        return this;
    }


}
