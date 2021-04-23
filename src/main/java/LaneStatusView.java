/**
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LaneStatusView implements ActionListener, LaneObserver, PinsetterObserver {

    private final JPanel LANE_PANEL;

    private final JLabel CURRENT_BOWLER_LABEL, PINS_DOWN_LABEL;
    private final JButton VIEW_LANE_BUTTON, VIEW_PINSETTER_BUTTON, MAINTENANCE_BUTTON;

    private final PinSetterView PINSETTER_VIEW;
    private final LaneView LANE_VIEW;
    private final Lane LANE;
    int laneNum;

    public LaneStatusView(Lane lane, int laneNum) {

        this.LANE = lane;
        this.laneNum = laneNum;

        PINSETTER_VIEW = new PinSetterView(laneNum);
        Pinsetter ps = lane.getPinsetter();
        ps.subscribe(PINSETTER_VIEW);

        LANE_VIEW = new LaneView(lane, laneNum);
        lane.subscribe(LANE_VIEW);


        LANE_PANEL = new JPanel();
        LANE_PANEL.setLayout(new FlowLayout());
        JLabel cLabel = new JLabel("Now Bowling: ");
        CURRENT_BOWLER_LABEL = new JLabel("(no one)");
        JLabel pdLabel = new JLabel("Pins Down: ");
        PINS_DOWN_LABEL = new JLabel("0");

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        VIEW_LANE_BUTTON = new JButton("View Lane");
        JPanel viewLanePanel = new JPanel();
        viewLanePanel.setLayout(new FlowLayout());
        VIEW_LANE_BUTTON.addActionListener(this);
        viewLanePanel.add(VIEW_LANE_BUTTON);

        VIEW_PINSETTER_BUTTON = new JButton("Pinsetter");
        JPanel viewPinSetterPanel = new JPanel();
        viewPinSetterPanel.setLayout(new FlowLayout());
        VIEW_PINSETTER_BUTTON.addActionListener(this);
        viewPinSetterPanel.add(VIEW_PINSETTER_BUTTON);

        MAINTENANCE_BUTTON = new JButton("Fix Problem");
        MAINTENANCE_BUTTON.setBackground(Color.GREEN);
        JPanel maintenancePanel = new JPanel();
        maintenancePanel.setLayout(new FlowLayout());
        MAINTENANCE_BUTTON.addActionListener(this);
        maintenancePanel.add(MAINTENANCE_BUTTON);

        VIEW_LANE_BUTTON.setEnabled(false);
        VIEW_PINSETTER_BUTTON.setEnabled(false);


        buttonPanel.add(viewLanePanel);
        buttonPanel.add(viewPinSetterPanel);
        buttonPanel.add(maintenancePanel);

        LANE_PANEL.add(cLabel);
        LANE_PANEL.add(CURRENT_BOWLER_LABEL);
        LANE_PANEL.add(pdLabel);
        LANE_PANEL.add(PINS_DOWN_LABEL);

        LANE_PANEL.add(buttonPanel);

    }

    public JPanel getLanePanel() {
        return LANE_PANEL;
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (LANE.isPartyAssigned()) {
            if (source.equals(VIEW_PINSETTER_BUTTON)) PINSETTER_VIEW.toggle();
            else if (source.equals(VIEW_LANE_BUTTON)) LANE_VIEW.toggle();
            else if (source.equals(MAINTENANCE_BUTTON)) {
                LANE.unPauseGame();
                MAINTENANCE_BUTTON.setBackground(Color.GREEN);
            }
        }
    }

    public void receiveLaneEvent(LaneEvent le) {
        CURRENT_BOWLER_LABEL.setText(le.getBowler().getNickName());
        if (le.isMechanicalProblem()) MAINTENANCE_BUTTON.setBackground(Color.RED);
        VIEW_LANE_BUTTON.setEnabled(LANE.isPartyAssigned());
        VIEW_PINSETTER_BUTTON.setEnabled(LANE.isPartyAssigned());
    }

    public void receivePinsetterEvent(PinsetterEvent pe) {
        Integer pinsDown = pe.totalPinsDown();
        PINS_DOWN_LABEL.setText(pinsDown.toString());
    }
}
