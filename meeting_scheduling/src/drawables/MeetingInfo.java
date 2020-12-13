package drawables;

import data.Meeting;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

import java.awt.*;

public class MeetingInfo implements Drawable {

    private final Color failColor = Color.RED;
    private final Color processingColor = Color.YELLOW;
    private final Color successColor = Color.GREEN;

    private final int posX;
    private final int posY;
    private Meeting meeting;

    public MeetingInfo(int posX, int posY, Meeting meeting){
        this.posX = posX;
        this.posY = posY;
        this.meeting = meeting;
    }

    @Override
    public void draw(SimGraphics simGraphics) {
        String info = "ID: " + meeting.getId() + "; GROUP: " + meeting.getGroupId();
        Color blockColor = this.processingColor;

        if(this.meeting.isScheduled()){
            if(this.meeting.getDay().equals("NULL")){
                blockColor = this.failColor;
            }
            else {
                blockColor = this.successColor;
                info = info + "; DAY: " + this.meeting.getDay() + "; START: " + this.meeting.getStartSlot() +
                        "; END: " + this.meeting.getEndSlot() +
                        "; ATTENDANCE: " + this.meeting.getAttendingEmployees().size() +
                        "OUT OF " + this.meeting.getGroupEmployees().size();
            }
        }

        simGraphics.drawFastRect(blockColor);
        simGraphics.drawString(info, Color.BLACK);
        simGraphics.drawRectBorder(new BasicStroke(), Color.BLACK);
    }

    @Override
    public int getX() {
        return posX;
    }

    @Override
    public int getY() {
        return posY;
    }
}
