package com.steamy.model;

import com.steamy.ControlDeskEvent;
import com.steamy.LaneEvent;
import com.steamy.PinSetterEvent;

public interface Communicator {
    void publish();
    void publish(int num);
    void receiveEvent(PinSetterEvent pe);
    void receiveEvent(LaneEvent le);
    void receiveEvent(ControlDeskEvent ce);
}

