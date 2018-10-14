/**
 * Audio Analyzer
 * 
 * Description: 
 * Real-time audio analysis with FFT and chord detection
 * 
 * 
 * Java Sound API guide:
 * https://docs.oracle.com/javase/tutorial/sound/sampled-overview.html
 * 
 * */

package audio_analysis;

import java.awt.Color;

import javax.sound.sampled.*; //handles capture and play back of audio
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.event.*;
import javax.swing.JButton;

import java.awt.Color;

public class AudioAnalyzerMain {
    public static int getMaxF(double a[]) {
	int max = 0;
	for (int i = 1; i < a.length; i++) {
	    if (a[max] < a[i])
		max = i;
	}
	return max;
    }

    public static void main(String[] args) {

	JFrame frame = new JFrame("FFT");
	JPanel p = new JPanel();
	p.setBackground(Color.RED);
	frame.add(p);
	frame.setSize(600, 400);
	frame.setVisible(true);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setResizable(false);
	JLabel l = new JLabel();
	p.add(l);
	frame.add(p);

	AudioCapture.setFormat(44100, 8);
	AudioCapture.setUp();

	byte[] input;
	int k = 0;
	while (true) {

	    input = AudioCapture.capture();

	    Complex[] cinput = new Complex[input.length];
	    for (int i = 0; i < input.length; i++)
		cinput[i] = new Complex(input[i], 0.0);

	    Complex[] test = FFT.fft(cinput);
	    double[] actual = new double[test.length];
	    
	    for (int i = 0; i < actual.length / 2; i++) {
		actual[i] = Math.sqrt(test[i].re() * test[i].re() + test[i].im() * test[i].im());
	    }
	    
	    int pos = getMaxF(actual);
	    String str = ((double) pos * 44100 / actual.length) + "    Hz";

	    
	    
	    double primaryF = pos * 44100 / actual.length;

	    float hue = 0;
	    if (primaryF != 0)
		hue = ((float) 20 / (float) primaryF); // lower frequencies have more effect on color change

	    p.setBackground(Color.getHSBColor(hue, 1, 1));
	    
	    

	    if ((k % 25) == 0) {
		l.setText(str);
		System.out.println("Primary Frequency: " + primaryF);
	    }

	    k++;
	}

    }

}
