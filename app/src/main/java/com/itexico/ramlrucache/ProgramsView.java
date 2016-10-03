
package com.itexico.ramlrucache;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.OverScroller;

import com.itexico.ramlrucache.cache.Images;
import com.itexico.ramlrucache.cache.ImageCache;


public final class ProgramsView extends View implements OnGestureListener {

    private static final String TAG = ProgramsView.class.getSimpleName();

    /* Object which will be used for trigering scroll events. */
    private final OverScroller mScroller = new OverScroller(getContext(), new DecelerateInterpolator());

    /* Gesture class Object */
    private final GestureDetector mGestureDetector = new GestureDetector(getContext(), this);

    private final Paint mPaint = new Paint();

    private final Paint mFillPaint = new Paint();

    private final Paint mProgramSepratorPaint = new Paint();

    /**
     *  Thumbnail Height.
     */
    private int mLogoHeight = 0;

    private int mLogoWidth = 0;

    /**
     * Seperator width between Programs.
     */
    private int mProgramsSeperator = 0;

    /**
     *  Program Name Text size.
     */
    private int mProgramsTextSize = 0;

    /**
     *  Program
     */
    private int mProgramsDurationSize = 0;

    /**
     * Horizontal Scroll Position.
     */
    private int mScrollPosX = 0;

    /**
     * Vertical Scroll Position.
     */
    private int mScrollPosY = 0;

    /**
     * Maximum scrollable horizontal Position.
     */
    private int mMaxPosX = 100000;

    private int mTextGap = 0;

    /**
     * Class used for  Logo caching.
     */
    private final ImageCache mImageCache = new ImageCache(0);

    private Images mImages = null;

    private Bitmap mDefaultLogoBitmap = null;

    private int mProgramNameTopGap;

    private int mProgramNameLeftEgde;


    public ProgramsView(final Context context) {
        super(context);
        localConstruct(context);
    }

    public ProgramsView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        localConstruct(context);
    }

    public ProgramsView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        localConstruct(context);
    }

    private void localConstruct(final Context context) {
        mGestureDetector.setIsLongpressEnabled(false);

        mProgramsTextSize = context.getResources().getDimensionPixelSize(R.dimen.program_name_text_size);
        mProgramsDurationSize = context.getResources().getDimensionPixelSize(R.dimen.program_dutation_size);

        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mProgramsTextSize);

        mFillPaint.setColor(Color.LTGRAY);
        mFillPaint.setStyle(Style.FILL_AND_STROKE);
        mFillPaint.setAntiAlias(true);
        mFillPaint.setTextSize(mProgramsTextSize);

        mProgramSepratorPaint.setColor(Color.BLACK);
        mProgramSepratorPaint.setStyle(Style.STROKE);
        mProgramSepratorPaint.setAntiAlias(true);
        mProgramsSeperator = context.getResources().getDimensionPixelSize(
                R.dimen.thumbnail_seprator);
        mProgramSepratorPaint.setStrokeWidth(mProgramsSeperator);

        // mProgramFavouritePaint.setDither(true);

        mLogoHeight = context.getResources().getDimensionPixelSize(R.dimen.thumbnail_height);

        mLogoWidth = context.getResources().getDimensionPixelSize(R.dimen.thumbnail_width);

        mImages = new Images(getContext());

        mDefaultLogoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);

        mTextGap = context.getResources().getDimensionPixelSize(R.dimen.gap_between_text);
        mProgramNameTopGap = context.getResources().getDimensionPixelSize(R.dimen.program_name_top_gap);
        mProgramNameLeftEgde = context.getResources().getDimensionPixelSize(R.dimen.program_name_left_edge);

    }

    /*
     * (non-Javadoc)
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        if ((event.getAction() == MotionEvent.ACTION_UP) && mScroller.isFinished()) {
            if (mScroller.springBack(getScrollPosX(), getScrollPosY(), 0, getMaxPosX(), 0, getMaxPosY())) {
                Log.v(TAG,
                        "springBack started:" + getScrollPosX() + "," + getScrollPosY() + ", finalX:"
                                + mScroller.getFinalX() + ",finalY:" + mScroller.getFinalY());
                ViewCompat.postInvalidateOnAnimation(this);
            } else {
                Log.v(TAG, "no springBack required");
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * @see android.view.GestureDetector.OnGestureListener#onDown(android.view.
     * MotionEvent)
     */
    @Override
    public boolean onDown(final MotionEvent arg0) {
        mScroller.forceFinished(true);
        ViewCompat.postInvalidateOnAnimation(this);
        return false;
    }

    /*
     * (non-Javadoc)
     * @see android.view.GestureDetector.OnGestureListener#onFling(android.view.
     * MotionEvent, android.view.MotionEvent, float, float)
     */
    @Override
    public boolean onFling(final MotionEvent arg0, final MotionEvent arg1, final float velocityX, final float velocityY) {
        boolean ret = false;
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        mScroller.forceFinished(true);
        mScroller.fling(getScrollPosX(), getScrollPosY(), -(int)velocityX, -(int)velocityY, 0, getMaxPosX(), 0,
                getMaxPosY(), width / 2, height / 2);
        Log.v(TAG, "fling:velocityX:" + velocityX + ",velocityY" + velocityY + ", finalX:" + mScroller.getFinalX()
                + ",finalY:" + mScroller.getFinalY());
        ret = true;
        ViewCompat.postInvalidateOnAnimation(this);
        return ret;
    }

    /*
     * (non-Javadoc)
     * @see
     * android.view.GestureDetector.OnGestureListener#onLongPress(android.view
     * .MotionEvent)
     */
    @Override
    public void onLongPress(final MotionEvent arg0) {
    }

    /*
     * (non-Javadoc)
     * @see
     * android.view.GestureDetector.OnGestureListener#onScroll(android.view.
     * MotionEvent, android.view.MotionEvent, float, float)
     */
    @Override
    public boolean onScroll(final MotionEvent arg0, final MotionEvent arg1, final float distanceX, final float distanceY) {
        boolean ret = false;
        mScroller.forceFinished(true);
        final int distX = (int)distanceX;
        final int distY = (int)distanceY;
        final int oldX = getScrollPosX();
        final int oldY = getScrollPosY();
        final int x = oldX + distX;
        final int y = oldY + distY;
        setScrollPosX(x);
        setScrollPosY(y);
        Log.v(TAG, "onScroll:distanceX:" + distanceX + ",distX:" + distX + ",distanceY" + distanceY + ",distY:" + distY);
        ViewCompat.postInvalidateOnAnimation(this);
        ret = true;
        return ret;
    }

    /*
     * (non-Javadoc)
     * @see
     * android.view.GestureDetector.OnGestureListener#onShowPress(android.view
     * .MotionEvent)
     */
    @Override
    public void onShowPress(final MotionEvent arg0) {
    }

    /*
     * (non-Javadoc)
     * @see
     * android.view.GestureDetector.OnGestureListener#onSingleTapUp(android.
     * view.MotionEvent)
     */
    @Override
    public boolean onSingleTapUp(final MotionEvent motionEvent) {
        final int xCoordinate = (int)motionEvent.getX();
        final int yCoordinate = (int)motionEvent.getY();
        final int[] itemPosition = getProgramPosition(xCoordinate, yCoordinate);
        return false;
    }

    /*
     * (non-Javadoc)
     * @see android.view.View#computeScroll()
     */
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            final int x = mScroller.getCurrX();
            final int y = mScroller.getCurrY();
            setScrollPosX(x);
            setScrollPosY(y);
            Log.v(TAG, "fling:getCurrX:" + x + ",getCurrY" + y + ", finalX:" + mScroller.getFinalX() + ",finalY:"
                    + mScroller.getFinalY());
            ViewCompat.postInvalidateOnAnimation(this);
        }
        // Animation,Scrolling is finished. Put  logos in Memory
        // cache.
        else {
            final int height = getMeasuredHeight();
            final int childHeight = getItemHeight();
            final int scrollX = getScrollPosY();

            int top = -(scrollX % childHeight);

            final int scrollY = getScrollPosY();

            int currentVisibleItemIndex = scrollY / childHeight;
            if (top != 0) {
                top -= childHeight;
                currentVisibleItemIndex--;
            }
            while (currentVisibleItemIndex < 0) {
                top += childHeight;
                currentVisibleItemIndex++;
            }
            // Iterate through columns.
            while ((top < height) && (currentVisibleItemIndex < getCount())) {
                top += childHeight;

                // Caching only visible items.
                final String imageCacheKey = String.valueOf(currentVisibleItemIndex);
                final Bitmap cachedBitmap = mImageCache.getBitmap(imageCacheKey);
                if (null == cachedBitmap) {
                    // Fetch Dummy Drawables from res.
                    final Bitmap tempBitmap = mImages.getBitmapAtIndex(currentVisibleItemIndex);
                    if (null != tempBitmap) {
                        mImageCache.putBitmap(imageCacheKey, tempBitmap);
                    }
                }
                currentVisibleItemIndex++;
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(final Canvas canvas) {
        final long startTime = System.currentTimeMillis();

        super.onDraw(canvas);
        final int right = getMeasuredWidth();
        final int height = getMeasuredHeight();
        final int childHeight = getItemHeight();
        final int scrollPositionY = getScrollPosY();
        Log.v(TAG, "MMM scrollPositionY:" + scrollPositionY + " phone width:" + right);

        int top = -(scrollPositionY % childHeight);
        int index = scrollPositionY / childHeight;
        if (top != 0) {
            top -= childHeight;
            index--;
        }
        while (index < 0) {
            top += childHeight;
            index++;
        }
        // Iterate through rows.
        while ((top < height) && (index < getCount())) {
            // Fetch  at index index.
            canvas.save();
            canvas.translate(0, top);
            canvas.clipRect(0, 0, right, childHeight);
            drawItem(canvas, index, right, childHeight, top);
            canvas.restore();
            top += childHeight;
            index++;
        }

        final long endTime = System.currentTimeMillis();
        final long timeInDraw = endTime - startTime;
        Log.v(TAG, "timeInDraw:" + timeInDraw);
    }

    /**
     * Draw  Item.
     * 
     * @param canvas -canvas
     * @param row - row index.
     * @param rowWidth -row width
     * @param rowHeight -row height
     * @param yOffset -y offset.
     */
    private void drawItem(final Canvas canvas, final int row, final int rowWidth, final int rowHeight,
            final int yOffset) {
        final int logoWidth = mLogoWidth;
        int itemWidth = 0; // It should be updated
                           // based on program
        final int[][] itemWidthArray = new int[][] {
                {
                        100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1100, 1200, 1300, 1400, 1500, 1600, 1700,
                        1800, 1900, 2000
                },
                {
                        10, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1100, 1200, 1300, 1400, 1500, 1600, 1700,
                        1800, 1900, 2000
                },
                {
                        200, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1100, 1200, 1300, 1400, 1500, 1600, 1700,
                        1800, 1900, 2000
                },
                {
                        250, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1100, 1200, 1300, 1400, 1500, 1600, 1700,
                        1800, 1900, 2000
                }
        };
        final int[] itemWidths = itemWidthArray[row % itemWidthArray.length];
        // final int[] itemWidths = {
        // 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1100, 1200, 1300,
        // 1400, 1500, 1600, 1700, 1800,
        // 1900, 2000
        // };

        // Find  at index index ,then initialize itemWidths array with
        // programs duration.
        itemWidth = itemWidths[0];
        final int scrollPositionX = getScrollPosX();
        int xOffset = 0;
        xOffset = -(scrollPositionX % itemWidth);
        int index = scrollPositionX / itemWidth;
        if (xOffset != 0) {
            xOffset -= itemWidth;
            index--;
        }
        while (index < 0) {
            xOffset += itemWidth;
            index++;
        }
        xOffset = -scrollPositionX;
        index = 0;
        // calculate program index for which end time is less then current
        // time.Initialize offset with that index program start time.

        // After applying given logic remove while and if condition below.
        while ((xOffset < 0) && (index > 0)) {
            xOffset += itemWidths[index];
            index++;
        }
        if ((xOffset > 0) && (index > 0)) {
            index--;
            xOffset -= itemWidths[index];
        }
        // not required..After applying given logic.
        Log.v(TAG, " scrollPositionX:" + scrollPositionX + ", xOffset:" + xOffset + ", index:" + index);
        xOffset += logoWidth;
        // Iterate through columns.
        while ((xOffset < rowWidth) && (index < 20)) {
            itemWidth = itemWidths[index];
            canvas.save();
            canvas.translate(xOffset, 0);
            canvas.clipRect(0, 0, itemWidth, rowHeight);
            // Pass Program as parameter here.
            drawItem(canvas, row, index, rowHeight, itemWidth, xOffset, yOffset);
            canvas.restore();

            xOffset += itemWidth;
            Log.v(TAG, "xOffset:" + xOffset);
            index++;
        }
        canvas.drawRect(0, 0, getMeasuredWidth() - 1, rowHeight - 1, mPaint);

        canvas.drawRect(0, 0, logoWidth - 1, rowHeight - 1, mFillPaint);

        final String imageCacheKey = String.valueOf(row);
        final Bitmap logoBitmap = mImageCache.getBitmap(imageCacheKey);

        if (null != logoBitmap) {
            canvas.drawBitmap(logoBitmap, 0, 0, mFillPaint);
        } else {
            canvas.drawBitmap(mDefaultLogoBitmap, 0, 0, mFillPaint);
        }
    }

    /**
     * Draw Rectangle for  item.
     * 
     * @param canvas -canvas
     * @param row - row index
     * @param col - column index
     * @param itemHeight -  item height
     * @param itemWidth - item width
     * @param xOffset - Horizontal offset
     * @param yOffset - Vertical offset.
     */
    private void drawItem(final Canvas canvas, final int row, final int col, final int itemHeight, final int itemWidth,
            final int xOffset, final int yOffset) {

        final Rect rect = new Rect(0, 0, itemWidth, itemHeight);
        drawRectText("The Harry Porter and Prisoner of Azcaban.", canvas, rect, mPaint);

        final float startX = 0;
        final float startY = 0;
        final float stopX = 0;
        final float stopY = itemHeight;
        canvas.drawLine(startX, startY, stopX, stopY, mProgramSepratorPaint);
        Log.v(TAG, "drawLine() called,startX" + startX + " ,startY:" + startY + " ,stopX" + stopX + "  ,stopY:" + stopY);
    }

    /**
     * It fetches  item height.
     * 
     * @return - height
     */
    private int getItemHeight() {
        return mLogoHeight;
    }

    /**
     * It fetches Horizontal Scrolled X Position.
     * 
     * @return
     */
    private int getScrollPosX() {
        return mScrollPosX;
    }

    /**
     * It fetches Vertical Scrolled Y Position.
     * 
     * @return
     */
    private int getScrollPosY() {
        return mScrollPosY;
    }

    /**
     * It fetches horizontal maximum scrollable X Position.
     * 
     * @return - x position
     */
    private int getMaxPosX() {
        return mMaxPosX;
    }

    /**
     * It fetches horizontal maximum scrollable Y Position.
     * 
     * @return - y position.
     */
    private int getMaxPosY() {
        return (getCount() * getItemHeight()) - getMeasuredHeight();
    }

    /**
     * It sets current X position.
     * 
     * @param posX
     */
    private void setScrollPosX(final int posX) {
        mScrollPosX = posX;
    }

    /**
     * It sets current y position.
     * 
     * @param posY
     */
    private void setScrollPosY(final int posY) {
        mScrollPosY = posY;
    }

    /**
     * It sets Maximum horizontal X position.
     * 
     * @param maxPosX
     */
    private void setMaxPosX(final int maxPosX) {
        mMaxPosX = maxPosX;
    }

    /**
     * It fetches  count.
     * 
     * @return - count.
     */
    private int getCount() {
        return 50;
    }

    /**
     * It fetches  at some position.
     * 
     * @param xCoordinate - X Position.
     * @param yCoordinate - Y position.
     * @return
     */
    private int[] getProgramPosition(final int xCoordinate, final int yCoordinate) {
        final int childHeight = getItemHeight();

        final int scrollY = getScrollPosY();
        int yOffset = -(scrollY % childHeight);
        int yIndex = scrollY / childHeight;
        if (yOffset != 0) {
            yOffset -= childHeight;
            yIndex--;
        }
        while (yIndex < 0) {
            yOffset += childHeight;
            yIndex++;
        }
        // Iterate through columns.

        while (yOffset < yCoordinate) {
            yOffset += childHeight;
            yIndex++;
        }

        final int logoWidth = childHeight;
        final int itemWidth = 200;
        int xOffset = 0;
        xOffset = -(getScrollPosX() % itemWidth);
        int xIndex = getScrollPosX() / itemWidth;
        if (xOffset != 0) {
            xOffset -= itemWidth;
            xIndex--;
        }
        while (xIndex < 0) {
            xOffset += itemWidth;
            xIndex++;
        }
        xOffset += logoWidth;
        // Iterate through rows.
        while (xOffset < xCoordinate) {
            xOffset += itemWidth;
            xIndex++;
        }
        Log.v(TAG, "onItemFound() Called. row:" + yIndex + " , column:" + xIndex + " xOffset:" + xOffset + " yOffset:"
                + yOffset);
        xOffset += itemWidth;
        final int[] positionArray = new int[2];
        positionArray[0] = yIndex;
        positionArray[1] = xIndex;
        return positionArray;

    }

    /*
     * (non-Javadoc)
     * @see android.view.View#onDetachedFromWindow()
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mImageCache.clearCache();
    }

    /**
     * Draws program name , start and end time.
     * 
     * @param text - text to be drawn.
     * @param canvas
     * @param r - rectangle.
     * @param paint - TextPaint Object.
     */
    private void drawRectText(final String text, final Canvas canvas, final Rect r, final Paint paint) {

        final int width = r.width();

        Log.v(TAG, "Rect top" + r.top + " , left:" + r.left + ", right:" + r.right + ", bottom:" + r.bottom
                + " , centerX:" + r.exactCenterX() + " ,centerY:" + r.exactCenterY() + " ,mTextGap:" + mTextGap);

        // Paint for ellipsizing program text.
        final TextPaint ellipsizeTextPaint = new TextPaint();
        ellipsizeTextPaint.setTextSize(mProgramsTextSize);
        ellipsizeTextPaint.setTypeface(Typeface.DEFAULT);
        CharSequence elipText = text;
        elipText = TextUtils.ellipsize(elipText, ellipsizeTextPaint, width, TextUtils.TruncateAt.END);

        // Paint to draw program name.
        paint.setTextSize(mProgramsTextSize);
        // paint.setTextAlign(Align.CENTER);
        paint.setTypeface(Typeface.DEFAULT_BOLD);

        Log.v(TAG, "Program Name X:" + mProgramNameLeftEgde + " ,Y:" + mProgramNameTopGap);
        canvas.drawText(elipText.toString(), mProgramNameLeftEgde, mProgramNameTopGap, paint);

        // Paint to draw duration text.
        paint.setTextSize(mProgramsDurationSize);
        paint.setTypeface(Typeface.DEFAULT);
        Log.v(TAG, "Program Duration X:" + mProgramNameLeftEgde + " ,Y:"
                + (mProgramNameTopGap + mProgramsTextSize + mTextGap));
        canvas.drawText("10:00-12:10", mProgramNameLeftEgde, mProgramNameTopGap + mProgramsTextSize + mTextGap,
                paint);

        // 12dp for program name
        // 9dp for timings
        // 6dp leading for program name
        // 16dp leading for timings

    }
}
