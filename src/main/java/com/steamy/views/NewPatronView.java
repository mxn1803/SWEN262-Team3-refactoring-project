package com.steamy.views;/* AddPartyView.java
 *
 *  Version
 *  $Id$
 *
 *  Revisions:
 *         $Log: NewPatronView.java,v $
 *         Revision 1.3  2003/02/02 16:29:52  ???
 *         Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 *
 *
 */

/**
 * Class for GUI components need to add a patron
 */

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewPatronView implements ActionListener {

    private final JFrame WINDOW;
    private final JButton ABORT_BUTTON, FINISHED_BUTTON;
    private final JTextField NICKNAME_TEXT_FIELD, FULL_NAME_TEXT_FIELD, EMAIL_TEXT_FIELD;
    private String nick, full, email;

    private final AddPartyView ADD_PARTY_VIEW;

    public NewPatronView(AddPartyView v) {

        ADD_PARTY_VIEW = v;

        WINDOW = new JFrame("Add Patron");
        WINDOW.getContentPane().setLayout(new BorderLayout());
        ((JPanel) WINDOW.getContentPane()).setOpaque(false);

        JPanel colPanel = new JPanel();
        colPanel.setLayout(new BorderLayout());

        // Patron Panel
        JPanel patronPanel = new JPanel();
        patronPanel.setLayout(new GridLayout(3, 1));
        patronPanel.setBorder(new TitledBorder("Your Info"));

        JPanel nickPanel = new JPanel();
        NICKNAME_TEXT_FIELD = new JTextField("", 15);
        nickPanel.add(new JLabel("Nick Name"));
        nickPanel.add(NICKNAME_TEXT_FIELD);

        JPanel fullPanel = new JPanel();
        FULL_NAME_TEXT_FIELD = new JTextField("", 15);
        fullPanel.add(new JLabel("Full Name"));
        fullPanel.add(FULL_NAME_TEXT_FIELD);

        JPanel emailPanel = new JPanel();
        EMAIL_TEXT_FIELD = new JTextField("", 15);
        emailPanel.add(new JLabel("E-Mail"));
        emailPanel.add(EMAIL_TEXT_FIELD);

        patronPanel.add(nickPanel);
        patronPanel.add(fullPanel);
        patronPanel.add(emailPanel);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));

        FINISHED_BUTTON = new JButton("Add Patron");
        JPanel finishedPanel = new JPanel();
        finishedPanel.setLayout(new FlowLayout());
        FINISHED_BUTTON.addActionListener(this);
        finishedPanel.add(FINISHED_BUTTON);

        ABORT_BUTTON = new JButton("Abort");
        JPanel abortPanel = new JPanel();
        abortPanel.setLayout(new FlowLayout());
        ABORT_BUTTON.addActionListener(this);
        abortPanel.add(ABORT_BUTTON);

        buttonPanel.add(abortPanel);
        buttonPanel.add(finishedPanel);

        // Clean up main panel
        colPanel.add(patronPanel, "Center");
        colPanel.add(buttonPanel, "East");

        WINDOW.getContentPane().add("Center", colPanel);

        WINDOW.pack();

        // Center Window on Screen
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        WINDOW.setLocation(((screenSize.width) / 2) - ((WINDOW.getSize().width) / 2), ((screenSize.height) / 2) - ((WINDOW.getSize().height) / 2));
        WINDOW.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(FINISHED_BUTTON)) {
            nick = NICKNAME_TEXT_FIELD.getText();
            full = FULL_NAME_TEXT_FIELD.getText();
            email = EMAIL_TEXT_FIELD.getText();
            ADD_PARTY_VIEW.updateNewPatron(this);
        }
        WINDOW.setVisible(false);
    }

    public String getNick() {
        return nick;
    }

    public String getFull() {
        return full;
    }

    public String getEmail() {
        return email;
    }
}
