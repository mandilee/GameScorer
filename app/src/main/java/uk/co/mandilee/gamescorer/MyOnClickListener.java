package uk.co.mandilee.gamescorer;

import android.view.View;

class MyOnClickListener implements View.OnClickListener {

    private final int mCurrentPlayer;
    private final int mActiveBall;

    public MyOnClickListener(int currentPlayer, int activeBall) {
        mCurrentPlayer = currentPlayer;
        mActiveBall = activeBall;
    }

    @Override
    public void onClick(View v) {
    }

}
