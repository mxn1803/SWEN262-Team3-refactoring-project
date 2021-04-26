package com.steamy.specialists;

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