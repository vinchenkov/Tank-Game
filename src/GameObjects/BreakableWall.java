package GameObjects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BreakableWall extends GameObject {

    public BreakableWall(BufferedImage img, int x, int y) {
        super(img, x, y);
        isCollidable = true;
        isDrawable = true;
    }

    public void breakWall() {
        isCollidable = false;
        isDrawable = false;
        needsDeleting = true;
    }

    @Override
    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if(isDrawable) {
            g2d.drawImage(img, x, y, null);
        }
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
