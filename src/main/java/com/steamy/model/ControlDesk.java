package com.steamy.model;/* ControlDesk.java
 *
 *  Version:
 *          $Id$
 * 
 *  Revisions:
 *         $Log: ControlDesk.java,v $
 *         Revision 1.13  2003/02/02 23:26:32  ???
 *         ControlDesk now runs its own thread and polls for free lanes to assign queue members to
 *         
 *         Revision 1.12  2003/02/02 20:46:13  ???
 *         Added " 's Party" to party names.
 *         
 *         Revision 1.11  2003/02/02 20:43:25  ???
 *         misc cleanup
 *         
 *         Revision 1.10  2003/02/02 17:49:10  ???
 *         Fixed problem in getPartyQueue that was returning the first element as every element.
 *         
 *         Revision 1.9  2003/02/02 17:39:48  ???
 *         Added accessor for lanes.
 *         
 *         Revision 1.8  2003/02/02 16:53:59  ???
 *         Updated comments to match javadoc format.
 *         
 *         Revision 1.7  2003/02/02 16:29:52  ???
 *         Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 *         
 *         Revision 1.6  2003/02/02 06:09:39  ???
 *         Updated many classes to support the ControlDeskView.
 *         
 *         Revision 1.5  2003/01/26 23:16:10  ???
 *         Improved thread handeling in lane/controldesk
 *         
 * 
 */

/**
 * Class that represents control desk
 *
 */

import com.steamy.events.LaneEvent;
import com.steamy.events.PinSetterEvent;
import com.steamy.io.BowlerFile;
import com.steamy.events.ControlDeskEvent;
import com.steamy.Queue;
import com.steamy.specialists.ControlSpecialist;
import com.steamy.specialists.LaneSpecialist;
import com.steamy.specialists.Specialist;

import java.util.*;
import java.io.*;

public class ControlDesk extends Thread implements Communicator {

    /** The collection of Lanes */
    private List<Lane> lanes;

    /** The party wait queue */
    private Queue partyQueue;
    private final Specialist SPECIALIST;

    /**
     * Constructor for the ControlDesk class
     *
     * @param numLanes   the numbler of lanes to be represented
     *
     */
    public ControlDesk(int numLanes, Specialist specialist) {
        this.SPECIALIST = specialist;
        lanes = new ArrayList<>(numLanes);
        partyQueue = new Queue();



        for (int i = 1; i <= numLanes; i++) {
            lanes.add(((ControlSpecialist) this.SPECIALIST).getLane(i - 1));
        }
        
        this.start();

    }

    public Specialist getSpecialist() { return this.SPECIALIST; }

    /**
     * com.steamy.Main loop for ControlDesk's thread
     * 
     */
    public void run() {
        while (true) {
            assignLane();
            
            try {
                sleep(250);
            } catch (Exception e) {}
        }
    }
        

    /**
     * Retrieves a matching Bowler from the bowler database.
     *
     * @param nickName    The NickName of the Bowler
     *
     * @return a Bowler object.
     *
     */

    private Bowler registerPatron(String nickName) {
        Bowler patron = null;

        try {
            // only one patron / nick.... no dupes, no checks

            patron = BowlerFile.getBowler(nickName);

        } catch (FileNotFoundException e) {
            System.err.println("Error..." + e);
        } catch (IOException e) {
            System.err.println("Error..." + e);
        }

        return patron;
    }

    /**
     * Iterate through the available lanes and assign the paties in the wait queue if lanes are available.
     *
     */

    private void assignLane() {
        Iterator it = lanes.iterator();

        while (it.hasNext() && partyQueue.hasMoreElements()) {
            Lane curLane = (Lane) it.next();

            if (!curLane.isPartyAssigned()) {
                System.out.println("ok... assigning this party");
                curLane.assignParty(((Party) partyQueue.next()));
            }
        }
        publish();
    }

    /**
     * Creates a party from a Vector of nickNAmes and adds them to the wait queue.
     *
     * @param partyNicks    A Vector of NickNames
     *
     */

    public void addPartyQueue(Vector partyNicks) {
        Vector partyBowlers = new Vector();
        for (int i = 0; i < partyNicks.size(); i++) {
            Bowler newBowler = registerPatron(((String) partyNicks.get(i)));
            partyBowlers.add(newBowler);
        }
        Party newParty = new Party(partyBowlers);
        partyQueue.add(newParty);
        publish();
    }

    /**
     * Returns a Vector of party names to be displayed in the GUI representation of the wait queue.
     *
     * @return a Vecotr of Strings
     *
     */

    public Vector getPartyQueue() {
        Vector displayPartyQueue = new Vector();
        for ( int i=0; i < ( (Vector)partyQueue.asVector()).size(); i++ ) {
            String nextParty =
                ((Bowler) ((Vector) ((Party) partyQueue.asVector().get( i ) ).getMembers())
                    .get(0))
                    .getNickName() + "'s Party";
            displayPartyQueue.addElement(nextParty);
        }
        return displayPartyQueue;
    }

    /**
     * Allows objects to subscribe as observers
     * 
     * @param adding    the ControlDeskObserver that will be subscribed
     *
     */

    /**
     * Accessor method for lanes
     * 
     * @return a HashSet of Lanes
     *
     */
    public List<Lane> getLanes() { return lanes; }

    @Override
    public void publish() {
        ControlDeskEvent cde = new ControlDeskEvent(getPartyQueue());
        SPECIALIST.receiveEvent(cde);
    }

    @Override
    public void publish(int num) {}

    @Override
    public void receiveEvent(PinSetterEvent pe) {}

    @Override
    public void receiveEvent(LaneEvent le) {}

    @Override
    public void receiveEvent(ControlDeskEvent ce) {}
}
