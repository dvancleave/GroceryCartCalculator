package team2.grocerycartcalculator;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import java.io.IOException;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

/**
 * A text view for use in ListViews. It allows swiping as an action
 * NYI
 */
public class SwipableItemView extends AppCompatTextView {
    // Determines the state of the item view. If it is swiped, the view is on the left and shows
    // A delete button
    private boolean isSwiped = false;
    private boolean isSwiping = false;
    private float startX;
    private float startY;
    private float currXDisp = 1.0f; // This is an averaged displacement to eliminate jumping
    private float originX;
    private float swipeOriginX;
    private long lastUpTime = 0;
    private boolean doOnce = true;
    private Animation.AnimationListener toSwipedAL;
    private Animation.AnimationListener toNotSwipedAL;
    private static final float  SWIPE_STICK_THRESHOLD = 0.05f;
    private static final float  SWIPE_RETURN_THRESHOLD = .90f; // Go to 90% of the disp to return
    private  float  SWIPE_RATIO = .90f;
    private static final float  SWIPE_THRESHOLD = 0.25f;
    private static final float  SWIPE_FINAL_DISP_PERCENT = .30f; // Goes off 50%
    private static final long   SWIPE_TIME = 50; // 100 ms
    private static final long   SWIPE_FRAMES = 30; // FPS
    // Below .80 it jitters too much. Above .95 is too unresponsive
    private static final float  DISP_X_AVERAGING_FACTOR = 0.80f; // How much of the original do we keep
    private static final long   TOUCH_PICKUP_TIME = 50; // 50 ms
    private static final float  SWIPE_DISP_MOD = 5f;
    private static final float  DELETE_BUTTON_WIDTH = .10f; // Fraction of screen width
    /**
     * Constructor for the view
     */
    public SwipableItemView(Context context) {
        super(context);
    }

    public SwipableItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        originX = getX();
    }
    public SwipableItemView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        originX = getX();
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if(doOnce)
        {
            originX = getX();
            doOnce = false;
            final View myView = this;
            toSwipedAL = new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    myView.setX(originX - (float) getWidth() * SWIPE_FINAL_DISP_PERCENT);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            };
            toNotSwipedAL = new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    myView.setX(originX);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            };
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float swipeClamp = originX - (float) (getWidth()) * SWIPE_FINAL_DISP_PERCENT; // Negate
        float toSwipedThreshold = originX - (float) getWidth() * SWIPE_THRESHOLD;
        float toNotSwipedThreshold = originX - (float) getWidth() * SWIPE_THRESHOLD * SWIPE_RETURN_THRESHOLD;
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
                        if (originX == swipeOriginX) {
                            if (getX() < toSwipedThreshold)
                                isSwiped = true;
                            else
                                isSwiped = false;
                            System.out.println("Swiped is now " + isSwiped);
                        } else {
                            if (getX() > toNotSwipedThreshold)
                                isSwiped = false;
                            else
                                isSwiped = true;
                        }
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
        return true;
    }

    public void changeToSwiped()
    {
        float targetX = originX - getWidth() * SWIPE_FINAL_DISP_PERCENT;
        TranslateAnimation animation = new TranslateAnimation(0, targetX - getX(), 0, 0);
        animation.setDuration(SWIPE_TIME);
        animation.setFillAfter(true);
        animation.setAnimationListener(toSwipedAL);

        //startAnimation(animation);
        setX(targetX);
    }

    public void changeToNotSwiped()
    {
        float targetX = originX;
        TranslateAnimation animation = new TranslateAnimation(0, targetX - getX(), 0, 0);
        animation.setDuration(SWIPE_TIME);
        animation.setFillAfter(true);
        animation.setAnimationListener(toNotSwipedAL);

        //startAnimation(animation);
        setX(targetX);
    }

    private float expAverage(float old, float x, float alpha)
    {
        float a = old*alpha;
        float b = (1 - alpha) * x;
        return a + b;
    }

    // Just for funsies. It works really well (ultra smooth)  but it can't work for negative values :(
    private float factAverage(float old, float x, float alpha)
    {
        float signa = (old < 0) ? -1 : 1;
        float signb = (x < 0) ? -1 : 1;
        double a = pow(signa * old, alpha);
        double b = pow(signb * x, 1 - alpha);
        return (float)(a * b) * signb;
    }
}
