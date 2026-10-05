package fr.tse.fise2.ui.panels;

import javax.swing.*;
import java.awt.*;

public class LoadingAnimation extends JPanel implements Runnable {

    private static final int POINTS_COUNT = 3;
    private static final int DIAMETER = 20;
    private static final int SPACING = 10;
    private static final int ANIMATION_DELAY = 200;
    private int currentPoint = 0;

    public LoadingAnimation() {
        setPreferredSize(new Dimension((DIAMETER + SPACING) * POINTS_COUNT, DIAMETER));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.yellow);
        for (int i = 0; i < POINTS_COUNT; i++) {
            if (i == currentPoint) {
                g.fillOval(i * (DIAMETER + SPACING), 0, DIAMETER, DIAMETER);
            } else {
                g.drawOval(i * (DIAMETER + SPACING), 0, DIAMETER, DIAMETER);
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            currentPoint = (currentPoint + 1) % POINTS_COUNT;
            repaint();
            try {
                Thread.sleep(ANIMATION_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Loading Animation");
        LoadingAnimation animation = new LoadingAnimation();
        frame.add(animation);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Thread animationThread = new Thread(animation);
        animationThread.start();
    }
}
