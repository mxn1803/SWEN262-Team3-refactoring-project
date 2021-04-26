package com.steamy.views;

import com.steamy.LaneEvent;
import com.steamy.views.specialists.Specialist;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class View implements ActionListener {
    private final JFrame WINDOW;
    private Specialist specialist;
    private boolean toggled;

    public View(Specialist s) {
        this.specialist = s;
        this.WINDOW = new JFrame();
        toggled = false;

    }

    public boolean getToggle(){
        return this.WINDOW.isVisible();
    }

    public void actionPerformed(ActionEvent actionEvent){ }
    public abstract void receiveEvent(LaneEvent le);


    public void toggleOn() { this.WINDOW.setVisible(true);}
    public void toggleOff() {this.WINDOW.setVisible(false);}

}
