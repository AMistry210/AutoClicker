package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AutoClicker {
    private boolean running = false;
    private Thread clickerThread;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AutoClicker::new);
    }

    public AutoClicker() {
        JFrame frame = new JFrame("AutoClicker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(4, 1));

        JTextField intervalField = new JTextField("1000");
        JLabel intervalLabel = new JLabel("Click interval (ms):");
        JPanel intervalPanel = new JPanel();
        intervalPanel.setLayout(new GridLayout(1, 2));
        intervalPanel.add(intervalLabel);
        intervalPanel.add(intervalField);
        frame.add(intervalPanel);

        JButton startButton = new JButton("Start");
        frame.add(startButton);

        JButton stopButton = new JButton("Stop");
        stopButton.setEnabled(false);
        frame.add(stopButton);

        JLabel statusLabel = new JLabel("Status: Stopped", SwingConstants.CENTER);
        frame.add(statusLabel);

        startButton.addActionListener(e -> {
            if (!running) {
                try {
                    int interval = Integer.parseInt(intervalField.getText());
                    if (interval <= 0)
                        throw new NumberFormatException();
                    startClicking(interval);
                    statusLabel.setText("Status: Running");
                    startButton.setEnabled(false);
                    stopButton.setEnabled(true);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid positive number for the interval.",
                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        stopButton.addActionListener(e -> stopClicking(statusLabel, startButton, stopButton));

        frame.setVisible(true);
    }

    private void startClicking(int interval) {
        running = true;
        clickerThread = new Thread(() -> {
            try {
                Robot robot = new Robot();
                while (running) {
                    robot.mousePress(java.awt.event.InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(java.awt.event.InputEvent.BUTTON1_DOWN_MASK);
                    Thread.sleep(interval);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        clickerThread.start();
    }

    private void stopClicking(JLabel statusLabel, JButton startButton, JButton stopButton) {
        running = false;
        try {
            if (clickerThread != null)
                clickerThread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        statusLabel.setText("Status: Stopped");
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }
}
