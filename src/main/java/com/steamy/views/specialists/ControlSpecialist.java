package com.steamy.views.specialists;

import com.steamy.ControlDeskEvent;
import com.steamy.LaneEvent;
import com.steamy.PinSetterEvent;
import com.steamy.model.ControlDesk;
import com.steamy.views.ControlDeskView;

public class ControlSpecialist extends Specialist {
    private final ControlDesk CONTROL_DESK;
    private final ControlDeskView CONTROL_DESK_VIEW;

    public ControlSpecialist(int numLanes, int maxPatrons) {
        super();
        ControlDesk tempCde = new ControlDesk(numLanes,this);
        this.CONTROL_DESK = tempCde;
        ControlDeskView cdv = new ControlDeskView(tempCde, maxPatrons, this);
        this.CONTROL_DESK_VIEW = cdv;
    }

    public void openControlDeskView(int maxMembers) {}

    public void openNewPatronView() {}

    public void openAddPartyView(int maxMembers) {}

    public void closeControlDeskView() {}

    public void closeNewPatronView() {}

    public void closeAddPartyView() {}

    public void receiveControlDeskEvent(ControlDeskEvent ce) {}

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
