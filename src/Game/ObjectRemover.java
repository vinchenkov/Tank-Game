package Game;

import GameObjects.GameObject;
import Interfaces.Animate;
import Interfaces.Collidable;
import Interfaces.Drawable;

import java.util.ArrayList;
import java.util.TimerTask;

public class ObjectRemover extends TimerTask {

    private ArrayList<Collidable> collidables;
    private ArrayList<Drawable> drawables;
    private ArrayList<Animate> animates;

    ObjectRemover(ArrayList<Collidable> collidables, ArrayList<Drawable> drawables, ArrayList<Animate> animates) {

        this.collidables = collidables;
        this.drawables = drawables;
        this.animates = animates;
    }
    @Override
    public void run() {
        collidables.forEach(collidable -> {
            if (collidable instanceof GameObject) {
                if(((GameObject) collidable).getNeedsDeleting() ) {
                    collidables.remove(collidable);
                }
            }
        });
        drawables.forEach(drawable -> {
            if (drawable instanceof GameObject) {
                if(((GameObject) drawable).getNeedsDeleting() ) {
                    collidables.remove(drawable);
                }
            }
        });
        animates.forEach(animate -> {
            if (animate instanceof GameObject) {
                if(((GameObject) animate).getNeedsDeleting() ) {
                    collidables.remove(animate);
                }
            }
        });
    }
}
