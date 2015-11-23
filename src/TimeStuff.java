
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class TimeStuff {
    private boolean timing;
    private long startTime;
    private String msg;
    private static TimeStuff mainTimer;
    
    //Wrappers for mainTimer. Use these for timing operations
    public static void initTimer(){ //Start a new Timer (i.e. with a new message)
        if(mainTimer == null) mainTimer = new TimeStuff();
    }
    public static void startTimer(){
        if(mainTimer == null) return;
        mainTimer._startTimer();
    }
    public static long stopTimer(String m){
        if(mainTimer == null) return -1;
        return mainTimer._stopTimer(m);
    }
    public static void saveMessageToFile(String filename){
        if(mainTimer == null) { return;}
        mainTimer._saveMessageToFile(filename);
    }
    
    //Timer operations, use the static versions instead (because I'm lazy)
    public TimeStuff(){
        timing = false;
        msg = "";
    }
    public void _startTimer(){
        timing = true;
        startTime = System.nanoTime();
    }
    public long _stopTimer(String message){
        if(timing){
            timing = false;
            long result = System.nanoTime() - startTime;
            msg += message + " " + result + "ns,";
            return result;
        } else {
            return -1;
        }
    }
    public void _saveMessageToFile(String fileName){
        /*
        try {
            PrintWriter writer;
            writer = new PrintWriter(fileName, "UTF-8");
            String[] strs = msg.split(",");
            for(String s: strs){
                writer.println(s);
            }
//            writer.println(msg);
            writer.close();
        } 
        catch (FileNotFoundException ex) { System.out.println("File not found!"); } 
        catch (UnsupportedEncodingException ex) {}
        */
        
        File log = new File("log.txt");

        try{
            if(!log.exists()){
                System.out.println("We had to make a new file.");
                log.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(log, true);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String[] strs = msg.split(",");
            for(String s: strs){
                bufferedWriter.write(s);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch(IOException e) {
            System.out.println("COULD NOT LOG!!");
        }
    }
}
