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

    @Override
    public boolean onInterceptTouchEvent (MotionEvent event)
    {
        switch(event.getActionMasked())
        {
            case (MotionEvent.ACTION_DOWN):
                long currTime = System.nanoTime() / 1000000;
                if(( currTime - lastUpTime )> SwipeableLayout.TOUCH_PICKUP_TIME)
                {
                    startX = event.getX();
                    startY = event.getY();
                    isSwiping = false;
                    break;
                }
            case (MotionEvent.ACTION_MOVE):
                float dx = startX - event.getX();
                float dy = startY - event.getY();
                // Tries to determine if we're swiping horizontally or vertically
                float rat = abs(dx / dy);
                /*
                if(isSwiping || (rat < SwipableLayout.SWIPE_RATIO))
                {
                    // This checks to see if we are swiping or not. If we aren't, we need to break
                    // The swiping threshold, and then we stop sticking
                    if (isSwiping || abs(dx) > abs(SWIPE_THRESHOLD)) {
                        isSwiping = true;
                    }
                }
                //*/
                if(isSwiping) {

                    System.out.println("Swiping!");
                    break;
                }
                boolean foo = rat < SwipeableLayout.SWIPE_RATIO;
                foo = foo && abs(dy) > abs(SWIPE_THRESHOLD);
                isSwiping = foo || isSwiping;
                if(isSwiping)
                    System.out.println("Swiping!");
                break;
            case (MotionEvent.ACTION_UP):
                lastUpTime = System.nanoTime() / 1000000;
                break;
        }
        return isSwiping;
    }
}
