/*
 *  constructs a prototype Lane View
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Vector;

public class LaneView implements LaneObserver, ActionListener {

    private final int BALL_COUNT;
    private final int FRAME_COUNT;

    private boolean initDone;

    private JFrame frame;
    private Container cpanel;
    private Vector bowlers;

    private JPanel[][] balls;
    private JLabel[][] ballLabel;
    private JPanel[][] scores;
    private JLabel[][] scoreLabel;
    private JPanel[][] ballGrid;
    private JPanel[] userScoreboard;

    private JButton maintenance;
    private Lane lane;

    public LaneView(Lane lane, int laneNum) {

        this.lane = lane;
        BALL_COUNT = 23;
        FRAME_COUNT = 10;

        initDone = true;
        frame = new JFrame("Lane " + laneNum + ":");
        cpanel = frame.getContentPane();
        cpanel.setLayout(new BorderLayout());

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.setVisible(false);
            }
        });

        cpanel.add(new JPanel());

    }

    public void toggle() { frame.setVisible(!frame.isVisible()); }

    private JPanel makeScoreboard(Party party) {

        initDone = false;
        bowlers = party.getMembers();
        int numBowlers = bowlers.size();

        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(0, 1));

        balls = new JPanel[numBowlers][BALL_COUNT];
        ballLabel = new JLabel[numBowlers][BALL_COUNT];
        scores = new JPanel[numBowlers][FRAME_COUNT];
        scoreLabel = new JLabel[numBowlers][FRAME_COUNT];
        ballGrid = new JPanel[numBowlers][FRAME_COUNT];
        userScoreboard = new JPanel[numBowlers];

        for (int i = 0; i < numBowlers; i++) {
            //ball outcome labels -- pin count, spare (/) or strike (X)
            for (int j = 0; j < BALL_COUNT; j++) {
                ballLabel[i][j] = new JLabel(" ");
                balls[i][j] = new JPanel();
                balls[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                balls[i][j].add(ballLabel[i][j]);
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
                scoreLabel[i][j] = new JLabel("  ", SwingConstants.CENTER);
                scores[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                scores[i][j].setLayout(new GridLayout(0, 1));
                scores[i][j].add(ballGrid[i][j], BorderLayout.EAST);
                scores[i][j].add(scoreLabel[i][j], BorderLayout.SOUTH);
                userScoreboard[i].add(scores[i][j], BorderLayout.EAST);
            }
            panel.add(userScoreboard[i]);
        }
        initDone = true;
        return panel;
    }

    public void receiveLaneEvent(LaneEvent le) {
        if (lane.isPartyAssigned()) {
            int numBowlers = le.getParty().getMembers().size();
            while (!initDone) {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                }
            }

            if (le.getFrameNum() == 1 && le.getBall() == 0 && le.getIndex() == 0) {
                System.out.println("Making the frame.");
                cpanel.removeAll();
                cpanel.add(makeScoreboard(le.getParty()), "Center");

                // Button Panel
                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FlowLayout());

                maintenance = new JButton("Maintenance Call");
                JPanel maintenancePanel = new JPanel();
                maintenancePanel.setLayout(new FlowLayout());
                maintenance.addActionListener(this);
                maintenancePanel.add(maintenance);

                buttonPanel.add(maintenancePanel);

                cpanel.add(buttonPanel, "South");

                frame.pack();

            }

            int[][] lescores = le.getCumulScore();
            for (int k = 0; k < numBowlers; k++) {
                for (int i = 0; i <= le.getFrameNum() - 1; i++) {
                    if (lescores[k][i] != 0) scoreLabel[k][i].setText((new Integer(lescores[k][i])).toString());
                }
                for (int i = 0; i < 21; i++) {
                    if (((int[]) ((HashMap) le.getScore()).get(bowlers.get(k)))[i] != -1)
                        if (((int[]) ((HashMap) le.getScore()).get(bowlers.get(k)))[i] == 10 && (i % 2 == 0 || i == 19))
                            ballLabel[k][i].setText("X");
                        else if (i > 0 && ((int[]) ((HashMap) le.getScore()).get(bowlers.get(k)))[i] + ((int[]) ((HashMap) le.getScore()).get(bowlers.get(k)))[i - 1] == 10 && i % 2 == 1)
                            ballLabel[k][i].setText("/");
                        else if (((int[]) ((HashMap) le.getScore()).get(bowlers.get(k)))[i] == -2) {

                            ballLabel[k][i].setText("F");
                        } else
                            ballLabel[k][i].setText((new Integer(((int[]) ((HashMap) le.getScore()).get(bowlers.get(k)))[i])).toString());
                }
            }

        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(maintenance)) {
            lane.pauseGame();
        }
    }

}
