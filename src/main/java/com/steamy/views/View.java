package com.steamy.views;

import com.steamy.LaneEvent;
import com.steamy.PinsetterEvent;
import com.steamy.views.specialists.Specialist;
import com.steamy.model.Communicator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class View implements ActionListener, Communicator {
    private final JFrame WINDOW;
    private final Specialist SPECIALIST;
    private boolean toggled;

    public View(Specialist s) {
        this.SPECIALIST = s;
        this.WINDOW = new JFrame();
        toggled = false;

    }

    public boolean getToggle(){
        return this.WINDOW.isVisible();
    }

    public void actionPerformed(ActionEvent actionEvent){ }
    public abstract void receiveEvent(LaneEvent le);
    public abstract void receiveEvent(PinsetterEvent pe);


    public void toggleOn() { this.WINDOW.setVisible(true);}
    public void toggleOff() {this.WINDOW.setVisible(false);}
    public Specialist getSpecialist() { return this.SPECIALIST; }

}
