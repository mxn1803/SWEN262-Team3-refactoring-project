package com.steamy.views;

import com.steamy.events.ControlDeskEvent;
import com.steamy.events.LaneEvent;
import com.steamy.events.PinSetterEvent;
import com.steamy.model.Communicator;
import com.steamy.specialists.Specialist;

import java.awt.event.ActionEvent;

public abstract class ListeningView extends View implements Communicator {
    private final Specialist SPECIALIST;

    public ListeningView(Specialist s) {
        super();
        this.SPECIALIST = s;
    }

    public abstract void actionPerformed(ActionEvent e);

    public abstract void receiveEvent(LaneEvent le);

    public abstract void receiveEvent(PinSetterEvent pe);

    public abstract void receiveEvent(ControlDeskEvent ce);

    public Specialist getSpecialist() { return this.SPECIALIST; }
}
