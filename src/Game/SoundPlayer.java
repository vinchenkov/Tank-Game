package Game;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.InputStream;

public class SoundPlayer {

    public static Clip clip1;
    public static Clip clip2;
    public static Clip clip3;
    public static Clip clip4;

    private static InputStream bulletShot;
    private static InputStream bulletExplosion;
    private static InputStream mortarShot;
    private static InputStream mortarExplosion;

    private SoundPlayer () {}

    public static void init() {

        try {
            bulletShot = SoundPlayer.class.getClassLoader().getResourceAsStream("bulletShot.wav");
            clip1 = AudioSystem.getClip();
            clip1.open(AudioSystem.getAudioInputStream(bulletShot));

            bulletExplosion = SoundPlayer.class.getClassLoader().getResourceAsStream("bulletExplosion.wav");
            clip2 = AudioSystem.getClip();
            clip2.open(AudioSystem.getAudioInputStream(bulletExplosion));

            mortarShot = SoundPlayer.class.getClassLoader().getResourceAsStream("mortarShot.wav");
            clip3 = AudioSystem.getClip();
            clip3.open(AudioSystem.getAudioInputStream(mortarShot));

            mortarExplosion = SoundPlayer.class.getClassLoader().getResourceAsStream("mortarExplosion.wav");
            clip4 = AudioSystem.getClip();
            clip4.open(AudioSystem.getAudioInputStream(mortarExplosion));
        }
        catch (Exception e) { }
    }

    public static void playBulletSound() {
        try {
            clip1.flush();
            clip1.setFramePosition(0);
            clip1.start();
        }
        catch (Exception e) { }
    }
    public static void playBulletExplosion() {
        try {
            clip2.flush();
            clip2.setFramePosition(0);
            clip2.start();
        }
        catch (Exception e) { }
    }
    public static void playMortarSound() {
        try {
            clip3.flush();
            clip3.setFramePosition(0);
            clip3.start();
        }
        catch (Exception e) { }
    }
    public static void playMortarExplosion() {
        try {
            clip4.flush();
            clip4.setFramePosition(0);
            clip4.start();
        }
        catch (Exception e) { }
    }
}
