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
    TenthFrame() {
        super(null);
        bowls = new Integer[3];
    }

    /**
     * Adds a bowl to the Frame
     * @param score the score for the bowl
     * @return `True` if the bowler has another ball left to throw
     */
    public boolean addBowl(int score) {
        if (bowls[0] == null) {     // Check if this was the first bowl this frame
            bowls[0] = score;
            return true;
        } else if (bowls[1] == null) {
            bowls[1] = score;
            return bowls[0] + bowls[1] > 10;    // This checks for both a 1st throw strike and 2nd throw spare
        }
        bowls[3] = score;
        return false;       // Third ball has been thrown
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
