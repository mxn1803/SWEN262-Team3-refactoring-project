package com.steamy.views.specialists;

import com.steamy.ControlDeskEvent;
import com.steamy.LaneEvent;
import com.steamy.PinsetterEvent;
import com.steamy.model.ControlDesk;

public class ControlSpecialist extends Specialist {
    private final ControlDesk CONTROL_DESK;

    public ControlSpecialist(ControlDesk controlDesk) {
        super();
        this.CONTROL_DESK = controlDesk;
    }

    public void openControlDeskView(int maxMembers) {}

    public void openNewPatronView() {}

    public void openAddPartyView(int maxMembers) {}

    public void closeControlDeskView() {}

    public void closeNewPatronView() {}

    public void closeAddPartyView() {}

    @Override
    public void receiveEvent(ControlDeskEvent ce) {}

    @Override
    public void receiveEvent(LaneEvent le) {

    }

    @Override
    public void receiveEvent(PinsetterEvent pe) {

    }
}
