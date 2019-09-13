package GameObjects;

import Interfaces.Collidable;
import Game.SoundPlayer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Missile extends Projectile {

    private boolean initialImpact;
    private Rectangle blastArea;
    private long initialImpactTimer;

    Missile(BufferedImage img, BufferedImage[] explosionImgs, int x, int y, int angle, Tank parentTank) {
        super(img, explosionImgs, x, y, angle, parentTank);
        velocity = 12;
        damage = 10;
        initialImpact = false;
        initialImpactTimer = 0;
    }

    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if(isDrawable) {
            AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
            rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
            if (!isExploding)
                g2d.drawImage(img, rotation, null);
            else {
                if(explosionCount % 2 == 0)
                  g2d.drawImage(explosionImgs[explosionCount/4], x - 60, y - 60, null);
                if (explosionCount < 23)
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
        else if( initialImpact ) {
            if( (System.currentTimeMillis() - initialImpactTimer) > 30 ) {
                initialImpact = false;
            }
            if (blastArea.intersects(obj.getRec())) {
                if (obj.isCollidable()) {
                    if (!parentTank.equals(obj)) {
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

    @Override
    protected void blowUp() {
        isExploding = true;
        isAnimate = false;
        isCollidable = false;
        initialImpact = true;
        blastArea = new Rectangle ((int) rec.getCenterX(), (int) rec.getCenterY(), 120 , 120);
        initialImpactTimer = System.currentTimeMillis();
        SoundPlayer.playMortarExplosion();
    }
}