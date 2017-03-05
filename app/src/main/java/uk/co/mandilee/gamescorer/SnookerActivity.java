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

public class SnookerActivity extends AppCompatActivity {

    final private int numRedBalls = 15;
    private final ArrayList<ImageButton> ibBalls = new ArrayList<>(); // ArrayList for balls to tidy code a bit
    private int playerOneScore = 0,
            playerTwoScore = 0,
            redBallsPotted = 0,
            activeBall = 1, // initialise so it sets correct player to begin with
            activePlayer = 0; // first active ball is red
    private Boolean freeBall = false, // NOT YET IMPLEMENTED
            isFoul = false,
            isMiss = false;
    private EditText
            etPlayerOneName, etPlayerTwoName; // EditText views for player names
    private TextView tvPlayerOneScore, tvPlayerTwoScore, // TextViews for player scores
            tvCurrentActivity, // TextView to update current Ball On
            tvRedBallsRemaining; // TextView to show how many red balls remaining
    private LinearLayout llPlayerOne, llPlayerTwo; // LinearLayouts to set background for active player
    private ImageButton ibBallRed, ibBallYellow, ibBallGreen, ibBallBrown, ibBallBlue, ibBallPink, ibBallBlack, ibBallMiss, ibBallFoul; // ImageButtons to refer to later
    private Button bBallFree; // Button for Free Ball - not yet implemented

    private static void setImageButtonEnabled(ImageButton item, boolean enabled) {
        item.setClickable(enabled);

        if (enabled) {
            item.setColorFilter(Color.argb(0, 255, 255, 255));
        } else {
            item.setColorFilter(Color.argb(255, 255, 255, 255));
        }
    }

    /* NOT YET IMPLEMENTED
    private void setFreeBall() {
        freeBall = true;
    }
    //*/

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

        // Grab ImageButton for Red Ball and set Listener
        ibBallRed = (ImageButton) findViewById(R.id.ball_red);
        ibBallRed.setOnClickListener(new MyOnClickListener(activePlayer, activeBall) {
            @Override
            public void onClick(View v) {
                setScore(1);
            }
        });

        // Grab ImageButton for Yellow Ball and set Listener
        ibBallYellow = (ImageButton) findViewById(R.id.ball_yellow);
        ibBallYellow.setOnClickListener(new MyOnClickListener(activePlayer, activeBall) {
            @Override
            public void onClick(View v) {
                setScore(2);
            }
        });

        // Grab ImageButton for Green Ball and set Listener
        ibBallGreen = (ImageButton) findViewById(R.id.ball_green);
        ibBallGreen.setOnClickListener(new MyOnClickListener(activePlayer, activeBall) {
            @Override
            public void onClick(View v) {
                setScore(3);
            }
        });

        // Grab ImageButton for Brown Ball and set Listener
        ibBallBrown = (ImageButton) findViewById(R.id.ball_brown);
        ibBallBrown.setOnClickListener(new MyOnClickListener(activePlayer, activeBall) {
            @Override
            public void onClick(View v) {
                setScore(4);
            }
        });

        // Grab ImageButton for Blue Ball and set Listener
        ibBallBlue = (ImageButton) findViewById(R.id.ball_blue);
        ibBallBlue.setOnClickListener(new MyOnClickListener(activePlayer, activeBall) {
            @Override
            public void onClick(View v) {
                setScore(5);
            }
        });

        // Grab ImageButton for Pink Ball and set Listener
        ibBallPink = (ImageButton) findViewById(R.id.ball_pink);
        ibBallPink.setOnClickListener(new MyOnClickListener(activePlayer, activeBall) {
            @Override
            public void onClick(View v) {
                setScore(6);
            }
        });

        // Grab ImageButton for Black Ball and set Listener
        ibBallBlack = (ImageButton) findViewById(R.id.ball_black);
        ibBallBlack.setOnClickListener(new MyOnClickListener(activePlayer, activeBall) {
            @Override
            public void onClick(View v) {
                setScore(7);
            }
        });

        // Grab ImageButton for Missed Ball and set Listener
        ibBallMiss = (ImageButton) findViewById(R.id.ball_miss);
        ibBallMiss.setOnClickListener(new MyOnClickListener(activePlayer, activeBall) {
            @Override
            public void onClick(View v) {
                isMiss = true;
                switchPlayer();
                isMiss = false;
            }
        });

        // Grab ImageButton for Fouled Ball and set Listener
        ibBallFoul = (ImageButton) findViewById(R.id.ball_foul);
        ibBallFoul.setOnClickListener(new MyOnClickListener(activePlayer, activeBall) {
            @Override
            public void onClick(View v) {
                isFoul = true;
                setFoul();
                isFoul = false;
            }
        });

        /* NOT YET IMPLEMENTED
        bBallFree = (Button) findViewById(R.id.ball_free);
        bBallFree.setOnClickListener(new MyOnClickListener(activePlayer, activeBall) {
            @Override
            public void onClick(View v) {
                setFreeBall();
            }
        });
        //*/

        // Grab Button for Resetting and set Listener
        Button bReset = (Button) findViewById(R.id.reset);
        bReset.setOnClickListener(new MyOnClickListener(activePlayer, activeBall) {
            @Override
            public void onClick(View v) {
                resetButton();
            }
        });

        // Add Ball ImageButtons to the ArrayList for later
        ibBalls.add(ibBallRed);
        ibBalls.add(ibBallYellow);
        ibBalls.add(ibBallGreen);
        ibBalls.add(ibBallBrown);
        ibBalls.add(ibBallBlue);
        ibBalls.add(ibBallPink);
        ibBalls.add(ibBallBlack);

        switchPlayer();
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
//        } else {
//            activeBall = 1;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ball.setBackgroundColor(getResources().getColor(backgroundColour, getTheme()));
        } else {
            ball.setBackgroundColor(getResources().getColor(backgroundColour));
        }
    }

    private void setScore(int addThisScore) {
        /* NOT YET IMPLEMENTED
        if (freeBall) {
            addThisScore = activeBall;
        }*/
        // if potted ball isn't active then set as foul!
        if (!isFoul && activeBall > 0 && activeBall != addThisScore) {
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
            if (ball == 1 && activeBall == 1) {
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
            allBallsPotted(ibBallRed);
            tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(R.string.yellow_ball));
            tvRedBallsRemaining.setText("");
            setBallActive();

        } else {
            switch (ball) {
                case 1:
                    allBallsPotted(ibBallRed);
                    activeBall = 2;
                    tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(R.string.yellow_ball));
                    break;

                case 2:
                    allBallsPotted(ibBallYellow);
                    activeBall = 3;
                    tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(R.string.green_ball));
                    break;

                case 3:
                    allBallsPotted(ibBallGreen);
                    tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(R.string.brown_ball));
                    activeBall = 4;
                    break;

                case 4:
                    allBallsPotted(ibBallBrown);
                    tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(R.string.blue_ball));
                    activeBall = 5;
                    break;

                case 5:
                    allBallsPotted(ibBallBlue);
                    tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(R.string.pink_ball));
                    activeBall = 6;
                    break;

                case 6:
                    allBallsPotted(ibBallPink);
                    tvCurrentActivity.setText(getText(R.string.ball_on) + " " + getText(R.string.blue_ball));
                    activeBall = 7;
                    break;

                case 7:
                    allBallsPotted(ibBallBlack);
                    activeBall = -1;
                    tvCurrentActivity.setText("");
                    checkWinner();
            }
            setBallActive();
        }
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
