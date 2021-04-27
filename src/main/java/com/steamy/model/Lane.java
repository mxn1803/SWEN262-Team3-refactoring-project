package com.steamy.model;
/* $Id$
 *
 * Revisions:
 *   $Log: Lane.java,v $
 *   Revision 1.52  2003/02/20 20:27:45  ???
 *   Fouls disables.
 *
 *   Revision 1.51  2003/02/20 20:01:32  ???
 *   Added things.
 *
 *   Revision 1.50  2003/02/20 19:53:52  ???
 *   Added foul support.  Still need to update laneview and test this.
 *
 *   Revision 1.49  2003/02/20 11:18:22  ???
 *   Works beautifully.
 *
 *   Revision 1.48  2003/02/20 04:10:58  ???
 *   Score reporting code should be good.
 *
 *   Revision 1.47  2003/02/17 00:25:28  ???
 *   Added disbale controls for View objects.
 *
 *   Revision 1.46  2003/02/17 00:20:47  ???
 *   fix for event when game ends
 *
 *   Revision 1.43  2003/02/17 00:09:42  ???
 *   fix for event when game ends
 *
 *   Revision 1.42  2003/02/17 00:03:34  ???
 *   Bug fixed
 *
 *   Revision 1.41  2003/02/16 23:59:49  ???
 *   Reporting of sorts.
 *
 *   Revision 1.40  2003/02/16 23:44:33  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.39  2003/02/16 23:43:08  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.38  2003/02/16 23:41:05  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.37  2003/02/16 23:00:26  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.36  2003/02/16 21:31:04  ???
 *   Score logging.
 *
 *   Revision 1.35  2003/02/09 21:38:00  ???
 *   Added lots of comments
 *
 *   Revision 1.34  2003/02/06 00:27:46  ???
 *   Fixed a race condition
 *
 *   Revision 1.33  2003/02/05 11:16:34  ???
 *   Boom-Shacka-Lacka!!!
 *
 *   Revision 1.32  2003/02/05 01:15:19  ???
 *   Real close now.  Honest.
 *
 *   Revision 1.31  2003/02/04 22:02:04  ???
 *   Still not quite working...
 *
 *   Revision 1.30  2003/02/04 13:33:04  ???
 *   Lane may very well work now.
 *
 *   Revision 1.29  2003/02/02 23:57:27  ???
 *   fix on pinsetter hack
 *
 *   Revision 1.28  2003/02/02 23:49:48  ???
 *   Pinsetter generates an event when all pins are reset
 *
 *   Revision 1.27  2003/02/02 23:26:32  ???
 *   ControlDesk now runs its own thread and polls for free lanes to assign queue members to
 *
 *   Revision 1.26  2003/02/02 23:11:42  ???
 *   parties can now play more than 1 game on a lane, and lanes are properly released after games
 *
 *   Revision 1.25  2003/02/02 22:52:19  ???
 *   Lane compiles
 *
 *   Revision 1.24  2003/02/02 22:50:10  ???
 *   Lane compiles
 *
 *   Revision 1.23  2003/02/02 22:47:34  ???
 *   More observering.
 *
 *   Revision 1.22  2003/02/02 22:15:40  ???
 *   Add accessor for pinsetter.
 *
 *   Revision 1.21  2003/02/02 21:59:20  ???
 *   added conditions for the party choosing to play another game
 *
 *   Revision 1.20  2003/02/02 21:51:54  ???
 *   LaneEvent may very well be observer method.
 *
 *   Revision 1.19  2003/02/02 20:28:59  ???
 *   fixed sleep thread bug in lane
 *
 *   Revision 1.18  2003/02/02 18:18:51  ???
 *   more changes. just need to fix scoring.
 *
 *   Revision 1.17  2003/02/02 17:47:02  ???
 *   Things are pretty close to working now...
 *
 *   Revision 1.16  2003/01/30 22:09:32  ???
 *   Worked on scoring.
 *
 *   Revision 1.15  2003/01/30 21:45:08  ???
 *   Fixed speling of received in Lane.
 *
 *   Revision 1.14  2003/01/30 21:29:30  ???
 *   Fixed some MVC stuff
 *
 *   Revision 1.13  2003/01/30 03:45:26  ???
 *   *** empty log message ***
 *
 *   Revision 1.12  2003/01/26 23:16:10  ???
 *   Improved thread handeling in lane/controldesk
 *
 *   Revision 1.11  2003/01/26 22:34:44  ???
 *   Total rewrite of lane and pinsetter for R2's observer model
 *   Added Lane/Pinsetter Observer
 *   Rewrite of scoring algorythm in lane
 *
 *   Revision 1.10  2003/01/26 20:44:05  ???
 *   small changes
 *
 *
 */

import com.steamy.events.ControlDeskEvent;
import com.steamy.events.LaneEvent;
import com.steamy.events.PinSetterEvent;
import com.steamy.io.ScoreHistoryFile;
import com.steamy.ControlDeskEvent;
import com.steamy.LaneEvent;
import com.steamy.PinSetterEvent;
import com.steamy.io.ScoreReport;
import com.steamy.specialists.LaneSpecialist;
import com.steamy.specialists.Specialist;
import com.steamy.views.EndGamePrompt;
import com.steamy.views.EndGameReport;

import java.util.HashMap;
import java.util.Vector;

public class Lane extends Thread implements Communicator {
    private Party party;
    private PinSetter setter;
    private Game currGame;

    private boolean laneOpen;
    private boolean gameIsHalted;
    private int gameNumber;

    private final Specialist SPECIALIST;

    /**
     * Constructs a new lane and starts its thread
     * @param specialist The Specialist for this Lane
     */
    public Lane(Specialist specialist) {
        this.SPECIALIST = specialist;
        this.setter = ((LaneSpecialist) this.SPECIALIST).getPinSetter();

        laneOpen = true;
        gameIsHalted = false;
        gameNumber = 0;

        this.start();
    }

    public Specialist getSpecialist() { return this.SPECIALIST; }

    /**
     * run()
     * <p>
     * entry point for execution of this lane
     */
    public void run() {
        while (laneOpen) {
            if (party != null && currGame != null && !currGame.isFinished()) {     // Make sure there is party playing games
                while (gameIsHalted) {      // Stall while game is paused
                    try {
                        sleep(10);
                    } catch (Exception ignored) { }
                }
                setter.ballThrown();    // Throw a ball
            }
            try {
                sleep(10);
            } catch (Exception ignored) { }
        }
    }

    /**
     * Creates a new game on this lane with the current {@link Party}
     */
    private void newGame() {
        currGame = new Game(party);
        gameNumber += 1;
        setter.resetPinSetter();
    }

    /**
     * Assigns a {@link Party} to this lane and creates a new {@link Game} for them
     *
     * @param theParty Party to be assigned
     */
    void assignParty(Party theParty) {
        gameIsHalted = true;    // Pause during party assign
        party = theParty;
        gameNumber = 0;
        newGame();
        publish();
        gameIsHalted = false;
    }

    /**
     * lanePublish()
     * <p>
     * Method that creates and returns a newly created laneEvent
     *
     * @return The new lane event
     */
    private LaneEvent createLaneEvent() {
        int partySize = party.getMembers().size();

        // Get score values for all bowlers
        HashMap scores = new HashMap();
        int[] curScores = new int[partySize];
        int[][] cumulativeScores = new int[partySize][10];
        for (int i = 0; i < partySize; i++) {
            Object bowler = party.getMembers().get(i);
            scores.put(bowler, currGame.getScoreCard(bowler));
            curScores[i] = currGame.getTotalScore(bowler);
            cumulativeScores[i] = currGame.getCumulativeCard(bowler);
        }
        return new LaneEvent(party,
                currGame.getBallIndex(),
                currGame.getCurrentBowler(),
                cumulativeScores,
                currGame.getScores(),
                currGame.getFrameNum(),
                curScores,
                currGame.getBallNum(),
                gameIsHalted);
    }

    /**
     * isPartyAssigned()
     * <p>
     * checks if a party is assigned to this lane
     *
     * @return true if party assigned, false otherwise
     */
    public boolean isPartyAssigned() {
        return party != null;
    }

    /**
     * Method that publishes an event to the mediator
     * Doesn't have params because it creates the events within itself
     */
    @Override
    public void publish() {
        LaneEvent le = createLaneEvent();
        SPECIALIST.receiveEvent(le);
    }

    @Override
    public void publish(int num) {}

    @Override
    public void receiveEvent(LaneEvent le) {}

    @Override
    public void receiveEvent(ControlDeskEvent ce) {}

    @Override
    public void receiveEvent(PinSetterEvent pe) {
        if (pe.pinsDownOnThisThrow() >= 0) {            // this is a real throw
            if (!currGame.recordThrow(pe.pinsDownOnThisThrow())) {
                System.out.println("pin reset");
                setter.resetPins();
            }

            // If that was the last throw, prompt the user to go again
            if (currGame.isFinished()) {
                // Prompt user to play again
                EndGamePrompt egp = new EndGamePrompt(((Bowler) party.getMembers().get(0)).getNickName() + "'s Party");
                int result = egp.getResult();
                egp.destroy();

                System.out.println("result was: " + result);

                // Reset game if they choose to play again
                if (result == 1) {                    // yes, want to play again
                    newGame();
                } else if (result == 2) {              // no, dont want to play another game
                    Vector printVector;
                    EndGameReport egr = new EndGameReport(((Bowler) party.getMembers().get(0)).getNickName() + "'s Party", party);
                    printVector = egr.getResult();
                    publish();
                    for (Object o : party.getMembers()) {
                        Bowler thisBowler = (Bowler) o;
                        int[] scoreCard = currGame.getScoreCard(thisBowler);
                        ScoreReport sr = new ScoreReport(thisBowler, scoreCard, gameNumber);
                        sr.sendEmail(thisBowler.getEmail());
                        for (Object obj : printVector) {
                            if (thisBowler.getNick() == obj) {
                                System.out.println("Printing " + thisBowler.getNick());
                                sr.sendPrintout();
                            }
                        }
                    }
                    party = null;
                }
            }
            publish();
            System.out.println("***********************");
        }
    }

    /**
     * Pause the execution of this game
     */
    public void pauseGame() {
        gameIsHalted = true;
        publish();
    }

    /**
     * Resume the execution of this game
     */
    public void unPauseGame() {
        gameIsHalted = false;
        publish();
    }

}
