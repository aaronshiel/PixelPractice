package pixelpractice;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//image.setRGB
//image.getRGB

public class grayscale_image {

	
	
	public static void main(String args[]) throws IOException {
		ImageWrapper imageWrapper = new ImageWrapper(run.class.getResource("selfie.jpg"));
		imageWrapper.Grayscale();
		//blur intensity must be an odd number
		imageWrapper.GaussianBlurAttempt(13);
		
		File outputfile = new File("C:\\Users\\sprin\\Desktop\\selfie_modified.jpg");
		System.out.println(ImageIO.write(imageWrapper.GetModified(), "jpg", outputfile));
	}

}
