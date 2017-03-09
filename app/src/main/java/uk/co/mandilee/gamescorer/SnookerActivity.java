package uk.co.mandilee.gamescorer;

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
    private Ball activeBall;

    private Player[] players;
    private Player activePlayer;

    private int minBall = 1; // increments as all balls of specific colour are potted to keep fouls correct after reds gone

    private Boolean isFree = false,
            isFoul = false,
            isMiss = false;

    private TextView tvCurrentActivity, // TextView to update current Ball On
            tvRedBallsRemaining; // TextView to show how many red balls remaining

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snooker);

        players = new Player[]{
                new Player((EditText) findViewById(R.id.player_one_name), (TextView) findViewById(R.id.player_one_score), (LinearLayout) findViewById(R.id.player_one)),
                new Player((EditText) findViewById(R.id.player_two_name), (TextView) findViewById(R.id.player_two_score), (LinearLayout) findViewById(R.id.player_two)),
        };
        activePlayer = players[1];

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
        activeBall = balls[0];

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
                        if (activeBall != null) {
                            setFoul(activeBall.getPoints());
                        } else {
                            setFoul(4);
                        }
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
        if (activePlayer == players[0]) {
            // If player 1 is active, switch to player 2
            activePlayer = players[1];
            players[1].setActive();
            players[0].setInactive();
        } else {
            // If player 2 is active, switch to player 2
            activePlayer = players[0];
            players[0].setActive();
            players[1].setInactive();
        }

        // if it's not a foul or a miss, or there are red balls remaining, set active ball to Red
        if ((!isFoul && !isMiss) || balls[0].getNumRemaining() > 0) {
            setActiveBall(0);
        } else {
            activeBall = balls[minBall];
        }
    }

    private void setScore(int addThisScore) {
        int activeBallPoints = 0;

        if (activeBall != null) {
            activeBallPoints = activeBall.getPoints();
        }

        if (isFree) {
            if (activeBall == null) {
                addThisScore = 1;
            } else {
                addThisScore = activeBallPoints;
            }
        }
        // if potted ball isn't active then set as foul!
        if (!isFoul && activeBallPoints > 0 && activeBallPoints != addThisScore && !isFree) {
            isFoul = true;
            if (activeBallPoints == 1) {
                showText(getString(R.string.foul_potted) + " " + getString(R.string.red_ball));
            }
            setFoul(addThisScore);
        } else {

            activePlayer.addScore(addThisScore);

            if (!isFoul || balls[0].getNumRemaining() > 0) {
                setActiveBall(addThisScore);
            }
        }

        isFoul = false;
        setFreeBall(false);
    }

    private void setFoul(int score) {
        if (score < 4) {
            score = 4;
        }
        switchPlayer();
        setScore(score);
    }

    private void setBallActive() {
        for (Ball ball : balls) {
            if (ball.getPoints() > 0 && ball.isImageButton()) {
                if (ball == activeBall) {
                    ball.setBallActive();
                } else {
                    ball.setBallInactive();
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
            balls[0].setOnePotted();
        }
        if (balls[0].getNumRemaining() > 0) {
            if ((ball == 1 && balls[0] == activeBall) || (isFree && balls[0] == activeBall)) {
                activeBall = null;
                setColoredBallsActive();
                tvCurrentActivity.setText(getText(R.string.colored_balls_on));

            } else if (ball == 1 && balls[0] != activeBall && !isFree) {
                isFoul = true;
                showText(getString(R.string.foul_potted) + " " + getString(R.string.color));
                setFoul(4);
                tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(balls[0].getTextResId()));

            } else if (!isFree) {
                activeBall = balls[0];
                setBallActive();
                tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(balls[0].getTextResId()));
            }
            tvRedBallsRemaining.setText(balls[0].getNumRemaining() + " " + getString(R.string.red_balls_remaining));

        } else if (balls[0].getNumRemaining() == 0) {
            balls[0].setOnePotted();
            activeBall = balls[1];
            minBall = 1;
            setBallActive();
            balls[0].disableImageButton();
            tvRedBallsRemaining.setText("");
            tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(balls[ball].getTextResId()));

        } else {
            if (ball == 7) {
                balls[6].disableImageButton();
                activeBall = null;
                checkWinner();
            } else {
                balls[(ball - 1)].disableImageButton();
                activeBall = balls[ball];
                minBall = ball;
            }
            tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(balls[ball].getTextResId()));
            setBallActive();
        }
        setFreeBall(false);
    }

    private void resetButton() {
        for (Player player : players) {
            player.doReset();
        }

        activeBall = balls[0];
        activePlayer = players[1];
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

        if (players[0].getScore() > players[1].getScore()) {
            players[0].setWinner();
            players[1].setInactive();
            showText(players[0].getPlayerName() + " " + getString(R.string.wins));

        } else if (players[0].getScore() < players[1].getScore()) {
            players[1].setWinner();
            players[0].setInactive();
            showText(players[1].getPlayerName() + " " + getString(R.string.wins));

        } else {
            players[0].setInactive();
            players[1].setInactive();
            showText(getString(R.string.game_draw));
        }
    }

}
