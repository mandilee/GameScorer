package uk.co.mandilee.gamescorer;

class Result {

    private static final int NO_IMAGE_PROVIDED = -1;
    private final int mPlayerOne;
    private final int mPlayerTwo;
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    Result(int playerOne, int playerTwo, int imageResourceId) {
        mPlayerOne = playerOne;
        mPlayerTwo = playerTwo;
        mImageResourceId = imageResourceId;
    }

    int getImageResourceId() {
        return mImageResourceId;
    }

    int getPlayerTwo() {
        return mPlayerTwo;
    }

    int getPlayerOne() {
        return mPlayerOne;
    }

    Boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }
}
