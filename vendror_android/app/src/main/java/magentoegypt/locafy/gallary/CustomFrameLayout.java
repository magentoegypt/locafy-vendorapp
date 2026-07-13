package magentoegypt.locafy.gallary;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Gil on 09/06/2014.
 */
public class CustomFrameLayout extends FrameLayout {


    private static boolean mMatchHeightToWidth;
    private static boolean mMatchWidthToHeight;

    public CustomFrameLayout(Context context) {
        super(context);
    }

    public CustomFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);



        try {
            mMatchHeightToWidth = true;
            mMatchWidthToHeight = true;
        } finally {

        }
    }



    //Squares the thumbnail
    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(mMatchHeightToWidth){
            setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
        } else if(mMatchWidthToHeight){
            setMeasuredDimension(heightMeasureSpec, heightMeasureSpec);
        }
    }
}
