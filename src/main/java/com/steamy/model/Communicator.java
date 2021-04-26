package com.steamy.model;

import com.steamy.ControlDeskEvent;
import com.steamy.LaneEvent;
import com.steamy.PinsetterEvent;
import com.steamy.views.specialists.Specialist;

public interface Communicator {


    void publish();
    void publish(int num);

    void receiveEvent(LaneEvent le);
    void receiveEvent(ControlDeskEvent ce);
    void receiveEvent(PinsetterEvent pe);

}

