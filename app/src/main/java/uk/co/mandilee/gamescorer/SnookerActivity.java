package uk.co.mandilee.gamescorer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SnookerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int NORMAL_BALL = 0,
            FREE_BALL = 1,
            MISS_BALL = 2,
            FOUL_BALL = 3,
            RESET = 4,
            UNDO_MOVE = 5;

    private List<Ball> balls;
    private Ball activeBall;

    private List<Player> players;
    private Player activePlayer;

    private List<Shot> shots;

    private int minBall = 0; // increments as all balls of specific colour are potted to keep fouls correct after reds gone
    private Ball redBall;

    private Boolean isFree = false,
            isFoul = false,
            isMiss = false;

    private TextView tvCurrentActivity, // TextView to update current Ball On
            tvRedBallsRemaining; // TextView to show how many red balls remaining
    private LinearLayout llSummary;
    private TableLayout tlTheBalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snooker);

        players = new ArrayList<>(); // activate the players
        players.add(new Player((EditText) findViewById(R.id.player_one_name), (TextView) findViewById(R.id.player_one_score), (LinearLayout) findViewById(R.id.player_one)));
        players.add(new Player((EditText) findViewById(R.id.player_two_name), (TextView) findViewById(R.id.player_two_score), (LinearLayout) findViewById(R.id.player_two)));
        activePlayer = players.get(1); // set the WRONG active player - we "switch" it to set the formatting soon

        balls = new ArrayList<>();
        // set up the balls
        balls.add(new Ball(R.string.red_ball, 1, 15, (ImageButton) findViewById(R.id.ball_red)));
        balls.add(new Ball(R.string.yellow_ball, 2, 1, (ImageButton) findViewById(R.id.ball_yellow)));
        balls.add(new Ball(R.string.green_ball, 3, 1, (ImageButton) findViewById(R.id.ball_green)));
        balls.add(new Ball(R.string.brown_ball, 4, 1, (ImageButton) findViewById(R.id.ball_brown)));
        balls.add(new Ball(R.string.blue_ball, 5, 1, (ImageButton) findViewById(R.id.ball_blue)));
        balls.add(new Ball(R.string.pink_ball, 6, 1, (ImageButton) findViewById(R.id.ball_pink)));
        balls.add(new Ball(R.string.black_ball, 7, 1, (ImageButton) findViewById(R.id.ball_black)));
        balls.add(new Ball(R.string.miss_ball, (ImageButton) findViewById(R.id.ball_miss), MISS_BALL));
        balls.add(new Ball(R.string.foul_ball, (ImageButton) findViewById(R.id.ball_foul), FOUL_BALL));
        // add the misc buttons
        balls.add(new Ball(R.string.free_button, (Button) findViewById(R.id.ball_free), FREE_BALL));
        balls.add(new Ball(R.string.undo_move, (Button) findViewById(R.id.undo), UNDO_MOVE));
        balls.add(new Ball(R.string.reset_button, (Button) findViewById(R.id.reset), RESET));

        activeBall = balls.get(0); // set the active ball
        redBall = balls.get(0);

        shots = new ArrayList<>();

        // Grab required Views
        tvCurrentActivity = (TextView) findViewById(R.id.current_activity);
        tvRedBallsRemaining = (TextView) findViewById(R.id.red_balls_remaining);
        llSummary = (LinearLayout) findViewById(R.id.summary);
        tlTheBalls = (TableLayout) findViewById(R.id.the_balls);

        // loop through balls and set OnClickListeners
        for (Ball ball : balls) {
            if (ball.isImageButton()) {
                ball.getImageButton().setOnClickListener(this);
            } else {
                ball.getButton().setOnClickListener(this);
            }
        }

        setActivePlayer(); // set formatting for active player and ball
    }

    @Override
    public void onClick(View v) {
        // Perform action on click

        for (Ball ball : balls) {
            if (ball.isImageButton()) {
                if (v.getId() == ball.getImageButton().getId()) {

                    // if it's a miss, just need to switch player
                    if (ball.getType() == MISS_BALL) {
                        shots.add(new Shot(players.indexOf(activePlayer), balls.indexOf(activeBall), MISS_BALL, 0));
                        setActivePlayer();
                        return; // no more to do so quit it here

                        // if it's a foul,
                        // or it's not a free ball and active ball isn't the one potted
                        // set the Foul flag
                    } else if (ball.getType() == FOUL_BALL ||   // foul has been clicked
                            (ball != activeBall && !isFree && activeBall != null) ||      // or non-active ball potted and not free
                            (activeBall == null && ball == redBall)) { // or red was potted when colours were On

                        int points;

                        setIsFoul(true);
                        setBallPotted(ball);

                        if (activeBall == null || activeBall.getPoints() < 4) {
                            points = 4;
                        } else {
                            points = activeBall.getPoints();
                        }

                        shots.add(new Shot(players.indexOf(activePlayer), balls.indexOf(ball), -points, balls.indexOf(activeBall), FOUL_BALL));
                        setActivePlayer();
                        activePlayer.addScore(points);
                        return;
                    }

                    // if it's a free ball then any ball is fair game
                    if (isFree && !isFoul) {
                        if (activeBall != null) {
                            // free ball gives active ball points
                            shots.add(new Shot(players.indexOf(activePlayer), balls.indexOf(ball), activeBall.getPoints(), balls.indexOf(activeBall), FREE_BALL));
                            activePlayer.addScore(activeBall.getPoints());
                        } else {
                            // null activeBall means a colour was potted.
                            // use those points
                            shots.add(new Shot(players.indexOf(activePlayer), balls.indexOf(ball), ball.getPoints(), -1, FREE_BALL));
                            activePlayer.addScore(ball.getPoints());
                        }
                        setIsFree(false);
                        return; // no more to do so quit it here
                    }

                    // if none of the above apply, it must be a proper pot!
                    if (activeBall == null) {
                        shots.add(new Shot(players.indexOf(activePlayer), balls.indexOf(ball), ball.getPoints(), -1, NORMAL_BALL));
                    } else {
                        shots.add(new Shot(players.indexOf(activePlayer), balls.indexOf(ball), ball.getPoints()));
                    }

                    activePlayer.addScore(ball.getPoints());
                    autoSetBallOn(ball);
                }

            } else {
                if (v.getId() == ball.getButton().getId()) {
                    int type = ball.getType();
                    if (type == FREE_BALL) {
                        setIsFree(true); // set the free ball

                    } else if (type == UNDO_MOVE) {
                        undo(); // set the free ball


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

    private void setBallPotted(Ball potted) {
        if (activeBall == potted || potted == redBall) {
            potted.setOnePotted();
            tvRedBallsRemaining.setText(String.valueOf(redBall.getNumRemaining()) + " " + getString(R.string.red_balls_remaining));
            if (potted.getNumRemaining() == 0) {
                potted.disableButton();
                showText("There are no " + getString(potted.getTextResId()) + " balls left");
            }
        }
    }

    private void autoSetBallOn(Ball potted) {
        setBallPotted(potted);

        if (activeBall == null) {
            activeBall = redBall;
        } else if (activeBall == balls.get(6)) { //black ball
            setWinner();
        } else if (potted == redBall) {             // if the red ball is potted
            if (redBall.getNumRemaining() > 0) {    // and there are some red balls left
                activeBall = null;                  // flip to "colored balls active"
            } else {
                activeBall = balls.get(1);          // otherwise next ball is Yellow
            }
        } else {
            for (Ball ball : balls) {               // loop through the balls
                if (ball.getNumRemaining() > 0) {   // find the first one with balls remaining
                    activeBall = ball;              // and set that active
                    break;
                }
            }
        }
        if (activeBall != null) {
            tvCurrentActivity.setText(getString(R.string.ball_on) + " " + getString(activeBall.getTextResId()));
        } else {
            tvCurrentActivity.setText(getString(R.string.ball_on) + " " + getString(R.string.colored_balls_on));
        }

        setBallStyles();
    }

    @NonNull
    private Boolean resetBallOn() {
        if (redBall.getNumRemaining() > 0) {        // if there are red balls remaining
            activeBall = redBall;                   // set it active
            tvRedBallsRemaining.setText(String.valueOf(redBall.getNumRemaining()) + " " + getString(R.string.red_balls_remaining));
            tvCurrentActivity.setText(getString(R.string.ball_on) + " " + getString(activeBall.getTextResId()));
            setBallStyles();
            return true;                            // and return true
        } else {
            return false;                           // otherwise, return false to trigger setOnBall()
        }
    }

    private void setBallStyles() {
        for (Ball ball : balls) {
            if (ball.isImageButton() && ball.getPoints() > 0) {
                if (activeBall == null) {
                    if (ball != redBall) {
                        ball.setBallActive();
                    } else {
                        ball.setBallInactive();
                    }
                } else {
                    if (ball == activeBall) {
                        ball.setBallActive();
                    } else {
                        ball.setBallInactive();
                    }
                }
            }
        }
    }

    private void setActivePlayer() {
        activePlayer.setInactive();                 // set current activePlayer as active
        if (activePlayer == players.get(0)) {       // if first player is active
            activePlayer = players.get(1);          // switch to player two
        } else {
            activePlayer = players.get(0);          // otherwise switch to first player
        }
        activePlayer.setActive();                   // set new activePlayer as active
        resetBallOn();
        setIsFoul(false);
        setIsFree(false);
    }

    private void setIsFoul(Boolean isTrue) {
        isFoul = isTrue;
    }

    private void setIsFree(Boolean isTrue) {
        isFree = isTrue;
    }

    private void resetButton() {
        for (Player player : players) {
            player.doReset();
        }

        shots.clear();

        tvCurrentActivity.setVisibility(View.VISIBLE);
        tvRedBallsRemaining.setVisibility(View.VISIBLE);
        tlTheBalls.setVisibility(View.VISIBLE);
        llSummary.setVisibility(View.GONE);

        activeBall = balls.get(0);
        activePlayer = players.get(1);
        isFoul = false;
        isMiss = false;

        for (Ball ball : balls) {
            ball.doReset();
            ball.enableButton();
        }

        setActivePlayer();
    }

    private void undo() {
        if (shots.size() > 0) {
            Shot temp = shots.get(shots.size() - 1);
            Ball tempBall = balls.get(temp.getBallPotted());
            shots.remove(shots.size() - 1);

            if (tempBall.getNumRemaining() < tempBall.getNumOnTable() && tempBall == redBall) {
                tempBall.unsetOnePotted();
            }
            if (temp.getType() == FOUL_BALL) {
                showText(getString(R.string.undone) + " " + getString(R.string.foul_ball));
                activePlayer.addScore(temp.getPoints());
                setActivePlayer();

            } else if (temp.getType() == MISS_BALL) {
                showText(getString(R.string.undone) + " " + getString(R.string.miss_ball));
                setActivePlayer();

            } else {
                activePlayer.addScore(-temp.getPoints());

                if (tempBall.getNumRemaining() < tempBall.getNumOnTable() && tempBall != redBall) {
                    tempBall.unsetOnePotted();
                }
                tempBall.enableButton();

                if (temp.getBallOn() == -1) {
                    activeBall = null;
                } else {
                    activeBall = balls.get(temp.getBallOn());
                }
                showText(getString(R.string.undone) + " " + getString(tempBall.getTextResId()));

            }

            tvRedBallsRemaining.setText(String.valueOf(redBall.getNumRemaining()) + " " + getString(R.string.red_balls_remaining));
            if (activeBall != null) {
                tvCurrentActivity.setText(getString(R.string.ball_on) + " " + getString(activeBall.getTextResId()));
            } else {
                tvCurrentActivity.setText(getString(R.string.colored_balls_on));
            }

            setBallStyles();

        } else {
            showText(getString(R.string.nothing_to_undo));
        }
    }

    private void setWinner() {
        for (Ball ball : balls) {
            if (ball.getType() != RESET) {
                ball.setBallInactive();
                ball.disableButton();
            }
        }

        tvCurrentActivity.setVisibility(View.GONE);
        tvRedBallsRemaining.setVisibility(View.GONE);
        tlTheBalls.setVisibility(View.GONE);
        setSummary();
        llSummary.setVisibility(View.VISIBLE);


        int player_one_score = players.get(0).getScore();
        int player_two_score = players.get(1).getScore();

        if (player_one_score > player_two_score) {
            players.get(0).setWinner();
            showText(getString(R.string.player_1_name) + " " + getString(R.string.wins));
        } else if (player_one_score < player_two_score) {
            players.get(1).setWinner();
            showText(getString(R.string.player_2_name) + " " + getString(R.string.wins));
        } else {
            players.get(0).setInactive();
            players.get(1).setInactive();
            showText(getString(R.string.game_draw));
        }
    }

    private void setSummary() {

        int numOutcomes = balls.size() - 3; // ignore free, undo and reset
        int[][] summary = new int[2][numOutcomes];
        String[] outcomes = new String[numOutcomes];
        String strOutcomes = "",
                strPlayerOneSummary = "",
                strPlayerTwoSummary = "";

        // Views for Summary
        TextView tvOutcomes = (TextView) findViewById(R.id.summary_outcomes);
        TextView playerOneSummary = (TextView) findViewById(R.id.summary_player_one);
        TextView playerTwoSummary = (TextView) findViewById(R.id.summary_player_two);

        // loop through the outcomes to get the text names
        for (int l = 0; l < numOutcomes; l++) {
            outcomes[l] = getString(balls.get(l).getTextResId());
        }

        // loop through the shots to get the summary
        for (Shot shot : shots) {
            if (shot.getType() == FOUL_BALL) {
                summary[shot.getPlayerId()][8]++;
            } else {
                summary[shot.getPlayerId()][shot.getBallPotted()]++;
            }
        }

        // create the strings from the arrays
        for (int l = 0; l < numOutcomes; l++) {
            strOutcomes += outcomes[l] + "\n";
            strPlayerOneSummary += summary[0][l] + "\n";
            strPlayerTwoSummary += summary[1][l] + "\n";
        }

        // set the strings as the display
        tvOutcomes.setText(strOutcomes);
        playerOneSummary.setText(strPlayerOneSummary);
        playerTwoSummary.setText(strPlayerTwoSummary);

    }
}
