package com.wyt.demo.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.Utils;
import com.wyt.demo.R;

import java.util.ArrayList;


public class AirFoamView extends View {


    //圆盘画笔
    private Paint discPaint;
    //圆环画笔
    private Paint ringPaint;
    //圆圈画笔
    private Paint circlePaint;
    //文字画笔
    private Paint textPaint;

    // 圆环颜色
    private int ringColor = Color.parseColor("#B5E1FF");
    //总半径
    private int radius;
    //宽
    private int width;
    //高
    private int height;
    //等级
    private String grade = "-";
    //城市
    private String city = "--";
    //pm2.5
    private int pm25 = 0;
    // 滤芯寿命
    private String filterLife = "--";

    //定位图标
    private Bitmap location_open = BitmapFactory.decodeResource(Utils.getApp().getResources(), R.mipmap.icon_location);
    private Bitmap location_close = BitmapFactory.decodeResource(Utils.getApp().getResources(), R.mipmap.icon_location_close);
    //气泡list
    private ArrayList<BubbleObject> bubbleObjects;
    //是否打开(显示气泡)
    private Boolean isOpen = true;

    public AirFoamView(Context context) {
        super(context);
    }


    public AirFoamView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bubbleObjects = new ArrayList<>();
        initPaint();
    }

    public AirFoamView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AirFoamView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        int len = Math.min(width, height);
        radius = len >> 1;
    }

    private void initPaint() {
        discPaint = new Paint();
        discPaint.setAntiAlias(true); // 是否抗锯齿
        discPaint.setStyle(Paint.Style.FILL);//画笔属性是实心圆

        ringPaint = new Paint();
        ringPaint.setAntiAlias(true); // 是否抗锯齿
        ringPaint.setStyle(Paint.Style.STROKE);//画笔属性是实心圆
        ringPaint.setStrokeWidth(16);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true); // 是否抗锯齿
        circlePaint.setStyle(Paint.Style.STROKE);//画笔属性是实心圆
        circlePaint.setStrokeWidth(1);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @SuppressLint("NewApi")
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        discPaint.setColor(Color.argb(isOpen ? 50 : 25, 255, 255, 255));
        circlePaint.setColor(Color.argb(isOpen ? 150 : 75, 255, 255, 255));
        textPaint.setARGB(isOpen ? 255 : 127, 255, 255, 255);
        int red = (ringColor & 0xff0000) >> 16;
        int green = (ringColor & 0x00ff00) >> 8;
        int blue = (ringColor & 0x0000ff);
        ringPaint.setColor(Color.argb(isOpen ? 255 : 127, red, green, blue));

        canvas.save();
        Path path = new Path();
        path.addCircle(width >> 1, height >> 1, (float) (radius * 0.75), Path.Direction.CCW);
        canvas.clipOutPath(path);
        if (bubbleObjects.size() > 0 && isOpen) {
            for (int i = 0; i < bubbleObjects.size(); i++) {
                //然后进行绘制
                BubbleObject bo = bubbleObjects.get(i);
                bo.drawObject(canvas);
            }
        }
        canvas.restore();
        canvas.drawCircle(width >> 1, height >> 1, (float) (radius * 0.75), discPaint);
        canvas.drawCircle(width >> 1, height >> 1, (float) (radius * 0.68), ringPaint);
        canvas.drawCircle(width >> 1, height >> 1, (float) (radius * 0.68) + 12, circlePaint);
        int size = radius / 10;
        textPaint.setTextSize(size);
        canvas.drawText("空气质量等级", width >> 1, (height >> 1) - (int) (radius * 0.36), textPaint);
        size = (int) (radius * 0.4);
        textPaint.setTextSize(size);
        canvas.drawText(grade, width >> 1, (height >> 1) + (size >> 2), textPaint);
        double s = 0.08 * city.length();
        canvas.drawBitmap(isOpen ? location_open : location_close, (width >> 1) - (int) (radius * s), (height >> 1) + (int) (radius * 0.24), new Paint());
        size = radius / 10;
        textPaint.setTextSize(size);
        s = 0.04 * city.length();
        canvas.drawText(city, (width >> 1) + (int) (radius * s), (height >> 1) + (int) (radius * 0.32), textPaint);
        canvas.drawText("室外PM2.5：" + pm25, width >> 1, (height >> 1) + (int) (radius * 0.48), textPaint);
        canvas.drawText("滤芯寿命剩余"+filterLife,width>>1,(int)(0.95*height),textPaint);
    }

    public void addBubbleObject(final int num,
                                final int red,
                                final int green,
                                final int blue,
                                final Boolean isDiffusion
    ) {
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                bubbleObjects.clear();
                for (int i = 0; i < num; i++) {
                    final BubbleObject bo = new BubbleObject(width, height, red, green, blue, isDiffusion);
                    bo.moveObject(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator listenX) {
                            float f = (float) listenX.getAnimatedValue();
                            bo.setCurrentX((int) f);
                            invalidate();
                        }
                    }, new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator listenY) {
                            float f = (float) listenY.getAnimatedValue();
                            bo.setCurrentY((int) f);
                        }
                    }, new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator listenA) {
                            if (bo.isDiffusion()) {
                                bo.setAlpha(255 - (int) listenA.getAnimatedValue());
                            } else {
                                bo.setAlpha((int) listenA.getAnimatedValue());
                            }
                        }
                    });
                    bubbleObjects.add(bo);
                }
                return true;
            }
        });
    }

    public AirFoamView setGrade(String grade) {
        this.grade = grade;
        return this;
    }

    public AirFoamView setCity(String city) {
        this.city = city;
        return this;
    }

    public AirFoamView setPm25(int pm25) {
        this.pm25 = pm25;
        return this;
    }

    public AirFoamView setRingColor(int ringColor) {
        this.ringColor = ringColor;
        return this;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
        invalidate();
    }

    public AirFoamView setFilterLife(String filterLife){
        this.filterLife = filterLife;
        return this;
    }
}
