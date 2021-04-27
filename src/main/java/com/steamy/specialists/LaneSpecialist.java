package com.steamy.specialists;

import com.steamy.ControlDeskEvent;
import com.steamy.LaneEvent;
import com.steamy.PinSetterEvent;
import com.steamy.model.Communicator;
import com.steamy.model.Lane;
import com.steamy.model.PinSetter;
import com.steamy.views.LaneStatusView;
import com.steamy.views.LaneView;
import com.steamy.views.PinSetterView;

public class LaneSpecialist extends Specialist {
    private final Communicator LANE;
    private final Communicator PINSETTER;

    public LaneSpecialist(int laneCount){
        super();
        PinSetter pinsetter = new PinSetter(this);
        this.PINSETTER = pinsetter;

        Lane tempLane = new Lane(this);
        this.LANE = tempLane;

        PinSetterView tempPinSetterView = new PinSetterView(laneCount, this);
        super.getOpenViews().put(ViewType.PIN_SETTER, tempPinSetterView);

        LaneView tempLaneView = new LaneView(laneCount, this);
        super.getOpenViews().put(ViewType.LANE, tempLaneView);

        LaneStatusView tempLaneStatusView = new LaneStatusView(this);
        super.getOpenViews().put(ViewType.LANE_STATUS, tempLaneStatusView);
        pinsetter.resetPinSetter();
    }

    @Override
    public void receiveEvent(LaneEvent le) {
        this.LANE.receiveEvent(le);
        this.PINSETTER.receiveEvent(le);
        super.getOpenViews().forEach((type, view) -> view.receiveEvent(le));
    }

    @Override
    public void receiveEvent(PinSetterEvent pe) {
        this.LANE.receiveEvent(pe);
        this.PINSETTER.receiveEvent(pe);
        super.getOpenViews().forEach((type, view) -> view.receiveEvent(pe));
    }

    @Override
    public void receiveEvent(ControlDeskEvent ce) {
        this.LANE.receiveEvent(ce);
        this.PINSETTER.receiveEvent(ce);
        super.getOpenViews().forEach((type, view) -> view.receiveEvent(ce));
    }

    public Lane getLane() { return (Lane) this.LANE; }
    public void pauseLane() { ((Lane) this.LANE).pauseGame(); }
    public void resumeLane() { ((Lane) this.LANE).unPauseGame(); }
    public boolean laneHasPartyAssigned() { return ((Lane) this.LANE).isPartyAssigned(); }
    public PinSetter getPinSetter() { return (PinSetter) this.PINSETTER; }
    public LaneStatusView getLaneStatusView(){
        return (LaneStatusView) super.getOpenViews().get(ViewType.LANE_STATUS);
    }
}
