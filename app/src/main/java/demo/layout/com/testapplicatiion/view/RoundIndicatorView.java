package demo.layout.com.testapplicatiion.view;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

import java.text.DecimalFormat;

import demo.layout.com.testapplicatiion.R;


/**
 * 测速表盘
 *
 * @author IVRING
 * @created at 2017/02/03 上午11:23
 */

public class RoundIndicatorView extends View {
    private static final String TAG = "RoundIndicatorView";

    private       Paint          paint;
    private       Paint          paint_2;
    private       Paint          paint_3;
    private       Paint          paint_4;
    private       float          maxNum;
    private       int            startAngle;
    private       int            sweepAngle;
    private       int            radius;
    private       int            mWidth;
    private       int            mHeight;
    private       int            sweepInWidth;//内圆的宽度
    private       int            sweepOutWidth;//外圆的宽度
    private final int            mOutInGapWidth = dp2px(10);//内外圆间隔
    private       float          currentNum     = 0;//需设置setter、getter 供属性动画使用
    private       int            mTopPadding    = 0;//表盘距离上边的边距
    private final int[]          indicatorColor = {0xffffffff, 0x00ffffff, 0x99ffffff, 0xffffffff};
    private final float[]        markArr        = {0f, 0.5f, 1f, 2f, 4f, 10f, 30f, 60f, 100f, 160f, 260f};
    private       ObjectAnimator anim;
    private final DecimalFormat  mDecimalFormat;
    private       int            mPerSweepAge;

    public float getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(float currentNum) {
        this.currentNum = currentNum;
        int color = calculateColor260(currentNum);
        setBackgroundColor(color);
        invalidate();
    }

    {
        //保留8位整数，两位非零的小数。
        mDecimalFormat = new DecimalFormat("########.##");
        anim = ObjectAnimator.ofFloat(this, "currentNum", 0);
        initPaint();
    }

    public RoundIndicatorView(Context context) {
        this(context, null);
    }

    public RoundIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundIndicatorView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(0xFF4492D8);
        initAttr(attrs, context);
    }

    public void setCurrentNumAnim(float num) {
//        float duration = (float)Math.abs(num-currentNum)/maxNum *1500+500; //根据进度差计算动画时间
//        将动画时间设置为1s
        float duration = (float) 1000;
        anim.setDuration((long) Math.min(duration, 1500));
        anim.setFloatValues(num);
        anim.start();
    }


    /**
     * 设置表针最后颜色
     *
     * @author IVRING
     * @time 2017/2/22 15:45
     */
    public void setLastBackgroundTwo() {
        setCurrentNumAnim(0);
    }

    /**
     * 0-260M时，计算颜色渐变值
     *
     * @author IVRING
     * @time 2017/3/20 15:39
     */
    private int calculateColor260(float value) {
        if (value > maxNum) {
            return 0xFF3FB830;
        }
        ArgbEvaluator evealuator = new ArgbEvaluator();
        float         fraction   = 0;
        int           color      = 0;
        if (value <= 1) {
            fraction = (float) value / 1;
            color = (int) evealuator.evaluate(fraction, 0xFF4492D8, 0xFF00CED1); //由蓝到天蓝
        } else if (value <= 4) {
            fraction = ((float) value - 1) / 3;
            color = (int) evealuator.evaluate(fraction, 0xFF00CED1, 0xFFFF9800); //由天蓝到黄
        } else if (value <= 60) {
            fraction = ((float) value - 4) / 56;
            color = (int) evealuator.evaluate(fraction, 0xFFFF9800, 0xFFFFC107); //由黄到橙
        } else {
            fraction = ((float) value - 60) / 200;
            color = (int) evealuator.evaluate(fraction, 0xFFFFC107, 0xFF3FB830); //由橙到绿
        }
        return color;
    }

    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xffffffff);
        paint_2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_4 = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private void initAttr(AttributeSet attrs, Context context) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundIndicatorView);
        maxNum = array.getFloat(R.styleable.RoundIndicatorView_maxNum, 260);
        startAngle = array.getInt(R.styleable.RoundIndicatorView_startAngle, 160);
        sweepAngle = array.getInt(R.styleable.RoundIndicatorView_sweepAngle, 220);
        mTopPadding = array.getInt(R.styleable.RoundIndicatorView_heightPadding, 60);
        //计算平均每个区间的角度
        mPerSweepAge = sweepAngle / (markArr.length - 1);
        //内外圆的宽度
        sweepInWidth = dp2px(8);
        sweepOutWidth = dp2px(3);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);

        if (wMode == MeasureSpec.EXACTLY) {
            mWidth = wSize;
        } else {
            mWidth = dp2px(300);
        }
        if (hMode == MeasureSpec.EXACTLY) {
            mHeight = hSize;
        } else {
//            mHeight = dp2px(300);
            mHeight = mWidth * 3 / 4;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = getMeasuredWidth() * 2 / 9; //不要在构造方法里初始化，那时还没测量宽高
        mWidth = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(mWidth / 2, radius + sweepInWidth + sweepOutWidth + mTopPadding);
        drawRound(canvas);  //画内外圆
        drawScaleMark(canvas);//画刻度
        drawIndicator(canvas); //画当前进度值
        drawCenterText(canvas);//画中间的文字
        canvas.restore();
    }

    /**
     * 画中间实时速度文字
     *
     * @author IVRING
     * @time 2017/3/20 10:58
     */
    private void drawCenterText(Canvas canvas) {
        paint_4.setStyle(Paint.Style.FILL);
        canvas.save();
        paint_4.setTextSize(radius / 3);
        paint_4.setColor(0xffffffff);
        String speed   = "0.00";
        String content = "Mb/S";

        if (currentNum < 1) {
            speed = (int) currentNum * 1024 + "";
            content = "Kb/s";
        } else if (currentNum >= 1) {
            speed = mDecimalFormat.format(currentNum);
            content = "Mb/S";
        }
        canvas.drawText(speed + "", -paint_4.measureText(speed) / 2, 0, paint_4);
        paint_4.setTextSize(radius / 3);
        Rect r = new Rect();
        paint_4.getTextBounds(content, 0, content.length(), r);
        canvas.drawText(content, -r.width() / 2, r.height() + 20, paint_4);
        canvas.restore();
    }

    /**
     * 画指针
     *
     * @author IVRING
     * @time 2017/3/20 10:58
     */
    private void drawIndicator(Canvas canvas) {
        canvas.save();
        paint_2.setStyle(Paint.Style.STROKE);
//      计算旋转角度方法
        float sweep = calculateSweep();
        paint_2.setStrokeWidth(sweepOutWidth);
        Shader shader = new SweepGradient(0, 0, indicatorColor, null);
        paint_2.setShader(shader);
        RectF rectf = new RectF(-radius - mOutInGapWidth, -radius - mOutInGapWidth, radius + mOutInGapWidth, radius + mOutInGapWidth);
        //画尾线
        canvas.drawArc(rectf, startAngle, sweep, false, paint_2);
        float x = (float) ((radius + mOutInGapWidth) * Math.cos(Math.toRadians(startAngle + sweep)));
        float y = (float) ((radius + mOutInGapWidth) * Math.sin(Math.toRadians(startAngle + sweep)));
        paint_3.setStyle(Paint.Style.FILL);
        paint_3.setColor(0xffffffff);
        //设置模糊过滤器半径
        setLayerType(LAYER_TYPE_SOFTWARE, null);//关闭硬件加速
        paint_3.setMaskFilter(new BlurMaskFilter(dp2px(3), BlurMaskFilter.Blur.SOLID)); //需关闭硬件加速
        //画点
        canvas.drawCircle(x, y, dp2px(3), paint_3);
        canvas.restore();
    }

    /**
     * 计算左边界
     *
     * @param arr
     * @param target
     * @return
     */
    private int binarySearchLeftIndex(float[] arr, float target) {
        int left  = 0;
        int right = arr.length - 1;
        //结束条件是left = right - 1；就是left比right大1
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) {
                //寻找left边界
                right = mid - 1;
            } else if (arr[mid] > target) {
                right = mid - 1;
            } else if (arr[mid] < target) {
                left = mid + 1;
            }
        }
        //判断left是否越界
        if (left >= arr.length) {
            return -1;
        }
        //匹配不到，就把left-1
        if (arr[left] != target) {
            return left - 1;
        }
        return left;
    }

    /**
     * 0-260M,计算指针旋转角度
     *
     * @author IVRING
     * @time 2017/3/20 14:34
     */
    private float calculateSweep() {
        float result = 0;
        //获取currentNum在数组中的左边界
        int leftIndex = binarySearchLeftIndex(markArr, currentNum);
        //根据左边界计算当前角度
        if (leftIndex != -1 && leftIndex + 1 < markArr.length) {
            float left  = markArr[leftIndex];
            float right = markArr[leftIndex + 1];
            //左边已经划过的角度 + 当前区间划过的比例
            result = leftIndex * mPerSweepAge + ((currentNum - left) / (right - left) * mPerSweepAge);
        }
        return result;
    }

    /**
     * 画刻度
     *
     * @author IVRING
     * @time 2017/3/20 10:59
     */
    private void drawScaleMark(Canvas canvas) {
        canvas.save();
        float angle = (float) sweepAngle / 50;//刻度间隔
        canvas.rotate(-270 + startAngle); //将起始刻度点旋转到正上方（270)
//        定义一个标记，记录画的刻度值次数
        for (int i = 0; i <= 50; i++) {
            if (i % 5 == 0) {   //画粗刻度和刻度值
                paint.setStrokeWidth(dp2px(2));
                paint.setAlpha(0x70);
                canvas.drawLine(0, -radius - sweepInWidth / 2, 0, -radius + sweepInWidth / 2 +
                        dp2px(1), paint);
//              画刻度值
                drawText(canvas, mDecimalFormat.format(markArr[i / 5]), paint);
            } else {         //画细刻度
                paint.setStrokeWidth(dp2px(1));
                paint.setAlpha(0x50);
                canvas.drawLine(0, -radius - sweepInWidth / 2, 0, -radius + sweepInWidth / 2,
                        paint);
            }
            canvas.rotate(angle); //逆时针
        }
        canvas.restore();
    }

    /**
     * 设置刻度文字大小
     *
     * @param canvas
     * @param text
     * @param paint
     */
    private void drawText(Canvas canvas, String text, Paint paint) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xffffffff);
        paint.setTextSize(sp2px(12));
        float width = paint.measureText(text); //相比getTextBounds来说，这个方法获得的类型是float，更精确些
        canvas.drawText(text, -width / 2, -radius + dp2px(15), paint);
        paint.setStyle(Paint.Style.STROKE);
    }

    private void drawRound(Canvas canvas) {
        canvas.save();
        //内圆
        paint.setAlpha(0x40);
        paint.setStrokeWidth(sweepInWidth);
        RectF rectf = new RectF(-radius, -radius, radius, radius);
        canvas.drawArc(rectf, startAngle, sweepAngle, false, paint);

        //外圆
        paint.setStrokeWidth(sweepOutWidth);
        RectF rectf2 = new RectF(-radius - mOutInGapWidth, -radius - mOutInGapWidth, radius + mOutInGapWidth, radius + mOutInGapWidth);
        canvas.drawArc(rectf2, startAngle, sweepAngle, false, paint);
        canvas.restore();
    }


    //一些工具方法
    protected int dp2px(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics());
    }

    protected int sp2px(int sp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                getResources().getDisplayMetrics());
    }

    public static DisplayMetrics getScreenMetrics(Context context) {
        WindowManager  wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }
}
