package drawables;

import agents.Employee;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

import java.awt.*;

public class Timeslot implements Drawable {

    private final int posX;
    private final int posY;
    private final int day;
    private final int dayTimeslot;
    private final Employee employee;

    public Timeslot(int posX, int posY, int day, int dayTimeslot, Employee employee) {
        this.posX = posX;
        this.posY = posY;
        this.day = day;
        this.dayTimeslot = dayTimeslot;
        this.employee = employee;
    }

    @Override
    public void draw(SimGraphics simGraphics) {
        Color blockColor = this.employee.hasTimeSlotScheduled(this.day, this.dayTimeslot) ? Color.GREEN : Color.LIGHT_GRAY;
        simGraphics.drawFastRect(blockColor);
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
