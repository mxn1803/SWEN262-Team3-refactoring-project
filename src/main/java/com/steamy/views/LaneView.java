package com.steamy.views;/*
 *  constructs a prototype Lane View
 *
 */

import com.steamy.events.ControlDeskEvent;
import com.steamy.events.LaneEvent;
import com.steamy.events.PinSetterEvent;
import com.steamy.model.Bowler;
import com.steamy.model.Party;
import com.steamy.specialists.LaneSpecialist;
import com.steamy.specialists.Specialist;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

public class LaneView extends ListeningView implements ActionListener {

    private final int BALL_COUNT;
    private final int FRAME_COUNT;

    private volatile boolean initDone;

    private final Container CONTAINER_PANEL;
    private Vector bowlers;

    private JLabel[][] ballLabels;
    private JLabel[][] scoreLabels;

    private JButton maintenance;

    public LaneView(int laneNum, Specialist specialist) {
        super(specialist);
        JFrame tempWindow = super.getWindow();
        tempWindow.setTitle("Lane " + laneNum + ":");
        BALL_COUNT = 21;
        FRAME_COUNT = 10;

        initDone = true;
        CONTAINER_PANEL = tempWindow.getContentPane();
        CONTAINER_PANEL.setLayout(new BorderLayout());

        ballLabels = null;
        scoreLabels = null;

        tempWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                tempWindow.setVisible(false);
            }
        });

        CONTAINER_PANEL.add(new JPanel());
    }

    private JPanel makeScoreboard(Party party) {

        initDone = false;
        bowlers = party.getMembers();
        int numBowlers = bowlers.size();

        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(0, 1));

        ballLabels = new JLabel[numBowlers][BALL_COUNT + 2];
        scoreLabels = new JLabel[numBowlers][FRAME_COUNT];

        JPanel[][] balls = new JPanel[numBowlers][BALL_COUNT + 2];
        JPanel[][] scores = new JPanel[numBowlers][FRAME_COUNT];
        JPanel[][] ballGrid = new JPanel[numBowlers][FRAME_COUNT];
        JPanel[] userScoreboard = new JPanel[numBowlers];

        for (int i = 0; i < numBowlers; i++) {
            //ball outcome labels -- pin count, spare (/) or strike (X)
            for (int j = 0; j < BALL_COUNT + 2; j++) {
                ballLabels[i][j] = new JLabel(" ");
                balls[i][j] = new JPanel();
                balls[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                balls[i][j].add(ballLabels[i][j]);
            }

            //little boxes along the top -- frames 1-9
            for (int j = 0; j < FRAME_COUNT - 1; j++) {
                ballGrid[i][j] = new JPanel();
                ballGrid[i][j].setLayout(new GridLayout(0, 3));
                ballGrid[i][j].add(new JLabel(" "), BorderLayout.EAST); //blank
                ballGrid[i][j].add(balls[i][2 * j], BorderLayout.EAST);      //first throw
                ballGrid[i][j].add(balls[i][2 * j + 1], BorderLayout.EAST);  //second throw
            }
            //frame 10
            ballGrid[i][FRAME_COUNT - 1] = new JPanel();
            ballGrid[i][FRAME_COUNT - 1].setLayout(new GridLayout(0, 3));
            ballGrid[i][FRAME_COUNT - 1].add(balls[i][2 * FRAME_COUNT - 1]); //first throw
            ballGrid[i][FRAME_COUNT - 1].add(balls[i][2 * FRAME_COUNT]);     //second throw
            ballGrid[i][FRAME_COUNT - 1].add(balls[i][2 * FRAME_COUNT + 1]); //optional third throw

            //user container
            userScoreboard[i] = new JPanel();
            userScoreboard[i].setBorder(BorderFactory.createTitledBorder(((Bowler) bowlers.get(i)).getNick()));
            userScoreboard[i].setLayout(new GridLayout(0, FRAME_COUNT));
            for (int j = 0; j < FRAME_COUNT; j++) {
                scores[i][j] = new JPanel();
                scoreLabels[i][j] = new JLabel("  ", SwingConstants.CENTER);
                scores[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                scores[i][j].setLayout(new GridLayout(0, 1));
                scores[i][j].add(ballGrid[i][j], BorderLayout.EAST);
                scores[i][j].add(scoreLabels[i][j], BorderLayout.SOUTH);
                userScoreboard[i].add(scores[i][j], BorderLayout.EAST);
            }
            panel.add(userScoreboard[i]);
        }
        initDone = true;
        return panel;
    }

    public void receiveEvent(LaneEvent le) {
        JFrame tempWindow = super.getWindow();
        LaneSpecialist tempSpecialist = (LaneSpecialist) super.getSpecialist();
        if (tempSpecialist.laneHasPartyAssigned()) {
            int numBowlers = le.getParty().getMembers().size();

            while (!initDone) Thread.onSpinWait();

            if (le.getFrameNum() == 1 && le.getBall() == 0 && le.getIndex() == 0) {
                System.out.println("Making the frame.");
                CONTAINER_PANEL.removeAll();
                CONTAINER_PANEL.add(makeScoreboard(le.getParty()), "Center");

                // Button Panel
                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FlowLayout());

                maintenance = new JButton("Maintenance Call");
                JPanel maintenancePanel = new JPanel();
                maintenancePanel.setLayout(new FlowLayout());
                maintenance.addActionListener(this);
                maintenancePanel.add(maintenance);

                buttonPanel.add(maintenancePanel);

                CONTAINER_PANEL.add(buttonPanel, "South");

                tempWindow.pack();
            }

            int[][] lescores = le.getCumulScore();
            for (int k = 0; k < numBowlers; k++) {
                for (int i = 0; i <= le.getFrameNum() - 1; i++) {
                    if (lescores[k][i] != 0) scoreLabels[k][i].setText((Integer.valueOf(lescores[k][i])).toString());
                }
                for (int i = 0; i < BALL_COUNT; i++) {
                    int outcome = ((int[]) le.getScore().get(bowlers.get(k)))[i];
                    int prevOutcome = i > 0 ? ((int[]) le.getScore().get(bowlers.get(k)))[i - 1] : 0;
                    boolean isFirstThrow = i % 2 == 0;
                    if (outcome != -1) {
                        if (outcome == 10 && (isFirstThrow || i == 19)) ballLabels[k][i].setText("X");
                        else if (outcome + prevOutcome == 10 && !isFirstThrow) ballLabels[k][i].setText("/");
                        else if (outcome == -2) ballLabels[k][i].setText("F");
                        else ballLabels[k][i].setText((Integer.valueOf(outcome)).toString());
                    }
                }
            }
        }
    }

    @Override
    public void publish() {}

    @Override
    public void publish(int num) {}

    @Override
    public void receiveEvent(PinSetterEvent pe) {}

    @Override
    public void receiveEvent(ControlDeskEvent ce) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(maintenance)) ((LaneSpecialist) super.getSpecialist()).pauseLane();
    }

}
