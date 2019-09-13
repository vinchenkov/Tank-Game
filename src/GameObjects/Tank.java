package GameObjects;


import Interfaces.Animate;
import Interfaces.Collidable;
import Interfaces.Drawable;
import Game.GameWorld;
import Game.Player;
import Game.SoundPlayer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static javax.imageio.ImageIO.read;

/**
 *
 *
 */
public class Tank extends GameObject implements Animate {

    private BufferedImage bulletImg;
    private BufferedImage missileImg;
    private BufferedImage scramblerImg;
    private BufferedImage[] bulletExplosion;
    private BufferedImage[] missileExplosion;
    private int vx;
    private int vy;
    private int px;
    private int py;
    private int angle;
    private int scramblerAmmo = 0;
    private int respawnTicker = 0;
    private long missileStartTime = 0;
    private long scrambleStartTime = 0;
    private long elapsedMissileTime = 0;
    private long elapsedScrambleTime = 0;
    private Point coordinates;
    private Player operatingPlayer;

    private final int R = 2;
    private final int ROTATIONSPEED = 4;

    private int fireRatePerSec = 1;
    private long timeSinceLastShot = 0;

    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean ShootPressed;
    private boolean hasMissiles;
    private boolean isScrambled;
    private boolean isRespawning;


    public Tank(BufferedImage img, int x, int y, int vx, int vy, int angle, Player operatingPlayer) {
        super(img, x, y);
        px = x;
        py = y;
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
        this.operatingPlayer = operatingPlayer;
        coordinates = new Point(x, y);
        isCollidable = true;
        isDrawable = true;
        needsDeleting = false;
        hasMissiles = false;
        bulletExplosion = new BufferedImage[6];
        missileExplosion = new BufferedImage[6];

        try {
            bulletImg = read( this.getClass().getClassLoader().getResourceAsStream("bullet.png"));
            missileImg = read( this.getClass().getClassLoader().getResourceAsStream("missile2v1.png"));
            scramblerImg = read( this.getClass().getClassLoader().getResourceAsStream("scramblerBullet.png"));
            bulletExplosion[0] = read( this.getClass().getClassLoader().getResourceAsStream("explosion1_1.png"));
            bulletExplosion[1] = read( this.getClass().getClassLoader().getResourceAsStream("explosion1_2.png"));
            bulletExplosion[2] = read( this.getClass().getClassLoader().getResourceAsStream("explosion1_3.png"));
            bulletExplosion[3] = read( this.getClass().getClassLoader().getResourceAsStream("explosion1_4.png"));
            bulletExplosion[4] = read( this.getClass().getClassLoader().getResourceAsStream("explosion1_5.png"));
            bulletExplosion[5] = read( this.getClass().getClassLoader().getResourceAsStream("explosion1_6.png"));
            missileExplosion[0] = read( this.getClass().getClassLoader().getResourceAsStream("explosion2_1.png"));
            missileExplosion[1] = read( this.getClass().getClassLoader().getResourceAsStream("explosion2_2.png"));
            missileExplosion[2] = read( this.getClass().getClassLoader().getResourceAsStream("explosion2_3.png"));
            missileExplosion[3] = read( this.getClass().getClassLoader().getResourceAsStream("explosion2_4.png"));
            missileExplosion[4] = read( this.getClass().getClassLoader().getResourceAsStream("explosion2_5.png"));
            missileExplosion[5] = read( this.getClass().getClassLoader().getResourceAsStream("explosion2_6.png"));
        } catch(Exception e) {}
    }


    public void toggleUpPressed() { UpPressed = true; }
    public void toggleDownPressed() { DownPressed = true; }
    public void toggleRightPressed() { RightPressed = true; }
    public void toggleLeftPressed() { LeftPressed = true; }
    public void toggleShootPressed() { ShootPressed = true; }
    public void unToggleUpPressed() { UpPressed = false; }
    public void unToggleDownPressed() { DownPressed = false; }
    public void unToggleRightPressed() { RightPressed = false; }
    public void unToggleLeftPressed() { LeftPressed = false; }
    public void unToggleShootPressed() { ShootPressed = false; }
    public void setAngle (int angle) { this.angle = angle; }
    public void setMissileStartTime() { missileStartTime = System.currentTimeMillis(); }
    public void setHasMissiles( boolean hasMissiles ) { this.hasMissiles = hasMissiles; }
    public void setScrambled( boolean isScrambled ) { this.isScrambled = isScrambled; }
    public void setScramblerAmmo( int scramblerAmmo ) { this.scramblerAmmo = scramblerAmmo;}
    public void setScrambleStartTime() { scrambleStartTime = System.currentTimeMillis(); }

    public void shootAmmunition (ArrayList<Animate> animates, ArrayList<Drawable> drawables) {
        if (this.ShootPressed) {
            if(!hasMissiles) {
                if ((System.currentTimeMillis() - timeSinceLastShot) >= 1000 / fireRatePerSec / 2 ) {
                    if(scramblerAmmo > 0 ) {
                        SoundPlayer.playBulletSound();
                        Scrambler newScrambler = this.addScrambler();
                        animates.add(newScrambler);
                        drawables.add(newScrambler);
                        timeSinceLastShot = System.currentTimeMillis();
                        scramblerAmmo--;
                    }
                    else {
                        SoundPlayer.playBulletSound();
                        Bullet newBullet = this.addBullet();
                        animates.add(newBullet);
                        drawables.add(newBullet);
                        timeSinceLastShot = System.currentTimeMillis();
                    }
                }
            }
            else {
                if ((System.currentTimeMillis() - timeSinceLastShot) >= 1000 / fireRatePerSec * 2) {
                    SoundPlayer.playMortarSound();
                    Missile newMissile = this.addMissile();
                    animates.add(newMissile);
                    drawables.add(newMissile);
                    timeSinceLastShot = System.currentTimeMillis();
                }
            }
        }
    }
    private void rotateLeft() {
        if(!isScrambled) {
            angle -= this.ROTATIONSPEED;
        }
        else {
            angle += this.ROTATIONSPEED;
        }
    }

    private void rotateRight() {
        if(!isScrambled) {
            angle += this.ROTATIONSPEED;
        }
        else {
            angle -= this.ROTATIONSPEED;
        }
    }

    private void moveBackwards() {
        if(!isRespawning) {
            vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
            vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        }
        else {
            vx = (int) Math.round(R * 2 * Math.cos(Math.toRadians(angle)));
            vy = (int) Math.round(R * 2 *  Math.sin(Math.toRadians(angle)));
        }
        px = x;
        py = y;
        if(!isScrambled) {
            x -= vx;
            y -= vy;
        }
        else {
            x += vx;
            y += vy;
        }
        rec.x = x;
        rec.y = y;
        checkBorder();
    }

    private void moveForwards() {
        if(!isRespawning) {
            vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
            vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        }
        else {
            vx = (int) Math.round(R * 2 * Math.cos(Math.toRadians(angle)));
            vy = (int) Math.round(R * 2 *  Math.sin(Math.toRadians(angle)));
        }
        px = x;
        py = y;
        if(!isScrambled) {
            x += vx;
            y += vy;
        }
        else {
            x -= vx;
            y -= vy;
        }
        rec.x = x;
        rec.y = y;
        checkBorder();
    }


    public void checkBorder() {
        if (x < 30) {
            x = 30;
            rec.x = x;
        }
        if (x >= GameWorld.GAME_WIDTH - 88) {
            x = GameWorld.GAME_WIDTH - 88;
            rec.x =x;
        }
        if (y < 36) {
            y = 36;
            rec.y =y;
        }
        if (y >= GameWorld.GAME_HEIGHT - 80) {
            y = GameWorld.GAME_HEIGHT - 80;
            rec.y =y;
        }
    }

    public void respawn() {
        isRespawning = true;
        isScrambled = false;
        hasMissiles = false;
        scramblerAmmo = 0;
        isCollidable = false;
    }

    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        if (!isRespawning) {
            g2d.drawImage(img, rotation, null);
        }
        else {
            if (respawnTicker % 5 == 0)
                g2d.drawImage(img, rotation, null);
            if (respawnTicker <= 180)
                respawnTicker++;
            else {
                respawnTicker = 0;
                isRespawning = false;
                isCollidable = true;
            }
        }
    }


    @Override
    public void updatePosition() {
        if (this.UpPressed) {
            this.moveForwards();
        }
        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }
        if(hasMissiles) {
            elapsedMissileTime = System.currentTimeMillis() - missileStartTime;
            if(elapsedMissileTime > 20000) {
                hasMissiles = false;
            }
        }
        if(isScrambled) {
            elapsedScrambleTime = System.currentTimeMillis() - scrambleStartTime;
            if(elapsedScrambleTime > 10000) {
                isScrambled = false;
            }
        }
    }

    @Override
    public void checkCollision(Collidable obj) {
        if (rec.intersects(obj.getRec())) {
            if (obj.isCollidable()) {
                if (obj instanceof SolidWall || obj instanceof BreakableWall || obj instanceof Tank) {
                    if (!this.equals(obj)) {
                        if (obj.isCollidable()) {
                            x = px;
                            y = py;
                        }
                    }
                } else if (obj instanceof PowerUp) {
                    ((PowerUp) obj).consumePowerUp(this);
                }
            }
        }
    }
    public Player getPlayer() { return operatingPlayer; }

    @Override
    public Rectangle getRec() {
        return rec;
    }

    @Override
    public boolean isCollidable() {
        return isCollidable;
    }

    public Point getCoordinates() {
        coordinates.x = x;
        coordinates.y = y;
        return coordinates;
    }

    private Bullet addBullet() { return new Bullet(bulletImg, bulletExplosion, x, y, angle, this); }
    private Missile addMissile() { return new Missile(missileImg, missileExplosion, x, y, angle, this); }
    private Scrambler addScrambler() { return new Scrambler(scramblerImg, bulletExplosion, x, y, angle, this); }

}
