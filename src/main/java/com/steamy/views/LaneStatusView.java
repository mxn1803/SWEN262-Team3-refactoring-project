package com.steamy.views; /**
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import com.steamy.events.ControlDeskEvent;
import com.steamy.events.LaneEvent;
import com.steamy.events.PinSetterEvent;
import com.steamy.specialists.LaneSpecialist;
import com.steamy.specialists.Specialist;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LaneStatusView extends ListeningView implements ActionListener {

    private final JPanel LANE_PANEL;

    private final JLabel CURRENT_BOWLER_LABEL, PINS_DOWN_LABEL;
    private final JButton VIEW_LANE_BUTTON, VIEW_PINSETTER_BUTTON, MAINTENANCE_BUTTON;

    public LaneStatusView(Specialist specialist) {
        super(specialist);

        LANE_PANEL = new JPanel();
        LANE_PANEL.setLayout(new FlowLayout());
        JLabel cLabel = new JLabel("Now Bowling: ");
        CURRENT_BOWLER_LABEL = new JLabel("(no one)");
        JLabel pdLabel = new JLabel("Pins Down: ");
        PINS_DOWN_LABEL = new JLabel("0");

        VIEW_LANE_BUTTON = new JButton("View Lane");
        VIEW_LANE_BUTTON.addActionListener(this);

        VIEW_PINSETTER_BUTTON = new JButton("Pinsetter");
        VIEW_PINSETTER_BUTTON.addActionListener(this);

        MAINTENANCE_BUTTON = new JButton("Fix Problem");
        MAINTENANCE_BUTTON.setBackground(Color.GREEN);
        MAINTENANCE_BUTTON.addActionListener(this);

        VIEW_LANE_BUTTON.setEnabled(false);
        VIEW_PINSETTER_BUTTON.setEnabled(false);

        LANE_PANEL.add(cLabel);
        LANE_PANEL.add(CURRENT_BOWLER_LABEL);
        LANE_PANEL.add(pdLabel);
        LANE_PANEL.add(PINS_DOWN_LABEL);
        LANE_PANEL.add(VIEW_LANE_BUTTON);
        LANE_PANEL.add(VIEW_PINSETTER_BUTTON);
        LANE_PANEL.add(MAINTENANCE_BUTTON);
    }

    public JPanel getLanePanel() {
        return LANE_PANEL;
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        LaneSpecialist tempSpecialist = (LaneSpecialist) super.getSpecialist();
        if (tempSpecialist.laneHasPartyAssigned()) {
            if (source.equals(VIEW_PINSETTER_BUTTON)) tempSpecialist.toggleView(Specialist.ViewType.PIN_SETTER);
            else if (source.equals(VIEW_LANE_BUTTON)) tempSpecialist.toggleView(Specialist.ViewType.LANE);
            else if (source.equals(MAINTENANCE_BUTTON)) {
                tempSpecialist.resumeLane();
                MAINTENANCE_BUTTON.setBackground(Color.GREEN);
            }
        }
    }

    @Override
    public void receiveEvent(LaneEvent le) {
        LaneSpecialist tempSpecialist = (LaneSpecialist) super.getSpecialist();
        CURRENT_BOWLER_LABEL.setText(le.getBowler().getNickName());
        if (le.isMechanicalProblem()) MAINTENANCE_BUTTON.setBackground(Color.RED);
        VIEW_LANE_BUTTON.setEnabled(tempSpecialist.getLane().isPartyAssigned());
        VIEW_PINSETTER_BUTTON.setEnabled(tempSpecialist.getLane().isPartyAssigned());
    }

    @Override
    public void publish() {}

    @Override
    public void publish(int num) {}

    @Override
    public void receiveEvent(PinSetterEvent pe) {
        Integer pinsDown = pe.totalPinsDown();
        PINS_DOWN_LABEL.setText(pinsDown.toString());
    }

    @Override
    public void receiveEvent(ControlDeskEvent ce) {}
}
