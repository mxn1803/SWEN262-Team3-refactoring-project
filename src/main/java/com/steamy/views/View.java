package com.steamy.views;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class View implements ActionListener {
    private final JFrame WINDOW;

    public View() { this.WINDOW = new JFrame(); }

    public JFrame getWindow() { return this.WINDOW; }
}
