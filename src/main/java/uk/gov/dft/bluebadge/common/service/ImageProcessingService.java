package uk.gov.dft.bluebadge.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.dft.bluebadge.common.api.model.Error;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.common.service.exception.InternalServerException;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@SuppressWarnings({"unused", "WeakerAccess"})
@Slf4j
public class ImageProcessingService {
  /**
   * Decodes Base64 string and creates image from it.
   *
   * @param base64 Source encodedstring.
   * @param loggingId Parent context object id, e.g. badge number.
   * @return Buffered image object.
   */
  @SuppressWarnings("WeakerAccess")
  public BufferedImage getBufferedImageFromBase64(String base64, String loggingId) {
    byte[] rawData;

    try {
      rawData = Base64.getDecoder().decode(base64);
    } catch (IllegalArgumentException e) {
      log.error("Could not process file.", e);
      Error error = new Error();
      error.setMessage("Could not decode image Base64.");
      throw new BadRequestException(error);
    }
    InputStream originalAsByteInputStream = new ByteArrayInputStream(rawData);
    ImageIO.setUseCache(false);

    BufferedImage image;
    try {
      image = ImageIO.read(originalAsByteInputStream);
    } catch (IOException e) {
      log.error("Could not process file.", e);
      Error error = new Error();
      error.setMessage("File storage failed, Could not read image file.");
      throw new BadRequestException(error);
    }

    if (null == image) {
      log.debug("Could not parse image. Badge:{}", loggingId);
      Error error = new Error();
      error.setMessage(
          "File storage failed, Could not parse image file, either unsupported type or corrupt.");
      throw new BadRequestException(error);
    }
    return image;
  }

  /**
   * Resize an image and return input stream for it.
   *
   * @param sourceImage Image to resize.
   * @param targetHeight Height to size to (width will be proportional).
   * @return Input stream.
   */
  @SuppressWarnings("WeakerAccess")
  public ByteArrayInputStream getInputStreamForSizedBufferedImage(
      BufferedImage sourceImage, int targetHeight) {
    int width = getProportionalWidth(sourceImage.getHeight(), sourceImage.getWidth(), targetHeight);
    BufferedImage outputImage =
        new BufferedImage(width, targetHeight, BufferedImage.TYPE_3BYTE_BGR);

    Graphics2D g2d = outputImage.createGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.drawImage(sourceImage, 0, 0, width, targetHeight, null);
    g2d.dispose();

    ByteArrayOutputStream imageOutputStream = new ByteArrayOutputStream();

    try {
      ImageIO.write(outputImage, "jpg", imageOutputStream);
    } catch (IOException e) {
      log.error("Could not process file - resizing.", e);
      Error error = new Error();
      error.setMessage("File storage failed, Could not resize file.");
      throw new InternalServerException(error);
    }
    return new ByteArrayInputStream(imageOutputStream.toByteArray());
  }

  /**
   * Return proportioanal width to use when resizing an image based upon height.
   *
   * @param sourceHeight Original height.
   * @param sourceWidth Original width.
   * @param targetHeight Target height.
   * @return Calculated target width.
   */
  int getProportionalWidth(int sourceHeight, int sourceWidth, int targetHeight) {
    // No change if 100% (Avoid any rounding)
    if (sourceHeight == targetHeight) {
      return sourceWidth;
    }

    float ratio = (float) targetHeight / (float) sourceHeight;

    return Math.round(ratio * sourceWidth);
  }
}
