package com.steamy.specialists;

import com.steamy.ControlDeskEvent;
import com.steamy.LaneEvent;
import com.steamy.PinSetterEvent;
import com.steamy.views.ListeningView;

import javax.swing.*;
import java.util.HashMap;

public abstract class Specialist {
    public enum ViewType {
        PIN_SETTER,
        LANE,
        LANE_STATUS,
        CONTROL_DESK
    }
    private HashMap<ViewType, ListeningView> openViews;

    public Specialist() { this.openViews = new HashMap<>(3); }

    public HashMap<ViewType, ListeningView> getOpenViews() { return this.openViews; }
    public void toggleView(ViewType type) {
        JFrame tempWindow = this.openViews.get(type).getWindow();
        tempWindow.setVisible(!tempWindow.isVisible());
    }

    public abstract void receiveEvent(LaneEvent le);
    public abstract void receiveEvent(PinSetterEvent pe);
    public abstract void receiveEvent(ControlDeskEvent ce);
}
