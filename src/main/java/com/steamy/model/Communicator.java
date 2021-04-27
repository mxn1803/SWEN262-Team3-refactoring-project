package com.steamy.model;

import com.steamy.events.ControlDeskEvent;
import com.steamy.events.LaneEvent;
import com.steamy.events.PinSetterEvent;

public interface Communicator {
    void publish();
    void publish(int num);
    void receiveEvent(PinSetterEvent pe);
    void receiveEvent(LaneEvent le);
    void receiveEvent(ControlDeskEvent ce);
}

