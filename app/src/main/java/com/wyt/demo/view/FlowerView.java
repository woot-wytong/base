package com.wyt.demo.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class FlowerView extends View {

    //椭圆画笔
    private Paint paint;
    //白圈画笔
    private Paint circlePaint;
    // 绘制文字
    Paint textPaint;
    // 正方形的宽高
    private int len;
    // 圆弧的半径
    private float radius;
    // 剪切圆的半径
    private int clipRadius;
    //初始角度
    float c = 0;
    //椭圆夹角
    float a = 45;
    //分数
    private int score;
    //旋转一圈用时
    private int f = 4096;
    //字
    private String textDesc = "今日体检得分";


    public FlowerView(Context context) {
        super(context);
        paint = new Paint();
    }

    public FlowerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true); // 是否抗锯齿
        paint.setColor(Color.argb(50, 255, 255, 255));//        给画笔设置颜色
        paint.setStyle(Paint.Style.FILL);//画笔属性是实心圆
        paint.setStrokeWidth(4);//设置画笔粗细
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true); // 是否抗锯齿
        circlePaint.setColor(Color.argb(100, 255, 255, 255));//        给画笔设置颜色
        circlePaint.setStyle(Paint.Style.STROKE);//画笔属性是空心圆
        circlePaint.setStrokeWidth(2);//设置画笔粗细

        textPaint = new Paint();
        textPaint.setARGB(255, 255, 255, 255);
        textPaint.setAntiAlias(true);

        rotate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 通过测量规则获得宽和高
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        len = Math.min(width, height);
        radius = len >> 1;
        clipRadius = (int) (0.375 * len);
        setMeasuredDimension(len, len);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawFlower(canvas);
        drawText(canvas);
    }

    //drawOval 绘制椭圆
    private void Doval(Canvas canvas) {
        /*四个参数：
                参数一：矩形距离父view左边距离
                参数二：矩形距离父view上边距离
                参数三：矩形距离父view左边距离
                参数四：矩形距离父view上边距离
                */
        //定义一个矩形区域
        // 矩形
        RectF oval = new RectF(0, (float) 0.125 * len, len, len - (float) 0.125 * len);
        //矩形区域内切椭圆
        canvas.drawOval(oval, paint);// drawOval 绘制椭圆
    }

    private void drawFlower(Canvas canvas) {
        // 保存之前的画布状态
        canvas.save();
        canvas.translate(radius, radius);
        canvas.rotate(c);
        canvas.translate(-radius, -radius);
        Path path = new Path();
        path.addCircle(len >> 1, len >> 1, clipRadius, Path.Direction.CCW);
        canvas.clipPath(path, Region.Op.DIFFERENCE);
        for (int i = 0; i < 4; i++) {
            // 移动画布，实际上是改变坐标系的位置
            Doval(canvas);
            canvas.translate(radius, radius);
            canvas.rotate(a);
            canvas.translate(-radius, -radius);
        }
        canvas.restore();
    }

    private void drawText(Canvas canvas) {
        canvas.drawCircle(radius, radius, (float) (clipRadius * 0.95), circlePaint);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(clipRadius);
        canvas.drawText("" + score, radius, radius + (clipRadius >> 2), textPaint);
        int size = clipRadius / 6;
        textPaint.setTextSize(size);
        canvas.drawText(textDesc, radius, radius + (clipRadius >> 1), textPaint);
    }

    ValueAnimator rotation;

    public void rotate() {
        rotation = ValueAnimator.ofFloat(0f, 359f);//最好是0f到359f，0f和360f的位置是重复的
        rotation.setRepeatCount(ObjectAnimator.INFINITE);
        rotation.setInterpolator(new LinearInterpolator());
        rotation.setDuration(f);
        rotation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                c = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        rotation.start();
    }

    //设置分数
    public void setScore(int score) {
        this.score = score;
    }

    //设置字
    public void setTextDesc(String textDesc) {
        this.textDesc = textDesc;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
        rotate();
    }
}
