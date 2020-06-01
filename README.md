## 一、前言

Android 自定义 View 是高级进阶不可或缺的内容，日常工作中，经常会遇到产品、UI 设计出花里胡哨的界面。当系统自带的控件不能满足开发需求时，就只能自己动手撸一个效果。

本文就带自定义 View 初学者手动撸一个效果，通过自定义 View 实现圆形进度条功能，**每行代码都有注释，保证易懂，看不懂你留言打我！！！**

## 二、实现效果


### 先看效果图

![在这里插入图片描述](http://www.ssbbww.com/u/s/20200601090853u3193-.gif)


### 步骤分析

实现以上效果，主要分为四个步骤：

1. 自定义属性
2. 绘制圆环
3. 绘制圆弧
4. 更新进度条
5. 绘制进度百分比

## 三、代码实现

### 1、自定义属性

为了实现绚丽多彩的环形进度条，将颜色、尺寸、风格等属性抽离自定义属性，这样就可以直接在 xml 文件中设置，根据项目徐需求可以更方便使用。这里将自定义属性的步骤详解说明一下：

1. 在 res/values 文件夹下新建 attrs.xml，将需要自定义的属性申明定义：

```
  <attr name="annulusColor" format="color"/>//圆环颜色
  <attr name="loadColor" format="color"/>//环形进度条加载颜色
  <attr name="annulusWidth" format="dimension"/>//圆环宽度
  <attr name="progress" format="integer"/>//圆环进度
  <attr name="textColor" format="color"/>//文本颜色
  <attr name="textSize" format="dimension"/>//文本字体大小
  <attr name="progressType">//环形进度条类型：0.实心 1.空心
      <enum name="fill" value="0"/>
      <enum name="stroke" value="1"/>
  </attr>
  <attr name="isShowText">//是否显示文本：0.显示 1.不显示
      <enum name="yes" value="0"/>
      <enum name="no" value="1"/>
  </attr>

  <declare-styleable name="AnnulusCustomizeView">
      <attr name="annulusColor"/>
      <attr name="loadColor"/>
      <attr name="annulusWidth"/>
      <attr name="progress"/>
      <attr name="textColor"/>
      <attr name="textSize"/>
      <attr name="progressType"/>
      <attr name="isShowText"/>
  </declare-styleable>
```

2. 在自定义 View 类中重写带有三个参数的构造方法，然后获取自定义属性：

```
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
```

3. 在布局页面 xml 中应用自定义属性，记得在父布局添加命名空间：

`xmlns:app="http://schemas.android.com/apk/res-auto"`

```
  <com.caobo.customizeview.view.AnnulusCustomizeView
      android:layout_marginTop="100dp"
      app:layout_constraintStart_toStartOf="parent"
      app:layout_constraintBottom_toBottomOf="parent"
      app:layout_constraintEnd_toEndOf="parent"
      app:layout_constraintTop_toTopOf="parent"
      android:id="@+id/mAnnulusCustomizeView3"
      android:layout_width="200dp"
      android:layout_height="200dp"
      android:padding="20dp"
      app:isShowText="yes"
      app:annulusColor="#BB1"
      app:annulusWidth="15dp"
      app:loadColor="#CCC"
      app:progress="0"
      app:progressType="stroke"
      app:textColor="#000000"
      app:textSize="50dp" />
```

以上就完成了自定义属性的声明、获取、应用的全部过程，android 中自带的很多 View 源码都有相关属性，可以自己查阅源码学习，其实也很简单。

### 2、绘制圆环

圆环绘制 canvas.drawCircle()完成，定义圆环的 x、y 轴位置，半径大小，设置画笔相关属性即可轻松完成。

```
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
```

### 3、绘制圆弧

圆弧是进度条更新时，所扫过的范围，圆弧绘制使用 canvas.drawArc()方法，具体绘制方法参数，这里不做详细描述，如果还不会的朋友，建议先补习一下 canvas 和 paint 相关 API 方法。

```
switch (mProgressType) {
  case STROKE:
      mPaint.setStyle(Paint.Style.STROKE);
      // 用于定义的圆弧的形状和大小的界限
      RectF ovalStroke = new RectF(centre - radius, centre - radius, centre + radius, centre + radius);
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
```

圆弧绘制根据相关自定义属性，定义圆弧的形状和大小的界限，很容易就可以完成。

### 4、更新进度条

本文通过线程睡眠来模拟进度条更新，真实项目中会根据下载或者上传进度来实时更新。

在自定义 View 中调用 postInvalidate()方法实时刷新绘制 View，实现进度条更新效果。

```
/**
 * 模拟数据
 */
private void setProgress() {
    new Thread() {
        @Override
        public void run() {
            for (int i = 1; i <= 100; i++) {
                mAnnulusCustomizeView1.setProgress(i);
                mAnnulusCustomizeView2.setProgress(i);
                mAnnulusCustomizeView3.setProgress(i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }.start();
}
```

```
/**
 * 根据外部进度传递更新View
 *
 * @param progress
 */
public synchronized void setProgress相关(final int progress) {
    this.mProgress = progress;
    new Thread() {
        @Override
        public void run() {
            if (mProgress == 100) {} // 完毕
            postInvalidate();
        }
    }.start();
}
```

### 5、绘制文本

进度条文本显示在圆环中心，定义文本的 x、y 轴位置，文本大小和颜色，就可以使用 canvas.drawText()方法完成绘制。

1. 测量文本高度

文本 0%——100%高度固定不变，所以可以使用 Paint.getTextBounds()方法，提前测量出文本高度：

```
// 返回在边界最小矩形，用户测量文本高度，因为文本高度根据字体大小固定
mPaint.getTextBounds("%", 0, "%".length(), rect);
```

2. 测量文本宽度

使用 Paint.measureText()方法，根据实时更新进度百分比，获取文本宽度

```
// 测量文本宽度
float measureTextWidth = mPaint.measureText(percentContext + "%");
```

3. 定义文本 x、y 轴位置

因为文本的 x、y 轴位置不在文本正中心，而在文本大概右下角位置，所以文本 x、y 轴位置测量方式需要大家注意：

```
int x = centre - measureTextWidth / 2;
int y = centre + rect.height() / 2;
```

4. 文本绘制

以上准备工作都就绪后，就可以开始绘制文本了。

```
canvas.drawText(percentContext + "%", centre - measureTextWidth / 2, centre + rect.height() / 2, mPaint);
```

完成以上所有工作，就可以实现一个绚丽的环形进度条功能了，是不是很简单。

## 总结

自定 view 在 Android 开发过程中应用极其广泛，为了更好的掌握，建议从自定义 View 绘制流程、Canvas、Paint、Path、onLayout()、onMeasure()、onDraw()系统化学习，然后上手多做练习，这样势必会对自定义 View 有很好的提升！

希望本文对初学自定义 View 的朋友有所帮助。

前文说过，保证每个自定义 View 初学者都能看懂，因为每行代码都会添加注释，如果没看懂的留言打我！！！
