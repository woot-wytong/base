package com.wyt.demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class ScaleDialView extends View {
    public static final int PRECISION_MODE_INTEGER = 0; //整数模式
    public static final int PRECISION_MODE_ONE_DECIMAL_PLACES = 1; //精确到小数点后一位
    public static final int PRECISION_MODE_TWO_DECIMAL_PLACES = 2;//精确到小数点后两位
    public int PRECISION_MODE_DEFUALE = PRECISION_MODE_INTEGER;//默认精度模式  整数模式

    private float mMaxValue = 100; //显示的最大值

    private String textUnit = ""; //右上角文字 单位
    private String textName = "";//中间要显示的文字名字

    // 画圆弧的画笔
    private Paint paint;
    // 正方形的宽高
    private int len;
    // 圆弧的半径
    private float radius;
    // 圆弧的经过总范围角度角度
    private float sweepAngle = 300;

    // 刻度经过角度范围
    private float targetAngle = 300;

    // 绘制文字
    Paint textPaint;

    // 监听角度变化对应的颜色变化
    private OnAngleColorListener onAngleColorListener;

    public ScaleDialView(Context context) {
        this(context, null);
    }

    public ScaleDialView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint();
        textPaint.setARGB(255, 255, 255, 255);
        textPaint.setAntiAlias(true);

        waterPaint = new Paint();
        waterPaint.setAntiAlias(true);

//        moveWaterLine();//让水波纹开始运动
    }

    /**
     * 设置动画效果，开启子线程定时绘制
     *
     * @param trueAngle
     */
    // 前进或者后退的状态，1代表前进，2代表后退。初始为后退状态。
    int state = 2;
    // 每次后退时的值，实现越来越快的效果
    private int[] back = {2, 2, 4, 4, 6, 6, 8, 8, 10};
    // 每次前进时的值，实现越来越慢的效果
    private int[] go = {10, 10, 8, 8, 6, 6, 4, 4, 2};
    // 前进的下标
    private int go_index = 0;
    // 后退的下标
    private int back_index = 0;
    private float score;
    private int color;

    private boolean isRunning;

    /**
     * 使用定时器自动开始变幻刻度
     *
     * @param trueAngle
     */
    public void change(final float trueAngle) {
        if (isRunning) {
            return;
        }
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                switch (state) {
                    case 1:
                        // 开始增加
                        targetAngle += go[go_index];
                        go_index++;
                        if (go_index == go.length) {// 到最后的元素时，下标一直为最后的
                            go_index--;
                        }
                        if (targetAngle >= trueAngle) {// 如果画过刻度大于等于真实角度
                            // 画过刻度=真实角度
                            targetAngle = trueAngle;
                            // 状态改为2
                            state = 2;
                            isRunning = false;
                            timer.cancel();
                        }
                        break;
                    case 2:
                        isRunning = true;
                        targetAngle -= back[back_index];
                        back_index++;
                        if (back_index == back.length) {
                            back_index--;
                        }

                        if (targetAngle <= 0) {
                            targetAngle = 0;
                            state = 1;
                        }
                        break;
                    default:
                        break;
                }
                computerScore();// 计算当前比例应该的多少分
                // 计算出当前所占比例，应该增长多少
                computerUp();

                postInvalidate();
            }
        }, 500, 30);
    }

    /**
     * 手动设置刻度盘的值
     *
     * @param trueAngle 0-300
     */
    public void setChange(final float trueAngle) {
        targetAngle = trueAngle;
        computerScore();//计算得分

        if (clipRadius == 0) {
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (clipRadius != 0) {
                        // 计算出当前所占比例，应该增长多少
                        computerUp();
                        postInvalidate();
                        timer.cancel();
                    }
                }
            }, 0, 10);
        } else {
            computerUp();
            invalidate();
        }
    }


    //计算当前比例应该得多少分
    private void computerScore() {
        score = targetAngle / 300 * mMaxValue;
    }

    //计算水位
    private void computerUp() {
        up = (int) (targetAngle / 360 * clipRadius * 2);
    }

    /**
     * 调用此方法水波纹开始运动
     */
    public void moveWaterLine() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                move += 1;
                if (move == 100) {
                    timer.cancel();
                }
                postInvalidate();
            }
        }, 500, 200);
    }

    // 存放第一条水波Y值
    private float[] firstWaterLine;
    // 第二条
    private float[] secondWaterLine;
    // 画水球的画笔
    private Paint waterPaint;
    // 影响三角函数的初相
    private float move;
    // 剪切圆的半径
    private int clipRadius;
    // 水球的增长值
    int up = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 通过测量规则获得宽和高
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        // 取出最小值
        len = Math.min(width, height);
        // 矩形
        RectF oval = new RectF(0, 0, len, len);
        radius = len / 2;
        clipRadius = (len / 2) - 45;
        firstWaterLine = new float[len];
        secondWaterLine = new float[len];
        setMeasuredDimension(len, len);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 画刻度线
        drawLine(canvas);
        // 画刻度线内的内容
        drawText(canvas);

    }

    /**
     * 画水球的功能
     *
     * @param canvas
     */
    private void drawWaterView(Canvas canvas) {
        // y = Asin(wx+b)+h ，w影响周期，A影响振幅，h影响y位置，b为初相；
        // 将周期定为view总宽度
        float mCycleFactorW = (float) (2 * Math.PI / len);

        // 得到第一条波的y值
        for (int i = 0; i < len; i++) {
            firstWaterLine[i] = (float) (10 * Math
                    .sin(mCycleFactorW * i + move) - up);
        }
        // 得到第一条波的y值
        for (int i = 0; i < len; i++) {
            secondWaterLine[i] = (float) (15 * Math.sin(mCycleFactorW * i
                    + move + 10) - up);
        }

        canvas.save();

        // 裁剪成圆形区域
        Path path = new Path();
        waterPaint.setColor(color);
        path.reset();
        canvas.clipPath(path);

        path.addCircle(len / 2, len / 2, clipRadius, Path.Direction.CCW);
        canvas.clipPath(path, android.graphics.Region.Op.REPLACE);
        // 将坐标系移到底部
        canvas.translate(0, len / 2 + clipRadius);

        for (int i = 0; i < len; i++) {
            canvas.drawLine(i, firstWaterLine[i], i, len, waterPaint);
        }
        for (int i = 0; i < len; i++) {
            canvas.drawLine(i, secondWaterLine[i], i, len, waterPaint);
        }
        canvas.restore();
    }

    /**
     * 实现画刻度线内的内容
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {

        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(clipRadius / 2);
        // 画分数
        switch (PRECISION_MODE_DEFUALE) {
            case PRECISION_MODE_ONE_DECIMAL_PLACES://小数点后一位
                float f1Score = (float) (Math.round(score * 10)) / 10;
                canvas.drawText("" + f1Score, radius, radius, textPaint);
                break;
            case PRECISION_MODE_TWO_DECIMAL_PLACES://小数点后两位
                DecimalFormat fnum = new DecimalFormat("##0.00");
                String f2Score = fnum.format(score);
                canvas.drawText(f2Score, radius, radius, textPaint);
                break;
            default://默认 整数模式
                canvas.drawText("" + (int) score, radius, radius, textPaint);
                break;
        }

        textPaint.setTextSize(clipRadius / 6);

        // 画固定值分
        canvas.drawText(textUnit, radius + clipRadius / 2, radius - clipRadius / 4, textPaint);
        textPaint.setTextSize(clipRadius / 4);
        // 画固定值立即优化
        canvas.drawText(textName, radius, radius + clipRadius / 2, textPaint);

    }


    float a = sweepAngle / 30;
    private Paint linePaint;

    /**
     * 实现画刻度线的功能
     *
     * @param canvas
     */
    private void drawLine(final Canvas canvas) {
        // 保存之前的画布状态
        canvas.save();
        // 移动画布，实际上是改变坐标系的位置
        canvas.translate(radius, radius);
        // 旋转坐标系,需要确定旋转角度
        canvas.rotate(30);
        // 初始化画笔
        linePaint = new Paint();
        // 设置画笔的宽度（线的粗细）
        linePaint.setStrokeWidth(4);
        // 设置抗锯齿
        linePaint.setAntiAlias(true);
        // 累计叠加的角度
        float c = 0;
        for (int i = 0; i <= 30; i++) {

            if (c <= targetAngle && targetAngle != 0) {// 如果累计画过的角度，小于当前有效刻度
                // 计算累计划过的刻度百分比（画过的刻度比上中共进过的刻度）
                double p = c / (double) sweepAngle;
                int red;
                int green;
                if (p >= 0.5) {
                    red = 255;
                    green = (int)((1-p) * 255) * 2;
                } else {
                    red = (int) (p * 255) * 2;
                    green = 255;
                }
                color = linePaint.getColor();
                if (onAngleColorListener != null) {
                    onAngleColorListener.onAngleColorListener(red, green);
                }
                linePaint.setARGB(255, red, green, 50);
                if (c + a > targetAngle) {
                    canvas.drawLine(0, radius, 0, radius - 40, linePaint);
                } else {
                    canvas.drawLine(0, radius - 10, 0, radius - 40, linePaint);
                }

                // 画过的角度进行叠加
                c += a;
            } else {
                linePaint.setColor(Color.WHITE);
                canvas.drawLine(0, radius - 10, 0, radius - 40, linePaint);
            }

            canvas.rotate(a);
        }
        // 恢复画布状态。
        canvas.restore();
    }


    public void setOnAngleColorListener(
            OnAngleColorListener onAngleColorListener) {
        this.onAngleColorListener = onAngleColorListener;
    }

    /**
     * 默认初始化配置  精度模式为整数 最大值为100
     *
     * @param textName 水球中间显示的名字
     * @param textUnit 右上角的单位
     */
    public void setInitConfig(String textName, String textUnit) {
        this.textName = textName;
        this.textUnit = textUnit;
    }

    /**
     * 默认初始化配置  精度模式为整数
     *
     * @param maxValue 最大值
     * @param textName 水球中间显示的名字
     * @param textUnit 右上角的单位
     */
    public void setInitConfig(int maxValue, String textName, String textUnit) {
        mMaxValue = maxValue;
        this.textName = textName;
        this.textUnit = textUnit;
    }

    /**
     * 初始化配置
     *
     * @param precisionModeDefuale 精度模式
     * @param maxValue             最大值
     * @param textName             水球中间显示的名字
     * @param textUnit             右上角的单位
     */
    public void setInitConfig(int precisionModeDefuale, float maxValue, String textName, String textUnit) {
        PRECISION_MODE_DEFUALE = precisionModeDefuale;
        mMaxValue = maxValue;
        this.textName = textName;
        this.textUnit = textUnit;
    }


    /**
     * 监听角度和颜色变化的接口
     *
     * @author Administrator
     */
    public interface OnAngleColorListener {
        void onAngleColorListener(int red, int green);
    }
}

