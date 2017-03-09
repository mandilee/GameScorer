package uk.co.mandilee.gamescorer;

/**
 * {@link Shot} represents a potted ball/action in the snooker game with the intent
 * of being able to undo actions and to provide a full summary at the end.
 * It contains the player, active ball, potted ball, number of points awarded
 * and shot type (normal, free, miss, foul)
 */
class Shot extends SnookerActivity {

    /**
     * the active player
     */
    private final int mPlayerId;

    /**
     * which ball was potted
     */
    private final int mBallPotted;

    /**
     * which ball should have been potted
     */
    private final int mBallOn;

    /**
     * how many points were awarded (negative were awarded to the other player)
     */
    private final int mPoints;

    /**
     * what type of action was processed
     */
    private int mType;

    /**
     * Create a new Shot object.
     *
     * @param playerId   is the ArrayList id of the player who was active
     * @param ballPotted is the ball that was potted (and was active)
     * @param points     is the number of points awarded
     */
    Shot(int playerId, int ballPotted, int points) {
        mPlayerId = playerId;
        mBallPotted = ballPotted;
        mBallOn = ballPotted;
        mPoints = points;
    }

    /**
     * Create a new Shot object.
     *
     * @param playerId   is the ArrayList id of the player who was active
     * @param ballPotted is the ball that was potted
     * @param points     is the number of points awarded
     * @param ballOn     is the ball that was active
     * @param type       is the type of action (free, miss, foul)
     */
    Shot(int playerId, int ballPotted, int points, int ballOn, int type) {
        mPlayerId = playerId;
        mBallPotted = ballPotted;
        mBallOn = ballOn;
        mPoints = points;
        mType = type;
    }
}
