package com.steamy.specialists;

import com.steamy.ControlDeskEvent;
import com.steamy.LaneEvent;
import com.steamy.PinSetterEvent;
import com.steamy.model.ControlDesk;
import com.steamy.views.AddPartyView;
import com.steamy.views.ControlDeskView;

public class ControlSpecialist extends Specialist {
    private final ControlDesk CONTROL_DESK;
    private final int MAX_MEMBERS;

    public ControlSpecialist(int numLanes, int maxPatrons) {
        super();
        this.CONTROL_DESK = new ControlDesk(numLanes,this);
        this.MAX_MEMBERS = maxPatrons;
        super.getOpenViews().put(ViewType.CONTROL_DESK, new ControlDeskView(this.CONTROL_DESK, this));
    }

    public void updateParty(AddPartyView apv) { this.CONTROL_DESK.addPartyQueue(apv.getParty()); }
    public int getMaxMembers() { return this.MAX_MEMBERS; }
    public int getNumLanes() { return this.CONTROL_DESK.getNumLanes(); }

    @Override
    public void receiveEvent(LaneEvent le) {}

    @Override
    public void receiveEvent(PinSetterEvent pe) {}

    @Override
    public void receiveEvent(ControlDeskEvent ce) {

    }
}
