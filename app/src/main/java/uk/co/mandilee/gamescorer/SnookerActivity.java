package uk.co.mandilee.gamescorer;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SnookerActivity extends AppCompatActivity implements View.OnClickListener {

    final private int numRedBalls = 15;

    private final ArrayList<ImageButton> ibBalls = new ArrayList<>(); // ArrayList for balls to tidy code a bit

    private int playerOneScore = 0,
            playerTwoScore = 0,
            redBallsPotted = 0,
            minBall = 1,
            activeBall = 1, // initialise so it sets correct player to begin with
            activePlayer = 0; // first active ball is red

    private Boolean isFree = false,
            isFoul = false,
            isMiss = false;

    private EditText
            etPlayerOneName, etPlayerTwoName; // EditText views for player names

    private TextView tvPlayerOneScore, tvPlayerTwoScore, // TextViews for player scores
            tvCurrentActivity, // TextView to update current Ball On
            tvRedBallsRemaining; // TextView to show how many red balls remaining

    private LinearLayout llPlayerOne, llPlayerTwo; // LinearLayouts to set background for active player

    private ImageButton ibBallRed, ibBallYellow, ibBallGreen, ibBallBrown, ibBallBlue, ibBallPink, ibBallBlack, ibBallMiss, ibBallFoul; // ImageButtons to refer to later

    private Button bBallFree, // Button for Free Ball
            bReset; // Button for Reset button

    private static void setImageButtonEnabled(ImageButton item, boolean enabled) {
        item.setClickable(enabled);

        if (enabled) {
            item.setColorFilter(Color.argb(0, 255, 255, 255));
        } else {
            item.setColorFilter(Color.argb(255, 255, 255, 255));
        }
    }

    private void setFreeBall(Boolean free) {
        isFree = free;

        int backgroundColour;
        // select colour based on whether or not active is true
        if (isFree) {
            backgroundColour = R.color.activeBallBackground;
        } else {
            backgroundColour = android.R.drawable.btn_default;
        }

        bBallFree.setBackgroundResource(backgroundColour);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snooker);

        // Grab required LinearLayouts
        llPlayerOne = (LinearLayout) findViewById(R.id.player_one);
        llPlayerTwo = (LinearLayout) findViewById(R.id.player_two);

        // Grab required TextViews
        tvPlayerOneScore = (TextView) findViewById(R.id.player_one_score);
        etPlayerOneName = (EditText) findViewById(R.id.player_one_name);
        tvPlayerTwoScore = (TextView) findViewById(R.id.player_two_score);
        etPlayerTwoName = (EditText) findViewById(R.id.player_two_name);
        tvCurrentActivity = (TextView) findViewById(R.id.current_activity);
        tvRedBallsRemaining = (TextView) findViewById(R.id.red_balls_remaining);

        ibBallRed = (ImageButton) findViewById(R.id.ball_red);
        ibBallRed.setOnClickListener(this);

        ibBallYellow = (ImageButton) findViewById(R.id.ball_yellow);
        ibBallYellow.setOnClickListener(this);

        ibBallGreen = (ImageButton) findViewById(R.id.ball_green);
        ibBallGreen.setOnClickListener(this);

        ibBallBrown = (ImageButton) findViewById(R.id.ball_brown);
        ibBallBrown.setOnClickListener(this);

        ibBallBlue = (ImageButton) findViewById(R.id.ball_blue);
        ibBallBlue.setOnClickListener(this);

        ibBallPink = (ImageButton) findViewById(R.id.ball_pink);
        ibBallPink.setOnClickListener(this);

        ibBallBlack = (ImageButton) findViewById(R.id.ball_black);
        ibBallBlack.setOnClickListener(this);

        ibBallMiss = (ImageButton) findViewById(R.id.ball_miss);
        ibBallMiss.setOnClickListener(this);

        ibBallFoul = (ImageButton) findViewById(R.id.ball_foul);
        ibBallFoul.setOnClickListener(this);

        bBallFree = (Button) findViewById(R.id.ball_free);
        bBallFree.setOnClickListener(this);

        bReset = (Button) findViewById(R.id.reset);
        bReset.setOnClickListener(this);

        ibBalls.add(ibBallRed);
        ibBalls.add(ibBallYellow);
        ibBalls.add(ibBallGreen);
        ibBalls.add(ibBallBrown);
        ibBalls.add(ibBallBlue);
        ibBalls.add(ibBallPink);
        ibBalls.add(ibBallBlack);

        switchPlayer();
    }

    @Override
    public void onClick(View v) {
        // Perform action on click
        switch (v.getId()) {
            case R.id.ball_red:
                setScore(1);
                break;
            case R.id.ball_yellow:
                setScore(2);
                break;
            case R.id.ball_green:
                setScore(3);
                break;
            case R.id.ball_brown:
                setScore(4);
                break;
            case R.id.ball_blue:
                setScore(5);
                break;
            case R.id.ball_pink:
                setScore(6);
                break;
            case R.id.ball_black:
                setScore(7);
                break;
            case R.id.ball_miss:
                isMiss = true;
                switchPlayer();
                break;
            case R.id.ball_foul:
                isFoul = true;
                setFoul();
                break;
            case R.id.reset:
                resetButton();
                break;
            case R.id.ball_free:
                setFreeBall(true);
                break;
        }
    }

    private void showText(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void switchPlayer() {
        if (activePlayer == 1) {
            // If player 1 is active, switch to player 2
            activePlayer = 2;
            setPlayerColour(tvPlayerTwoScore, etPlayerTwoName, llPlayerTwo, true, false);
            setPlayerColour(tvPlayerOneScore, etPlayerOneName, llPlayerOne, false, false);

        } else {
            // If player 2 is active, switch to player 2
            activePlayer = 1;
            setPlayerColour(tvPlayerTwoScore, etPlayerTwoName, llPlayerTwo, false, false);
            setPlayerColour(tvPlayerOneScore, etPlayerOneName, llPlayerOne, true, false);
        }

        // if it's not a foul or a miss, or there are red balls remaining, set active ball to Red
        if ((!isFoul && !isMiss) || redBallsPotted < numRedBalls) {
            setActiveBall(0);
        } else {
            activeBall = minBall;
        }
    }

    private void setPlayerColour(TextView tvScore, TextView tvName, LinearLayout linearLayout, Boolean active, Boolean winner) {
        int textColor;
        int backgroundColour;
        // select colours based on whether or not active is true
        if (winner) {
            textColor = R.color.winnerPlayerText;
            backgroundColour = R.color.winnerPlayerBackground;
        } else if (active) {
            textColor = R.color.activePlayerText;
            backgroundColour = R.color.activePlayerBackground;
        } else {
            textColor = R.color.inactivePlayerText;
            backgroundColour = R.color.inactivePlayerBackground;
        }
        // set colours by methods required by build version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tvScore.setTextColor(getResources().getColor(textColor, getTheme()));
            tvName.setTextColor(getResources().getColor(textColor, getTheme()));
            linearLayout.setBackgroundColor(getResources().getColor(backgroundColour, getTheme()));
        } else {
            tvScore.setTextColor(getResources().getColor(textColor));
            tvName.setTextColor(getResources().getColor(textColor));
            linearLayout.setBackgroundColor(getResources().getColor(backgroundColour));
        }

    }

    private void setBallColour(ImageButton ball, Boolean active) {
        int backgroundColour;
        // select colour based on whether or not active is true
        if (active) {
            backgroundColour = R.color.activeBallBackground;
        } else {
            backgroundColour = R.color.inactiveBallBackground;
        }
        // set colours by methods required by build version
        ball.setBackgroundResource(backgroundColour);
    }

    private void setScore(int addThisScore) {
        if (isFree) {
            if (activeBall == 0) {
                addThisScore = 1;
            } else {
                addThisScore = activeBall;
            }
        }
        // if potted ball isn't active then set as foul!
        if (!isFoul && activeBall > 0 && activeBall != addThisScore && !isFree) {
            isFoul = true;
            if (activeBall == 1) {
                showText(getString(R.string.foul_potted) + " " + getString(R.string.red_ball));
            }
            activeBall = addThisScore;
            setFoul();
        } else {

            if (activePlayer == 1) {
                playerOneScore += addThisScore;
                tvPlayerOneScore.setText(String.valueOf(playerOneScore));
            } else {
                playerTwoScore += addThisScore;
                tvPlayerTwoScore.setText(String.valueOf(playerTwoScore));
            }
            if (!isFoul || redBallsPotted < numRedBalls) {
                setActiveBall(addThisScore);
            }
            isFoul = false;
            setFreeBall(false);
        }
    }

    private void setFoul() {
        int score = activeBall;
        if (score < 4) {
            score = 4;
        }
        switchPlayer();
        setScore(score);
    }

    private void setBallActive() {
        for (int i = 0; i < ibBalls.size(); i++) {
            if (i == (activeBall - 1) || (activeBall == 0 && i > 1)) {
                setBallColour(ibBalls.get(i), true);
            } else {
                setBallColour(ibBalls.get(i), false);
            }
        }
    }

    private void setColoredBallsActive() {
        for (int i = 0; i < ibBalls.size(); i++) {
            if (i == 0) {
                setBallColour(ibBalls.get(i), false);
            } else {
                setBallColour(ibBalls.get(i), true);
            }
        }
    }

    private void allBallsPotted(ImageButton disableMe) {
        setImageButtonEnabled(disableMe, false);
    }

    private void setActiveBall(int ball) {
        if (ball == 1) {
            redBallsPotted++;
        }
        if (redBallsPotted < numRedBalls) {
            if (isFree) {
                if (activeBall == 1) {
                    activeBall = 0;
                    setColoredBallsActive();
                    tvCurrentActivity.setText(getText(R.string.colored_balls_on));
                }

            } else if (ball == 1 && activeBall == 1) {
                activeBall = 0;
                setColoredBallsActive();
                tvCurrentActivity.setText(getText(R.string.colored_balls_on));

            } else if (ball == 1 && activeBall != 1) {
                isFoul = true;
                showText(getString(R.string.foul_potted) + " " + getString(R.string.color));
                setFoul();

            } else {
                activeBall = 1;
                setBallActive();
                tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(R.string.red_ball));
            }
            tvRedBallsRemaining.setText((numRedBalls - redBallsPotted) + " " + getString(R.string.red_balls_remaining));

        } else if (redBallsPotted == numRedBalls) {
            redBallsPotted++;
            activeBall = 2;
            setBallActive();
            allBallsPotted(ibBallRed);
            tvRedBallsRemaining.setText("");
            tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(R.string.yellow_ball));

        } else {
            switch (ball) {
                case 1:
                    allBallsPotted(ibBallRed);
                    activeBall = 2;
                    minBall = 2;
                    tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(R.string.yellow_ball));
                    break;

                case 2:
                    allBallsPotted(ibBallYellow);
                    activeBall = 3;
                    minBall = 3;
                    tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(R.string.green_ball));
                    break;

                case 3:
                    allBallsPotted(ibBallGreen);
                    activeBall = 4;
                    minBall = 4;
                    tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(R.string.brown_ball));
                    break;

                case 4:
                    allBallsPotted(ibBallBrown);
                    activeBall = 5;
                    minBall = 5;
                    tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(R.string.blue_ball));
                    break;

                case 5:
                    allBallsPotted(ibBallBlue);
                    activeBall = 6;
                    minBall = 6;
                    tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(R.string.pink_ball));
                    break;

                case 6:
                    allBallsPotted(ibBallPink);
                    activeBall = 7;
                    minBall = 7;
                    tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(R.string.black_ball));
                    break;

                case 7:
                    allBallsPotted(ibBallBlack);
                    activeBall = -1;
                    tvCurrentActivity.setText("");
                    checkWinner();
            }
            setBallActive();
        }
        setFreeBall(false);
    }

    private void resetButton() {
        playerOneScore = 0;
        playerTwoScore = 0;
        tvPlayerOneScore.setText("0");
        tvPlayerTwoScore.setText("0");
        activeBall = 0;
        activePlayer = 0;
        redBallsPotted = 0;
        isFoul = false;
        isMiss = false;

        for (int i = 0; i < ibBalls.size(); i++) {
            setImageButtonEnabled(ibBalls.get(i), true);
        }
        setImageButtonEnabled(ibBallMiss, true);
        setImageButtonEnabled(ibBallFoul, true);

        switchPlayer();
    }

    private void checkWinner() {
        setImageButtonEnabled(ibBallMiss, false);
        setImageButtonEnabled(ibBallFoul, false);

        if (playerOneScore > playerTwoScore) {
            setPlayerColour(tvPlayerOneScore, etPlayerOneName, llPlayerOne, false, true);
            setPlayerColour(tvPlayerTwoScore, etPlayerTwoName, llPlayerTwo, false, false);
            showText(getString(R.string.player_1_wins));

        } else if (playerOneScore < playerTwoScore) {
            setPlayerColour(tvPlayerOneScore, etPlayerOneName, llPlayerOne, false, false);
            setPlayerColour(tvPlayerTwoScore, etPlayerTwoName, llPlayerTwo, false, true);
            showText(getString(R.string.player_2_wins));

        } else {
            setPlayerColour(tvPlayerOneScore, etPlayerOneName, llPlayerOne, false, false);
            setPlayerColour(tvPlayerTwoScore, etPlayerTwoName, llPlayerTwo, false, false);
            showText(getString(R.string.game_draw));
        }
    }

}
