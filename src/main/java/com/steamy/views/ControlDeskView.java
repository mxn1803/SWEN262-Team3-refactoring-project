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

import com.steamy.events.ControlDeskEvent;
import com.steamy.events.LaneEvent;
import com.steamy.events.PinSetterEvent;
import com.steamy.specialists.ControlSpecialist;
import com.steamy.specialists.Specialist;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

public class ControlDeskView extends ListeningView {

    private final JButton ADD_PARTY_BUTTON;
    private final JButton FINISHED_BUTTON;
    private final JList PARTY_LIST;

    public ControlDeskView(Specialist specialist) {
        super(specialist);
        JFrame tempWindow = super.getWindow();

        final int NUM_LANES = ((ControlSpecialist) super.getSpecialist()).getNumLanes();

        tempWindow.setTitle("Control Desk");
        tempWindow.getContentPane().setLayout(new BorderLayout());
        ((JPanel) tempWindow.getContentPane()).setOpaque(false);

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
        laneStatusPanel.setLayout(new GridLayout(NUM_LANES, 1));
        laneStatusPanel.setBorder(new TitledBorder("Lane Status"));

        for (int i = 1; i <= NUM_LANES; i++) {
            LaneStatusView tempLaneStatusView = ((ControlSpecialist) this.getSpecialist()).getLaneStatusView(i - 1);
            JPanel lanePanel = tempLaneStatusView.getLanePanel();
            lanePanel.setBorder(new TitledBorder("Lane" + i));
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

        tempWindow.getContentPane().add("Center", colPanel);
        tempWindow.pack();

        /* Close program when this window closes */
        tempWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Center Window on Screen
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        tempWindow.setLocation(((screenSize.width) / 2) - ((tempWindow.getSize().width) / 2), ((screenSize.height) / 2) - ((tempWindow.getSize().height) / 2));
        tempWindow.setVisible(true);
    }

    /**
     * Handler for actionEvents
     *
     * @param e the ActionEvent that triggered the handler
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(ADD_PARTY_BUTTON)) {
            AddPartyView addPartyWin = new AddPartyView(this, ((ControlSpecialist) super.getSpecialist()).getMaxMembers());
        }
        if (e.getSource().equals(FINISHED_BUTTON)) {
            super.getWindow().setVisible(false);
            System.exit(0);
        }
    }

    @Override
    public void receiveEvent(LaneEvent le) {}

    @Override
    public void publish() {}

    @Override
    public void publish(int num) {}

    @Override
    public void receiveEvent(PinSetterEvent pe) {}

    /**
     * Receive a broadcast from a ControlDesk
     *
     * @param ce the ControlDeskEvent that triggered the handler
     */
    @Override
    public void receiveEvent(ControlDeskEvent ce) { PARTY_LIST.setListData(ce.getPartyQueue()); }

    /**
     * Receive a new party from andPartyView.
     *
     * @param addPartyView the AddPartyView that is providing a new party
     */
    public void updateAddParty(AddPartyView addPartyView) { ((ControlSpecialist) super.getSpecialist()).updateParty(addPartyView); }


}
