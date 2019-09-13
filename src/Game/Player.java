package Game;

import GameObjects.Tank;
import Interfaces.Animate;
import Interfaces.Drawable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static javax.imageio.ImageIO.read;

public class Player {

    private int lives, HP;
    private boolean haveLost;

    private BufferedImage sprite;
    private Tank tank;
    private PlayerControl pControl;
    private Point spawnPoint;
    private int spawnAngle;

    Player(int lives, int HP, int x, int y, PlayerControl pControl) {

        this.lives = lives;
        this.HP = HP;
        try{ sprite = read(this.getClass().getClassLoader().getResourceAsStream("TankUpdated.png")); } catch(Exception e){}
        if( x < GameWorld.SCREEN_WIDTH/2) {
            spawnAngle = 0;
            this.tank = new Tank(sprite, x, y, 0, 0, spawnAngle, this);
        }
        else {
            spawnAngle = 180;
            this.tank = new Tank(sprite, x, y, 0, 0, spawnAngle, this);
        }
        this.pControl = pControl;
        this.pControl.setTank(tank);
        haveLost = false;
        spawnPoint = new Point(x, y);
    }

    public Tank getPlayerObj() { return tank; }
    public PlayerControl getKeyListener() { return pControl; }
    public Point getTankCoordinates() { return tank.getCoordinates(); }
    public int getHP() { return HP; }
    public int getLives() { return lives; }
    public boolean getPlayerResult() { return haveLost; }
    public void setHP( int newHP ) { HP = newHP; }

    public void takeDamage( int damage ) {
        if( HP - damage <= 0 ) {
            if( (lives - 1 ) == 0 ) {
                haveLost = true;
                GameWorld.GAME_FINISHED = true;
            }
            else {
                tank.setX(spawnPoint.x);
                tank.setY(spawnPoint.y);
                tank.respawn();
                tank.setAngle(spawnAngle);
                lives--;
                HP = 100;
            }
        }
        else {
            HP = HP - damage;
        }
    }

    public void shootAmmunition (ArrayList<Animate> animates, ArrayList<Drawable> drawables) {
        tank.shootAmmunition(animates, drawables);
    }
}
