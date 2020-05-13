import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class ImageWrapper {
	
	private BufferedImage original, modifiedImage, copy;
	
	final private int width;
	final private int height;
	private int[][] colorPallete;
	final private URL fileHold;
	
	//TODO: Add the sobel operator function!!!!
	//TODO: Be smart about where we see the X and Y of each edge
	//TODO: Need that so we can find direction and stuff
	
	
	
	public ImageWrapper(URL file) throws IOException {
		fileHold = file;
		modifiedImage = ImageIO.read(file);
		width = modifiedImage.getWidth();
		height = modifiedImage.getHeight();
		colorPallete = new int[height][width];
		colorPallete = ConvertTo2D();
	}
	
	
	//TODO: Create a new image of same height and width as original
	//	    and then just fill it in as we traverse the original
	public void CreateEdges(int intensity) {
		//to be sure that we can do the divide 3 tactic
		if(intensity < 9)
			intensity = 9;
		while((intensity % 3) != 0) {
			intensity++;
		}
		
		class EdgeStruct{
			public int xStrength;
			public int yStrength;
			public int pixelDirection;
			public EdgeStruct(int x, int y) {
				xStrength = x;
				yStrength = y;
				pixelDirection = (int)Math.atan(x/y);
			}
			
			public int getXStrength() {
				return xStrength;
			}
		}
		

		//TODO: APPLY SurroundingPixels2D for each pixel from colorPallete to XEdgeIntensityKernel
		// and YEdgeIntensityKernel. Add each returning value into the constructor of EdgeStruct
		EdgeStruct edgeProperties[][] = new EdgeStruct[height][width];
		
		//i = height, j = width
		for(int i = (intensity/2); i < height-(intensity/2); i++) {
			for(int j = (intensity/2); j < width-(intensity/2); j++) {
				//edgeProperties[i][j] = EdgeStruct(XEdgeIntensityKernel(), YEdgeIntensityKernel());
				edgeProperties[i][j] = new EdgeStruct(XEdgeIntensityKernel(SurroundingPixels2D(i, j, intensity), intensity, intensity), 1);
			}
		}
		
		//TODO: LEFT OFF HERE, all values in edgeProperties are negative for some reason
		for(int i = 0; i < 1000; i++) {
			if(edgeProperties[i][i]!= null)
		System.out.println(edgeProperties[i][i].getXStrength());
		}
		//TODO: This is the image we will draw on
		//BufferedImage edgeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}
	
	//TODO: Watch video again to see how we should be coloring the edges
	//TODO: scale the values to be between -255 <-> 255 in order to be black and white edges
	//TODO: Create YEdgeIntensityKernel
	//return a value depicting how strongly white or black to color the edge
	public int XEdgeIntensityKernel(int[][] colors, int height, int width) {
		//ADDS FROM THE TOP DOWN
		int leftIntensity = 0;
		int rightIntensity = 0;
		int modifier = width/3;
		int powerModifier = 0;
		for(int i = 0; i < width-1; i++) {
			powerModifier = 0;
			for(int j = 0; j < height-1; j++) {
				//we are within the ignored block
				if(i >= modifier && i < (width - modifier))
					break;
				//if we are approaching width/2
				if(j <= width/2 && j != 0)
					powerModifier++;
				if(j > width/2 && j != 0)
					powerModifier--;
				if(i < modifier)
					leftIntensity -= colors[j][i] * Math.pow(2, powerModifier);
				if(i >= (width - modifier))
					rightIntensity += colors[j][i] * Math.pow(2, powerModifier);
			}
		}
		return(leftIntensity + rightIntensity);
	}
	
	//takes photo of this class and grayscales it
	public void Grayscale() {
		for(int i=0; i <height; i++) {
			for(int j = 0; j < width; j++) {
				Color c = new Color(modifiedImage.getRGB(j, i));
				int red = (int)(c.getRed() * 0.299);
				int green = (int)(c.getGreen() * 0.587);
				int blue = (int)(c.getBlue() * 0.114);
				int gray = red+green+blue;
				Color newColor = new Color(gray,gray,gray);
				modifiedImage.setRGB(j, i, newColor.getRGB());
				colorPallete[i][j] = gray;
			}
		}
	}
	
	//i = height, j = width
	//blurIntensity decides the width of the kernel
	public void GaussianBlurAttempt(int blurIntensity) {
		if((blurIntensity % 2) == 0)
			blurIntensity++;
		for(int i = (blurIntensity/2); i < height-(blurIntensity/2); i++) {
			for(int j = (blurIntensity/2); j < width-(blurIntensity/2); j++) {
				int newColor = Kernel(SurroundingPixels(i, j, blurIntensity));
				Color newRGB = new Color(newColor, newColor, newColor);
				modifiedImage.setRGB(j, i, newRGB.getRGB());
				colorPallete[i][j] = newRGB.getRed();
			}
		}
	}
	
	
	//i = height, j = width, kernelSize is the width of the kernel
	public int[] SurroundingPixels(int i, int j, int kernelSize) {
		int[] toReturn = new int[(kernelSize*kernelSize)];
		int toReturnIncrement = 0;
		//k = height, l = width
		for(int k = -(kernelSize/2); k <= (kernelSize/2); k++) {
			for(int l = -(kernelSize/2); l <= (kernelSize/2); l++) {
				toReturn[toReturnIncrement] = colorPallete[i+k][j+l];
				toReturnIncrement++;
			}
		}
		
		return toReturn;
	}
	
	//i = height, j = width, kernelSize is the width of the kernel
	public int[][] SurroundingPixels2D(int i, int j, int kernelSize) {
		int[][] toReturn = new int[kernelSize][kernelSize];
		int incrementX = 0;
		int incrementY = 0;
		//k = height, l = width
		for(int k = -(kernelSize/2); k <= (kernelSize/2); k++) {
			for(int l = -(kernelSize/2); l <= (kernelSize/2); l++) {
				toReturn[incrementY][incrementX] = colorPallete[i+k][j+l];
				incrementX++;
			}
			incrementX = 0;
			incrementY++;
		}
		
		return toReturn;
	}
	
	//intakes pixel RGB values and returns what value to make the target pixel
	public int Kernel(int...nums) {
		int kernelDivisor = 0;
		int kernelNumerator = 0;
		//TODO: make this automatic, not hardcoded
		int[] kernel = new int[nums.length];
		Arrays.fill(kernel, 1);
		
		for(int n: kernel) {
			kernelDivisor += n;
		}
		for(int i = 0; i < nums.length-1; i++) {
	
			kernelNumerator += (nums[i] * kernel[i]);
		}
		int toReturn = (int)Math.ceil((double)kernelNumerator/(double)kernelDivisor);
		return toReturn;
	}

	//TODO: Check if colorPallete is offset by 1? because it looks a little dif
	public int[][] ConvertTo2D() {
		int[][] colors = new int[height][width];
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				colors[i][j] = new Color(modifiedImage.getRGB(j, i)).getRed();
			}
		}
		return colors;
	}
	
	public BufferedImage GetModified() {
		return modifiedImage;
	}
	
	
}
