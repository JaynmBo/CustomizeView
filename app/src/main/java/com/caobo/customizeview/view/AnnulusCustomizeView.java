package com.caobo.customizeview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.caobo.customizeview.R;

import androidx.annotation.Nullable;

/**
 * Created by Jaynm
 * on 2020-05-23.
 */
public class AnnulusCustomizeView extends View {
    // 画笔
    private Paint mPaint;
    // 圆环宽度
    private int mAnnulusWidth;
    // 圆环颜色
    private int mAnnulusColor;
    // 加载进度圆弧扫过的颜色
    private int mLoadColor;
    // 百分比文本颜色
    private int mTextColor;
    // 百分比文本大小
    private int mTextSize;
    // 进度
    private int mProgress = 0;
    // 最大进度，默认100
    private int maxProgress = 100;
    // 类型：0代表实心  1代表空心
    private int mProgressType;
    public static final int FILL = 0;
    public static final int STROKE = 1;

    // 是否显示百分比文本
    private int mIsShowText;

    private Rect rect;

    public AnnulusCustomizeView(Context context) {
        this(context, null);
    }

    public AnnulusCustomizeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 如果含有自定义属性，则重写带有三个参数的构造方法
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public AnnulusCustomizeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取自定义属性
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AnnulusCustomizeView, defStyleAttr, 0);
        int indexCount = a.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int aIndex = a.getIndex(i);
            switch (aIndex) {
                case R.styleable.AnnulusCustomizeView_annulusWidth:
                    mAnnulusWidth = a.getDimensionPixelSize(aIndex,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                    10,
                                    getResources().getDisplayMetrics()));
                    break;
                case R.styleable.AnnulusCustomizeView_annulusColor:
                    mAnnulusColor = a.getColor(aIndex, Color.BLACK);
                    break;
                case R.styleable.AnnulusCustomizeView_loadColor:
                    mLoadColor = a.getColor(aIndex, Color.BLACK);
                    break;
                case R.styleable.AnnulusCustomizeView_textColor:
                    mTextColor = a.getColor(aIndex, Color.BLACK);
                    break;
                case R.styleable.AnnulusCustomizeView_textSize:
                    mTextSize = a.getDimensionPixelSize(aIndex,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                    15,
                                    getResources().getDisplayMetrics()));
                    break;
                case R.styleable.AnnulusCustomizeView_progressType:
                    mProgressType = a.getInt(aIndex, 1);
                    break;
                case R.styleable.AnnulusCustomizeView_isShowText:
                    mIsShowText = a.getInt(aIndex, 1);
                    break;
                case R.styleable.AnnulusCustomizeView_progress:
                    mProgress = a.getInt(aIndex, 10);
                    break;
            }
        }
        a.recycle();

        mPaint = new Paint();
        rect = new Rect();
        // 设置画笔颜色
        mPaint.setTextSize(mTextSize);
        // 返回在边界最小矩形，用户测量文本高度，因为文本高度根据字体大小固定
        mPaint.getTextBounds("%", 0, "%".length(), rect);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO:绘制圆环
        // 获取圆形坐标
        int centre = getWidth() / 2;
        // 获取半径
        int radius = centre - mAnnulusWidth / 2;
        // 取消锯齿
        mPaint.setAntiAlias(true);
        // 设置画笔宽度
        mPaint.setStrokeWidth(mAnnulusWidth);
        // 设置空心
        mPaint.setStyle(Paint.Style.STROKE);
        // 设置画笔颜色
        mPaint.setColor(mAnnulusColor);
        canvas.drawCircle(centre, centre, radius, mPaint);

        // TODO:画圆弧，进度
        // 设置进度颜色
        mPaint.setColor(mLoadColor);
        mPaint.setStrokeWidth(mAnnulusWidth);
        switch (mProgressType) {
            case STROKE:
                mPaint.setStyle(Paint.Style.STROKE);
                // 用于定义的圆弧的形状和大小的界限
                RectF ovalStroke = new RectF(centre - radius, centre - radius,
                        centre + radius, centre + radius);
                /**
                 startAngle：从-90°开始，也就是钟表的12点钟位置。
                 sweepAngle：圆弧扫过的角度
                 useCenter：设置我们的圆弧在绘画的时候，是否经过圆形，当Paint.Style.STROKE时，true无效果
                 */
                canvas.drawArc(ovalStroke, -90, mProgress * 360 / maxProgress, false, mPaint);
                break;

            case FILL:
                mPaint.setStyle(Paint.Style.FILL);
                // 用于定义的圆弧的形状和大小的界限
                RectF ovalFill = new RectF(centre - radius - mAnnulusWidth / 2, centre - radius - mAnnulusWidth / 2,
                        centre + radius + mAnnulusWidth / 2, centre + radius + mAnnulusWidth / 2);
                canvas.drawArc(ovalFill, -90, mProgress * 360 / maxProgress, true, mPaint);
                break;
        }

        // 如果不显示文本，直接return
        if (mIsShowText == 1)
            return;
        // TODO：绘制文本
        // 计算圆弧进度获取文本内容
        int percentContext = (int) ((float) mProgress / (float) maxProgress * (float) 100);
        // 设置绘制文本画笔颜色
        mPaint.setColor(mTextColor);
        // 设置绘制文本画笔风格
        mPaint.setStyle(Paint.Style.FILL);
        // 设置绘制文本画笔宽度，可添加自定义属性
        mPaint.setStrokeWidth(3);
        // 测量文本宽度
        float measureTextWidth = mPaint.measureText(percentContext + "%");
        canvas.drawText(percentContext + "%", centre - measureTextWidth / 2, centre + rect.height() / 2, mPaint);
    }

    /**
     * 根据外部进度传递更新View
     *
     * @param progress
     */
    public synchronized void setProgress(final int progress) {
        this.mProgress = progress;
        new Thread() {
            @Override
            public void run() {
                if (mProgress == 100) {} // 完毕
                postInvalidate();
            }
        }.start();
    }

    public synchronized int getmProgress() {
        return mProgress;
    }
}
