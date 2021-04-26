package com.steamy.views;/*
 * PinSetterView/.java
 *
 * Version:
 *   $Id$
 *
 * Revision:
 *   $Log$
 */

/**
 * constructs a prototype PinSetter GUI
 */

import com.steamy.LaneEvent;
import com.steamy.PinsetterEvent;
import com.steamy.PinsetterObserver;
import com.steamy.views.specialists.Specialist;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;


public class PinSetterView extends View{


    private final Vector<JLabel> PIN_LABELS;
    private final Vector<JPanel> PIN_PANELS;
    private final JPanel FIRST_ROLL, SECOND_ROLL;
    private final JFrame WINDOW;

    /**
     * Constructs a Pin Setter GUI displaying which roll it is with
     * yellow boxes along the top (1 box for first roll, 2 boxes for second)
     * and displays the pins as numbers in this format:
     *
     *                7   8   9   10
     *                  4   5   6
     *                    2   3
     *                      1
     *
     */
    public PinSetterView(int laneNum, Specialist specialist) {
        super(specialist);

        PIN_LABELS = new Vector<>(10);
        PIN_PANELS = new Vector<>(10);
        WINDOW = new JFrame("Lane " + laneNum + ":");

        FIRST_ROLL = new JPanel();
        FIRST_ROLL.setBackground(Color.yellow);

        SECOND_ROLL = new JPanel();
        SECOND_ROLL.setBackground(Color.black);

        JPanel top = new JPanel();
        top.add(FIRST_ROLL, BorderLayout.WEST);
        top.add(SECOND_ROLL, BorderLayout.EAST);
        top.setBackground(Color.BLACK);

        for (int i = 0; i < PIN_PANELS.capacity(); i++) {
            PIN_PANELS.add(new JPanel());
            PIN_LABELS.add(new JLabel(Integer.toString(i + 1)));
            PIN_PANELS.get(i).add(PIN_LABELS.get(i));
        }

        JPanel pins = new JPanel();
        pins.setLayout(new GridLayout(4, 7));
        final int[] pinIndex = { 6, 7, 8, 9, 3, 4, 5, 1, 2, 0 };
        int indexTracker = 0;
        for (int i = 0; i < 28; i++) {
            boolean isEven = i % 2 == 0;
            boolean isSkipped = i == 14 || i == 20 || i == 22 || i == 26;
            pins.add(isEven && !isSkipped ? PIN_PANELS.get(pinIndex[indexTracker]) : new JPanel());
            indexTracker += isEven && !isSkipped ? 1 : 0;
        }
        pins.setBackground(Color.BLACK);
        pins.setForeground(Color.YELLOW);


        Container cpanel = WINDOW.getContentPane();
        cpanel.add(top, BorderLayout.NORTH);
        cpanel.add(pins, BorderLayout.CENTER);

        WINDOW.pack();
        WINDOW.setVisible(true);
    }





    public void toggle() { WINDOW.setVisible(!WINDOW.isVisible()); }

    @Override
    public void receiveEvent(LaneEvent le) {

    }

    /**
     * This method receives a pinsetter event.  The event is the current
     * state of the PinSetter and the method changes how the GUI looks
     * accordingly.  When pins are "knocked down" the corresponding label
     * is grayed out.  When it is the second roll, it is indicated by the
     * appearance of a second yellow box at the top.
     *
     * @param pe    The state of the pinsetter is sent in this event.
     */
    @Override
    public void receiveEvent(PinsetterEvent pe) {
        if (!pe.isFoulCommited()) {
            for (int c = 0; c < 10; c++) {
                if (pe.pinKnockedDown(c))
                    PIN_LABELS.get(c).setForeground(Color.lightGray);
            }
        }
        if (pe.getThrowNumber() == 1) SECOND_ROLL.setBackground(Color.yellow);

        if (pe.pinsDownOnThisThrow() == -1) {
            for (int i = 0; i != 10; i++) {
                PIN_LABELS.get(i).setForeground(Color.black);
            }
            SECOND_ROLL.setBackground(Color.black);
        }
    }
}
