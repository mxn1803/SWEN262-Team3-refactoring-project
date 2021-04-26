package com.steamy.views.specialists;

import com.steamy.LaneEvent;
import com.steamy.PinsetterEvent;
import com.steamy.model.Communicator;
import com.steamy.model.Lane;
import com.steamy.model.PinSetter;
import com.steamy.views.LaneStatusView;
import com.steamy.views.View;

public class LaneSpecialist extends Specialist {
    private final Communicator LANE;
    private final Communicator PINSETTER;
    private final View LANE_VIEW;
    private final View PINSETTER_VIEW;
    private final View LANE_STATUS_VIEW;



    public LaneSpecialist(int laneCount){
        super();
        this.LANE = new Lane(this);
        this.LANE_STATUS_VIEW = new LaneStatusView((Lane) LANE, laneCount, this);
        this.PINSETTER_VIEW =  ((LaneStatusView) LANE_STATUS_VIEW).getPinsetterView();
        this.PINSETTER = ((Lane) LANE).getPinsetter();
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
        LANE_VIEW.receiveEvent(le);
    }

    public void receiveEvent(PinsetterEvent pe) {
        PINSETTER_VIEW.receiveEvent(pe);
    }

    public Lane getLane() { return (Lane)this.LANE; }
    public PinSetter getPinSetter() { return (PinSetter) this.PINSETTER; }
    public LaneStatusView getLaneStatusView(){
        return ((LaneStatusView) LANE_STATUS_VIEW);
    }
}
