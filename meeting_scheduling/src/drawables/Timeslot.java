package drawables;

import agents.Employee;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

import java.awt.*;

public class Timeslot implements Drawable {

    private final Color occupiedColor = Color.RED;
    private final Color freeColor = Color.LIGHT_GRAY;

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
        Color blockColor = this.employee.hasTimeSlotScheduled(this.day, this.dayTimeslot) ? this.occupiedColor : this.freeColor;
        simGraphics.drawFastRect(blockColor);
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
