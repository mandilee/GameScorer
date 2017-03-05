package uk.co.mandilee.gamescorer;

import android.graphics.Color;
import android.widget.Button;
import android.widget.ImageButton;

class Ball {

    private final int mTextResId;
    private final int mPoints;
    private final int mNumOnTable;
    private int mNumPottedPlayerOne = 0;
    private int mNumPottedPlayerTwo = 0;
    private int mNumRemaining;

    private ImageButton mImageButton;
    private Button mButton;

    private String mType;


    Ball(int textResId, int points, int numOnTable, ImageButton imageButton) {
        mTextResId = textResId;
        mPoints = points;
        mNumOnTable = numOnTable;
        mNumRemaining = numOnTable;
        mImageButton = imageButton;
    }

    Ball(int textResId, ImageButton imageButton, String type) {
        mTextResId = textResId;
        mPoints = 0;
        mNumOnTable = 0;
        mNumRemaining = 0;
        mImageButton = imageButton;
        mType = type;
    }

    Ball(int textResId, Button button, String type) {
        mTextResId = textResId;
        mPoints = 0;
        mNumOnTable = 0;
        mNumRemaining = 0;
        mButton = button;
        mType = type;
    }

    String getType() {
        return mType;
    }

    int getTextResId() {
        return mTextResId;
    }


    int getPoints() {
        return mPoints;
    }

    ImageButton getImageButton() {
        return mImageButton;
    }

    Button getButton() {
        return mButton;
    }

    Boolean isImageButton() {
        return mImageButton != null;
    }

    int getNumRemaining() {
        return mNumRemaining;
    }

    void setOnePotted() {
        mNumRemaining--;
    }


    int getNumPottedPlayerTwo() {
        return mNumPottedPlayerTwo;
    }

    void setNumPottedPlayerTwo() {
        mNumPottedPlayerTwo++;
    }

    int getNumPottedPlayerOne() {
        return mNumPottedPlayerOne;
    }

    void setNumPottedPlayerOne() {
        mNumPottedPlayerOne++;
    }

    void doReset() {
        mNumPottedPlayerOne = 0;
        mNumPottedPlayerTwo = 0;
        mNumRemaining = mNumOnTable;
    }


    void setBallActive() {
        mImageButton.setBackgroundResource(R.color.activeBallBackground);
    }

    void setBallInactive() {
        mImageButton.setBackgroundResource(R.color.snookerTableGreen);
    }


    void disableImageButton() {
        if (isImageButton()) {
            mImageButton.setClickable(false);
            mImageButton.setColorFilter(Color.argb(255, 10, 108, 3));
        }
    }

    void enableImageButton() {
        if (isImageButton()) {
            mImageButton.setClickable(true);
            mImageButton.setColorFilter(Color.argb(0, 10, 108, 3));
        }
    }
}

