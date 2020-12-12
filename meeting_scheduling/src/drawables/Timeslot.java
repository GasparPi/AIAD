package drawables;

import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

import java.awt.*;

public class Timeslot implements Drawable {

    private final int posX;
    private final int posY;

    public Timeslot(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    public void draw(SimGraphics simGraphics) {
        simGraphics.drawFastRect(Color.GREEN);
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
