package com.steamy.views;/* AddPartyView.java
 *
 *  Version:
 *          $Id$
 *
 *  Revisions:
 *         $Log: AddPartyView.java,v $
 *         Revision 1.7  2003/02/20 02:05:53  ???
 *         Fixed addPatron so that duplicates won't be created.
 *
 *         Revision 1.6  2003/02/09 20:52:46  ???
 *         Added comments.
 *
 *         Revision 1.5  2003/02/02 17:42:09  ???
 *         Made updates to migrate to observer model.
 *
 *         Revision 1.4  2003/02/02 16:29:52  ???
 *         Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 *
 *
 */

/**
 * Class for GUI components need to add a party
 */

import com.steamy.model.Bowler;
import com.steamy.io.BowlerFile;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * Constructor for GUI used to Add Parties to the waiting party queue.
 *
 */

public class AddPartyView implements ActionListener, ListSelectionListener {

    private final int MAX_SIZE;

    private final JFrame WINDOW;
    private final JButton ADD_PATRON_BUTTON;
    private final JButton NEW_PATRON_BUTTON;
    private final JButton REMOVE_PATRON_BUTTON;
    private final JButton FINISHED_BUTTON;
    private final JList<String> PARTY_LIST;
    private final JList PREVIOUS_BOWLERS;
    private final Vector<String> PARTY;
    private Vector bowlerDb;

    private final ControlDeskView CONTROL_DESK_VIEW;

    private String selectedNick, selectedMember;

    public AddPartyView(ControlDeskView controlDesk, int max) {

        this.CONTROL_DESK_VIEW = controlDesk;
        MAX_SIZE = max;

        WINDOW = new JFrame("Add Party");
        WINDOW.getContentPane().setLayout(new BorderLayout());
        ((JPanel) WINDOW.getContentPane()).setOpaque(false);

        JPanel colPanel = new JPanel();
        colPanel.setLayout(new GridLayout(1, 3));

        // Party Panel
        JPanel partyPanel = new JPanel();
        partyPanel.setLayout(new FlowLayout());
        partyPanel.setBorder(new TitledBorder("Your Party"));

        PARTY = new Vector<>();
        Vector<String> empty = new Vector<>();
        empty.add("(Empty)");

        PARTY_LIST = new JList<String>(empty);
        PARTY_LIST.setFixedCellWidth(120);
        PARTY_LIST.setVisibleRowCount(5);
        PARTY_LIST.addListSelectionListener(this);
        JScrollPane partyPane = new JScrollPane(PARTY_LIST);
        //        partyPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        partyPanel.add(partyPane);

        // Bowler Database
        JPanel bowlerPanel = new JPanel();
        bowlerPanel.setLayout(new FlowLayout());
        bowlerPanel.setBorder(new TitledBorder("Bowler Database"));

        try {
            bowlerDb = new Vector(BowlerFile.getBowlers());
        } catch (Exception e) {
            System.err.println("File Error");
            bowlerDb = new Vector();
        }
        PREVIOUS_BOWLERS = new JList(bowlerDb);
        PREVIOUS_BOWLERS.setVisibleRowCount(8);
        PREVIOUS_BOWLERS.setFixedCellWidth(120);
        JScrollPane bowlerPane = new JScrollPane(PREVIOUS_BOWLERS);
        bowlerPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        PREVIOUS_BOWLERS.addListSelectionListener(this);
        bowlerPanel.add(bowlerPane);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));

        ADD_PATRON_BUTTON = new JButton("Add to Party");
        JPanel addPatronPanel = new JPanel();
        addPatronPanel.setLayout(new FlowLayout());
        ADD_PATRON_BUTTON.addActionListener(this);
        addPatronPanel.add(ADD_PATRON_BUTTON);

        REMOVE_PATRON_BUTTON = new JButton("Remove Member");
        JPanel remPatronPanel = new JPanel();
        remPatronPanel.setLayout(new FlowLayout());
        REMOVE_PATRON_BUTTON.addActionListener(this);
        remPatronPanel.add(REMOVE_PATRON_BUTTON);

        NEW_PATRON_BUTTON = new JButton("New Patron");
        JPanel newPatronPanel = new JPanel();
        newPatronPanel.setLayout(new FlowLayout());
        NEW_PATRON_BUTTON.addActionListener(this);
        newPatronPanel.add(NEW_PATRON_BUTTON);

        FINISHED_BUTTON = new JButton("Finished");
        JPanel finishedPanel = new JPanel();
        finishedPanel.setLayout(new FlowLayout());
        FINISHED_BUTTON.addActionListener(this);
        finishedPanel.add(FINISHED_BUTTON);

        buttonPanel.add(addPatronPanel);
        buttonPanel.add(remPatronPanel);
        buttonPanel.add(newPatronPanel);
        buttonPanel.add(finishedPanel);

        // Clean up main panel
        colPanel.add(partyPanel);
        colPanel.add(bowlerPanel);
        colPanel.add(buttonPanel);

        WINDOW.getContentPane().add("Center", colPanel);
        WINDOW.pack();

        // Center Window on Screen
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        WINDOW.setLocation(((screenSize.width) / 2) - ((WINDOW.getSize().width) / 2), ((screenSize.height) / 2) - ((WINDOW.getSize().height) / 2));
        WINDOW.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(ADD_PATRON_BUTTON)) {
            if (selectedNick != null && PARTY.size() < MAX_SIZE) {
                if (PARTY.contains(selectedNick)) {
                    System.err.println("Member already in Party");
                } else {
                    PARTY.add(selectedNick);
                    PARTY_LIST.setListData(PARTY);
                }
            }
        }
        if (e.getSource().equals(REMOVE_PATRON_BUTTON)) {
            if (selectedMember != null) {
                PARTY.removeElement(selectedMember);
                PARTY_LIST.setListData(PARTY);
            }
        }
        if (e.getSource().equals(NEW_PATRON_BUTTON)) {
            NewPatronView newPatron = new NewPatronView(this);
        }
        if (e.getSource().equals(FINISHED_BUTTON)) {
            if (PARTY != null && PARTY.size() > 0) {
                CONTROL_DESK_VIEW.updateAddParty(this);
            }
            WINDOW.setVisible(false);
        }

    }

    /**
     * Handler for List actions
     * @param e the ListActionEvent that triggered the handler
     */

    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource().equals(PREVIOUS_BOWLERS)) {
            selectedNick = ((String) ((JList) e.getSource()).getSelectedValue());
        }
        if (e.getSource().equals(PARTY_LIST)) {
            selectedMember = ((String) ((JList) e.getSource()).getSelectedValue());
        }
    }

    /**
     * Called by NewPatronView to notify AddPartyView to update
     *
     * @param newPatron the NewPatronView that called this method
     */
    public void updateNewPatron(NewPatronView newPatron) {
        try {
            Bowler checkBowler = BowlerFile.getBowler(newPatron.getNick());
            if (checkBowler == null) {
                BowlerFile.putBowlerInfo(newPatron.getNick(), newPatron.getFull(), newPatron.getEmail());
                bowlerDb = new Vector(BowlerFile.getBowlers());
                PREVIOUS_BOWLERS.setListData(bowlerDb);
                PARTY.add(newPatron.getNick());
                PARTY_LIST.setListData(PARTY);
            } else {
                System.err.println("A Bowler with that name already exists.");
            }
        } catch (Exception e2) {
            System.err.println("File I/O Error");
        }
    }

    /**
     * Accessor for Party
     */
    public Vector<String> getParty() {
        return PARTY;
    }

}
