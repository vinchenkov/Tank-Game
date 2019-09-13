package GameObjects;

import java.awt.image.BufferedImage;

public class MissilePowerUp extends PowerUp {

    public MissilePowerUp(BufferedImage img, int x, int y) {
        super(img, x, y);
    }

    @Override
    public void consumePowerUp(Tank tank) {
        isCollidable = false;
        isDrawable = false;
        tank.setMissileStartTime();
        tank.setHasMissiles(true);
    }
}
