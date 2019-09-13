package GameObjects;

import Interfaces.Animate;
import Interfaces.Collidable;
import Game.GameWorld;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Projectile extends GameObject implements Animate {
    protected Tank parentTank;
    protected BufferedImage[] explosionImgs;
    protected boolean isExploding;
    protected boolean isAnimate;
    protected int explosionCount;
    protected int angle;
    protected int velocity;
    protected int damage;

    Projectile(BufferedImage img, BufferedImage[] explosionImgs, int x, int y, int angle, Tank parentTank) {
        super(img, (x + 28 + (int) Math.round(16 * Math.cos(Math.toRadians(angle)))), ( y + 26 + (int) Math.round(16 * Math.sin(Math.toRadians(angle)))));
        this.explosionImgs = explosionImgs;
        this.angle = angle;
        this.parentTank = parentTank;
        explosionCount = 0;
        isCollidable = true;
        isDrawable = true;
        isAnimate = true;
        isExploding = false;
    }

    public void checkBorder() {
        if (x < 30) {
            x = 30;
            rec.x = x;
            blowUp();
        }
        if (x >= GameWorld.GAME_WIDTH - 44) {
            x = GameWorld.GAME_WIDTH - 44;
            rec.x =x;
            blowUp();
        }
        if (y < 32) {
            y = 32;
            rec.y =y;
            blowUp();
        }
        if (y >= GameWorld.GAME_HEIGHT - 44) {
            y = GameWorld.GAME_HEIGHT - 44;
            rec.y =y;
            blowUp();
        }
    }

    public void drawImage(Graphics g) {

    }


    @Override
    public void updatePosition() {
        if(isAnimate) {
            int vx = (int) Math.round(velocity * Math.cos(Math.toRadians(angle)));
            int vy = (int) Math.round(velocity * Math.sin(Math.toRadians(angle)));
            x += vx;
            y += vy;
            rec.x = x;
            rec.y = y;
            checkBorder();
        }
    }

    @Override
    public void checkCollision(Collidable obj) {

    }

    @Override
    public boolean isCollidable() {
        return isCollidable;
    }

    @Override
    public Rectangle getRec() { return rec; }

    protected abstract void blowUp();

}
