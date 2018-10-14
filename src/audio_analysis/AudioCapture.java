package audio_analysis;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;



public class AudioCapture {
    
    private static float sampleRate = 44100;	//samples per second 
    private static int sampleSize = 8;
    
    private static int CHANNELS = 1;		//Define the audio format
    private static boolean SIGNED = true;
    private static boolean BYTE_ORDER = true;
    
    
    private static TargetDataLine line;		//Path for moving audio into and out of a system 
    
    private static int dataCaptureSize;
    //public static ByteArrayOutputStream output  = new ByteArrayOutputStream();
    
    
    /**
     * Setter method for setting the values of the static variables
     * @param rate
     * @param bits
     */
    public static void setFormat(float rate, int bits) {
	sampleRate = rate;
	sampleSize = bits;
    }
    
    
    /**
     * Setup for opening the audio line connection and starting audio capturing
     */
    public static void setUp() {
	
	AudioFormat inputFormat = new AudioFormat(sampleRate, sampleSize, CHANNELS, SIGNED, BYTE_ORDER);	//Audio input format 
	
        DataLine.Info lineInfo = new DataLine.Info(TargetDataLine.class, inputFormat);	//Info for connecting the line
        
        
        try {  
            
            line = (TargetDataLine) AudioSystem.getLine(lineInfo);		//Line is ready to start capturing audio
            line.open(inputFormat);
        
            
            line.start();	//begin capturing data
            
            dataCaptureSize= (int) Math.pow(2, Math.floor(Math.log(line.getBufferSize()/5) / Math.log(2)));	//ensure power of two and fraction of input buffer
	
        } catch (LineUnavailableException e) {		//Try-catch block is required  
            System.err.println(e);
            return;
        }
    }
    
    
    /**
     * Returns a portion of the recorded audio input as a byte[]
     * @return buffer of captured audio points
     */
    public static byte[] capture() {
            
        byte[] data = new byte[dataCaptureSize];	
        
        int numBytesCaptured;
        numBytesCaptured = line.read(data, 0, data.length);	//it fills the data array with bytes (from 0 to length) 
        							//RETURNS NUMBER OF BYTES CAPTURED
        return data;
        
        //output.write(data, 0, numBytesCaptured);		//helpful for storing buffers (0 to length)
    }
}
