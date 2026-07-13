/*
 *
 *   Copyright/* *
 *             * CedCommerce
 *             *
 *             * NOTICE OF LICENSE
 *             *
 *             * This source file is subject to the End User License Agreement (EULA)
 *             * that is bundled with this package in the file LICENSE.txt.
 *             * It is also available through the world-wide-web at this URL:
 *             * http://cedcommerce.com/license-agreement.txt
 *             *
 *             * @category  Ced
 *             * @package   MultiVendor
 *             * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *             * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *             * @license   http://cedcommerce.com/license-agreement.txt
 *
 *
 *
 */

package magentoegypt.locafy.base_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.nineoldandroids.view.ViewHelper;
import magentoegypt.locafy.R;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("DrawAllocation")
public class Ced_MultiVendor_VendorPiechart extends View implements View.OnTouchListener {

    private final static String TAG = "PacpieChart";
    private final static float DEFAULT_LINE_STROKE_WIDTH = 3.0F;
    private final static float DEFUALT_SLICE_STROKE_WIDTH = 0.0F;
    private final Context context;
    private final Paint slicePaints = new Paint();
    private final Paint linePaints = new Paint();
    private final int lineColor;
    private final int sliceColor;
    private OnClickListener listener;
    private Ced_MultiVendor_VendorPiechartState state = Ced_MultiVendor_VendorPiechartState.WAIT;
    private float lineStrokeWidth = DEFAULT_LINE_STROKE_WIDTH;
    private float sliceStrokeWidth = DEFUALT_SLICE_STROKE_WIDTH;
    private boolean lineAntiAlias = true;
    private boolean sliceAntiAlias = true;
    private boolean isRotationActivated = false;
    private int backgroundColor;
    private int diameter;
    private float start;
    private float sweep;
    private int maxConnection;
    private long touchDownTime;
    private List<Ced_MultiVendor_VendorPieChartSlice> slicesList = new ArrayList<>();

    public Ced_MultiVendor_VendorPiechart(Context context) {
        super(context);
        this.context = context;
        this.lineColor = context.getResources().getColor(R.color.default_line_color);
        this.sliceColor = context.getResources().getColor(R.color.default_slice_color);
        this.backgroundColor = context.getResources().getColor(android.R.color.transparent);
    }

    public Ced_MultiVendor_VendorPiechart(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.backgroundColor = context.getResources().getColor(android.R.color.transparent);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PacPieChart, 0, 0);
        try {
            this.sliceAntiAlias = ta.getBoolean(R.styleable.PacPieChart_sliceAntiAlias, true);
            this.sliceColor = ta.getColor(R.styleable.PacPieChart_sliceDefaultColor, context.getResources().getColor(R.color.default_slice_color));
            this.sliceStrokeWidth = ta.getFloat(R.styleable.PacPieChart_sliceStrokeWidth, DEFUALT_SLICE_STROKE_WIDTH);
            this.lineAntiAlias = ta.getBoolean(R.styleable.PacPieChart_lineAntiAlias, true);
            this.lineColor = ta.getColor(R.styleable.PacPieChart_lineDefaultColor, context.getResources().getColor(R.color.default_slice_color));
            this.lineStrokeWidth = ta.getFloat(R.styleable.PacPieChart_lineStrokeWidth, DEFAULT_LINE_STROKE_WIDTH);
            this.isRotationActivated = ta.getBoolean(R.styleable.PacPieChart_activate_rotation, isRotationActivated);
        } finally {
            ta.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (state != Ced_MultiVendor_VendorPiechartState.IS_READY_TO_DRAW) {
            return;
        }
        canvas.drawColor(backgroundColor);
        slicePaints.setAntiAlias(lineAntiAlias);
        slicePaints.setStyle(Paint.Style.FILL);
        slicePaints.setColor(sliceColor);
        slicePaints.setStrokeWidth(sliceStrokeWidth);
        linePaints.setAntiAlias(sliceAntiAlias);
        linePaints.setColor(lineColor);
        linePaints.setStrokeWidth(lineStrokeWidth);
        linePaints.setStyle(Paint.Style.STROKE);
        diameter = this.getMeasuredWidth();
        if (this.getMeasuredHeight() < diameter) {
            diameter = this.getMeasuredHeight();
        }
        int paddingVertical = (this.getMeasuredHeight() - diameter) / 2;
        int paddingHorizontal = (this.getMeasuredWidth() - diameter) / 2;
        RectF mOvals = new RectF(this.getPaddingLeft(), this.getPaddingTop(), diameter - this.getPaddingRight(), diameter - this.getPaddingBottom());
        mOvals.offsetTo(paddingHorizontal + getPaddingLeft(), paddingVertical + getPaddingTop());
        start = Ced_MultiVendor_VendorPiechartState.START_INC.stateCode;
        for (int i = 0; i < slicesList.size(); i++) {
            Ced_MultiVendor_VendorPieChartSlice item = slicesList.get(i);
            slicePaints.setColor(sliceColor);
            if (-1 != item.color) {
                slicePaints.setColor(item.color);
            }
            if (0F == item.count) {
                throw new RuntimeException("percent is 0, will not be draw");
            }
            sweep = (float) 360 * (item.count / (float) maxConnection);
            Log.d(TAG, "sweep = " + sweep + " & start = " + start);
            canvas.drawArc(mOvals, start, sweep, true, slicePaints);
            canvas.drawArc(mOvals, start, sweep, true, linePaints);
            start = start + sweep;
        }
        this.setOnTouchListener(this);
      //  state = Ced_MultiVendor_VendorPiechartState.IS_DRAW;
    }

    public void setValues(List<Ced_MultiVendor_VendorPieChartSlice> data) {
        if (null == data || 0 == data.size()) {
            throw new RuntimeException("data cannot be null or empty !");
        }
        slicesList = data;
        for (int i = 0; i < data.size(); ++i) {
            maxConnection += data.get(i).count;
        }
        state = Ced_MultiVendor_VendorPiechartState.IS_READY_TO_DRAW;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        boolean handled = false;
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                touchDownTime = SystemClock.elapsedRealtime();
                return true;
            case MotionEvent.ACTION_UP:
                if (SystemClock.elapsedRealtime() - touchDownTime <= 300) {
                    float x = event.getX();
                    float y = event.getY();
                    performClick(x, y);
                    handled = true;
                }
                return false;
        }
        return handled;
    }

    public void performClick(float x, float y) {


    }

    public void expand() {
        final int initialHeight = Ced_MultiVendor_VendorPiechart.this.getMeasuredHeight();
        Ced_MultiVendor_VendorPiechart.this.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {

                state = Ced_MultiVendor_VendorPiechartState.IS_READY_TO_DRAW;
                ViewHelper.setScaleX(Ced_MultiVendor_VendorPiechart.this, interpolatedTime);
                ViewHelper.setScaleY(Ced_MultiVendor_VendorPiechart.this, interpolatedTime);
                if (isRotationActivated) {
                    ViewHelper.setRotation(Ced_MultiVendor_VendorPiechart.this, 360 * interpolatedTime);
                }
                Ced_MultiVendor_VendorPiechart.this.invalidate();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int) (initialHeight * 2 / Ced_MultiVendor_VendorPiechart.this.getContext().getResources().getDisplayMetrics().density));
        Ced_MultiVendor_VendorPiechart.this.startAnimation(a);
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}