package com.steamy.views.specialists;

import com.steamy.LaneEvent;
import com.steamy.PinsetterEvent;
import com.steamy.model.Lane;
import com.steamy.model.PinSetter;
import com.steamy.views.LaneStatusView;
import com.steamy.views.LaneView;
import com.steamy.views.PinSetterView;
import com.steamy.views.View;

public class LaneSpecialist extends Specialist {
    private final Lane LANE;
    private final PinSetter PINSETTER;
    private final View LANE_VIEW;
    private final View PINSETTER_VIEW;
    private final View LANE_STATUS_VIEW;

    public LaneSpecialist(Lane lane) {
        super();
        this.LANE = lane;
        this.PINSETTER = new PinSetter();
        this.LANE_VIEW = new View(this);
        this.PINSETTER_VIEW = new View(this);
        this.LANE_STATUS_VIEW = new View(this);
    }

    public LaneSpecialist(Lane lane, int laneCount){
        super();
        this.LANE = lane;
        this.LANE_STATUS_VIEW = new LaneStatusView(lane, laneCount, this);
        this.PINSETTER_VIEW =  ((LaneStatusView) LANE_STATUS_VIEW).getPinsetterView();
        this.PINSETTER = LANE.getPinsetter();
        this.LANE_VIEW = ((LaneStatusView) LANE_STATUS_VIEW).getLaneView();
    }

    public void openLaneView() {
        LANE_VIEW.toggleOn();
    }

    public void closeLaneView() {
        LANE_VIEW.toggleOff();
    }

    public void openLaneStatusView() {
        LANE_STATUS_VIEW.toggleOn();
    }

    public void closeLaneStatusView() {
        LANE_STATUS_VIEW.toggleOff();
    }

    public void openPinSetterView() {
        PINSETTER_VIEW.toggleOn();
    }

    public void closePinSetterView() {
        PINSETTER_VIEW.toggleOff();
    }







    public void receiveEvent(LaneEvent le) {


    }

    public void receivePinSetterEvent(PinsetterEvent pe) {}

    public Lane getLane() { return this.LANE; }
    public PinSetter getPinSetter() { return this.PINSETTER; }
    public LaneStatusView getLaneStatusView(){
        return ((LaneStatusView) LANE_STATUS_VIEW);
    }
}
