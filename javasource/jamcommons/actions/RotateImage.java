// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package jamcommons.actions;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
import com.mendix.core.Core;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.webui.CustomJavaAction;

/**
 * Rotate an image and writes the image to a new object
 */
public class RotateImage extends CustomJavaAction<java.lang.Boolean>
{
	private IMendixObject __originalImage;
	private system.proxies.Image originalImage;
	private IMendixObject __resizedImage;
	private system.proxies.Image resizedImage;
	private java.lang.Long angle;

	public RotateImage(IContext context, IMendixObject originalImage, IMendixObject resizedImage, java.lang.Long angle)
	{
		super(context);
		this.__originalImage = originalImage;
		this.__resizedImage = resizedImage;
		this.angle = angle;
	}

	@java.lang.Override
	public java.lang.Boolean executeAction() throws Exception
	{
		this.originalImage = __originalImage == null ? null : system.proxies.Image.initialize(getContext(), __originalImage);

		this.resizedImage = __resizedImage == null ? null : system.proxies.Image.initialize(getContext(), __resizedImage);

		// BEGIN USER CODE
		if (this.originalImage.getHasContents(getContext())) {
			InputStream imageInputStream = Core.getImage(getContext(), this.originalImage.getMendixObject(), false);
			BufferedImage image = ImageIO.read(imageInputStream);
			
			BufferedImage rotatedImage = this.rotate(image, this.angle);
			
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(rotatedImage, "png", byteArrayOutputStream);
			InputStream resizedImageInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			
			Core.storeImageDocumentContent(getContext(), this.resizedImage.getMendixObject(), resizedImageInputStream, 100, 100);
			this.resizedImage.setName("resizedImage.png");
		}
		return true;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "RotateImage";
	}

	// BEGIN EXTRA CODE
	private  BufferedImage rotate(BufferedImage image, double angle) {
		double radiansAngle = Math.toRadians(angle);
	    double sin = Math.abs(Math.sin(radiansAngle));
	    double cos = Math.abs(Math.cos(radiansAngle));
	    
	    int width = image.getWidth();
	    int height = image.getHeight();
	    int newWidth = (int)Math.floor(width*cos+height*sin);
	    int newHeight = (int) Math.floor(height * cos + width * sin);
	    
	    BufferedImage result = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g = result.createGraphics();
	    g.translate((newWidth - width) / 2, (newHeight - height) / 2);
	    g.rotate(radiansAngle, width / 2, height / 2);
	    g.drawRenderedImage(image, null);
	    g.dispose();
	    
	    return result;
	}

	// END EXTRA CODE
}