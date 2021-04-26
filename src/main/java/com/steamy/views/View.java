package com.steamy.views;

import com.steamy.ControlDeskEvent;
import com.steamy.LaneEvent;
import com.steamy.PinSetterEvent;
import com.steamy.model.Communicator;
import com.steamy.specialists.Specialist;

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
    public abstract void receiveEvent(PinSetterEvent pe);
    public abstract void receiveEvent(ControlDeskEvent ce);


    public void toggleOn() { this.WINDOW.setVisible(true);}
    public void toggleOff() {this.WINDOW.setVisible(false);}
    public void toggle() {this.WINDOW.setVisible(!getToggle());}
    public Specialist getSpecialist() { return this.SPECIALIST; }

}
