import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

//image.setRGB
//image.getRGB

public class grayscale_image {

	public static void main(String args[]) throws IOException {
	    long start = System.nanoTime();
		ImageWrapper imageWrapper = new ImageWrapper(grayscale_image.class.getResource("selfie.jpg"));
		
		imageWrapper.Grayscale();
		imageWrapper.GaussianBlurAttempt(25);
		
		imageWrapper.CreateEdges(9);
		
		
		File outputfile = new File("C:\\Users\\Desktop\\Desktop\\selfie_modified.jpg");
		
		System.out.println(ImageIO.write(imageWrapper.GetModified(), "jpg", outputfile));
		System.out.println((System.nanoTime() - start) / 1000000000.0  + " seconds!");
	}

}
