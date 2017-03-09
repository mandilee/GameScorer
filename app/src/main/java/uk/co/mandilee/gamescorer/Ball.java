package uk.co.mandilee.gamescorer;

import android.graphics.Color;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * {@link Ball} represents a snooker ball on the table / other action affecting the game play.
 * It contains the colour, number of points, number on the table, number remaining,
 * ImageButton or Button and type (FREE_BALL, MISS_BALL, FOUL_BALL, RESET)
 */
class Ball {

    /**
     * the colour of the ball
     */
    private final int mTextResId;

    /**
     * number of points for potting the ball
     */
    private final int mPoints;

    /**
     * number of these balls on the table
     */
    private final int mNumOnTable;

    /**
     * number of these balls remaining in this frame
     */
    private int mNumRemaining;

    /**
     * the ImageButton representing the ball
     */
    private ImageButton mImageButton;

    /**
     * the button representing the other action
     */
    private Button mButton;

    /**
     * the type of ball/action - FREE_BALL, MISS_BALL, FOUL_BALL, RESET
     */
    private int mType;

    /**
     * Create a new Ball object.
     *
     * @param textResId   is the colour of the ball
     * @param points      is the number of points awarded for potting the ball
     * @param numOnTable  is the number of these balls on the table
     * @param imageButton is the ImageButton of the ball
     */
    Ball(int textResId, int points, int numOnTable, ImageButton imageButton) {
        mTextResId = textResId;
        mPoints = points;
        mNumOnTable = numOnTable;
        mNumRemaining = numOnTable;
        mImageButton = imageButton;
    }

    /**
     * Create a new Ball object.
     *
     * @param textResId   is the colour of the ball
     * @param imageButton is the ImageButton of the ball
     * @param type        is the type of action - MISS_BALL / FOUL_BALL
     */
    Ball(int textResId, ImageButton imageButton, int type) {
        mTextResId = textResId;
        mPoints = 0;
        mNumOnTable = 0;
        mNumRemaining = 0;
        mImageButton = imageButton;
        mType = type;
    }

    /**
     * Create a new Ball object.
     *
     * @param textResId is the colour of the ball
     * @param button    is the button of the action
     * @param type      is the type of action - FREE_BALL / RESET
     */
    Ball(int textResId, Button button, int type) {
        mTextResId = textResId;
        mPoints = 0;
        mNumOnTable = 0;
        mNumRemaining = 0;
        mButton = button;
        mType = type;
    }

    /**
     * Get the type of ball / action.
     */
    int getType() {
        return mType;
    }

    /**
     * Get the default colour of the ball
     */
    int getTextResId() {
        return mTextResId;
    }

    /**
     * Get the number of points to award
     */
    int getPoints() {
        return mPoints;
    }

    /**
     * Get the ImageButton of the ball
     */
    ImageButton getImageButton() {
        return mImageButton;
    }

    /**
     * Get the Button of the action
     */
    Button getButton() {
        return mButton;
    }

    /**
     * Does this ball have an ImageButton?
     */
    Boolean isImageButton() {
        return mImageButton != null;
    }

    /**
     * Get how many of these balls are left on the table
     */
    int getNumRemaining() {
        return mNumRemaining;
    }

    /**
     * Decrease the number of balls remaining
     */
    void setOnePotted() {
        mNumRemaining--;
    }

    /**
     * Reset the number of balls
     */
    void doReset() {
        mNumRemaining = mNumOnTable;
    }

    /**
     * Set the ball as active by changing the background
     */
    void setBallActive() {
        mImageButton.setBackgroundResource(R.color.activeBallBackground);
    }

    /**
     * Get the ball as inactive by changing the background
     */
    void setBallInactive() {
        mImageButton.setBackgroundResource(R.color.snookerTableGreen);
    }

    /**
     * All balls have been potted so hide and disable the ImageButton
     */
    void disableImageButton() {
        if (isImageButton()) {
            mImageButton.setClickable(false);
            mImageButton.setColorFilter(Color.argb(255, 10, 108, 3));
        }
    }

    /**
     * Enable and display the ImageButton
     */
    void enableImageButton() {
        if (isImageButton()) {
            mImageButton.setClickable(true);
            mImageButton.setColorFilter(Color.argb(0, 10, 108, 3));
        }
    }
}

