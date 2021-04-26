package com.steamy.views.specialists;

import com.steamy.ControlDeskEvent;
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
    private final Specialist CONTROL_SPECIALIST;



    public LaneSpecialist(int laneCount, Specialist controlSpecialist) {
        super();
        this.CONTROL_SPECIALIST = controlSpecialist;
        this.PINSETTER = new PinSetter(this);
        this.LANE = new Lane(this);
        this.PINSETTER_VIEW = new PinSetterView(laneCount, this);
        this.LANE_VIEW = new LaneView((Lane) this.LANE, laneCount, this);
        this.LANE_STATUS_VIEW = new LaneStatusView(
                (Lane) this.LANE,
                (PinSetterView) this.PINSETTER_VIEW,
                (LaneView) this.LANE_VIEW,
                this
        );
        ((PinSetter) this.PINSETTER).reset();
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
        this.LANE.receiveEvent(le);
        this.PINSETTER.receiveEvent(le);
        this.PINSETTER_VIEW.receiveEvent(le);
        this.LANE_VIEW.receiveEvent(le);
        this.LANE_STATUS_VIEW.receiveEvent(le);
    }

    public void receiveEvent(PinsetterEvent pe) {
        this.LANE.receiveEvent(pe);
        this.PINSETTER.receiveEvent(pe);
        this.PINSETTER_VIEW.receiveEvent(pe);
        this.LANE_VIEW.receiveEvent(pe);
        this.LANE_STATUS_VIEW.receiveEvent(pe);
    }

    @Override
    public void receiveEvent(ControlDeskEvent ce) {
        this.LANE.receiveEvent(ce);
        this.PINSETTER.receiveEvent(ce);
        this.PINSETTER_VIEW.receiveEvent(ce);
        this.LANE_VIEW.receiveEvent(ce);
        this.LANE_STATUS_VIEW.receiveEvent(ce);
    }

    public Lane getLane() { return (Lane)this.LANE; }
    public PinSetter getPinSetter() { return (PinSetter) this.PINSETTER; }
    public LaneStatusView getLaneStatusView(){
        return ((LaneStatusView) LANE_STATUS_VIEW);
    }
}
