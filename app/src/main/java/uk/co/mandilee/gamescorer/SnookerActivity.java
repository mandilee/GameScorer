package uk.co.mandilee.gamescorer;

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

public class SnookerActivity extends AppCompatActivity implements View.OnClickListener {

    private Ball[] balls;

    private int playerOneScore = 0,
            playerTwoScore = 0,
            minBall = 1, // increments as all balls of specific colour are potted to keep fouls correct after reds gone
            activeBall = 1, // first active ball is red
            activePlayer = 0; // initialise so it sets correct player to begin with

    private Boolean isFree = false,
            isFoul = false,
            isMiss = false;

    private EditText etPlayerOneName, etPlayerTwoName; // EditText views for player names

    private TextView tvPlayerOneScore, tvPlayerTwoScore, // TextViews for player scores
            tvCurrentActivity, // TextView to update current Ball On
            tvRedBallsRemaining; // TextView to show how many red balls remaining

    private LinearLayout llPlayerOne, llPlayerTwo; // LinearLayouts to set background for active player

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snooker);

        balls = new Ball[]{
                new Ball(R.string.red_ball, 1, 15, (ImageButton) findViewById(R.id.ball_red)),
                new Ball(R.string.yellow_ball, 2, 1, (ImageButton) findViewById(R.id.ball_yellow)),
                new Ball(R.string.green_ball, 3, 1, (ImageButton) findViewById(R.id.ball_green)),
                new Ball(R.string.brown_ball, 4, 1, (ImageButton) findViewById(R.id.ball_brown)),
                new Ball(R.string.blue_ball, 5, 1, (ImageButton) findViewById(R.id.ball_blue)),
                new Ball(R.string.pink_ball, 6, 1, (ImageButton) findViewById(R.id.ball_pink)),
                new Ball(R.string.black_ball, 7, 1, (ImageButton) findViewById(R.id.ball_black)),
                new Ball(R.string.miss_ball, (ImageButton) findViewById(R.id.ball_miss), "miss"),
                new Ball(R.string.foul_ball, (ImageButton) findViewById(R.id.ball_foul), "foul"),
                new Ball(R.string.free_button, (Button) findViewById(R.id.ball_free), "free"),
                new Ball(R.string.reset_button, (Button) findViewById(R.id.reset), "reset"),
        };

        // Grab required PlayerOne info
        llPlayerOne = (LinearLayout) findViewById(R.id.player_one);
        tvPlayerOneScore = (TextView) findViewById(R.id.player_one_score);
        etPlayerOneName = (EditText) findViewById(R.id.player_one_name);

        // Grab required PlayerTwo info
        llPlayerTwo = (LinearLayout) findViewById(R.id.player_two);
        tvPlayerTwoScore = (TextView) findViewById(R.id.player_two_score);
        etPlayerTwoName = (EditText) findViewById(R.id.player_two_name);

        // Grab required TextViews
        tvCurrentActivity = (TextView) findViewById(R.id.current_activity);
        tvRedBallsRemaining = (TextView) findViewById(R.id.red_balls_remaining);

        // loop through balls and set OnClickListeners
        for (Ball ball : balls) {
            if (ball.isImageButton()) {
                ball.getImageButton().setOnClickListener(this);
            } else {
                ball.getButton().setOnClickListener(this);
            }
        }

        // set active player and ball
        switchPlayer();
    }

    private void setFreeBall(Boolean free) {
        isFree = free;

        int backgroundColour;
        // select colour based on whether or not free is true
        if (isFree) {
            backgroundColour = R.color.activeBallBackground;
        } else {
            backgroundColour = android.R.drawable.btn_default;
        }

        balls[9].getButton().setBackgroundResource(backgroundColour);
    }

    @Override
    public void onClick(View v) {
        // Perform action on click

        for (Ball ball : balls) {
            if (ball.isImageButton()) {
                if (v.getId() == ball.getImageButton().getId()) {
                    int points = ball.getPoints();
                    String type = ball.getType();
                    if (points > 0) {
                        setScore(points);
                        return;
                    } else if (type.equals("miss")) {
                        isMiss = true;
                        switchPlayer();
                    } else if (type.equals("foul")) {
                        isFoul = true;
                        setFoul();
                    }
                }
            } else {
                if (v.getId() == ball.getButton().getId()) {
                    String type = ball.getType();
                    if (type.equals("free")) {
                        setFreeBall(true);
                    } else if (type.equals("reset")) {
                        resetButton();
                    }
                }
            }
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
        if ((!isFoul && !isMiss) || balls[0].getNumRemaining() > 0) {
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
            if (!isFoul || balls[0].getNumRemaining() > 0) {
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
        for (int i = 0; i < balls.length; i++) {
            if (balls[i].getPoints() > 0 && balls[i].isImageButton()) {
                if (i == (activeBall - 1) || (activeBall == 0 && i > 1)) {
                    balls[i].setBallActive();
                } else {
                    balls[i].setBallInactive();
                }
            }
        }
    }

    private void setColoredBallsActive() {
        for (int i = 0; i < balls.length; i++) {
            if (balls[i].getPoints() > 0 && balls[i].isImageButton()) {
                if (i == 0) {
                    balls[i].setBallInactive();
                } else {
                    balls[i].setBallActive();
                }
            }
        }
    }

    private void setActiveBall(int ball) {
        if (ball == 1) {
            if (activePlayer == 1) {
                balls[0].setNumPottedPlayerOne();
            } else {
                balls[0].setNumPottedPlayerTwo();
            }
            balls[0].setOnePotted();
        }
        if (balls[0].getNumRemaining() > 0) {
            if ((ball == 1 && activeBall == 1) || (isFree && activeBall == 1)) {
                activeBall = 0;
                setColoredBallsActive();
                tvCurrentActivity.setText(getText(R.string.colored_balls_on));

            } else if (ball == 1 && activeBall != 1 && !isFree) {
                isFoul = true;
                showText(getString(R.string.foul_potted) + " " + getString(R.string.color));
                setFoul();
                tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(balls[0].getTextResId()));

            } else if (!isFree) {
                activeBall = 1;
                setBallActive();
                tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(balls[0].getTextResId()));
            }
            tvRedBallsRemaining.setText(balls[0].getNumRemaining() + " " + getString(R.string.red_balls_remaining));

        } else if (balls[0].getNumRemaining() == 0) {
            balls[0].setOnePotted();
            activeBall = 2;
            minBall = 2;
            setBallActive();
            balls[0].disableImageButton();
            tvRedBallsRemaining.setText("");
            tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(balls[ball].getTextResId()));

        } else {
            switch (ball) {
                case 1:
                    balls[0].disableImageButton();
                    activeBall = 2;
                    minBall = 2;
                    break;

                case 2:
                    balls[1].disableImageButton();
                    activeBall = 3;
                    minBall = 3;
                    break;

                case 3:
                    balls[2].disableImageButton();
                    activeBall = 4;
                    minBall = 4;
                    break;

                case 4:
                    balls[3].disableImageButton();
                    activeBall = 5;
                    minBall = 5;
                    break;

                case 5:
                    balls[4].disableImageButton();
                    activeBall = 6;
                    minBall = 6;
                    break;

                case 6:
                    balls[5].disableImageButton();
                    activeBall = 7;
                    minBall = 7;
                    break;

                case 7:
                    balls[6].disableImageButton();
                    activeBall = -1;
                    checkWinner();
            }
            tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(balls[ball].getTextResId()));
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
        isFoul = false;
        isMiss = false;

        for (Ball ball : balls) {
            ball.doReset();
            ball.enableImageButton();
        }

        switchPlayer();
    }

    private void checkWinner() {
        balls[7].disableImageButton();
        balls[8].disableImageButton();

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
