package com.steamy.views.specialists;

import com.steamy.LaneEvent;
import com.steamy.PinsetterEvent;
import com.steamy.model.Lane;
import com.steamy.model.PinSetter;

public class LaneSpecialist extends Specialist {
    private final Lane LANE;
    private final PinSetter PINSETTER;

    public LaneSpecialist(Lane lane) {
        super();
        this.LANE = lane;
        this.PINSETTER = new PinSetter();
    }

    public void openLaneView() {}

    public void openLaneStatusView() {}

    public void openPinSetterView() {}

    public void closeLaneView() {}

    public void closeLaneStatusView() {}

    public void closePinSetterView() {}

    public void receiveLaneEvent(LaneEvent le) {}

    public void receivePinSetterEvent(PinsetterEvent pe) {}

    public Lane getLane() { return this.LANE; }
    public PinSetter getPinSetter() { return this.PINSETTER; }
}
