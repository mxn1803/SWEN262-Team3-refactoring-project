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
import java.util.Vector;

public class LaneView implements LaneObserver, ActionListener {

    private final int BALL_COUNT;
    private final int FRAME_COUNT;

    private volatile boolean initDone;

    private final JFrame win;
    private final Container cpanel;
    private Vector bowlers;

    private JLabel[][] ballLabels;
    private JLabel[][] scoreLabels;

    private JButton maintenance;
    private final Lane lane;

    public LaneView(Lane lane, int laneNum) {

        this.lane = lane;
        BALL_COUNT = 21;
        FRAME_COUNT = 10;

        initDone = true;
        win = new JFrame("Lane " + laneNum + ":");
        cpanel = win.getContentPane();
        cpanel.setLayout(new BorderLayout());

        ballLabels = null;
        scoreLabels = null;

        win.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                win.setVisible(false);
            }
        });

        cpanel.add(new JPanel());

    }

    public void toggle() { win.setVisible(!win.isVisible()); }

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

    public void receiveLaneEvent(LaneEvent le) {
        if (lane.isPartyAssigned()) {
            int numBowlers = le.getParty().getMembers().size();

            while (!initDone) Thread.onSpinWait();

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

                win.pack();

            }

            int[][] lescores = le.getCumulScore();
            for (int k = 0; k < numBowlers; k++) {
                for (int i = 0; i <= le.getFrameNum() - 1; i++) {
                    if (lescores[k][i] != 0)
                        scoreLabels[k][i].setText((Integer.valueOf(lescores[k][i])).toString());
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

    public void actionPerformed(ActionEvent e) { if (e.getSource().equals(maintenance)) lane.pauseGame(); }
}
