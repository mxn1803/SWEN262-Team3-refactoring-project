package com.steamy.views.specialists;

import com.steamy.ControlDeskEvent;
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

    public void receiveControlDeskEvent(ControlDeskEvent ce) {}
}
