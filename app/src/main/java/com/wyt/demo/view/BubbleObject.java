package com.wyt.demo.view;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.animation.LinearInterpolator;

import java.util.Random;

public class BubbleObject {
    private int initX;//初始位置X
    private int initY;//初始位置Y
    private int radius;//气泡半径
    private long time;//一次运动所需时间
    private int currentX;//当前位置X
    private int currentY;//当前位置Y
    private int stopX;//结束位置X
    private int stopY;//结束位置Y
    private int alpha;//透明度
    private int red, green, blue; //  颜色
    private boolean isDiffusion;//是否扩散
    private long delay;// 开始时延


    /***
     * @param width view.width
     * @param height view.height
     * @param isDiffusion 是否扩散
     */
    public BubbleObject(int width,
                        int height,
                        int red,
                        int green,
                        int blue,
                        boolean isDiffusion) {
        //产生随机数
        Random random = new Random();
        //周长
        int perimeter = 2 * width + 2 * height;
        //在周长上随机产生的数
        int randomNum = random.nextInt(perimeter);
        //确定起始坐标和结束坐标,颜色
        this.isDiffusion = isDiffusion;
        if (isDiffusion) {
            alpha = 255;
            initX = width / 2;
            initY = height / 2;
            if (randomNum < width) {
                stopX = randomNum;
                stopY = 0;
            } else if (randomNum < width + height) {
                stopX = width;
                stopY = randomNum - width;
            } else if (randomNum < 2 * width + height) {
                stopX = 2 * width - randomNum + height;
                stopY = height;
            } else {
                stopX = 0;
                stopY = perimeter - randomNum;
            }
        } else {
            alpha = 0;
            stopX = width / 2;
            stopY = height / 2;
            if (randomNum < width) {
                initX = randomNum;
                initY = 0;
            } else if (randomNum < width + height) {
                initX = width;
                initY = randomNum - width;
            } else if (randomNum < 2 * width + height) {
                initX = 2 * width - randomNum + height;
                initY = height;
            } else {
                initX = 0;
                initY = perimeter - randomNum;
            }
        }

        //气泡最大半径
        int maxRadius = (Math.min(width, height)) / 40;
        double d = Math.random();
        if (d < 0.5) {
            d += 0.5;
        }
        radius = (int) (maxRadius * d);
        //默认时间为3秒
        time = 3000L;
        currentX = initX;
        currentY = initY;
        this.red = red;
        this.green = green;
        this.blue = blue;
        delay = random.nextInt(10000);
    }

    /**
     * 移动物体
     */
    public void moveObject(ValueAnimator.AnimatorUpdateListener listenX,
                           ValueAnimator.AnimatorUpdateListener listenY,
                           ValueAnimator.AnimatorUpdateListener listenA) {
        final ValueAnimator animatorX = ValueAnimator.ofFloat(initX, stopX);
//        animatorX.setStartDelay(delay);
        animatorX.setDuration(time);//播放时长
        animatorX.setRepeatCount(ObjectAnimator.INFINITE);
        animatorX.setInterpolator(new LinearInterpolator());
        animatorX.addUpdateListener(listenX);

        final ValueAnimator animatorY = ValueAnimator.ofFloat(initY, stopY);
//        animatorX.setStartDelay(delay);
        animatorY.setDuration(time);//播放时长
        animatorY.setRepeatCount(ObjectAnimator.INFINITE);
        animatorY.setInterpolator(new LinearInterpolator());
        animatorY.addUpdateListener(listenY);

        final ValueAnimator animatorA = ValueAnimator.ofInt(0, 255);
//        animatorX.setStartDelay(delay);
        animatorA.setDuration(time);//播放时长
        animatorA.setRepeatCount(ObjectAnimator.INFINITE);
        animatorA.setInterpolator(new LinearInterpolator());
        animatorA.addUpdateListener(listenA);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animatorX.start();
                animatorY.start();
                animatorA.start();
            }
        },delay);

    }

    /**
     * 绘制物体对象
     *
     * @param canvas
     */
    public void drawObject(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true); // 是否抗锯齿
        paint.setColor(Color.argb(alpha, red, green, blue));
        paint.setStyle(Paint.Style.FILL);//画笔属性是实心圆
        canvas.drawCircle(currentX, currentY, radius, paint);
    }

    public void setCurrentX(int currentX) {
        this.currentX = currentX;
    }

    public void setCurrentY(int currentY) {
        this.currentY = currentY;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getRadius() {
        return radius;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getInitX() {
        return initX;
    }

    public int getInitY() {
        return initY;
    }

    public int getStopX() {
        return stopX;
    }

    public int getStopY() {
        return stopY;
    }

    public boolean isDiffusion() {
        return isDiffusion;
    }
}
