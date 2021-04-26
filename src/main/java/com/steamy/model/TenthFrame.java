package com.steamy.model;

/**
 * Class which represents the tenth frame of a Bowling game
 *
 * @author Daniel Campman
 * @version 04/26/21
 */
public class TenthFrame extends Frame {

    /**
     * Creates an object representing the tenth frame of a bowling game
     * It accepts three throws and calculates points appropriately
     */
    public TenthFrame() {
        super(null);
        bowls = new Integer[3];
    }

    /**
     * Returns the score for this frame, computed according to the rules of bowling
     * @return The combined score of the two bowls this frame and any additional
     *          bowls from Strike or Spare bonuses
     */
    public int getScore() {
        // Get the value of the throws this frame
        int score = bowls[0] == null ? 0 : bowls[0];
        score += bowls[1] == null ? 0 : bowls[1];
        score += bowls[1] == null ? 0 : bowls[1];
        return score;
    }

    /**
     * Gets the bonus for a spare from this Frame
     * @return The bonus the previous frame earns for a spare
     */
    private int getSpareBonus() {
        return bowls[0] == null ? 0 : bowls[0];
    }

    /**
     * Gets the bonus for a strike from this Frame
     * @return The bonus the previous frame earns for a Strike
     */
    private int getStrikeBonus() {
        int firstThrow = bowls[0] == null ? 0 : bowls[0];
        int secondThrow = bowls[0] == null ? 0 : bowls[0];
        return firstThrow + secondThrow;
    }
}
