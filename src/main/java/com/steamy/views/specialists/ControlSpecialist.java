package com.steamy.views.specialists;

import com.steamy.ControlDeskEvent;
import com.steamy.LaneEvent;
import com.steamy.PinSetterEvent;
import com.steamy.model.ControlDesk;
import com.steamy.views.ControlDeskView;

public class ControlSpecialist extends Specialist {
    private final int MAX_MEMBERS;
    private final ControlDesk CONTROL_DESK;

    public ControlSpecialist(ControlDesk controlDesk, int maxMembers) {
        super();
        this.MAX_MEMBERS = maxMembers;
        this.CONTROL_DESK = controlDesk;

        ControlDeskView tempControlDeskView = new ControlDeskView(this.CONTROL_DESK, this.MAX_MEMBERS);
        super.getOpenViews().put(ViewType.CONTROL_DESK, tempControlDeskView);
    }

//    public void openControlDeskView(int maxMembers) {}
//
//    public void openNewPatronView() {}
//
//    public void openAddPartyView(int maxMembers) {}
//
//    public void closeControlDeskView() {}
//
//    public void closeNewPatronView() {}
//
//    public void closeAddPartyView() {}

    @Override
    public void receiveEvent(LaneEvent le) {

    }

    @Override
    public void receiveEvent(PinSetterEvent pe) {

    }

    @Override
    public void receiveEvent(ControlDeskEvent ce) {

    }
}
