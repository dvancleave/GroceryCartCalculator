package team2.grocerycartcalculator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

import static java.lang.Math.abs;


public class SwipeTolerantListView extends ListView {

    private boolean isSwiping = false;
    private long lastUpTime;
    public float startX;
    public float startY;

    private static float SWIPE_THRESHOLD = 8f;

    public SwipeTolerantListView(Context context) {
        super(context);
    }

    public SwipeTolerantListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    public SwipeTolerantListView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    //@Override
    public boolean onInterceptTouchEventOld (MotionEvent event)
    {
        return super.onInterceptTouchEvent(event);
    }
}
