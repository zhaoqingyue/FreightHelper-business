/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: LinePieView.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.customview
 * @Description: 自定义饼状统计图
 * @author: zhaoqy
 * @date: 2017年6月6日 下午12:12:37
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.view.View;

import com.mtq.bus.freighthelper.R;

public class LinePieView extends View {

	/**
	 * 使用wrap_content时默认的尺寸
	 */
	private static final int DEFAULT_WIDTH = 800;
	private static final int DEFAULT_HEIGHT = 800;

	/**
	 * 中心坐标
	 */
	private int centerX;
	private int centerY;

	/**
	 * 半径
	 */
	private float radius;

	/**
	 * 弧形外接矩形
	 */
	private RectF rectF;

	/**
	 * 中间文本的大小
	 */
	private Rect centerTextBound = new Rect();

	/**
	 * 数据文本的大小
	 */
	private Rect dataTextBound = new Rect();

	/**
	 * 扇形画笔
	 */
	private Paint mArcPaint;

	/**
	 * 中心文本画笔
	 */
	private Paint centerTextPaint;

	/**
	 * 数据画笔
	 */
	private Paint dataPaint;

	/**
	 * 数据源数字数组
	 */
	private int[] numbers;

	/**
	 * 数据源名称数组
	 */
	// private String[] names;

	/**
	 * 数据源总和
	 */
	private int sum;

	/**
	 * 颜色数组
	 */
	private int[] colors;

	// 自定义属性 Start

	/**
	 * 中间字体大小
	 */
	private float centerTextSize = 80;

	/**
	 * 数据字体大小
	 */
	private float dataTextSize = 40;

	/**
	 * 中间字体颜色
	 */
	private int centerTextColor = Color.BLACK;

	/**
	 * 数据字体颜色
	 */
	private int dataTextColor = Color.BLACK;

	/**
	 * 圆圈的宽度
	 */
	private float circleWidth = 50;

	// 自定义属性 End

	public LinePieView(Context context) {
		super(context);
		init();
	}

	public LinePieView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LinePieView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.PieView);
		centerTextSize = typedArray.getDimension(
				R.styleable.PieView_centerTextSize, centerTextSize);
		dataTextSize = typedArray.getDimension(
				R.styleable.PieView_dataTextSize, dataTextSize);
		circleWidth = typedArray.getDimension(R.styleable.PieView_circleWidth,
				circleWidth);
		centerTextColor = typedArray.getColor(
				R.styleable.PieView_centerTextColor, centerTextColor);
		dataTextColor = typedArray.getColor(R.styleable.PieView_dataTextColor,
				dataTextColor);
		typedArray.recycle();
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		mArcPaint = new Paint();
		mArcPaint.setStrokeWidth(circleWidth);
		mArcPaint.setAntiAlias(true);
		mArcPaint.setStyle(Paint.Style.STROKE);

		centerTextPaint = new Paint();
		centerTextPaint.setTextSize(centerTextSize);
		centerTextPaint.setAntiAlias(true);
		centerTextPaint.setColor(centerTextColor);

		dataPaint = new Paint();
		dataPaint.setStrokeWidth(2);
		dataPaint.setTextSize(dataTextSize);
		dataPaint.setAntiAlias(true);
		dataPaint.setColor(dataTextColor);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int measureWidthSize = MeasureSpec.getSize(widthMeasureSpec);
		int measureHeightSize = MeasureSpec.getSize(heightMeasureSpec);
		int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
		int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (measureWidthMode == MeasureSpec.AT_MOST
				&& measureHeightMode == MeasureSpec.AT_MOST) {
			setMeasuredDimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		} else if (measureWidthMode == MeasureSpec.AT_MOST) {
			setMeasuredDimension(DEFAULT_WIDTH, measureHeightSize);
		} else if (measureHeightMode == MeasureSpec.AT_MOST) {
			setMeasuredDimension(measureWidthSize, DEFAULT_HEIGHT);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		centerX = getMeasuredWidth() / 2;
		centerY = getMeasuredHeight() / 2;
		// 设置半径为宽高最小值的1/4
		radius = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 4;
		// 设置扇形外接矩形
		rectF = new RectF(centerX - radius, centerY - radius, centerX + radius,
				centerY + radius);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		calculateAndDraw(canvas);
	}

	/**
	 * 计算比例并且绘制扇形和数据
	 */
	private void calculateAndDraw(Canvas canvas) {
		if (numbers == null || numbers.length == 0) {
			return;
		}
		
		calculateAndDrawArc(canvas);
		// 绘制中心数字总和
		canvas.drawText(sum + "", centerX - centerTextBound.width() / 2,
				centerY + centerTextBound.height() / 2, centerTextPaint);
	}

	private void calculateAndDrawArc(Canvas canvas) {
		if (numbers == null || numbers.length == 0)
			return;

		// 扇形开始度数
		int startAngle = 0;
		// 所占百分比
		float percent;
		// 所占度数
		float angle;

		for (int i = 0; i < numbers.length; i++) {
			percent = numbers[i] / (float) sum;
			// 获取百分比在360中所占度数
			if (i == numbers.length - 1) {// 保证所有度数加起来等于360
				angle = 360 - startAngle;
			} else {
				angle = (float) Math.ceil(percent * 360);
			}
			// 绘制第i段扇形
			drawArc(canvas, startAngle, angle, colors[i]);
			startAngle += angle;

			// 绘制数据
			if (numbers[i] <= 0) {
				continue;
			}
			// 当前扇形弧线相对于纵轴的中心点度数,由于扇形的绘制是从三点钟方向开始，所以加90
			float arcCenterDegree = 90 + startAngle - angle / 2;
			drawData(canvas, arcCenterDegree, i, percent);
		}
	}

	/**
	 * @Title: isAllZero
	 * @Description: 全部为0
	 * @return: boolean
	 */
	private boolean isAllZero() {
		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i] > 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 计算每段弧度的中心坐标
	 * 
	 * @param degree
	 *            当前扇形中心度数
	 */
	private float[] calculatePosition(float degree) {
		// 由于Math.sin(double a)中参数a不是度数而是弧度，所以需要将度数转化为弧度
		// 而Math.toRadians(degree)的作用就是将度数转化为弧度
		// sin 一二正，三四负 sin（180-a）=sin(a)
		// 扇形弧线中心点距离圆心的x坐标
		float x = (float) (Math.sin(Math.toRadians(degree)) * (radius * 1.6));
		// cos 一四正，二三负
		// 扇形弧线中心点距离圆心的y坐标
		float y = (float) (Math.cos(Math.toRadians(degree)) * (radius * 1.6));

		// 每段弧度的中心坐标(扇形弧线中心点相对于view的坐标)
		float startX = centerX + x;
		float startY = centerY - y;

		float[] position = new float[2];
		position[0] = startX;
		position[1] = startY;
		return position;
	}

	/**
	 * 绘制数据
	 * 
	 * @param canvas
	 *            画布
	 * @param degree
	 *            第i段弧线中心点相对于纵轴的夹角度数
	 * @param i
	 *            第i段弧线
	 * @param percent
	 *            数据百分比
	 */
	private void drawData(Canvas canvas, float degree, int i, float percent) {
		// 弧度中心坐标
		float startX = calculatePosition(degree)[0];
		float startY = calculatePosition(degree)[1];

		// 数字开始坐标
		float numberStartX = 0;
		float numberStartY = 0;

		// 根据每个弧度的中心点坐标绘制数据
		dataPaint.getTextBounds(numbers[i] + "", 0, (numbers[i] + "").length(),
				dataTextBound);
		numberStartX = startX - dataTextBound.width() / 2;
		numberStartY = startY + dataTextBound.height() / 2;
		// 绘制数字
		canvas.drawText(numbers[i] + "", numberStartX, numberStartY, dataPaint);
	}

	/**
	 * 绘制扇形
	 * 
	 * @param canvas
	 *            画布
	 * @param startAngle
	 *            开始度数
	 * @param angle
	 *            扇形的度数
	 * @param color
	 *            颜色
	 */
	private void drawArc(Canvas canvas, float startAngle, float angle, int color) {
		mArcPaint.setColor(color);
		// -0.5和+0.5是为了让每个扇形之间没有间隙
		canvas.drawArc(rectF, startAngle - 0.5f, angle + 0.5f, false, mArcPaint);
	}

	/**
	 * 设置数据(自定义颜色)
	 * 
	 * @param numbers
	 *            数字数组
	 * @param names
	 *            名称数组
	 * @param colors
	 *            颜色数组
	 */
	public void setData(int[] numbers, int[] colors) {
		if (numbers == null || numbers.length == 0 || colors == null
				|| colors.length == 0) {
			return;
		}

		if (numbers.length != colors.length) {
			return;
		}

		this.numbers = numbers;
		this.colors = colors;

		if (isAllZero()) {
			this.numbers = numbers;
			this.colors = colors;
		} else {
			/**
			 * 去掉numbers为0的项
			 */
			int zero = 0; // 统计0的个数
			for (int i = 0; i < numbers.length; i++) {
				if (numbers[i] == 0) {
					zero++;
				}
			}

			int newnumbers[] = new int[numbers.length - zero]; 
			int newcolors[] = new int[numbers.length - zero];

			int j = 0; // 新数组的索引
			for (int i = 0; i < numbers.length; i++) { // 遍历原来旧的数组
				if (numbers[i] != 0) { // 假如不等于0
					newnumbers[j] = numbers[i]; // 赋值给新的数组
					newcolors[j] = colors[i];
					j++;
				}
			}
			this.numbers = newnumbers;
			this.colors = newcolors;
		}

		sum = 0;
		for (int i = 0; i < this.numbers.length; i++) {
			// 计算总和
			sum +=  this.numbers[i];
		}
		// 计算总和数字的宽高
		centerTextPaint.getTextBounds(sum + "", 0, (sum + "").length(),
				centerTextBound);
		invalidate();
	}
}
