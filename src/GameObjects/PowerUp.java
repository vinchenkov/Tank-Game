package GameObjects;

import GameObjects.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class PowerUp extends GameObject {

    public PowerUp (BufferedImage img, int x, int y) {
        super(img, x, y);
        isCollidable = true;
        isDrawable = true;
    }

    public abstract void consumePowerUp(Tank tank);

    @Override
    public Rectangle getRec() {
        return rec;
    }

    @Override
    public boolean isCollidable() {
        return isCollidable;
    }

    @Override
    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if(isDrawable) {
            g2d.drawImage(img, x, y, null);
        }
    }
}
