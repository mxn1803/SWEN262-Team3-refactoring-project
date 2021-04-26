package com.steamy.model;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Class representing a game being played on a Lane
 *
 * @author Daniel Campman
 * @version 04/26/2021
 */
public class Game {

    private Party party;
    private Lane lane;

    private HashMap<Bowler, Frame[]> scores;
    private int frameNum = 0;

    private Iterator bowlerIterator;

    /**
     * Creates a game of bowling to be played
     *
     * @param lane The {@link Lane} the game is being played on
     * @param party The party playing the game
     */
    public Game(Lane lane, Party party) {
        this.lane = lane;
        this.party = party;
        bowlerIterator = party.getMembers().iterator();

        frameNum = 0;
        scores = new HashMap<>();
        for (Bowler member : party.getMembers()) {

        }
    }

    /**
     * Returns whether the game has been finished
     * @return `True` if every {@link Bowler} in the {@link Party} has
     * completed their last frame
     */
    public boolean isFinished() {
        return true;
    }

    /**
     * Makes the next {@link Bowler} in the game take their throw. The {@link Bowler}
     * and frame this throw is assigned to will be managed by the game
     * @param val The value of the throw that the {@link Bowler} made
     * @return `True` if there are more turns left to take.
     * `False` if the game has finished
     */
    public boolean recordThrow(int val) {
        if (bowlerIterator.hasNext()) {
            bowlerIterator.next();
        }
        return isFinished();
    }
}
