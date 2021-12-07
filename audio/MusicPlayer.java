package audio;

import java.util.*;
import java.io.*;
import javax.sound.sampled.*;

public class MusicPlayer implements Runnable {

    private String filePath;

    public MusicPlayer(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        AudioInputStream as;
        try {
            as = AudioSystem.getAudioInputStream(new File(filePath));// 音频文件在项目根目录的img文件夹下
            AudioFormat format = as.getFormat();
            SourceDataLine sdl = null;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            sdl = (SourceDataLine) AudioSystem.getLine(info);
            sdl.open(format);
            sdl.start();
            int nBytesRead = 0;
            byte[] abData = new byte[512];
            while (nBytesRead != -1) {
                nBytesRead = as.read(abData, 0, abData.length);
                if (nBytesRead >= 0)
                    sdl.write(abData, 0, nBytesRead);
            }
            // 关闭SourceDataLine
            sdl.drain();
            sdl.close();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
