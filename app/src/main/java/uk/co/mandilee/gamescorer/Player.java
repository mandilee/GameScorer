package uk.co.mandilee.gamescorer;

import android.graphics.Color;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * {@link Player} represents a player in the game of snooker.
 * It contains a the Views to be manipulated - EditText player name, TextView player score
 * and LinearLayout (to show active player), and int score
 */
class Player {

    /**
     * EditText that holds the player name
     */
    private final EditText mEtPlayerName;

    /** TextView that holds the player score */
    private final TextView mTvPlayerScore;

    /** LinearLayout that holds the player */
    private final LinearLayout mLlPlayer;

    /** the players score */
    private int mScore = 0;

    /**
     * Create a new Player object.
     *
     * @param etPlayerName  is the EditText view that contains the players name
     * @param tvPlayerScore is the TextView that displays the players score
     * @param llPlayer      is the LinearLayout that holds the Player information
     */
    Player(EditText etPlayerName, TextView tvPlayerScore, LinearLayout llPlayer) {
        mEtPlayerName = etPlayerName;
        mTvPlayerScore = tvPlayerScore;
        mLlPlayer = llPlayer;
    }

    /**
     * Return the player name
     */
    String getPlayerName() {
        return String.valueOf(mEtPlayerName.getText());
    }

    /**
     * Add the given points to the players score
     */
    void addScore(int score) {
        mScore += score;
        displayScore();
    }

    /**
     * Update the TextView with the players score
     */

    private void displayScore() {
        mTvPlayerScore.setText(String.valueOf(mScore));
    }


    /**
     * Get the players score
     */
    int getScore() {
        return mScore;
    }


    /**
     * Reset the players score
     */
    void doReset() {
        mScore = 0;
        displayScore();
    }

    /**
     * Set the player inactive by changing the background
     */
    void setInactive() {
        setColors(Color.GRAY, R.color.snookerTableGreen);
    }

    /**
     * Set the player active by changing the background
     */
    void setActive() {
        setColors(Color.BLACK, R.color.activePlayerBackground);
    }

    /**
     * Set the player as the winner by changing the background
     */
    void setWinner() {
        setColors(Color.BLACK, R.color.winnerPlayerBackground);
    }

    /**
     * Set the Text and Background colours as given
     */
    private void setColors(int textColor, int backgroundColour) {
        mEtPlayerName.setTextColor(textColor);
        mTvPlayerScore.setTextColor(textColor);
        mLlPlayer.setBackgroundResource(backgroundColour);

    }
}
