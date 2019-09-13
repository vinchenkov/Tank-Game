package Game;

import Interfaces.Animate;
import Interfaces.Collidable;
import Interfaces.Drawable;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static javax.imageio.ImageIO.read;

public class GameWorld extends JPanel {

    public static final int SCREEN_WIDTH = 960;
    public static final int SCREEN_HEIGHT = 600;
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;
    public static int GAME_WIDTH;
    public static int GAME_HEIGHT;
    public static boolean GAME_FINISHED;

    private ArrayList<Drawable> drawables;
    private ArrayList<Collidable> collidables;
    private ArrayList<Animate> animates;

    private TileMap tileMap = new TileMap();
    private BufferedImage world;
    private BufferedImage background;
    private BufferedImage heart;
    private Graphics2D buffer;
    private JFrame jf;
    private Player p1;
    private Player p2;
    private PlayerControl p1Control;
    private PlayerControl p2Control;
    private Point splitScreenCoordinate1;
    private Point splitScreenCoordinate2;


    public static void main(String[] args) {
        Thread x;
        GameWorld gameWorld = new GameWorld();
        gameWorld.init();
        //Timer timer = new Timer();
        //TimerTask objRemover = new Game.ObjectRemover( gameWorld.collidables, gameWorld.drawables, gameWorld.animates );
        //timer.schedule(objRemover, 3000, 1000 );
        try {
            while (!GAME_FINISHED) {
                gameWorld.checkCollisions();
                gameWorld.updatePositions();
                gameWorld.addNewObjects();
                gameWorld.repaint();
                Thread.sleep(1000 / 90);
            }
        } catch (InterruptedException ignored) { }

        try {
            for (int i = 0; i < 450; i++) {
                gameWorld.checkCollisions();
                gameWorld.updatePositions();
                gameWorld.repaint();
                Thread.sleep(1000 / 90);
            }
        } catch(InterruptedException ignored) { }
    }


    private void init() {

        GAME_FINISHED = false;

        world = new BufferedImage(GameWorld.SCREEN_WIDTH, GameWorld.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);

        try {
            /*
             * note class loaders read files from the out folder (build folder in netbeans) and not the
             * current working directory.
             */
            world= read( this.getClass().getClassLoader().getResourceAsStream("tankbg.bmp"));
            background = read( this.getClass().getClassLoader().getResourceAsStream("tankbg.bmp"));
            heart = read( this.getClass().getClassLoader().getResourceAsStream("heart.png"));
            buffer = world.createGraphics();

            GAME_HEIGHT = world.getHeight();
            GAME_WIDTH = world.getWidth();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        drawables = new ArrayList<>();
        animates = new ArrayList<>();
        collidables = new ArrayList<>();

        p1Control = new PlayerControl(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        p2Control = new PlayerControl(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);

        p1 = new Player(3, 100, 33, 33, p1Control);
        p2 = new Player(3, 100, GAME_WIDTH - 100, GAME_HEIGHT - 100, p2Control);

        splitScreenCoordinate1 = new Point();
        splitScreenCoordinate2 = new Point();

        SoundPlayer.init();

        tileMap.loadTiles(drawables, collidables);

        drawables.add(p1.getPlayerObj());
        animates.add(p1.getPlayerObj());
        collidables.add(p1.getPlayerObj());
        drawables.add(p2.getPlayerObj());
        animates.add(p2.getPlayerObj());
        collidables.add(p2.getPlayerObj());

        jf = new JFrame("Tank Game");

        jf.setLayout(new BorderLayout());
        jf.add(this);
        jf.addKeyListener(p1.getKeyListener());
        jf.addKeyListener(p2.getKeyListener());
        jf.setSize(GameWorld.SCREEN_WIDTH, GameWorld.SCREEN_HEIGHT + 30);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }


    public void updatePositions() {
        animates.forEach((animate) -> {
            animate.updatePosition();
        });
    }


    public void addNewObjects() {
        p1.shootAmmunition(animates, drawables);
        p2.shootAmmunition(animates, drawables);
    }

    public void checkCollisions() {
        animates.forEach((animate) ->  {
            collidables.forEach((collidable) -> {
                animate.checkCollision(collidable);
            });
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);

        buffer.drawImage(background, 0, 0, null);
        drawables.forEach((drawable) -> {
            drawable.drawImage(buffer);
        });

       getSplitScreenCoordinates(p1.getTankCoordinates(), splitScreenCoordinate1);
       getSplitScreenCoordinates(p2.getTankCoordinates(), splitScreenCoordinate2);

       BufferedImage split1 = world.getSubimage(splitScreenCoordinate1.x, splitScreenCoordinate1.y, SCREEN_WIDTH/2, SCREEN_HEIGHT);
       BufferedImage split2 = world.getSubimage(splitScreenCoordinate2.x, splitScreenCoordinate2.y, SCREEN_WIDTH/2, SCREEN_HEIGHT);
       BufferedImage mm = world.getSubimage(0, 0, GAME_WIDTH, GAME_HEIGHT);

       g2.drawImage(split1,0,0,null);
       g2.drawImage(split2, SCREEN_WIDTH/2, 0, null);

       g.setColor(Color.GREEN);
       g2.fillRect(160, 550, p1.getHP(), 25);
       g2.fillRect(640, 550, p2.getHP(), 25);

       g2.setColor(Color.BLACK);
       g2.drawRect(0, 0, SCREEN_WIDTH/2, SCREEN_HEIGHT);

       g2.setColor(Color.WHITE);
       g2.drawString("P1", 135, 570);
       g2.drawString("P2", 615, 570);

       for( int i = 0; i < p1.getLives() - 1; i++) {
           g2.drawImage(heart, 300  + i * 32, 550, null);
       }
        for( int i = 0; i < p2.getLives() - 1; i++) {
            g2.drawImage( heart, 780 + i * 32, 550, null);
        }
       g2.drawImage(mm, 400, 480, 160, 100, null );


       if(GAME_FINISHED) {
           g2.setColor(Color.WHITE);
           g2.setFont(new Font(null, Font.BOLD, 50));

           if(p1.getPlayerResult()) {
               g2.drawString("PLAYER 2 YOU WIN", 250, SCREEN_HEIGHT/2);
           }
           else {
               g2.drawString("PLAYER 1 YOU WIN", 250, SCREEN_HEIGHT/2);
           }
       }
       super.paintComponent(buffer);
    }


    private void getSplitScreenCoordinates(Point point, Point newPoint) {

        int x = point.x;
        int y = point.y;

        x = x - SCREEN_WIDTH/4;
        if( x < 0 ) {
            x = 0;
        }
        else if( x > (GAME_WIDTH - SCREEN_WIDTH/2) ) {
            x = GAME_WIDTH - SCREEN_WIDTH/2;
        }

        y = y - SCREEN_HEIGHT/4;
        if( y < 0) {
            y = 0;
        }
        else if( y > (GAME_HEIGHT - SCREEN_HEIGHT )) {
            y = GAME_HEIGHT - SCREEN_HEIGHT;
        }

        newPoint.x = x;
        newPoint.y = y;
    }
}
