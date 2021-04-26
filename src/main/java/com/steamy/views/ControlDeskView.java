package com.steamy.views;/* ControlDeskView.java
 *
 *  Version:
 *            $Id$
 *
 *  Revisions:
 *         $Log$
 *
 */

/**
 * Class for representation of the control desk
 */

import com.steamy.LaneEvent;
import com.steamy.PinSetterEvent;
import com.steamy.model.ControlDesk;
import com.steamy.ControlDeskEvent;
import com.steamy.model.Lane;
import com.steamy.views.specialists.LaneSpecialist;
import com.steamy.views.specialists.Specialist;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class ControlDeskView extends View implements ActionListener {

    private final JButton ADD_PARTY_BUTTON;
    private final JButton FINISHED_BUTTON;
    private final JFrame WINDOW;
    private final JList PARTY_LIST;

    /** The maximum  number of members in a party */
    private final int MAX_MEMBERS;

    private final ControlDesk CONTROL_DESK;

    /**
     * Displays a GUI representation of the ControlDesk
     *
     */
    public ControlDeskView(ControlDesk controlDesk, int maxMembers, Specialist specialist) {
        super(specialist);

        this.CONTROL_DESK = controlDesk;
        this.MAX_MEMBERS = maxMembers;
        int numLanes = controlDesk.getNumLanes();

        WINDOW = new JFrame("Control Desk");
        WINDOW.getContentPane().setLayout(new BorderLayout());
        ((JPanel) WINDOW.getContentPane()).setOpaque(false);

        JPanel colPanel = new JPanel();
        colPanel.setLayout(new BorderLayout());

        // Controls Panel
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridLayout(3, 1));
        controlsPanel.setBorder(new TitledBorder("Controls"));

        ADD_PARTY_BUTTON = new JButton("Add Party");
        JPanel addPartyPanel = new JPanel();
        addPartyPanel.setLayout(new FlowLayout());
        ADD_PARTY_BUTTON.addActionListener(this);
        addPartyPanel.add(ADD_PARTY_BUTTON);
        controlsPanel.add(addPartyPanel);

        FINISHED_BUTTON = new JButton("Finished");
        JPanel finishedPanel = new JPanel();
        finishedPanel.setLayout(new FlowLayout());
        FINISHED_BUTTON.addActionListener(this);
        finishedPanel.add(FINISHED_BUTTON);
        controlsPanel.add(finishedPanel);

        // Lane Status Panel
        JPanel laneStatusPanel = new JPanel();
        laneStatusPanel.setLayout(new GridLayout(numLanes, 1));
        laneStatusPanel.setBorder(new TitledBorder("Lane Status"));

        List<Lane> lanes = controlDesk.getLanes();
        Iterator it = lanes.iterator();
        int laneCount = 0;
        while (it.hasNext()) {
            Lane curLane = (Lane) it.next();
            LaneSpecialist laneSpecialist = (LaneSpecialist) curLane.getSpecialist();
            LaneStatusView laneStat = laneSpecialist.getLaneStatusView();
            //LaneStatusView laneStat = new LaneStatusView(curLane, (laneCount + 1));



            JPanel lanePanel = laneStat.getLanePanel();
            lanePanel.setBorder(new TitledBorder("Lane" + ++laneCount));
            laneStatusPanel.add(lanePanel);
        }

        // Party Queue Panel
        JPanel partyPanel = new JPanel();
        partyPanel.setLayout(new FlowLayout());
        partyPanel.setBorder(new TitledBorder("Party Queue"));

        Vector empty = new Vector();
        empty.add("(Empty)");

        PARTY_LIST = new JList(empty);
        PARTY_LIST.setFixedCellWidth(120);
        PARTY_LIST.setVisibleRowCount(10);
        JScrollPane partyPane = new JScrollPane(PARTY_LIST);
        partyPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        partyPanel.add(partyPane);

        // Clean up main panel
        colPanel.add(controlsPanel, "East");
        colPanel.add(laneStatusPanel, "Center");
        colPanel.add(partyPanel, "West");

        WINDOW.getContentPane().add("Center", colPanel);

        WINDOW.pack();

        /* Close program when this window closes */
        WINDOW.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Center Window on Screen
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        WINDOW.setLocation(((screenSize.width) / 2) - ((WINDOW.getSize().width) / 2), ((screenSize.height) / 2) - ((WINDOW.getSize().height) / 2));
        WINDOW.setVisible(true);

    }

    /**
     * Handler for actionEvents
     *
     * @param e    the ActionEvent that triggered the handler
     *
     */

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(ADD_PARTY_BUTTON)) {
            AddPartyView addPartyWin = new AddPartyView(this, MAX_MEMBERS);
        }
        if (e.getSource().equals(FINISHED_BUTTON)) {
            WINDOW.setVisible(false);
            System.exit(0);
        }
    }

    @Override
    public void receiveEvent(LaneEvent le) {

    }

    @Override
    public void publish() {

    }

    @Override
    public void publish(int num) {

    }

    @Override
    public void receiveEvent(PinSetterEvent pe) {

    }

    /**
     * Receive a broadcast from a ControlDesk
     *
     * @param ce    the ControlDeskEvent that triggered the handler
     *
     */
    @Override
    public void receiveEvent(ControlDeskEvent ce) {
        PARTY_LIST.setListData(ce.getPartyQueue());
    }

    /**
     * Receive a new party from andPartyView.
     *
     * @param addPartyView    the AddPartyView that is providing a new party
     *
     */
    public void updateAddParty(AddPartyView addPartyView) { CONTROL_DESK.addPartyQueue(addPartyView.getParty()); }



}
