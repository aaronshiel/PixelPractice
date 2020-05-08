package pixelpractice;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

public class run{

	//DRAWS LINE ACROSS PICTURE
	public static void mainMethod(String args[])  throws IOException{
		BufferedImage image = ImageIO.read(run.class.getResource("Blank.png"));
		
		final float imageSlope = ((float)image.getWidth()/(float)image.getHeight());
		float slopeIntercept = 0;
		slopeIntercept+=imageSlope;
		System.out.println(imageSlope);
		int rowOffSet = 0;
		
		
		for(int i = 0; i < image.getWidth(); i++) {
			if(i <= slopeIntercept) {
				image.setRGB(i,rowOffSet,-1);
			}else {
				rowOffSet++;
				image.setRGB(i,rowOffSet,-1);
				slopeIntercept+=imageSlope;
			}
		}
		
		File outputfile = new File("C:\\Users\\Desktop\\Desktop\\Blank.png");
		System.out.println(ImageIO.write(image, "png", outputfile));
		
		
		//int[][] convertedImage = convertTo2D(image);
		//System.out.println("Done");
	}
	
	private static int[][] convertTo2D(BufferedImage image) {
		final int width = image.getWidth();
		final int height = image.getHeight();
		int[][] result = new int[height][width];
		
		for(int row = 0; row < height; row ++) {
			for(int col = 0; col < width; col++) {
				result[row][col] = image.getRGB(col, row);
			}
		}
		return result;
	}
}
