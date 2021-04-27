package com.steamy.model;

import com.steamy.io.ScoreHistoryFile;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class representing a game being played on a Lane
 *
 * @author Daniel Campman
 * @version 04/26/2021
 */
class Game {

    private Party party;
    private Lane lane;

    private HashMap<Object, Frame[]> scores;
    private int frameIndex;
    private Object currBowler;
    private int ballNum;
    private int ballIndex;

    private Iterator bowlerIterator;

    /**
     * Creates a game of bowling to be played
     *
     * @param lane The Lane the game takes place on
     * @param party The party playing the game
     */
    Game(Lane lane, Party party) {
        this.lane = lane;
        this.party = party;
        bowlerIterator = party.getMembers().iterator();
        currBowler = bowlerIterator.next();

        frameIndex = 0;
        ballNum = 0;
        ballIndex = 0;
        scores = new HashMap<>();
        for (Object member : party.getMembers()) {
            Frame[] frames = new Frame[10];
            frames[9] = new TenthFrame();       // Add tenth frame
            for (int i = 8; i >= 0; i--) {      // Add other nine frames
                frames[i] = new Frame(frames[i + 1]);
            }
            scores.put(member, frames);
        }
    }

    /**
     * Returns the {@link Bowler} whose current turn it is.
     * @return The current {@link Bowler}. `null` if game is finished
     */
    Bowler getCurrentBowler() {
        return (Bowler)currBowler;
    }

    /**
     * Returns the current frame number
     * @return The frame number
     */
    int getFrameNum() {
        return Math.min(frameIndex + 1, 9);
    }

    /**
     * Returns how many balls have been thrown this game
     * @return The ball index
     */
    int getBallIndex() {
        return ballIndex;
    }

    /**
     * Returns whether the game has been finished
     * @return `True` if every {@link Bowler} in the {@link Party} has
     * completed their last frame
     */
    boolean isFinished() {
        return frameIndex == 10;
    }

    /**
     * Gets the total score for the given {@link Bowler} across all frames
     * @param bowler The bowler whose score should be fetched
     * @return Their total score at this point in the game
     */
    int getTotalScore(Object bowler) {
        int score = 0;
        for (Frame frame : scores.get(bowler)) {
            score += frame.getScore();
        }
        return score;
    }

    /**
     * Gets the current ball number for the {@link Bowler} in the current {@link Frame}
     * @return The current ball number
     */
    int getBallNum() {
        return ballNum;
    }

    /**
     * Gets the scores for every frame of this game
     * @param bowler The {@link Bowler} to generate the scorecard for
     * @return An array of a player's score for each {@link Frame}
     */
    int[] getScoreCard(Object bowler) {
        int[] scoreCard = new int[10];
        for (int i = 0; i < 10; i++) {
            scoreCard[i] = scores.get(bowler)[i].getScore();
        }
        return scoreCard;
    }

    /**
     * Gets every player's scores
     * @return A HashMap of every player's scores for each ball
     */
    HashMap getScores() {
        return scores;
    }

    /**
     * Gets the scores for every frame of this game
     * @param bowler The {@link Bowler} to generate the scorecard for
     * @return An array of a player's score for each {@link Frame}
     */
    int[] getCumulativeCard(Object bowler) {
        int[] scoreCard = new int[10];
        scoreCard[0] = scores.get(bowler)[0].getScore();
        for (int i = 1; i < 10; i++) {
            Frame frame = scores.get(bowler)[i];
            scoreCard[i] = frame.getBowl(0) == null ? 0 : scores.get(bowler)[i].getScore() + scoreCard[i - 1];
        }
        return scoreCard;
    }

    /**
     * Makes the next {@link Bowler} in the game take their throw. The {@link Bowler}
     * and frame this throw is assigned to will be managed by the game
     * If this method is called while `isFinished()` returns `true`, an exception will be thrown
     * @param val The value of the throw that the {@link Bowler} made.
     * @return `True` if the bowler has another throw to make
     */
    boolean recordThrow(int val) {
        // Record score
        Frame currFrame = scores.get(currBowler)[frameIndex];
        ballNum += 1;
        ballIndex += 1;
        // If the bowler does not have another ball, move to the next player in line
        if (!currFrame.addBowl(val)) {
            // If this bowler has just finished their 10th frame, save their score
            if (frameIndex == 9) {
                try {
                    Bowler bowler = (Bowler)currBowler;
                    Date date = new Date();
                    String dateString = "" + date.getHours() + ":" + date.getMinutes() + " " + date.getMonth() + "/" + date.getDay() + "/" + (date.getYear() + 1900);
                    ScoreHistoryFile.addScore(bowler.getNick(), dateString, Integer.toString(getTotalScore(bowler)));
                } catch (Exception e) {
                    System.err.println("Exception in addScore. " + e);
                }
            }
            // Cycle to next player
            if (bowlerIterator.hasNext()) {
                currBowler = bowlerIterator.next();
            } else {
                frameIndex += 1;
                bowlerIterator = party.getMembers().iterator();
                currBowler = bowlerIterator.next();
            }
            ballNum = 0;
            return false;
        }

        // Reset after a strike or spare on the 10th frame
        if (frameIndex == 9 && currFrame.getScore() == 10) {
            lane.resetPins();
        }

        return true;
    }
}
