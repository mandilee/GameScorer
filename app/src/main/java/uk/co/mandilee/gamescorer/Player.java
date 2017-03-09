package uk.co.mandilee.gamescorer;

import android.graphics.Color;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by manda on 05/03/2017.
 */

class Player {
    private final EditText mEtPlayerName;
    private final TextView mTvPlayerScore;
    private final LinearLayout mLlPlayer;
    private int mScore = 0;

    public Player(EditText etPlayerName, TextView tvPlayerScore, LinearLayout llPlayer) {
        mEtPlayerName = etPlayerName;
        mTvPlayerScore = tvPlayerScore;
        mLlPlayer = llPlayer;
    }

    public String getPlayerName() {
        return String.valueOf(mEtPlayerName.getText());
    }

// --Commented out by Inspection START (06/03/2017 22:31):
//    public void setPlayerName(String playerName) {
//        mPlayerName = playerName;
//    }
// --Commented out by Inspection STOP (06/03/2017 22:31)

    public void addScore(int score) {
        mScore += score;
        displayScore();
    }

    private void displayScore() {
        mTvPlayerScore.setText(String.valueOf(mScore));
    }

    public int getScore() {
        return mScore;
    }

    public void doReset() {
        mScore = 0;
        displayScore();
    }

    public void setInactive() {
        setColors(Color.GRAY, R.color.snookerTableGreen);
    }

    public void setActive() {
        setColors(Color.BLACK, R.color.activePlayerBackground);
    }

    public void setWinner() {
        setColors(Color.BLACK, R.color.winnerPlayerBackground);
    }

    private void setColors(int textColor, int backgroundColour) {
        mEtPlayerName.setTextColor(textColor);
        mTvPlayerScore.setTextColor(textColor);
        mLlPlayer.setBackgroundResource(backgroundColour);

    }
}
