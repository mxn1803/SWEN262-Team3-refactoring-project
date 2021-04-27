package com.steamy.views;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class View implements ActionListener {
    private final JFrame tempWindow;

    public View() { this.tempWindow = new JFrame(); }

    public JFrame getWindow() { return this.tempWindow; }
}
