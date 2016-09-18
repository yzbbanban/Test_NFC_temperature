package com.wangban.yzbbanban.test_nfc_temperature;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by YZBbanban on 16/9/17.
 */

public class ScaleView extends View {
    private int mWidth;
    private int mHeight;
    private int mPercent;
    private float mTikeWidth;//刻度宽度
    private int mScendArcWidth;//第二个弧的宽度
    private int mMinCircleRadius;//最小圆的半径
    private int mRectWidth;//文字矩形的宽
    private int mRectHeight;//文字矩形的高
    private String mText = "";//文字的内容
    private int mTextSize;//文字的大小
    //文字颜色
    private int mTextColor;
    private int mArcColor;
    //小圆和指针颜色
    private int mMinCircleColor;
    private int mTikeCount;

    private Context mContext;

    private int mBackgroundColor;

    public ScaleView(Context context) {
        this(context, null);
    }

    public ScaleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //加载布局
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScaleView, defStyleAttr, 0);
        mArcColor = a.getColor(R.styleable.ScaleView_arcColor, Color.parseColor("#ff0000"));
        mMinCircleColor = a.getColor(R.styleable.ScaleView_pointerColor, Color.parseColor("#00ffff"));
        mTikeCount = a.getInt(R.styleable.ScaleView_tikeCount, 12);
        mTextSize = a.getDimensionPixelSize(PxUtils.spToPx(R.styleable.ScaleView_android_textSize, mContext), 24);
        mText = a.getString(R.styleable.ScaleView_android_text);
        mBackgroundColor = a.getColor(R.styleable.ScaleView_android_background, Color.parseColor("#0000ff"));
        mScendArcWidth = 50;


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = PxUtils.dpToPx(200, mContext);
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = PxUtils.dpToPx(200, mContext);
        }
        Log.i("wing", "onMeasure: mWidth+");
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    public void draw(Canvas canvas) {
        //最外面的圆
        Paint p = new Paint();
//        描边的宽
        int strokeWidth = 20;
//        设置描边的宽
        p.setStrokeWidth(strokeWidth);
//        一直存在
        p.setAntiAlias(true);

        p.setStyle(Paint.Style.FILL);
        p.setColor(mBackgroundColor);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2 - strokeWidth / 2, p);
//        设置绘画风格:描边
        p.setStyle(Paint.Style.STROKE);
//        设置颜色
        p.setColor(Color.parseColor("#aa00ff"));

//        canvas.drawArc(new RectF(strokeWidth, strokeWidth, mWidth - strokeWidth, mHeight - strokeWidth), 145, 250, false, p);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2 - strokeWidth / 2, p);
// 内弧的矩形
        p.setStrokeWidth(mScendArcWidth);
        p.setColor(Color.MAGENTA);
        RectF secondRectF = new RectF(strokeWidth + 50, strokeWidth + 50, mWidth - strokeWidth - 50, mHeight - strokeWidth - 50);
        float secondRectWidth = mWidth - strokeWidth - 50 - (strokeWidth + 50);
        float secondRectHeight = mHeight - strokeWidth - 50 - (strokeWidth + 50);
        float percent = mPercent / 100f;
//        外弧
//        充满的圆弧度数,-5是大小弧的偏差
        float fill = 250 * percent;
//        空的圆弧度数
        float empty = 250 - fill;
        if (percent == 0) {
            p.setColor(Color.MAGENTA);
        }
        canvas.drawArc(secondRectF, 135, 11, false, p);
        canvas.drawArc(secondRectF, 145, fill, false, p);

        p.setColor(Color.WHITE);
//        画圆弧未充满的部分
        canvas.drawArc(secondRectF, 145 + fill, empty, false, p);
        if (percent == 1) {
            p.setColor(mArcColor);
        }
        canvas.drawArc(secondRectF, 144 + fill + empty, 10, false, p);

//        Paint pp=new Paint();
//        pp.setStrokeWidth(30);
//        RectF r = new RectF(30.0f, 30.0f, mWidth/2-15, mHeight/2-15);
//        pp.setStyle(Paint.Style.STROKE);
//        pp.setColor(Color.GREEN);
//        canvas.drawRect(r, pp);

        p.setStrokeWidth(8);
        p.setColor(Color.YELLOW);
        mMinCircleRadius = 15;
        canvas.drawCircle(mWidth / 2, mHeight / 2, mMinCircleRadius, p);
        //绘制小圆外圈
        p.setStrokeWidth(3);
        p.setColor(mMinCircleColor);
        canvas.drawCircle(mWidth / 2, mHeight / 2, 30, p);

        p.setColor(Color.WHITE);
        mTikeWidth = 20;
        p.setStrokeWidth(3);
        canvas.drawLine(mWidth / 2, 0.0f, mWidth / 2, mTikeWidth, p);
        //        温度
        p.setColor(Color.GREEN);
        p.setStrokeWidth(3);
        p.setTextSize(40);
        String tText = "温度";
        float tLength = p.measureText(tText);
        canvas.drawText(tText, (mWidth - tLength) / 2, secondRectHeight / 2, p);
//        旋转角度
        float rAngle = 250f / mTikeCount;
//        通过旋转画布,绘制右面的刻度
        for (int i = 0; i < mTikeCount / 2; i++) {
            canvas.rotate(rAngle, mWidth / 2, mHeight / 2);
            canvas.drawLine(mWidth / 2, 0.0f, mWidth / 2, mTikeWidth, p);
        }
//        旋转回来
        canvas.rotate(-rAngle * mTikeCount / 2, mWidth / 2, mHeight / 2);
//        左边的刻度
        for (int i = 0; i < mTikeCount / 2; i++) {
            canvas.rotate(-rAngle, mWidth / 2, mHeight / 2);
            canvas.drawLine(mWidth / 2, 0.0f, mWidth / 2, mTikeWidth, p);
        }
//        再旋转回来
        canvas.rotate(rAngle * mTikeCount / 2, mWidth / 2, mHeight / 2);
        //指针
        p.setColor(Color.YELLOW);
        p.setStrokeWidth(10);

        canvas.rotate((250 * percent - 250 / 2), mWidth / 2, mHeight / 2);
        canvas.drawLine(mWidth / 2, (mHeight / 2 - secondRectHeight / 2) + mScendArcWidth / 2 + 5, mWidth / 2, mHeight / 2 - mMinCircleRadius, p);
        //将画布旋转回来
        canvas.rotate(-(250 * percent - 250 / 2), mWidth / 2, mHeight / 2);
//        绘制底部矩形
        p.setStyle(Paint.Style.FILL);
        p.setColor(mMinCircleColor);
        mRectHeight = 25;
        mRectWidth = 60;
//        底部矩形框
        float rectBottomY = mHeight / 2 + secondRectHeight / 3 + mRectHeight;

        canvas.drawRect(mWidth / 2 + secondRectHeight / 2, mHeight / 2 + secondRectHeight / 3, mWidth / 2 + mRectWidth / 2, rectBottomY, p);
//        文字
        p.setTextSize(mTextSize);
        mTextColor = Color.WHITE;
        p.setColor(mTextColor);
        float textLength = p.measureText(mText);
        canvas.drawText(mText, (mWidth - textLength) / 2, rectBottomY + 40, p);


//        刻度数字
        p.setColor(Color.DKGRAY);
        p.setStrokeWidth(2);
        p.setTextSize(24);
        String scaleText = "6";
        float sLength = p.measureText(scaleText);
        canvas.drawText(scaleText, (mWidth - sLength) / 2, mHeight / 2 - secondRectHeight / 2 + 10, p);


        float scaleTextAngle = 250 / mTikeCount;//配合双数,在中心
        for (int i = 0; i < mTikeCount / 2; i++) {
            String sText = String.valueOf(i + 7);
            canvas.rotate(scaleTextAngle, mWidth / 2, mHeight / 2);
            canvas.drawText(sText, (mWidth - sLength) / 2, mHeight / 2 - secondRectHeight / 2 + 10, p);
        }
        canvas.rotate(-scaleTextAngle * mTikeCount / 2, mWidth / 2, mHeight / 2);
        scaleTextAngle = 250f / mTikeCount;
        p.setTextSize(24);
        for (int i = mTikeCount / 2 - 1; i >= 0; i--) {
            String sText = String.valueOf(i);
            canvas.rotate(-scaleTextAngle, mWidth / 2, mHeight / 2);
            canvas.drawText(sText, (mWidth - sLength) / 2, mWidth / 2 - secondRectHeight / 2 + 10, p);
        }
        canvas.rotate(scaleTextAngle * mTikeCount / 2, mWidth / 2, mHeight / 2);

        super.draw(canvas);
    }

    /**
     * 设置百分比
     *
     * @param percent 百分比
     */
    public void setPercent(int percent) {
        mPercent = percent;
        invalidate();
    }

    /**
     * 设置文字
     *
     * @param text 文字
     */
    public void setText(String text) {
        mText = text;
        invalidate();
    }

    /**
     * 设置圆弧颜色
     *
     * @param Color 颜色
     */
    public void setArcColor(int color) {
        mArcColor = color;
        invalidate();
    }

    /**
     * 设置字体大小
     *
     * @param size 字体大小
     */
    public void setTextSize(int size) {
        mTextSize = size;
        invalidate();
    }

    public void setBackgroundColor(int color) {
        mBackgroundColor = color;
        invalidate();
    }

    /**
     * 设置粗弧的宽度
     *
     * @param width 粗弧宽度
     */
    public void setArcWidth(int width) {
        mScendArcWidth = width;
        invalidate();
    }
}
