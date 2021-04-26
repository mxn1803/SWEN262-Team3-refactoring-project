package com.steamy.specialists;

import com.steamy.ControlDeskEvent;
import com.steamy.LaneEvent;
import com.steamy.PinSetterEvent;
import com.steamy.views.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Specialist {
    protected enum ViewType {
        PIN_SETTER,
        LANE,
        LANE_STATUS,
        ADD_PARTY,
        CONTROL_DESK,
        NEW_PATRON
    }
    private HashMap<ViewType, View> openViews;

    public Specialist() { this.openViews = new HashMap<>(3); }

    public HashMap<ViewType, View> getOpenViews() { return this.openViews; }

    public abstract void receiveEvent(LaneEvent le);
    public abstract void receiveEvent(PinSetterEvent pe);
    public abstract void receiveEvent(ControlDeskEvent ce);
}
