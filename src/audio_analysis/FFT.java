package audio_analysis;

import static java.lang.Math.*;


public class FFT {

    public static Complex[] fft(Complex[] x) {
	int n = x.length;

	// base case
	if (n == 1)
	    return new Complex[] { x[0] };

	// radix 2 Cooley-Tukey FFT
	if (n % 2 != 0) {
	    throw new IllegalArgumentException("n is not a power of 2");
	}

	// fft of even terms
	Complex[] even = new Complex[n / 2];
	for (int k = 0; k < n / 2; k++) {
	    even[k] = x[2 * k];
	}
	Complex[] q = fft(even);

	// fft of odd terms
	Complex[] odd = even; // reuse the array
	for (int k = 0; k < n / 2; k++) {
	    odd[k] = x[2 * k + 1];
	}
	Complex[] r = fft(odd);

	// combine
	Complex[] y = new Complex[n];
	for (int k = 0; k < n / 2; k++) {
	    double kth = -2 * k * Math.PI / n;
	    Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
	    y[k] = q[k].plus(wk.times(r[k]));
	    y[k + n / 2] = q[k].minus(wk.times(r[k]));
	}
	return y;
    }

    public static double[] fftReal(byte[] data) {

	byte[] input = data;

	Complex[] cinput = new Complex[input.length];

	for (int i = 0; i < input.length; i++)
	    cinput[i] = new Complex(input[i], 0.0);

	Complex[] compOut = fft(cinput);

	int outSize = (int) Math.floor(compOut.length / 4);

	double[] realOut = new double[outSize];

	for (int i = 0; i < outSize; i++) {
	    realOut[i] = Math.sqrt(compOut[i].re() * compOut[i].re() + compOut[i].im() * compOut[i].im());
	}
	return realOut;
    }

    public static void main(String[] args) {

    }

}



class Complex {
    private final double re; // the real part
    private final double im; // the imaginary part

    // create a new object with the given real and imaginary parts
    public Complex(double real, double imag) {
	re = real;
	im = imag;
    }

    // return a string representation of the invoking Complex object
    public String toString() {
	if (im == 0)
	    return re + "";
	if (re == 0)
	    return im + "i";
	if (im < 0)
	    return re + " - " + (-im) + "i";
	return re + " + " + im + "i";
    }

    // return abs/modulus/magnitude
    public double abs() {
	return Math.hypot(re, im);
    }

    // return angle/phase/argument, normalized to be between -pi and pi
    public double phase() {
	return Math.atan2(im, re);
    }

    // return a new Complex object whose value is (this + b)
    public Complex plus(Complex b) {
	Complex a = this; // invoking object
	double real = a.re + b.re;
	double imag = a.im + b.im;
	return new Complex(real, imag);
    }

    // return a new Complex object whose value is (this - b)
    public Complex minus(Complex b) {
	Complex a = this;
	double real = a.re - b.re;
	double imag = a.im - b.im;
	return new Complex(real, imag);
    }

    // return a new Complex object whose value is (this * b)
    public Complex times(Complex b) {
	Complex a = this;
	double real = a.re * b.re - a.im * b.im;
	double imag = a.re * b.im + a.im * b.re;
	return new Complex(real, imag);
    }

    // return a new object whose value is (this * alpha)
    public Complex scale(double alpha) {
	return new Complex(alpha * re, alpha * im);
    }

    // return a new Complex object whose value is the conjugate of this
    public Complex conjugate() {
	return new Complex(re, -im);
    }

    // return a new Complex object whose value is the reciprocal of this
    public Complex reciprocal() {
	double scale = re * re + im * im;
	return new Complex(re / scale, -im / scale);
    }

    // return the real or imaginary part
    public double re() {
	return re;
    }

    public double im() {
	return im;
    }

    // return a / b
    public Complex divides(Complex b) {
	Complex a = this;
	return a.times(b.reciprocal());
    }

    // return a new Complex object whose value is the complex exponential of this
    public Complex exp() {
	return new Complex(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
    }

    // return a new Complex object whose value is the complex sine of this
    public Complex sin() {
	return new Complex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
    }

    // return a new Complex object whose value is the complex cosine of this
    public Complex cos() {
	return new Complex(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
    }

    // return a new Complex object whose value is the complex tangent of this
    public Complex tan() {
	return sin().divides(cos());
    }

    // a static version of plus
    public static Complex plus(Complex a, Complex b) {
	double real = a.re + b.re;
	double imag = a.im + b.im;
	Complex sum = new Complex(real, imag);
	return sum;
    }

    // See Section 3.3.
    public boolean equals(Object x) {
	if (x == null)
	    return false;
	if (this.getClass() != x.getClass())
	    return false;
	Complex that = (Complex) x;
	return (this.re == that.re) && (this.im == that.im);
    }


}
