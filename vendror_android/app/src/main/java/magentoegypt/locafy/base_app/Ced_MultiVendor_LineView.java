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
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import magentoegypt.locafy.R;
public class Ced_MultiVendor_LineView extends View
{
    private int mViewHeight;
    private boolean autoSetDataOfGird = true;
    private boolean autoSetGridWidth = true;
    private int dataOfAGird = 10;
    private int bottomTextHeight = 0;
    private ArrayList<String> bottomTextList = new ArrayList<String>();
    private ArrayList<ArrayList<Integer>> dataLists;
    private ArrayList<Integer> dataList;
    private ArrayList<Integer> xCoordinateList = new ArrayList<Integer>();
    private ArrayList<Integer> yCoordinateList = new ArrayList<Integer>();
    private ArrayList<ArrayList<Dot>> drawDotLists = new ArrayList<ArrayList<Dot>>();
    private ArrayList<Dot> drawDotList = new ArrayList<Dot>();
    private Paint bottomTextPaint = new Paint();
    private int bottomTextDescent;
    private Paint popupTextPaint = new Paint();
    private final int bottomTriangleHeight = 12;
    public boolean showPopup = true;
    private Dot pointToSelect;
    private Dot selectedDot;
    private int topLineLength = Ced_MultiVendor_MyUtils.dip2px(getContext(), 12);
    private int sideLineLength = Ced_MultiVendor_MyUtils.dip2px(getContext(), 45)/3*2;
    private int backgroundGridWidth = Ced_MultiVendor_MyUtils.dip2px(getContext(), 45);
    private final int popupTopPadding = Ced_MultiVendor_MyUtils.dip2px(getContext(), 2);
    private final int popupBottomMargin = Ced_MultiVendor_MyUtils.dip2px(getContext(), 5);
    private final int bottomTextTopMargin = Ced_MultiVendor_MyUtils.sp2px(getContext(), 5);
    private final int bottomLineLength = Ced_MultiVendor_MyUtils.sp2px(getContext(), 22);
    private final int DOT_INNER_CIR_RADIUS = Ced_MultiVendor_MyUtils.dip2px(getContext(), 2);
    private final int DOT_OUTER_CIR_RADIUS = Ced_MultiVendor_MyUtils.dip2px(getContext(), 5);
    private final int MIN_TOP_LINE_LENGTH = Ced_MultiVendor_MyUtils.dip2px(getContext(), 12);
    private final int MIN_VERTICAL_GRID_NUM = 4;
    private final int MIN_HORIZONTAL_GRID_NUM = 1;
    private final int BACKGROUND_LINE_COLOR = Color.parseColor("#EEEEEE");
    private final int BOTTOM_TEXT_COLOR = Color.parseColor("#9B9A9B");
    public static final int SHOW_POPUPS_All = 1;
    public static final int SHOW_POPUPS_MAXMIN_ONLY = 2;
    public static final int SHOW_POPUPS_NONE = 3;
    private int showPopupType = SHOW_POPUPS_NONE;
    public void setShowPopup(int popupType) {
        this.showPopupType = popupType;
    }
    private Boolean drawDotLine = false;
    private String[] colorArray = {"#e74c3c","#2980b9","#1abc9c"};
    private int[] popupColorArray = {R.drawable.dsahimage,R.drawable.addressbook,R.drawable.addressbook};
    private final Point tmpPoint = new Point();
    public void setDrawDotLine(Boolean drawDotLine) {
        this.drawDotLine = drawDotLine;
    }
    private Runnable animator = new Runnable() {
        @Override
        public void run() {
            boolean needNewFrame = false;
            for(ArrayList<Dot> data : drawDotLists){
                for(Dot dot : data){
                    dot.update();
                    if(!dot.isAtRest()){
                        needNewFrame = true;
                    }
                }
            }
            if (needNewFrame) {
                postDelayed(this, 25);
            }
            invalidate();
        }
    };

    public Ced_MultiVendor_LineView(Context context){
        this(context,null);
    }
    public Ced_MultiVendor_LineView(Context context, AttributeSet attrs){
        super(context, attrs);
        popupTextPaint.setAntiAlias(true);
        popupTextPaint.setColor(Color.WHITE);
        popupTextPaint.setTextSize(Ced_MultiVendor_MyUtils.sp2px(getContext(), 13));
        popupTextPaint.setStrokeWidth(5);
        popupTextPaint.setTextAlign(Paint.Align.CENTER);
        bottomTextPaint.setAntiAlias(true);
        bottomTextPaint.setTextSize(Ced_MultiVendor_MyUtils.sp2px(getContext(), 12));
        bottomTextPaint.setTextAlign(Paint.Align.CENTER);
        bottomTextPaint.setStyle(Paint.Style.FILL);
        bottomTextPaint.setColor(BOTTOM_TEXT_COLOR);
    }
    public void setBottomTextList(ArrayList<String> bottomTextList){
        this.dataList = null;
        this.bottomTextList = bottomTextList;

        Rect r = new Rect();
        int longestWidth = 0;
        String longestStr = "";
        bottomTextDescent = 0;
        for(String s:bottomTextList){
            bottomTextPaint.getTextBounds(s,0,s.length(),r);
            if(bottomTextHeight<r.height()){
                bottomTextHeight = r.height();
            }
            if(autoSetGridWidth&&(longestWidth<r.width())){
                longestWidth = r.width();
                longestStr = s;
            }
            if(bottomTextDescent<(Math.abs(r.bottom))){
                bottomTextDescent = Math.abs(r.bottom);
            }
        }

        if(autoSetGridWidth){
            if(backgroundGridWidth<longestWidth){
                backgroundGridWidth = longestWidth+(int)bottomTextPaint.measureText(longestStr,0,1);
            }
            if(sideLineLength<longestWidth/2){
                sideLineLength = longestWidth/2;
            }
        }

        refreshXCoordinateList(getHorizontalGridNum());
    }
    public void setDataList(ArrayList<ArrayList<Integer>> dataLists){
        selectedDot = null;
        this.dataLists = dataLists;
        for(ArrayList<Integer> list : dataLists){
            if(list.size() > bottomTextList.size()){
                throw new RuntimeException("vendor.LineView error:" +
                        " dataList.size() > bottomTextList.size() !!!");
            }
        }
        int biggestData = 0;
        for(ArrayList<Integer> list : dataLists){
            if(autoSetDataOfGird){
                for(Integer i:list){
                    if(biggestData<i){
                        biggestData = i;
                    }
                }
            }
            dataOfAGird = 1;
            while(biggestData/10 > dataOfAGird){
                dataOfAGird *= 10;
            }
        }

        refreshAfterDataChanged();
        showPopup = true;
        setMinimumWidth(0);
        postInvalidate();
    }

    private void refreshAfterDataChanged(){
        int verticalGridNum = getVerticalGridlNum();
        refreshTopLineLength(verticalGridNum);
        refreshYCoordinateList(verticalGridNum);
        refreshDrawDotList(verticalGridNum);
    }

    private int getVerticalGridlNum(){
        int verticalGridNum = MIN_VERTICAL_GRID_NUM;
        if(dataLists != null && !dataLists.isEmpty()){
            for(ArrayList<Integer> list : dataLists){
                for(Integer integer:list){
                    if(verticalGridNum<(integer+1)){
                        verticalGridNum = integer+1;
                    }
                }
            }
        }
        return verticalGridNum;
    }

    private int getHorizontalGridNum(){
        int horizontalGridNum = bottomTextList.size()-1;
        if(horizontalGridNum<MIN_HORIZONTAL_GRID_NUM){
            horizontalGridNum = MIN_HORIZONTAL_GRID_NUM;
        }
        return horizontalGridNum;
    }

    private void refreshXCoordinateList(int horizontalGridNum){
        xCoordinateList.clear();
        for(int i=0;i<(horizontalGridNum+1);i++){
            xCoordinateList.add(sideLineLength + backgroundGridWidth*i);
        }

    }

    private void refreshYCoordinateList(int verticalGridNum){
        yCoordinateList.clear();
        for(int i=0;i<(verticalGridNum+1);i++){
            yCoordinateList.add(topLineLength +
                    ((mViewHeight-topLineLength-bottomTextHeight-bottomTextTopMargin-
                            bottomLineLength-bottomTextDescent)*i/(verticalGridNum)));
        }
    }

    private void refreshDrawDotList(int verticalGridNum){
        if(dataLists != null && !dataLists.isEmpty()){
            if(drawDotLists.size() == 0){
                for(int k = 0; k < dataLists.size(); k++){
                    drawDotLists.add(new ArrayList<Ced_MultiVendor_LineView.Dot>());
                }
            }
            for(int k = 0; k < dataLists.size(); k++){
                int drawDotSize = drawDotLists.get(k).isEmpty()? 0:drawDotLists.get(k).size();

                for(int i=0;i<dataLists.get(k).size();i++){
                    int x = xCoordinateList.get(i);
                    int y = yCoordinateList.get(verticalGridNum - dataLists.get(k).get(i));
                    if(i>drawDotSize-1){
                        drawDotLists.get(k).add(new Dot(x, 0, x, y, dataLists.get(k).get(i),k));
                    }else{
                        drawDotLists.get(k).set(i, drawDotLists.get(k).get(i).setTargetData(x,y,dataLists.get(k).get(i),k));
                    }
                }

                int temp = drawDotLists.get(k).size() - dataLists.get(k).size();
                for(int i=0; i<temp; i++){
                    drawDotLists.get(k).remove(drawDotLists.get(k).size()-1);
                }
            }
        }
        removeCallbacks(animator);
        post(animator);
    }

    private void refreshTopLineLength(int verticalGridNum){
        if((mViewHeight-topLineLength-bottomTextHeight-bottomTextTopMargin)/
                (verticalGridNum+2)<getPopupHeight()){
            topLineLength = getPopupHeight()+DOT_OUTER_CIR_RADIUS+DOT_INNER_CIR_RADIUS+2;
        }else{
            topLineLength = MIN_TOP_LINE_LENGTH;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackgroundLines(canvas);
        drawLines(canvas);
        drawDots(canvas);


        for(int k=0; k < drawDotLists.size(); k++){

            for(Dot d: drawDotLists.get(k)){
                if(showPopupType == SHOW_POPUPS_All)
                    drawPopup(canvas, String.valueOf(d.data), d.setupPoint(tmpPoint),popupColorArray[k%3]);
                else if(showPopupType == SHOW_POPUPS_MAXMIN_ONLY){

                        drawPopup(canvas, String.valueOf(d.data), d.setupPoint(tmpPoint),popupColorArray[k%3]);

                }
            }
        }
        if(showPopup && selectedDot != null){
            drawPopup(canvas,
                    String.valueOf(selectedDot.data),
                    selectedDot.setupPoint(tmpPoint),popupColorArray[selectedDot.linenumber%3]);
        }
    }
    private void drawPopup(Canvas canvas,String num, Point point,int PopupColor)
    {
        boolean singularNum = (num.length() == 1);
        int sidePadding = Ced_MultiVendor_MyUtils.dip2px(getContext(), singularNum ? 8 : 5);
        int x = point.x;
        int y = point.y- Ced_MultiVendor_MyUtils.dip2px(getContext(), 5);
        Rect popupTextRect = new Rect();
        popupTextPaint.getTextBounds(num, 0, num.length(), popupTextRect);
        Rect r = new Rect(x-popupTextRect.width()/2-sidePadding,
                y - popupTextRect.height()-bottomTriangleHeight-popupTopPadding*2-popupBottomMargin,
                x + popupTextRect.width()/2+sidePadding,
                y+popupTopPadding-popupBottomMargin);
        popupTextPaint.setColor(Color.GRAY);
        popupTextPaint.setTextSize(25);
        canvas.drawText(num, x, y-bottomTriangleHeight-popupBottomMargin, popupTextPaint);
    }

    private int getPopupHeight(){
        Rect popupTextRect = new Rect();
        popupTextPaint.getTextBounds("9",0,1,popupTextRect);
        Rect r = new Rect(-popupTextRect.width()/2,
                - popupTextRect.height()-bottomTriangleHeight-popupTopPadding*2-popupBottomMargin,
                + popupTextRect.width()/2,
                +popupTopPadding-popupBottomMargin);
        return r.height();
    }
    private void drawDots(Canvas canvas){
        Paint bigCirPaint = new Paint();
        bigCirPaint.setAntiAlias(true);
        Paint smallCirPaint = new Paint(bigCirPaint);
        smallCirPaint.setColor(Color.parseColor("#FFFFFF"));
        if(drawDotLists!=null && !drawDotLists.isEmpty()){
            for(int k=0; k < drawDotLists.size(); k++){
                bigCirPaint.setColor(getResources().getColor(R.color.AppTheme));
                for(Dot dot : drawDotLists.get(k)){
                    canvas.drawCircle(dot.x,dot.y,DOT_OUTER_CIR_RADIUS,bigCirPaint);
                    canvas.drawCircle(dot.x,dot.y,DOT_INNER_CIR_RADIUS,smallCirPaint);
                }
            }
        }
    }

    private void drawLines(Canvas canvas){
        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(Ced_MultiVendor_MyUtils.dip2px(getContext(), 2));
        for(int k = 0; k<drawDotLists.size(); k ++){
            linePaint.setColor(getResources().getColor(R.color.AppTheme));
            for(int i=0; i<drawDotLists.get(k).size()-1; i++){
                canvas.drawLine(drawDotLists.get(k).get(i).x,
                        drawDotLists.get(k).get(i).y,
                        drawDotLists.get(k).get(i+1).x,
                        drawDotLists.get(k).get(i+1).y,
                        linePaint);
            }
        }
    }


    private void drawBackgroundLines(Canvas canvas){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(Ced_MultiVendor_MyUtils.dip2px(getContext(), 1f));
        paint.setColor(BACKGROUND_LINE_COLOR);
        PathEffect effects = new DashPathEffect(
                new float[]{10,5,10,5}, 1);

        for(int i=0;i<xCoordinateList.size();i++){
            canvas.drawLine(xCoordinateList.get(i),
                    0,
                    xCoordinateList.get(i),
                    mViewHeight - bottomTextTopMargin - bottomTextHeight-bottomTextDescent,
                    paint);
        }
        paint.setPathEffect(effects);
        Path dottedPath = new Path();
        for(int i=0;i<yCoordinateList.size();i++){
            if((yCoordinateList.size()-1-i)%dataOfAGird == 0){
                dottedPath.moveTo(0, yCoordinateList.get(i));
                dottedPath.lineTo(getWidth(), yCoordinateList.get(i));
                canvas.drawPath(dottedPath, paint);
            }
        }
        if(bottomTextList != null){
            for(int i=0;i<bottomTextList.size();i++){
                canvas.drawText(bottomTextList.get(i), sideLineLength+backgroundGridWidth*i, mViewHeight-bottomTextDescent, bottomTextPaint);
            }
        }

        if(!drawDotLine){
            for(int i=0;i<yCoordinateList.size();i++){
                if((yCoordinateList.size()-1-i)%dataOfAGird == 0){
                    canvas.drawLine(0,yCoordinateList.get(i),getWidth(),yCoordinateList.get(i),paint);
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mViewWidth = measureWidth(widthMeasureSpec);
        mViewHeight = measureHeight(heightMeasureSpec);
        refreshAfterDataChanged();
        setMeasuredDimension(mViewWidth,mViewHeight);
    }

    private int measureWidth(int measureSpec){
        int horizontalGridNum = getHorizontalGridNum();
        int preferred = backgroundGridWidth*horizontalGridNum+sideLineLength*2;
        return getMeasurement(measureSpec, preferred);
    }

    private int measureHeight(int measureSpec){
        int preferred = 0;
        return getMeasurement(measureSpec, preferred);
    }

    private int getMeasurement(int measureSpec, int preferred){
        int specSize = MeasureSpec.getSize(measureSpec);
        int measurement;
        switch(MeasureSpec.getMode(measureSpec)){
            case MeasureSpec.EXACTLY:
                measurement = specSize;
                break;
            case MeasureSpec.AT_MOST:
                measurement = Math.min(preferred, specSize);
                break;
            default:
                measurement = preferred;
                break;
        }
        return measurement;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            pointToSelect = findPointAt((int) event.getX(), (int) event.getY());
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (pointToSelect != null) {
                selectedDot = pointToSelect;
                pointToSelect = null;
                postInvalidate();
            }
        }

        return true;
    }

    private Dot findPointAt(int x, int y) {
        if (drawDotLists.isEmpty()) {
            return null;
        }

        final int width = backgroundGridWidth/2;
        final Region r = new Region();

        for (ArrayList<Dot> data : drawDotLists) {
            for (Dot dot : data) {
                final int pointX = dot.x;
                final int pointY = dot.y;

                r.set(pointX - width, pointY - width, pointX + width, pointY + width);
                if (r.contains(x, y)){
                    return dot;
                }
            }
        }

        return null;
    }



    class Dot{
        int x;
        int y;
        int data;
        int targetX;
        int targetY;
        int linenumber;
        int velocity = Ced_MultiVendor_MyUtils.dip2px(getContext(), 18);

        Dot(int x,int y,int targetX,int targetY,Integer data,int linenumber){
            this.x = x;
            this.y = y;
            this.linenumber = linenumber;
            setTargetData(targetX, targetY,data,linenumber);
        }

        Point setupPoint(Point point) {
            point.set(x, y);
            return point;
        }

        Dot setTargetData(int targetX,int targetY,Integer data,int linenumber){
            this.targetX = targetX;
            this.targetY = targetY;
            this.data = data;
            this.linenumber = linenumber;
            return this;
        }

        boolean isAtRest(){
            return (x==targetX)&&(y==targetY);
        }

        void update(){
            x = updateSelf(x, targetX, velocity);
            y = updateSelf(y, targetY, velocity);
        }

        private int updateSelf(int origin, int target, int velocity){
            if (origin < target) {
                origin += velocity;
            } else if (origin > target){
                origin-= velocity;
            }
            if(Math.abs(target-origin)<velocity){
                origin = target;
            }
            return origin;
        }
    }
}
