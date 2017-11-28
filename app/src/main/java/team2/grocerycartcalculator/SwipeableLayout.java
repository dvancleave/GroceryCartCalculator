package team2.grocerycartcalculator;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import static java.lang.Math.abs;

/**
 * A text view for use in ListViews. It allows swiping as an action
 * NYI
 */
public class SwipeableLayout extends RelativeLayout {

    private boolean isSwiped = false;
    private boolean isSwiping = false;
    private float startX;
    private float startY;
    private float currXDisp = 1.0f; // This is an averaged displacement to eliminate jumping
    private float originX;
    private float swipeOriginX;
    private long lastUpTime = 0L;
    private boolean doOnce = true;
    public View deleteButton;
    protected static final float    SWIPE_STICK_THRESHOLD = 0.05f;
    protected static final float    SWIPE_RETURN_THRESHOLD = .80f; // Go to 90% of the disp to return
    protected static final float    SWIPE_RATIO = .90f;
    protected static final float    SWIPE_THRESHOLD_PERCENT = .75f;
    // Below .80 it jitters too much. Above .95 is too unresponsive
    protected static final float    DISP_X_AVERAGING_FACTOR = 0.80f; // How much of the original do we keep
    protected static final long     TOUCH_PICKUP_TIME = 50; // 50 ms
    protected static final float    SWIPE_DISP_MOD = 5f;

    public SwipeableLayout(Context context) {
        super(context);
    }

    public SwipeableLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    public SwipeableLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public void setDeleteButton(View button)
    {
        deleteButton = button;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if(doOnce)
        {
            originX = getX();
            doOnce = false;
        }
    }

    /*
    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        boolean ret = super.dispatchTouchEvent(event);
        if(ret)
        {
            /*
             * If we're swiping currently, then don't let the listview move us.
             * But if the last upTime is greater than our lift tolerance and we have lifted our
             * finger, then we stopped swiping. The touchEvent hasn't set isSwiping to false yet,
             * but we should recognise this
             /
            long currTime = System.nanoTime() / 1000000;
            boolean isLifted = lifted && (currTime - lastUpTime) > TOUCH_PICKUP_TIME;
            //requestDisallowInterceptTouchEvent(isSwiping && !isLifted);
        }
        return ret;
    }
    //*/

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float dbw = deleteButton.getWidth();
        float toSwipedThreshold = originX - (float) deleteButton.getWidth() * SWIPE_THRESHOLD_PERCENT;
        float toNotSwipedThreshold = originX - (float) deleteButton.getWidth() * SWIPE_THRESHOLD_PERCENT * SWIPE_RETURN_THRESHOLD;
        switch(event.getActionMasked())
        {
            case (MotionEvent.ACTION_DOWN):
                //Let's groove tonight, share the spice of life
                /*
                 * We have this logic set up because the event handler sometimes thinks we raised
                 * our finger and it will spazz it. If we actually picked up our finger, it would
                 * take longer than this threshold to replaced it. So anything less shouldn't be
                 * a start touch event. Instead, we treat it as a movement event and we have it
                 * roll over to the next case.
                 */
                long currTime = System.nanoTime() / 1000000;
                if(( currTime - lastUpTime )> TOUCH_PICKUP_TIME)
                {
                    startX = event.getX();
                    startY = event.getY();
                    currXDisp = 0;
                    // We now have relative movement, so our origin can be the left or right!
                    swipeOriginX = getX();
                    isSwiping = false;
                    break;
                }
            case (MotionEvent.ACTION_MOVE):
                float dx = startX - event.getX();
                float dy = startY - event.getY();
                // Tries to determine if we're swiping horizontally or vertically
                float rat = abs(dx / dy);
                if(isSwiping || (rat > SWIPE_RATIO))
                {
                    // This checks to see if we are swiping or not. If we aren't, we need to break
                    // The swiping threshold, and then we stop sticking
                    if (isSwiping || abs(dx) > abs(getWidth() * SWIPE_STICK_THRESHOLD)) {
                        // We average to smooth movement and nullify spazzing
                        currXDisp = expAverage(currXDisp, dx, DISP_X_AVERAGING_FACTOR);
                        setX(swipeOriginX - currXDisp * SWIPE_DISP_MOD);
                        isSwiping = true;
                        /*
                         * Checks if we switched over. If we started not swiped and cross this threshold
                         * then we become swiped. If we started swiped and cross another threshold, we
                         * become not swiped
                         */
                        if (originX == swipeOriginX)
                            isSwiped = getX() < toSwipedThreshold;
                        else
                            isSwiped = getX() <= toNotSwipedThreshold;
                    }
                }
                break;
            case (MotionEvent.ACTION_UP):
                lastUpTime = System.nanoTime() / 1000000;
                if(isSwiped)
                    changeToSwiped();
                else
                    changeToNotSwiped();
                break;
            default:
                return super.onTouchEvent(event);
        }
        requestDisallowInterceptTouchEvent(isSwiping);
        return true;
    }

    public void changeToSwiped()
    {
        float targetX = originX - deleteButton.getWidth();
        setX(targetX);
    }

    public void changeToNotSwiped()
    {
        float targetX = originX;
        setX(targetX);
    }

    private float expAverage(float old, float x, float alpha)
    {
        float a = old*alpha;
        float b = (1 - alpha) * x;
        return a + b;
    }
}
