package VideoGame;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundManager {
    public static final Clip music = getClip("Super Mario Bros. Theme Song");

    public SoundManager() {

    }

    public static void main(String[] args) throws Exception {
        play(music);

        Thread.sleep(1000);
    }

    public static void play(Clip clip) {
        clip.setFramePosition(0);
        clip.start();
    }

    private static Clip getClip(String filename) {
        Clip clip = null;

        try {
            clip = AudioSystem.getClip();
            AudioInputStream sample = AudioSystem.getAudioInputStream(new File("Sounds/" + filename + ".wav"));
            clip.open(sample);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return clip;
    }
}
