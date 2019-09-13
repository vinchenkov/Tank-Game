package GameObjects;

import GameObjects.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SolidWall extends GameObject {

    private boolean isCollidable;

    public SolidWall(BufferedImage img, int x, int y) {
        super(img, x, y);
        isCollidable = true;
    }

    @Override
    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(img, x, y, null);
    }

    @Override
    public Rectangle getRec() {
        return rec;
    }

    @Override
    public boolean isCollidable() {
        return isCollidable;
    }
}
