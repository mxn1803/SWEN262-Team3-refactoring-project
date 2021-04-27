package com.steamy.views;/*
 *  constructs a prototype Lane View
 *
 */

import com.steamy.events.ControlDeskEvent;
import com.steamy.events.LaneEvent;
import com.steamy.events.PinSetterEvent;
import com.steamy.model.Bowler;
import com.steamy.model.Frame;
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

            String text;
            int[][] lescores = le.getCumulScore();
            for (int k = 0; k < numBowlers; k++) {
                for (int i = 0; i <= le.getFrameNum() - 1; i++) {
                    if (lescores[k][i] != 0) {
                        text = String.valueOf(lescores[k][i]);
                        scoreLabels[k][i].setText(text);
                    }
                }
                Object bowler = bowlers.get(k);
                Frame[] score = (Frame[])le.getScore().get(bowler);
                Frame frame = score[0];

                for (int ballNum = 0; ballNum < BALL_COUNT; ballNum++) {
                    if (ballNum % 2 == 0 && ballNum != 20) {
                        frame = score[ballNum / 2];
                    }

                    if (ballNum > 17) { // Tenth frame hits different
                        if (ballNum == 18 && frame.getBowl(0) != null) {
                            // First bowl of 10th frame
                            ballLabels[k][19].setText(frame.getBowl(0) == 10 ? "X" : String.valueOf(frame.getBowl(0)));
                        } else if (ballNum == 19 && frame.getBowl(1) != null){
                            // Second bowl of 10th frame
                            if (frame.getBowl(0) == 10) {
                                ballLabels[k][20].setText(frame.getBowl(1) == 10 ? "X" : String.valueOf(frame.getBowl(1)));
                            } else {
                                int sum = frame.getBowl(0) + frame.getBowl(1);
                                ballLabels[k][20].setText(sum == 10 ? "/" : String.valueOf(frame.getBowl(1)));
                            }
                        } else if (ballNum == 20 && frame.getBowl(2) != null) {
                            // Third bowl of 10th frame
                            String prevText = ballLabels[k][ballNum].getText();
                            if (prevText.equals("/") || prevText.equals("X")) {  // Last ball was strike or spare
                                ballLabels[k][21].setText(frame.getBowl(2) == 10 ? "X" : String.valueOf(frame.getBowl(2)));
                            } else {  // Last ball was spare
                                int sum = frame.getBowl(1) + frame.getBowl(2);
                                ballLabels[k][21].setText(sum == 10 ? "/" : String.valueOf(frame.getBowl(2)));
                            }
                        }
                    } else if (ballNum % 2 == 0 && frame.getBowl(0) != null) { // Set first ball label
                        if (frame.getBowl(0) == 10) { // Strike
                            ballLabels[k][ballNum].setText("X");
                        } else if (frame.getBowl(0) != null) {  // Any other non-null value
                            ballLabels[k][ballNum].setText(String.valueOf(frame.getBowl(0)));
                        }
                    } else if (frame.getBowl(1) != null) {   // Set second ball label
                        if (frame.getBowl(0) + frame.getBowl(1) == 10) { // Spare
                            ballLabels[k][ballNum].setText("/");
                        } else {
                            ballLabels[k][ballNum].setText(String.valueOf(frame.getBowl(1)));
                        }
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
