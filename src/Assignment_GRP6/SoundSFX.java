package Assignment_GRP6;

import java.io.File; 
import java.io.IOException; 

import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 

public class SoundSFX {
    AudioInputStream Soundeffect;
    Clip clip;
    File file;

    static SoundSFX trailer;
    static SoundSFX coinSFX;
    static SoundSFX gas;
    static SoundSFX weaponsSFX;
    static SoundSFX titanScreamSFX;
    static SoundSFX wallsSFX;
    static SoundSFX nukeSFX;
    static SoundSFX ending;

    public SoundSFX(String filename)  {
        // check if audio file exists or not
        file = new File("./SFX/" + filename);
        if (!file.exists()) {
            System.out.println("Audio file was missing!");
            System.out.println("Remember to download the .rar from Github and unzip to AoT project directory");
        }
        
        try {
            Soundeffect = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(Soundeffect);
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Audio file format not supported!");
        } catch (LineUnavailableException e) {
            System.out.println("Line is unavailable at this moment!");
        } catch (IOException e) {
            System.out.println("Problem with file input");
        }
    }
    
    public void play(){
        clip.start();
    }
    
    public void close(){
        clip.close();
    }

    //12.00 seconds for Gas sound
    public static void GasSFX() throws InterruptedException{
        gas = new SoundSFX("GAS.wav");
        gas.play();
        Thread.sleep(12000);
        gas.close();
    }
    
    //5.00 seconds for Coin sound
    public static void CoinSFX() throws InterruptedException {
        coinSFX = new SoundSFX("COIN.wav");
        coinSFX.play();
        Thread.sleep(5000);
        coinSFX.close();
    }
    
    //5.00 seconds for Weapons sound
    public static void WeaponsSFX() throws InterruptedException {
        weaponsSFX = new SoundSFX("WEAPONS_TEST_SOUND.wav");
        weaponsSFX.play();
        Thread.sleep(5000);
        weaponsSFX.close();
    }
    
    //17 seconds for opening subtitles
    public static void displayTrailerSubtitles() throws InterruptedException { 
        String str = "";

        //first subtitle
        Thread.sleep(3680);         //3680
        str += "その時";
        str += "\nAt that moment";
        System.out.println(str);
        str = "";

        //second subtitle
        Thread.sleep(1910);         //5590
        str += "\n\n思い出した";
        str += "\nI realised";
        System.out.println(str);
        str = "";
        
        //third subtitle
        Thread.sleep(3160);         //8750
        str += "\n\nそうだ";
        System.out.println(str);
        str = "";
        
        //fourth subtitle
        Thread.sleep(1570);         //10320
        str += "\n\nこの世界は";
        str += "\nthat this world";
        System.out.println(str);
        str = "";
        
        //fifth subtitle 
        Thread.sleep(2300);         //12020
        str += "\n\n残酷なんだ";
        str += "\nwas cruel";
        System.out.println(str);
        str = "";

        //sixth subtitle 
        Thread.sleep(2900);         //14820
        str += "\n\n" + Color.colorize("ヤメロォォォォォォォォォォォォォォォォォォォォォォォォォォォォォォォォォ！", "red");
        str += "\n" + Color.colorize("STOP!!", "red");
        System.out.println(str);
        
        Thread.sleep(1480);         //17000
    }
    
    // 6 seconds for Titan Screaming
    public static void TitanSFX() throws InterruptedException {
        titanScreamSFX = new SoundSFX("TITAN_SCREAM.wav");
        titanScreamSFX.play();
        Thread.sleep(6000);
        titanScreamSFX.close();
    }
    
    // 4 seconds for Wall sound
    public static void WallSFX() throws InterruptedException {
        wallsSFX = new SoundSFX("WALL_SFX.wav");
        wallsSFX.play();
        Thread.sleep(4000);
        wallsSFX.close();
    }

    // nuke SFX
    public static void NukeSFX() throws InterruptedException{
        nukeSFX = new SoundSFX("NUKE.wav");
        nukeSFX.play();
        Thread.sleep(3750);
        AoT.clearScreen();
        Thread.sleep(9250);
        nukeSFX.close();
    }

    public static void playTrailers() throws InterruptedException {
        // start trailer 
        trailer = new SoundSFX("TRAILER.wav");
        trailer.play();
        displayTrailerSubtitles();
        
        // initiate game objects 
        
        // coin 
        Thread.sleep(1000);
        System.out.println("\nCounting the coins...");
        //coin = new Coin();
        CoinSFX();
        System.out.println(Color.colorize("OK\n", "green"));
        
        //leaderboard
        Thread.sleep(1000);
        System.out.println("Writing to leaderboards...");
        Thread.sleep(1000);
        System.out.println(Color.colorize("OK\n", "green"));
        
        //arena
        Thread.sleep(1000);
        System.out.println("Preparing the arena...");
        Thread.sleep(1000);
        System.out.println(Color.colorize("OK\n", "green"));
        
        //gas 
        Thread.sleep(1000);
        System.out.println("Pumping the gas...");
        GasSFX();
        System.out.println(Color.colorize("OK\n", "green"));
        
        //weapons
        Thread.sleep(1000);
        System.out.println("Testing the weapons...");
        WeaponsSFX();
        Thread.sleep(1000);
        System.out.println(Color.colorize("OK\n", "green"));
        
        //walls
        Thread.sleep(1000);
        System.out.println("Piling up the bricks...");
        WallSFX();
        System.out.println(Color.colorize("OK\n", "green"));
        
        //titans
        Thread.sleep(1000);
        System.out.println(Color.colorize("!!!!!!!!\n", "red"));
        TitanSFX();            
        
        //wait for Welcome screen
        Thread.sleep(2800);
        AoT.displayAsciiArt("WELCOME_SCREEN.txt", "red");
        Thread.sleep(5000);
        
        // close trailer
        trailer.close();
    }

    public static void playCredits() throws InterruptedException {
        ending = new SoundSFX("ENDING.wav");

        ending.play();
        Thread.sleep(3100);
        System.out.println(Color.colorize("!!!!!!!!\n", "red"));
        Thread.sleep(29000);
        System.out.print("か。か。。。");
        Thread.sleep(1000);
        System.out.print("壁に");
        Thread.sleep(2000);
        System.out.print("穴");
        Thread.sleep(1000);
        System.out.println("挙げられた");
        System.out.println("They blew a h-hole into the w-w-wall......");

        System.out.println("");
        Thread.sleep(7100);
        String str =  Color.colorize("いってくるぞ！\n" +
                "They're coming!", "red");
        System.out.println(str);

        System.out.println("");
        Thread.sleep(750);
        str = Color.colorize("巨人たちが入ってきた！！！！\n" +
                "THE TITANS ARE COMING!!!", "red");
        System.out.println(str);

        System.out.println("");
        Thread.sleep(6000);
        str = "ダメなんだ。。。　\n" +
                "It's all over.";
        System.out.println(str);

        System.out.println("");
        Thread.sleep(1500);
        str = "この町を\n" +
                "This town is...";
        System.out.println(str);

        System.out.println("");
        Thread.sleep(1500);
        str = "もう";
        System.out.println(str);

        System.out.println("");
        Thread.sleep(1750);
        str = Color.colorize("ブスの巨人をしてんりょうされる！！！\n" +
                "GOING TO BE OVERRUN BY TITANS!!!", "red");
        System.out.println(str);
        Thread.sleep(6000);

        ending.close();
    }
}
