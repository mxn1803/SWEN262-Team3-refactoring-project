package com.steamy.specialists;

import com.steamy.ControlDeskEvent;
import com.steamy.LaneEvent;
import com.steamy.PinSetterEvent;
import com.steamy.model.ControlDesk;
import com.steamy.views.ControlDeskView;

public class ControlSpecialist extends Specialist {
    private final ControlDesk CONTROL_DESK;

    public ControlSpecialist(int numLanes, int maxPatrons) {
        super();
        this.CONTROL_DESK = new ControlDesk(numLanes,this);
        super.getOpenViews().put(ViewType.CONTROL_DESK, new ControlDeskView(this.CONTROL_DESK, maxPatrons, this));
    }

    @Override
    public void receiveEvent(LaneEvent le) {}

    @Override
    public void receiveEvent(PinSetterEvent pe) {}

    @Override
    public void receiveEvent(ControlDeskEvent ce) {

    }
}
