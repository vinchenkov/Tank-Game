package GameObjects;

import Interfaces.Collidable;
import Interfaces.Drawable;

import java.awt.*;

import java.awt.image.BufferedImage;

public abstract class GameObject implements Drawable, Collidable {
    protected int x, y, width, height;
    protected BufferedImage img;
    protected Rectangle rec;
    protected boolean isCollidable;
    protected boolean isDrawable;
    protected boolean needsDeleting;

    public GameObject(BufferedImage img, int x, int y){
        this.img = img;
        this.x = x;
        this.y = y;
        width = img.getWidth(null);
        height = img.getHeight(null);
        rec = new Rectangle (x, y, img.getWidth(), img.getHeight());
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public boolean getNeedsDeleting() { return needsDeleting; }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    public void setX(int a){
        x = a;
        rec.x = a;
    }

    public void setY(int b){
        y = b;
        rec.y = b;
    }

}
