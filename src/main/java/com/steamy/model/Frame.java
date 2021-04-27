package com.steamy.model;

/**
 * Represents a frame of a bowling {@link Game}
 *
 * @author Daniel Campman
 * @version 04/26/2021
 */
public class Frame {

    private Frame nextFrame;        // Frame after this one
    Integer[] bowls;

    /**
     * Creates an empty frame and links it with subsequent frames for
     * score calculations.
     * @param next The frame after this one
     */
    Frame(Frame next) {
        nextFrame = next;
        bowls = new Integer[2];
    }

    /**
     * Adds a bowl to the Frame
     * @param score the score for the bowl
     * @return `True` if the bowler has another ball left to throw
     */
    public boolean addBowl(int score) {
        if (bowls[0] == null) {     // Check if this was the first bowl this frame
            bowls[0] = score;
            return score != 10;     // Go to next frame if they bowled a strike
        }
        bowls[1] = score;
        return false;
    }

    /**
     * Sets the score for a given bowl in this frame
     * @param index The index of the throw being set
     * @param score The score for the given throw
     */
    public void setBowl(int index, int score) {
        bowls[index] = score;
    }

    /**
     * Gets the value of a given bowl in this frame
     * @param index The index of the throw to get
     * @return The value of the bowl. `null` if this bowl hasn't been cast yet
     * (`0` indicates the ball was thrown and no pins were knocked down)
     */
    public Integer getBowl(int index) {
        return bowls[index];
    }

    /**
     * Returns the score for this frame, computed according to the rules of bowling
     * @return The combined score of the two bowls this frame and any additional
     *          bowls from Strike or Spare bonuses
     */
    public int getScore() {
        // Get the value of the throws this frame
        int firstThrow = bowls[0] == null ? 0 : bowls[0];
        int secondThrow = bowls[1] == null ? 0 : bowls[1];
        int score = firstThrow + secondThrow;

        if (firstThrow == 10) {     // If strike, add strike bonus
            score += nextFrame.getStrikeBonus();
        } else if (score == 10) {   // If spare, add spare bonus
            score += nextFrame.getSpareBonus();
        }

        return score;
    }

    /**
     * Gets the bonus for a spare from this Frame
     * @return The bonus the previous frame earns for a spare
     */
    int getSpareBonus() {
        return bowls[0] == null ? 0 : bowls[0];
    }

    /**
     * Gets the bonus for a strike from this Frame
     * @return The bonus the previous frame earns for a Strike
     */
    int getStrikeBonus() {
        Integer nextBowl;
        // If this was a strike, get the first bowl of next frame
        if (bowls[0] != null && bowls[0] == 10) {
            nextBowl = nextFrame.getBowl(0);
        } else {
            nextBowl = bowls[1];
        }
        return getSpareBonus() + (nextBowl == null ? 0 : nextBowl);
    }
}
