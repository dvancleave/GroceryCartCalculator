package team2.grocerycartcalculator;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * A text view for use in ListViews. It allows swiping as an action
 * NYI
 */
public class SwipableItemView extends AppCompatTextView {
    // Determines the state of the item view. If it is swiped, the view is on the left and shows
    // A delete button
    private boolean isSwiped = false;
    private float startX;
    private float startY;
    private float dx;
    private float dy;
    private static final float SWIPE_STICK_THRESHOLD = 0.10f;
    private static final float SWIPE_THRESHOLD = 0.40f;
    private static final float SWIPE_RATIO = 0.75f;
    private float swipeThreshold;
    private float swipeStickThreshold;
    /**
     * Constructor for the view
     */
    public SwipableItemView(Context context) {
        super(context);
    }

    public SwipableItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public SwipableItemView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        swipeThreshold = -1 * (float)(getWidth()) * SWIPE_THRESHOLD; //negative to go right!
        swipeStickThreshold = -1 * (float)(getWidth()) * SWIPE_STICK_THRESHOLD; //negative to go right!
        switch(event.getActionMasked())
        {
            case (MotionEvent.ACTION_DOWN):
                //Let's groove tonight, share the spice of life
                startX = event.getX();
                startY = event.getY();
                break;
            case (MotionEvent.ACTION_MOVE):
                dx = event.getX() - startX;
                dy = event.getY() - startY;
                if(dx/dy >= SWIPE_RATIO)
                {
                    System.out.println("We above the swiping ratio!");
                    if(dx <= swipeStickThreshold)
                    {
                        System.out.println("We are starting to swipe!");
                        //TODO start swipe movement
                        setX(startX + dx);
                        if (dx <= swipeThreshold) {
                            System.out.println("We swiped!");
                            isSwiped = true;
                            //TODO Complete swipe action
                        }
                    }
                }
                break;
            default:
                return super.onTouchEvent(event);
        }
        return true;
    }
}
