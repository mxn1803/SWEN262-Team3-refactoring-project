package com.steamy.views;

import com.steamy.events.ControlDeskEvent;
import com.steamy.events.LaneEvent;
import com.steamy.events.PinSetterEvent;
import com.steamy.model.Communicator;
import com.steamy.specialists.Specialist;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class ListeningView implements ActionListener, Communicator {
    private final JFrame WINDOW;
    private final Specialist SPECIALIST;

    public ListeningView(Specialist s) {
        this.SPECIALIST = s;
        this.WINDOW = new JFrame();
    }

    public boolean getToggle(){
        return this.WINDOW.isVisible();
    }

    public abstract void actionPerformed(ActionEvent e);
    public abstract void receiveEvent(LaneEvent le);
    public abstract void receiveEvent(PinSetterEvent pe);
    public abstract void receiveEvent(ControlDeskEvent ce);

    public void toggle() { this.WINDOW.setVisible(!getToggle()); }
    public JFrame getWindow() { return this.WINDOW; }
    public Specialist getSpecialist() { return this.SPECIALIST; }
}
