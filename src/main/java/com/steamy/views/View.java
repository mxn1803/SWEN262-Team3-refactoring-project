package com.steamy.views;

import com.steamy.views.specialists.Specialist;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class View implements ActionListener {
    private final JFrame WINDOW;
    private Specialist specialist;

    public View(Specialist s) {
        this.specialist = s;
        this.WINDOW = new JFrame();
    }

    public abstract void actionPerformed(ActionEvent actionEvent);

    public void toggle() { this.WINDOW.setVisible(!this.WINDOW.isVisible()); }
}
