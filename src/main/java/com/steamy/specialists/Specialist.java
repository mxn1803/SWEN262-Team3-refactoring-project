package com.steamy.specialists;

import com.steamy.ControlDeskEvent;
import com.steamy.LaneEvent;
import com.steamy.PinSetterEvent;
import com.steamy.views.ListeningView;

import java.util.HashMap;

public abstract class Specialist {
    public enum ViewType {
        PIN_SETTER,
        LANE,
        LANE_STATUS,
        ADD_PARTY,
        CONTROL_DESK,
        NEW_PATRON
    }
    private HashMap<ViewType, ListeningView> openViews;

    public Specialist() { this.openViews = new HashMap<>(3); }

    public HashMap<ViewType, ListeningView> getOpenViews() { return this.openViews; }
    public void toggleView(ViewType type) { this.openViews.get(type).toggle(); }

    public abstract void receiveEvent(LaneEvent le);
    public abstract void receiveEvent(PinSetterEvent pe);
    public abstract void receiveEvent(ControlDeskEvent ce);
}
