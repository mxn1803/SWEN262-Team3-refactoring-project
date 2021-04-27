package com.steamy.views;

import com.steamy.model.Bowler;
import com.steamy.model.Party;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.Vector;

public class EndGameReport extends View implements ListSelectionListener {
    private final JButton PRINT_BUTTON, FINISHED_BUTTON;
    private final Vector<String> RETURN_VALUE;

    private int result;

    private String selectedMember;

    public EndGameReport(String partyName, Party party) {
        super();
        JFrame tempWindow = super.getWindow();

        result = 0;
        RETURN_VALUE = new Vector<>();
        tempWindow.setTitle("End Game Report for " + partyName + "?");
        tempWindow.getContentPane().setLayout(new BorderLayout());
        ((JPanel) tempWindow.getContentPane()).setOpaque(false);

        JPanel colPanel = new JPanel();
        colPanel.setLayout(new GridLayout(1, 2));

        // Member Panel
        JPanel partyPanel = new JPanel();
        partyPanel.setLayout(new FlowLayout());
        partyPanel.setBorder(new TitledBorder("Party Members"));

        Vector<String> myVector = new Vector<>();
        Iterator iter = (party.getMembers()).iterator();

        while (iter.hasNext()) myVector.add(((Bowler) iter.next()).getNick());

        JList<String> MEMBER_LIST = new JList<>(myVector);
        MEMBER_LIST.setFixedCellWidth(120);
        MEMBER_LIST.setVisibleRowCount(5);
        MEMBER_LIST.addListSelectionListener(this);
        JScrollPane partyPane = new JScrollPane(MEMBER_LIST);
        partyPanel.add(partyPane);

        partyPanel.add(MEMBER_LIST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1));

        PRINT_BUTTON = new JButton("Print Report");
        JPanel printButtonPanel = new JPanel();
        printButtonPanel.setLayout(new FlowLayout());
        PRINT_BUTTON.addActionListener(this);
        printButtonPanel.add(PRINT_BUTTON);

        FINISHED_BUTTON = new JButton("Finished");
        JPanel finishedPanel = new JPanel();
        finishedPanel.setLayout(new FlowLayout());
        FINISHED_BUTTON.addActionListener(this);
        finishedPanel.add(FINISHED_BUTTON);

        buttonPanel.add(PRINT_BUTTON);
        buttonPanel.add(FINISHED_BUTTON);

        // Clean up main panel
        colPanel.add(partyPanel);
        colPanel.add(buttonPanel);

        tempWindow.getContentPane().add("Center", colPanel);
        tempWindow.pack();

        // Center Window on Screen
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        tempWindow.setLocation(((screenSize.width) / 2) - ((tempWindow.getSize().width) / 2), ((screenSize.height) / 2) - ((tempWindow.getSize().height) / 2));
        tempWindow.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(PRINT_BUTTON)) {
            //Add selected to the vector.
            RETURN_VALUE.add(selectedMember);
        }
        if (e.getSource().equals(FINISHED_BUTTON)) {
            result = 1;
            super.getWindow().setVisible(false);
        }

    }

    public void valueChanged(ListSelectionEvent e) {
        selectedMember = ((String) ((JList) e.getSource()).getSelectedValue());
    }

    public Vector<String> getResult() {
        while (result == 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.err.println("Interrupted");
            }
        }
        return RETURN_VALUE;
    }
}

