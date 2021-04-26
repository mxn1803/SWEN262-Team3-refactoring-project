package com.steamy.views.specialists;

import com.steamy.LaneEvent;
import com.steamy.views.View;

import java.util.ArrayList;
import java.util.List;

public abstract class Specialist {
    private List<View> openViews;

    public Specialist() { this.openViews = new ArrayList<>(3); }

    public List<View> getOpenViews() { return this.openViews; }

    public abstract void receiveEvent(LaneEvent le);
}
