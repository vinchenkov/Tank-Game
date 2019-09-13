package GameObjects;

import Interfaces.Collidable;
import Game.SoundPlayer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bullet extends Projectile {

    Bullet(BufferedImage img, BufferedImage[] explosionImgs, int x, int y, int angle, Tank parentTank ) {
        super(img, explosionImgs, x, y, angle, parentTank);
        velocity = 4;
        damage = 10;
    }

    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if(isDrawable) {
            if (!isExploding) {
                g2d.drawImage(img, x, y, null);
            }
            else {
                g2d.drawImage(explosionImgs[explosionCount], x, y, null);
                if (explosionCount < 5)
                    explosionCount++;
                else {
                    isDrawable = false;
                    needsDeleting = true;
                }
            }
        }
    }




    @Override
    public void checkCollision(Collidable obj) {
        if (isCollidable) {
            if (rec.intersects(obj.getRec())) {
                if (obj.isCollidable()) {
                    if (!parentTank.equals(obj)) {
                        blowUp();
                        if (obj instanceof Tank) {
                            ((Tank) obj).getPlayer().takeDamage(damage);
                        }
                        if (obj instanceof BreakableWall) {
                            ((BreakableWall) obj).breakWall();
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isCollidable() {
        return isCollidable;
    }

    @Override
    public Rectangle getRec() { return rec; }

    protected void playExplosionSound() {
        SoundPlayer.playBulletExplosion();
    }

    @Override
    protected void blowUp() {
        isExploding = true;
        isAnimate = false;
        isCollidable = false;
        playExplosionSound();
    }

}
