package com.steamy.views.specialists;

import com.steamy.LaneEvent;
import com.steamy.PinsetterEvent;
import com.steamy.model.Communicator;
import com.steamy.model.Lane;
import com.steamy.model.PinSetter;
import com.steamy.views.LaneStatusView;
import com.steamy.views.LaneView;
import com.steamy.views.PinSetterView;
import com.steamy.views.View;

public class LaneSpecialist extends Specialist {
    private final Communicator LANE;
    private final Communicator PINSETTER;
    private final View LANE_VIEW;
    private final View PINSETTER_VIEW;
    private final View LANE_STATUS_VIEW;



    public LaneSpecialist(int laneCount){
        super();
        PinSetter pinsetter = new PinSetter(this);
        this.PINSETTER = pinsetter;
        Lane tempLane = new Lane(pinsetter, this);
        this.LANE = tempLane;
        PinSetterView tempPinView = new PinSetterView(laneCount, this);
        this.PINSETTER_VIEW = tempPinView;
        LaneView tempLaneView = new LaneView(tempLane, laneCount, this);
        this.LANE_VIEW = tempLaneView;
        this.LANE_STATUS_VIEW = new LaneStatusView(tempLane, tempPinView, tempLaneView, this);
        pinsetter.reset();


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
