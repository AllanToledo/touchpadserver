package com.company;

import javax.sound.sampled.AudioSystem;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.awt.*;
import java.awt.event.InputEvent;

public class ControlMouse {

    private Robot robot;
    private boolean leftMouseIsPressed = false;
    private boolean rightMouseIsPressed = false;

    public ControlMouse(Robot robot) {
        this.robot = robot;
    }

    public ControlMouse(Robot robot, double deathMargin) {
        this.robot = robot;
    }

    public void move(int x, int y) {
        robot.mouseMove(x, y);
    }

    public void wheel(int wheel) {
        if (wheel != 0)
            robot.mouseWheel(wheel);
    }

    public void toClick(boolean leftButton, boolean rightButton) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (leftButton && !leftMouseIsPressed) {
                    robot.mousePress(InputEvent.BUTTON1_MASK);
                    leftMouseIsPressed = true;
                }
                if (rightButton && !rightMouseIsPressed) {
                    robot.mousePress(InputEvent.BUTTON3_MASK);
                    rightMouseIsPressed = true;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (leftMouseIsPressed) {
                    robot.mouseRelease(InputEvent.BUTTON1_MASK);
                    leftMouseIsPressed = false;
                }
                if (rightMouseIsPressed) {
                    rightMouseIsPressed = false;
                    robot.mouseRelease(InputEvent.BUTTON3_MASK);
                }

            }
        }).start();
        /*
        if(leftButton && !leftMouseIsPressed){
            robot.mousePress(InputEvent.BUTTON1_MASK);
            leftMouseIsPressed = true;
        } else if(!leftButton && leftMouseIsPressed) {
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            leftMouseIsPressed = false;
        }
        if(rightButton && !rightMouseIsPressed){
            robot.mousePress(InputEvent.BUTTON3_MASK);
            rightMouseIsPressed = true;
        } else if(!rightButton && rightMouseIsPressed){
            rightMouseIsPressed = false;
            robot.mouseRelease(InputEvent.BUTTON3_MASK);
        }
        */
    }
}