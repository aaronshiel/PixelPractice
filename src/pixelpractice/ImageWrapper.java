package pixelpractice;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageWrapper {
	private BufferedImage original, modifiedImage, copy;
	
	final private int width;
	final private int height;
	
	//TODO: Add the sober operator function!!!!
	//TODO: Be smart about where we see the X and Y of each edge
	//TODO: Need that so we can find direction and stuff
	
	public ImageWrapper(URL file) throws IOException {
		//TODO: not need so many copies
		modifiedImage = ImageIO.read(file);
		original = ImageIO.read(file);
		copy = ImageIO.read(file);
		width = modifiedImage.getWidth();
		height = modifiedImage.getHeight();
	}
	
	//takes photo of this class and grayscales it
	public void Grayscale() {
		for(int i=0; i <height; i++) {
			for(int j = 0; j < width; j++) {
				Color c = new Color(modifiedImage.getRGB(j, i));
				int red = (int)(c.getRed() * 0.299);
				int green = (int)(c.getGreen() * 0.587);
				int blue = (int)(c.getBlue() * 0.114);
				Color newColor = new Color(red+green+blue,red+green+blue,red+green+blue);
				modifiedImage.setRGB(j, i, newColor.getRGB());
				copy.setRGB(j, i, newColor.getRGB());
			}
		}
	}
	
	//takes modifiedImage and Gaussian blurs it
	public void GaussianBlurCorrect() {
		float[] matrix = new float[400];
		for(int i =0; i < 400 ;i++) {
			matrix[i] = 1.0f/400.0f;
		}
		BufferedImageOp op = new ConvolveOp(new java.awt.image.Kernel(20, 20, matrix));
		modifiedImage = op.filter(copy, modifiedImage);
	}
	
	//i = height, j = width
	//blurIntensity decides the width of the kernel
	public void GaussianBlurAttempt(int blurIntensity) {
		for(int i = (blurIntensity/2); i < height-(blurIntensity/2); i++) {
			for(int j = (blurIntensity/2); j < width-(blurIntensity/2); j++) {
				int newColor = Kernel(SurroundingPixels(i, j, blurIntensity));
				modifiedImage.setRGB(j, i, new Color(newColor, newColor, newColor).getRGB());
			}
		}
	}
	
	//i = height, j = width
	public int[] SurroundingPixels(int i, int j, int kernelSize) {
		int[] toReturn = new int[(kernelSize*kernelSize)];
		int toReturnIncrement = 0;
		//k = height, l = width
		for(int k = -(kernelSize/2); k <= (kernelSize/2); k++) {
			for(int l = -(kernelSize/2); l <= (kernelSize/2); l++) {
				toReturn[toReturnIncrement] = new Color(modifiedImage.getRGB(j+l, i+k)).getRed();
				toReturnIncrement++;
			}
		}
		
		return toReturn;
	}
	
	//intakes pixel RGB values and returns what value to make the target pixel
	//assumes a 3x3 kernel
	public int Kernel(int...nums) {
		int kernelDivisor = 0;
		int kernelNumerator = 0;
		//TODO: make this automatic, not hardcoded
		int[] kernel = new int[nums.length];
		for(int i = 0; i < nums.length;i++)
			kernel[i] = 1;
		
		//System.out.println("Before Value: " + nums[4]);
		//create the divisor
		for(int n: kernel) {
			kernelDivisor += n;
		}
		
		
		for(int i = 0; i < nums.length-1; i++) {
			//System.out.println("nums[ " + i + "] = " + nums[i] + "kernel[ " + i + "] = " + kernel[i]);
			kernelNumerator += (nums[i] * kernel[i]);
		}
		//System.out.println("Numerator: " + kernelNumerator + " denominator: " + kernelDivisor);
		int toReturn = (int)Math.ceil((double)kernelNumerator/(double)kernelDivisor);
		//System.out.println("After Value: " + toReturn);
		return toReturn;
	}
	
	public void PrintValues() {
		for(int i=0; i <1; i++) {
			for(int j = 0; j < width; j++) {
				System.out.print(modifiedImage.getRGB(j, i) + " ");
			}
			System.out.println();
		}
	}
	
	public BufferedImage GetOriginal() {
		return original;
	}
	
	public BufferedImage GetModified() {
		return modifiedImage;
	}
}
