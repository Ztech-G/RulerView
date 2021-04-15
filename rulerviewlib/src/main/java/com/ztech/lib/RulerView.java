package com.ztech.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class RulerView extends View {
    public static final int X_AXIS = 1;
    public static final int Y_AXIS = 2;
    /**
     * 正向
     */
    public static final int POSITIVE = 3;
    /**
     * 反向
     */
    public static final int NEGATIVE = 4;
    /**
     * x轴还是y轴方向上的尺子
     */
    private int xOry = X_AXIS;
    /**
     * 代表尺子正反
     * 平行放置是 正 代表 数字在上面 反代表数字在下面
     * 垂直放置时 正代表 数字放在左面 反代表数字放在右面
     */
    private int pOrn = NEGATIVE;
    /**
     * 绘制尺子上的线
     */
    private Paint linePaint;
    /**
     * 绘制尺子的文本
     */
    private Paint textPaint;
    /**
     * 刻度线颜色
     */
    private int lineColor = Color.BLACK;
    /**
     * 文本颜色
     */
    private int textColor = Color.BLACK;
    /**
     * 刻度线宽度
     */
    private float lineWidth = 2f;
    /**
     * 刻度线的长度
     */
    private float lineLength = 10f;
    /**
     * 尺子长度 单位mm
     */
    private int lengthOfRuler = 100;
    /**
     * 文本大小
     */
    private float textSize = 20f;
    /**
     * 开始位置的内边距 为了让第一位的0能正常显示
     */
    private float startPadding;
    private Context context;

    public RulerView(Context context) {
        super(context);
        init(context,null);
    }

    public RulerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RulerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public RulerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        switch (xOry) {
            case X_AXIS:
                setMeasuredDimension((int)(startPadding+ TypeValueUtil.getPxByMmX(lengthOfRuler, context)+getFontWidth(String.valueOf(lengthOfRuler))),(int)(lineLength+getFontHeight()));
                break;
            case Y_AXIS:
                setMeasuredDimension((int)(lineLength+getFontWidth(String.valueOf(lengthOfRuler))),(int)(startPadding+ TypeValueUtil.getPxByMmY(lengthOfRuler, context)+getFontHeight()));
                break;
            default:
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                break;
        }
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RulerView);
        xOry = a.getInteger(R.styleable.RulerView_xOry,X_AXIS);
        pOrn = a.getInteger(R.styleable.RulerView_pOrn,POSITIVE);
        lineColor = a.getColor(R.styleable.RulerView_lineColor,Color.BLACK);
        textColor = a.getColor(R.styleable.RulerView_textColor,Color.BLACK);
        lineWidth = TypeValueUtil.getPxByDp(a.getDimension(R.styleable.RulerView_lineWidth,2),context);
        lineLength = TypeValueUtil.getPxByDp(a.getDimension(R.styleable.RulerView_lineLength,20),context);
        textSize = TypeValueUtil.getPxBySp(a.getDimension(R.styleable.RulerView_textSize,20),context);
        lengthOfRuler = a.getInteger(R.styleable.RulerView_lengthOfRuler,100);
        a.recycle();
        linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(lineWidth);
        textPaint = new Paint();
        textPaint.setColor(textColor);
        linePaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        this.context = context;
        startPadding = getFontWidth("0");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (xOry) {
            case X_AXIS:
                drawXAxis(canvas);
                break;
            case Y_AXIS:
                drawYAxis(canvas);
                break;
            default:
                break;
        }
    }

    private void drawXAxis(Canvas canvas) {
        switch (pOrn) {
            case POSITIVE:
                //1.先绘制刻度线和数字
                drawPositiveTextAndLines(canvas);
                //2.现在顶部绘制一条直线
                canvas.drawLine(
                        startPadding,
                        lineLength + getFontHeight(),
                        startPadding + TypeValueUtil.getPxByMmX(lengthOfRuler, context),
                        lineLength + getFontHeight(),
                        linePaint);
                break;
            case NEGATIVE:
                //1.现在顶部绘制一条直线
                canvas.drawLine(
                        startPadding,
                        0,
                        startPadding + TypeValueUtil.getPxByMmX(lengthOfRuler, context),
                        0,
                        linePaint);
                //2.循环绘制刻度线
                drawNegativeTextAndLines(canvas);
                break;
            default:
                break;
        }

    }

    private void drawYAxis(Canvas canvas) {
        switch (pOrn) {
            case POSITIVE:
                //1.现在顶部绘制一条直线
                //起点: x = 最大刻度值对应的文本的宽度 + 刻度线的最大长度 ，y = 开始的内边距（startPadding）
                //终点：x = 最大刻度值对应的文本的宽度 + 刻度线的最大长度 , y = 开始的内边距（startPadding）+ 尺子的长度
                canvas.drawLine(getFontWidth(String.valueOf(lengthOfRuler)) + lineLength,
                        startPadding,
                        getFontWidth(String.valueOf(lengthOfRuler)) + lineLength,
                        startPadding + TypeValueUtil.getPxByMmY(lengthOfRuler, context),
                        linePaint);
                drawPositiveTextAndLines(canvas);
                break;
            case NEGATIVE://反向 先绘制一个线 再绘制刻度和数字
                //1.现在顶部绘制一条直线
                //起点: x = 0 ，y = 开始的内边距（startPadding）
                //终点：x = 0 , y = 开始的内边距（startPadding）+ 尺子的长度
                canvas.drawLine(0,
                        startPadding,
                        0,
                        startPadding + TypeValueUtil.getPxByMmY(lengthOfRuler, context),
                        linePaint);
                drawNegativeTextAndLines(canvas);
                break;
            default:
                break;
        }
    }

    /**
     * 绘制正向的刻度线和刻度值
     * @param canvas 画布
     */
    private void drawPositiveTextAndLines(Canvas canvas) {
        for (int i = 0; i < lengthOfRuler + 1; i++) {
            if (i % 5 == 0) {
                //当前长度除5取余为0代表整除  0 5 10 15 20 ...
                int remainder = i / 5;
                if (remainder % 2 == 0) {
                    //商除2整除表示是 0 10 20 ...
                    drawText(canvas, i, getTextLength());
                    drawPositiveLine(canvas, i, getTextLength(), lineLength + getTextLength());
                } else {
                    drawPositiveLine(canvas, i, lineLength * 0.3f + getTextLength(), lineLength + getTextLength());
                }
            } else {
                drawPositiveLine(canvas, i, lineLength * 0.7f + getTextLength(), lineLength + getTextLength());
            }
        }
    }

    /**
     * 绘制反向的刻度线和刻度值
     * @param canvas 画布
     */
    private void drawNegativeTextAndLines(Canvas canvas) {
        //2.绘制数字和刻度线
        for (int i = 0; i < lengthOfRuler + 1; i++) {
            if (i % 5 == 0) {
                //当前长度除5取余为0代表整除  0 5 10 15 20 ...
                int remainder = i / 5;
                if (remainder % 2 == 0) {
                    //商除2整除表示是 0 10 20 ...
                    drawNegativeLine(canvas, i, lineLength);
                    drawText(canvas, i, lineLength + getTextLength());
                } else {
                    drawNegativeLine(canvas, i, lineLength * 0.7f);
                }
            } else {
                drawNegativeLine(canvas, i, lineLength * 0.3f);
            }
        }
    }

    /**
     * 绘制刻度
     *
     * @param canvas 画布
     * @param degreeScale 刻度值
     * @param y 文本开始点的距离 相对于x轴或y轴
     */
    private void drawText(Canvas canvas, int degreeScale, float y) {
        switch (xOry) {
            case X_AXIS:
                canvas.drawText(String.valueOf(degreeScale)
                        , startPadding + TypeValueUtil.getPxByMmX(degreeScale, context) - getFontWidth(String.valueOf(degreeScale)) / 2
                        , y, textPaint);
                break;
            case Y_AXIS:
                canvas.drawText(String.valueOf(degreeScale)
                        , y - getTextLength()
                        , startPadding + TypeValueUtil.getPxByMmY(degreeScale, context) + getFontHeight() / 2f, textPaint);
                break;
            default:
                break;
        }

    }

    /**
     * 绘制反向的刻度线 根据不同的坐标轴绘制不同的线
     *
     * @param canvas 画布
     * @param x      当xOry为x轴时 此字段表示线段的x坐标，y轴时为线段的y坐标
     * @param length 当前刻度线的长度
     */
    private void drawNegativeLine(Canvas canvas, int x, float length) {
        switch (xOry) {
            case X_AXIS:
                canvas.drawLine(startPadding + TypeValueUtil.getPxByMmX(x, context),
                        0,
                        startPadding + TypeValueUtil.getPxByMmX(x, context),
                        length,
                        linePaint);
                break;
            case Y_AXIS:
                canvas.drawLine(0,
                        startPadding + TypeValueUtil.getPxByMmY(x, context),
                        length,
                        startPadding + TypeValueUtil.getPxByMmY(x, context),
                        linePaint);
                break;
            default:
                break;
        }
    }

    /**
     * 绘制正向的刻度线
     * @param canvas 画布
     * @param x 刻度值
     * @param startPoint 开始点的位置 y值
     * @param endPoint 结束点的位置 y值
     */
    private void drawPositiveLine(Canvas canvas, int x, float startPoint, float endPoint) {
        switch (xOry) {
            case X_AXIS:
                //起点：x = 开始内边距 + 刻度值， y= 开始的位置
                //终点：x = 开始内边距 + 刻度值， y= 结束的位置
                canvas.drawLine(startPadding + TypeValueUtil.getPxByMmX(x, context),
                        startPoint,
                        startPadding + TypeValueUtil.getPxByMmX(x, context),
                        endPoint, linePaint);
                break;
            case Y_AXIS:
                //起点：x = 开始内边距 + 刻度值， y= 开始的位置
                //终点：x = 开始内边距 + 刻度值， y= 结束的位置
                canvas.drawLine(startPoint,
                        startPadding + TypeValueUtil.getPxByMmY(x, context),
                        endPoint,
                        startPadding + TypeValueUtil.getPxByMmY(x, context), linePaint);
                break;
            default:
                break;
        }

    }

    /**
     * 获取字体高度
     * @return
     */
    public int getFontHeight() {
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.top);
    }

    /**
     * 获取字体宽度
     * @param text
     * @return
     */
    public float getFontWidth(String text) {
        return textPaint.measureText(text);
    }

    /**
     * 获取文本长度
     * @return
     */
    public float getTextLength() {
        float result = 0;
        switch (xOry) {
            case X_AXIS:
                result = getFontHeight();
                break;
            case Y_AXIS:
                result = getFontWidth(String.valueOf(lengthOfRuler));//要返回最大刻度的文本长度，防止文本控件太小
                break;
            default:
                break;
        }
        return result;
    }

    public int getxOry() {
        return xOry;
    }

    public void setxOry(int xOry) {
        this.xOry = xOry;
    }

    public int getpOrn() {
        return pOrn;
    }

    public void setpOrn(int pOrn) {
        this.pOrn = pOrn;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public float getLineLength() {
        return lineLength;
    }

    public void setLineLength(float lineLength) {
        this.lineLength = lineLength;
    }

    public int getLengthOfRuler() {
        return lengthOfRuler;
    }

    public void setLengthOfRuler(int lengthOfRuler) {
        this.lengthOfRuler = lengthOfRuler;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }
}
