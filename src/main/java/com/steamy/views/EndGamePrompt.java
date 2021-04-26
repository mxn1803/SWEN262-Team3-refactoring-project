package com.steamy.views; /**
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndGamePrompt implements ActionListener {

    private final JFrame WINDOW;
    private final JButton YES_BUTTON, NO_BUTTON;

    private int result;

    public EndGamePrompt(String partyName) {

        result = 0;

        WINDOW = new JFrame("Another Game for " + partyName + "?");
        WINDOW.getContentPane().setLayout(new BorderLayout());
        ((JPanel) WINDOW.getContentPane()).setOpaque(false);

        JPanel colPanel = new JPanel();
        colPanel.setLayout(new GridLayout(2, 1));

        // Label Panel
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout());

        JLabel message = new JLabel("Party " + partyName + " has finished bowling.\nWould they like to bowl another game?");

        labelPanel.add(message);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));

        YES_BUTTON = new JButton("Yes");
        JPanel yesButtonPanel = new JPanel();
        yesButtonPanel.setLayout(new FlowLayout());
        YES_BUTTON.addActionListener(this);
        yesButtonPanel.add(YES_BUTTON);

        NO_BUTTON = new JButton("No");
        JPanel noButtonPanel = new JPanel();
        noButtonPanel.setLayout(new FlowLayout());
        NO_BUTTON.addActionListener(this);
        noButtonPanel.add(NO_BUTTON);

        buttonPanel.add(YES_BUTTON);
        buttonPanel.add(NO_BUTTON);

        // Clean up main panel
        colPanel.add(labelPanel);
        colPanel.add(buttonPanel);

        WINDOW.getContentPane().add("Center", colPanel);
        WINDOW.pack();

        // Center Window on Screen
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        WINDOW.setLocation(((screenSize.width) / 2) - ((WINDOW.getSize().width) / 2), ((screenSize.height) / 2) - ((WINDOW.getSize().height) / 2));
        WINDOW.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(YES_BUTTON)) result = 1;
        if (e.getSource().equals(NO_BUTTON)) result = 2;
    }

    public int getResult() {
        while (result == 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.err.println("Interrupted");
            }
        }
        return result;
    }

    public void destroy() {
        WINDOW.setVisible(false);
    }

}

