package bph.data;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import javax.imageio.ImageIO;

public class ImageLoader {

	private final BufferedImage[] images;

	public ImageLoader(String dir, String blank, String... pics) {
		Objects.requireNonNull(pics);
		Objects.requireNonNull(dir);
		Objects.requireNonNull(blank);
		images = new BufferedImage[pics.length + 1];
		setImage(0, dir, blank);
		for (var i = 0; i < pics.length; i++) {
			setImage(i + 1, dir, pics[i]);
		}
	}

	private void setImage(int position, String dirPath, String imagePath) {
		Objects.requireNonNull(dirPath);
		Objects.requireNonNull(imagePath);
		var path = Path.of(dirPath + "/" + imagePath);
		try (var input = Files.newInputStream(path)) {
			images[position] = ImageIO.read(input);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public BufferedImage background() {
		return images[0];
	}

	public BufferedImage image(int i) {
		Objects.checkIndex(i, images.length - 1);
		return images[i + 1];
	}

	public int size() {
		return images.length;
	}
}