package team2.grocerycartcalculator;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import static java.lang.Math.abs;

/**
 * A text view for use in ListViews. It allows swiping as an action
 * NYI
 */
public class SwipeableLayout extends RelativeLayout {
    // Used to determine the swipe position we should be in
    private boolean isSwiped = false;
    // If set, we have started swiping. Moving our finger doesn't change our gesture
    private boolean isSwiping = false;
    // Used to calculate our dx and dy for swipe recognition
    private float startX;
    private float startY;
    // This is an averaged displacement to eliminate jumping
    private float currXDisp = 1.0f;
    // The first x position of this. Used to reset our position
    private float originX;
    // Starting x position at the beginning of a gesture
    private float swipeOriginX;
    // Used to keep track of how long it's been since we let go. If low, it is a glitch
    private long lastUpTime = 0L;
    // Hacky way to set originX since it isn't set on create. Need to set onDraw
    private boolean doOnce = true;
    // Hacky way to do clicks since ListView can't handle these clicks
    private OnClickListener onClickListener;
    // Used to keep track of the swiping distance we need to make
    private View deleteButton;
    // Threshold before we start swiping. If 0, we swipe as soon as we move. Percentage
    protected static final float    SWIPE_STICK_THRESHOLD = 0.05f;
    // Percentage of the deleteButton width to go to set
    protected static final float    SWIPE_THRESHOLD_PERCENT = .75f;
    // Ratio of dx/dy to determine if we are going horizontal enough for swiping
    protected static final float    SWIPE_RATIO = .90f;
    // Threshold of the Swipe Threshold to reset
    protected static final float    SWIPE_RETURN_THRESHOLD = .80f;
    // How much of the original dispX do we keep
    // Below .80 it jitters too much. Above .95 is too unresponsive
    protected static final float    DISP_X_AVERAGING_FACTOR = 0.80f;
    protected static final long     TOUCH_PICKUP_TIME = 50; // 50 ms
    // Speeds up our swipe gesture
    protected static final float    SWIPE_DISP_MOD = 5f;
    // Movement tolerance to be considered a click
    protected static final float    CLICK_TOLERANCE = 8f;

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

    public void setOnClickListener(OnClickListener listener)
    {
        onClickListener = listener;
    }

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
                {
                    changeToNotSwiped();
                    // Check if we clicked
                    dx = startX - event.getX();
                    dy = startY - event.getY();
                    if(!isSwiping && abs(dx) < CLICK_TOLERANCE && abs(dy) < CLICK_TOLERANCE)
                    {
                        // Make us do the click event
                        if(onClickListener != null)
                            onClickListener.onClick(this);
                    }
                }
                break;
            default:
                return super.onTouchEvent(event);
        }
        // If we are swiping, then don't let the listview scroll. Otherwise, let it scroll
        requestDisallowInterceptTouchEvent(isSwiping);
        return true;
    }

    // Moves this to the swiped position
    public void changeToSwiped()
    {
        float targetX = originX - deleteButton.getWidth();
        setX(targetX);
    }

    // Moves this to the not swiped position
    public void changeToNotSwiped()
    {
        float targetX = originX;
        setX(targetX);
    }

    // Averaging function to smoothen the motion
    private float expAverage(float old, float x, float alpha)
    {
        float a = old*alpha;
        float b = (1 - alpha) * x;
        return a + b;
    }
}
