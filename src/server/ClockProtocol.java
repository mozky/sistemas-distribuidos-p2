package server;

import utils.CustomClock;

public class ClockProtocol {
    private static final int WAITING = 0;
    private static final int SENT_INSTRUCTIONS = 1;
    private static final int SECONDS = 2;
    private static final int MINUTES = 3;
    private static final int HOURS = 4;

    private CustomClock clock;

    ClockProtocol(CustomClock clock) {
        this.clock = clock;
    }

    public int state = WAITING;

    public String processInput(String theInput) {
        String theOutput = null;
        if (theInput != null && theInput.equalsIgnoreCase("bye")) {
            theOutput = "Bye";
        } else {
            switch (state) {
                case WAITING:
                    theOutput = "Please type 'seconds', 'minutes', or 'hours' to take a role...";
                    state = SENT_INSTRUCTIONS;
                    break;
                case SENT_INSTRUCTIONS:
                    switch(theInput) {
                        case "seconds":
                            theOutput = "You are now the seconds clock";
                            state = SECONDS;
                            break;
                        case "minutes":
                            theOutput = "You are now the minutes clock";
                            state = MINUTES;
                            break;
                        case "hours":
                            theOutput = "You are now the hours clock";
                            state = HOURS;
                            break;
                        default:
                            theOutput = "WHAT?";
                    }
                    break;
                case SECONDS:
                    theOutput = String.valueOf(this.clock.getTime().getSecond());
                    break;
                case MINUTES:
                    if (theInput != null) {
                        if (theInput.equalsIgnoreCase("plus")) {
                            this.clock.plusMinute();
                        } else if (theInput.equalsIgnoreCase("minus")) {
                            this.clock.minusMinute();
                        }
                    }
                    theOutput = String.valueOf(this.clock.getTime().getMinute());
                    break;
                case HOURS:
                    if (theInput != null) {
                        if (theInput.equalsIgnoreCase("plus")) {
                            this.clock.plusHour();
                        } else if (theInput.equalsIgnoreCase("minus")) {
                            this.clock.minusHour();
                        }
                    }
                    theOutput = String.valueOf(this.clock.getTime().getHour());
                    break;
                default:
                    theOutput = "WHAT?";
            }
        }
        return theOutput;
    }
}
