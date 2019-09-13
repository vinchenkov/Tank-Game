package GameObjects;

import java.awt.image.BufferedImage;

public class ScramblerPowerUp extends PowerUp {
    public ScramblerPowerUp(BufferedImage img, int x, int y) {
        super(img, x, y);
    }

    @Override
    public void consumePowerUp(Tank tank) {
        isCollidable = false;
        isDrawable = false;
        tank.setScramblerAmmo(3);
        tank.setHasMissiles(false);
    }
}
