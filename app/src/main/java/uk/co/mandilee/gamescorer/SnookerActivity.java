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

import java.util.ArrayList;
import java.util.List;

public class SnookerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int NORMAL_BALL = 0;
    private static final int FREE_BALL = 1;
    private static final int MISS_BALL = 2;
    private static final int FOUL_BALL = 3;
    private static final int RESET = 4;


    private List<Ball> balls;
    private Ball activeBall;

    private List<Player> players;
    private Player activePlayer;

    private List<Shot> shots;

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

        players = new ArrayList<>(); // activate the players
        players.add(new Player((EditText) findViewById(R.id.player_one_name), (TextView) findViewById(R.id.player_one_score), (LinearLayout) findViewById(R.id.player_one)));
        players.add(new Player((EditText) findViewById(R.id.player_two_name), (TextView) findViewById(R.id.player_two_score), (LinearLayout) findViewById(R.id.player_two)));
        activePlayer = players.get(1); // set the WRONG active player - we "switch" it to set the formatting soon

        balls = new ArrayList<>(); // set up the balls
        balls.add(new Ball(R.string.red_ball, 1, 15, (ImageButton) findViewById(R.id.ball_red)));
        balls.add(new Ball(R.string.yellow_ball, 2, 1, (ImageButton) findViewById(R.id.ball_yellow)));
        balls.add(new Ball(R.string.green_ball, 3, 1, (ImageButton) findViewById(R.id.ball_green)));
        balls.add(new Ball(R.string.brown_ball, 4, 1, (ImageButton) findViewById(R.id.ball_brown)));
        balls.add(new Ball(R.string.blue_ball, 5, 1, (ImageButton) findViewById(R.id.ball_blue)));
        balls.add(new Ball(R.string.pink_ball, 6, 1, (ImageButton) findViewById(R.id.ball_pink)));
        balls.add(new Ball(R.string.black_ball, 7, 1, (ImageButton) findViewById(R.id.ball_black)));
        balls.add(new Ball(R.string.miss_ball, (ImageButton) findViewById(R.id.ball_miss), MISS_BALL));
        balls.add(new Ball(R.string.foul_ball, (ImageButton) findViewById(R.id.ball_foul), FOUL_BALL));
        balls.add(new Ball(R.string.free_button, (Button) findViewById(R.id.ball_free), FREE_BALL));
        balls.add(new Ball(R.string.reset_button, (Button) findViewById(R.id.reset), RESET));
        activeBall = balls.get(0); // set the active ball

        shots = new ArrayList<>();

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

        switchPlayer(); // set formatting for active player and ball
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

        balls.get(9).getButton().setBackgroundResource(backgroundColour);
    }

    @Override
    public void onClick(View v) {
        // Perform action on click

        for (Ball ball : balls) {
            if (ball.isImageButton()) {
                if (v.getId() == ball.getImageButton().getId()) {
                    int points = ball.getPoints();
                    int type = ball.getType();

                    if (points > 0) {

                        // activeBall was potted, all is good
                        if (activeBall == ball) {
                            shots.add(new Shot(players.indexOf(activePlayer), balls.indexOf(activeBall), points));
                            setScore(points);

                            // activeBall == null when all colored balls are on i.e. when a red has just been potted
                            // if ball index > 0 (i.e. not red) all is good
                        } else if (activeBall == null && balls.indexOf(ball) > 0) {
                            shots.add(new Shot(players.indexOf(activePlayer), balls.indexOf(ball), points, 0, NORMAL_BALL));
                            setScore(ball.getPoints());

                            // if is FreeBall, all is good
                        } else if (isFree && activeBall != null) {
                            //public Shot(int playerId, int ballPotted, int points, int ballOn, int type)
                            shots.add(new Shot(players.indexOf(activePlayer), balls.indexOf(ball), activeBall.getPoints(), balls.indexOf(activeBall), FREE_BALL));
                            setScore(activeBall.getPoints());

                            // if is FreeBall, all is good
                        } else if (isFree && activeBall == null) {
                            shots.add(new Shot(players.indexOf(activePlayer), balls.indexOf(ball), ball.getPoints(), 0, FREE_BALL));
                            setScore(ball.getPoints());

                            // otherwise it's a foul!
                        } else {
                            isFoul = true;
                            shots.add(new Shot(players.indexOf(activePlayer), balls.indexOf(activeBall), 0, balls.indexOf(ball), FOUL_BALL));
                            setFoul();
                        }

                        // missed ball switches player. That is all
                    } else if (type == MISS_BALL) {
                        isMiss = true;
                        shots.add(new Shot(players.indexOf(activePlayer), balls.indexOf(activeBall), 0, balls.indexOf(ball), MISS_BALL));
                        switchPlayer();

                        // foul ball gives penalty points to the other player
                    } else if (type == FOUL_BALL) {
                        isFoul = true;
                        shots.add(new Shot(players.indexOf(activePlayer), balls.indexOf(activeBall), 0, balls.indexOf(ball), FOUL_BALL));
                        setFoul();
                    }
                }

                // ball wasn't potted, button was pressed
            } else {
                if (v.getId() == ball.getButton().getId()) {
                    int type = ball.getType();
                    if (type == FREE_BALL) {
                        setFreeBall(true); // set the free ball
                    } else if (type == RESET) {
                        resetButton(); // reset
                    }
                }
            }
        }
    }

    private void showText(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void switchPlayer() {
        if (activePlayer == players.get(0)) {
            // If player 1 is active, switch to player 2
            activePlayer = players.get(1);
            players.get(1).setActive();
            players.get(0).setInactive();
        } else {
            // If player 2 is active, switch to player 2
            activePlayer = players.get(0);
            players.get(0).setActive();
            players.get(1).setInactive();
        }

        // if it's not a foul or a miss, or there are red balls remaining, set active ball to Red
        if ((!isFoul && !isMiss) || balls.get(0).getNumRemaining() > 0) {
            setActiveBall(0);
        } else {
            activeBall = balls.get(minBall);
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
            setFoul();
        } else {
            activePlayer.addScore(addThisScore);

            if (!isFoul || balls.get(0).getNumRemaining() > 0) {
                setActiveBall(addThisScore);
            }
        }

        isFoul = false;
        setFreeBall(false);
        //*/
    }

    private void setFoul() {
        int score = 0;
        if (activeBall != null) {
            score = activeBall.getPoints();
        }
        if (score < 4) { // minimum of 4 penalty points
            score = 4;
        }
        switchPlayer(); // other player gets the points so switch first
        setScore(score); // then set the score
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
        for (int i = 0; i < balls.size(); i++) {
            if (balls.get(i).getPoints() > 0 && balls.get(i).isImageButton()) {
                if (i == 0) {
                    balls.get(i).setBallInactive();
                } else {
                    balls.get(i).setBallActive();
                }
            }
        }
    }

    private void setActiveBall(int ball) {
        if (ball == 1) {
            balls.get(0).setOnePotted();
        }
        if (balls.get(0).getNumRemaining() > 0) {
            if ((ball == 1 && balls.get(0) == activeBall) || (isFree && balls.get(0) == activeBall)) {
                activeBall = null;
                setColoredBallsActive();
                tvCurrentActivity.setText(getText(R.string.colored_balls_on));

            } else if (ball == 1 && balls.get(0) != activeBall && !isFree) {
                isFoul = true;
                showText(getString(R.string.foul_potted) + " " + getString(R.string.color));
                setFoul();
                tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(balls.get(0).getTextResId()));

            } else if (!isFree) {
                activeBall = balls.get(0);
                setBallActive();
                tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(balls.get(0).getTextResId()));
            }
            tvRedBallsRemaining.setText(balls.get(0).getNumRemaining() + " " + getString(R.string.red_balls_remaining));

        } else if (balls.get(0).getNumRemaining() == 0) {
            balls.get(0).setOnePotted();
            activeBall = balls.get(1);
            minBall = 1;
            setBallActive();
            balls.get(0).disableImageButton();
            tvRedBallsRemaining.setText("");
            tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(balls.get(ball).getTextResId()));

        } else {
            if (ball == 7) {
                balls.get(6).disableImageButton();
                activeBall = null;
                checkWinner();
            } else {
                balls.get((ball - 1)).disableImageButton();
                activeBall = balls.get(ball);
                minBall = ball;
            }
            tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(balls.get(ball).getTextResId()));
            setBallActive();
        }
        setFreeBall(false);
    }

    private void resetButton() {
        for (Player player : players) {
            player.doReset();
        }

        activeBall = balls.get(0);
        activePlayer = players.get(1);
        isFoul = false;
        isMiss = false;

        for (Ball ball : balls) {
            ball.doReset();
            ball.enableImageButton();
        }

        switchPlayer();
    }

    private void checkWinner() {
        balls.get(7).disableImageButton();
        balls.get(8).disableImageButton();

        if (players.get(0).getScore() > players.get(1).getScore()) {
            players.get(0).setWinner();
            players.get(1).setInactive();
            showText(players.get(0).getPlayerName() + " " + getString(R.string.wins));

        } else if (players.get(0).getScore() < players.get(1).getScore()) {
            players.get(1).setWinner();
            players.get(0).setInactive();
            showText(players.get(1).getPlayerName() + " " + getString(R.string.wins));

        } else {
            players.get(0).setInactive();
            players.get(1).setInactive();
            showText(getString(R.string.game_draw));
        }
    }

}
