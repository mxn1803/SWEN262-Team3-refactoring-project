package com.steamy.specialists;

import com.steamy.events.ControlDeskEvent;
import com.steamy.events.LaneEvent;
import com.steamy.events.PinSetterEvent;
import com.steamy.model.ControlDesk;
import com.steamy.model.Lane;
import com.steamy.views.AddPartyView;
import com.steamy.views.ControlDeskView;
import com.steamy.views.LaneStatusView;

import java.util.ArrayList;
import java.util.List;

public class ControlSpecialist extends Specialist {
    private final ControlDesk CONTROL_DESK;
    private final int MAX_MEMBERS;
    private final List<LaneSpecialist> LANE_SPECIALISTS;

    public ControlSpecialist(int numLanes, int maxPatrons) {
        super();
        this.LANE_SPECIALISTS = new ArrayList<>(numLanes);
        for (int i = 1; i <= numLanes; i++) this.LANE_SPECIALISTS.add(new LaneSpecialist(i));

        this.CONTROL_DESK = new ControlDesk(numLanes,this);
        this.MAX_MEMBERS = maxPatrons;
        super.getOpenViews().put(ViewType.CONTROL_DESK, new ControlDeskView(this.CONTROL_DESK, this));
    }

    public void updateParty(AddPartyView apv) { this.CONTROL_DESK.addPartyQueue(apv.getParty()); }
    public int getMaxMembers() { return this.MAX_MEMBERS; }
    public int getNumLanes() { return this.LANE_SPECIALISTS.size(); }
    public Lane getLane(int index) { return this.LANE_SPECIALISTS.get(index).getLane(); }
    public LaneStatusView getLaneStatusView(int index) { return this.LANE_SPECIALISTS.get(index).getLaneStatusView(); }

    @Override
    public void receiveEvent(LaneEvent le) {}

    @Override
    public void receiveEvent(PinSetterEvent pe) {}

    @Override
    public void receiveEvent(ControlDeskEvent ce) {

    }
}
