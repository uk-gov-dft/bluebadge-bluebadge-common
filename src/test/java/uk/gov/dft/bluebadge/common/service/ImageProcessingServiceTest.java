package uk.gov.dft.bluebadge.common.service;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;

public class ImageProcessingServiceTest {

  private final String BADGE_NUMBER = "KKKKKK";

  private final String IMAGE_JPG_BASE64_GOOD =
      "/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRo"
          + "fHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjI"
          + "yMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAQABgDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwI"
          + "EAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmN"
          + "kZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb"
          + "3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobH"
          + "BCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqO"
          + "kpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDLs2+z3C3AbAXgqO4NWtN02TU"
          + "b9JlXFvHIWJPc5NP0rSzdv5sgxEnXPeuysreJIlESYQ9MCtIVnTVkeTgcO2ryQ+OEemM9cUVcERJBJKgdqK53q7nrrQ//2Q==";
  private final String IMAGE_PNG_BASE64_GOOD =
      "iVBORw0KGgoAAAANSUhEUgAAABgAAAAQCAYAAAAMJL+VAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAD"
          + "aklEQVQ4jT2Uy3JkRQxEjx5VdV9tt+1p20NAEDCw4h/4DD6Jv2RJBBHAMIwf3X1vVYlFm9lLmSllSjL8+kuMJZEssz2fyDHww8OPfH/4gL8W/vn9"
          + "M7//9gdLuUJIrK3y519/cZUrP//0yIeHHUNOnF6fcAt2cyFnMA3Or0d8LhkRgR6gwvH1xMdPn3jcb+xspquhPpDKTG9CbCDi5OQs48IwDIwl4xqY"
          + "VdyF3jeiN0op6JInEo4GZDXaVnl6euL1+YnaVpQgJUMkWE9HzsdnTIS76yuWeWTIBXcnF6d4QhGidVprqILrqWE9AFAraBEGM2I70zji1igeSH1l"
          + "fflEPZ3YDQMPdwsWnbadkVCIjmXDFNwS5gVaxbVCQlFVXJ15KZSeOb88s5VCVEHjCM1IcmKcjaurwv3NTMkCBL02TBsqgoggqqSUsOR46gk0UMBC"
          + "mccRq87Lv/8wDoYdhfXlM0ueOOwHDnc3zPPE/c3ILguDNugbomBm9DjTe6U10JTwLRqCUcwwc6Zpwqrx8e8nXp4/osfg/PrETRIeb97x4Zt7Sk7c"
          + "zBlvR1w7XTqmgbvRukE3VJ3j8Yyvc4beed1Wcq3UT0FqgHW27QXWlet94f1h5LuHPXcFxiTkdiQ4IdJIppgqvZ1REbyMiARDmfETGyoKArU2Tu1E"
          + "78LQg2mc2JWR27Rwv1/YDU6RjdwFo6OpIw7uFw8jhIhLYEARMTx6R4FkitZKX1eyJJZS2I8T76drHpdb9qkwaiap4nIxNyJQkTfwoNZGrfULyTQt"
          + "uHVIIhQxVIKkwnWeeJx33JUdh901h3mhhFLaRYgLIEqHy5ECvXdqrWzbdiFWBcAzirXAo5N7MKlzO4wc5oXH+YbbYWRQxWpHCVQdM4gIzBLihoig"
          + "KrhfknSpsUv0vYNuDYvGLJm7YeR+nLlJI++mmVEUbx2NhouiVDoBAiGK9A5vk6SUMDPcM2bGtm14buA9mHDezTu+3t/y1bxnZwlfK7RGRJCSk1wI"
          + "Gq13zAwRo78RXNRegAHaW5+X1ilh7Nw5zAvf3h64n64otfP5jz+J1rCS8OSYCL13QgURI6VE753+Rvj/Wlq7mA3gC871OPGwu+L91S1TGP3pyHpe"
          + "6acVqRVVY/BEGQbWtoII4zhRu+CevxgdEZcnJ07JiVor/wFTTXuxHEJ3MgAAAABJRU5ErkJggg==";
  private final String IMAGE_GIF_BASE64 =
      "R0lGODlhOAHwAPAAAAAAAP///ywAAAAAOAHwAAAC/kSMp8nrDZ+MdNqKr858+w5+YkiOZhOk6sq2"
          + "7gvH8kzX9o3n+s73/j8rCU/EobGIPCqTzKXTBIxKp9Sq9YrN4p7cprcL/orD5LFGi06r1+y2e1WOm+X0ub2OF733/L7/v5YneEc4aFiIOAS4yNjo"
          + "+KhyKJlIOWlZKQapucnZKXUJihk6Klpq4ImaqrqaQupqCvsqi8Raa3vbN6sby7vriwscLGz1W9x7bHw4vMzcXJOMHA09beZsfW1NrS3NvR2CDR6O"
          + "691dTn6uIK6+7onubg4PzT5Pz/h+H58/W8/f74YPUJ/AS/4KGsQSMOHAhXkOOnz4Q6FEhhTDQLyI0cbE/o0VOx7JCDJkC44kPZrsIDJlyJIsT7pE"
          + "oTImxJY0X7qUifNgzZ02qeX82Y+n0J4CgRqdNzQp0X1Hm4ZTCnVpNKdUr0W9KnVS1a3MsHrNiomr2GBfy4KVMzatLbNsz1ZTCzdV27lul8S924mu"
          + "3rpE8PqFtDcwX5R/Cy8SjHiwBcOM/SR+rDhB48lvIFtWTDlzoMicLz/RDFqL586kP4Y+XWW0aqmoW38qDXs1Yde0eciOjbtC7d06bvs2yTu4xtzE"
          + "f58SjjyG8eLEkzt3sTx60efUWzGXXrN6dezXSWunzj08t+/PxZs/Rt75+fWw0idnDz+se+Hx6yOaT7+7fXz4/oPv109Uf7z9R2AmAtZWYIJMHIgg"
          + "gApuwyBtDzrYUYSuTYihHhailmGHZ2x4mociSgBiiBSOuEuJoaF4IjwqgsYiiy9qFmOLr8yYWY0j4kiZjjZOxWNjPnoYpJA/DslLkYwhmaGShjF5"
          + "ZHtO/gXlhFNSGWWV8l15l5YKcomXl1lSAmaXY4qJVplwoXkmIWqu2SabFr2ZlpxxvkWnWHbal+dYe975WZ9c/RmfoIMCiihHhm5FaKK0LEpVo+xB"
          + "GqmjkkJBaVOXnpepppZ+yl+nRm0Kqgeijloqqbqd+pOqqS7Gak6ucherrK/O6kCtOOEqna4y8XprOr6qBOxywxIbgyywx6ZUbLDLitTsb8+ulGy1"
          + "v0wLUrSvYpuRtrdxi5G3pYJ7kbirkTuTteoyha5O675LSrsOmfupvO7Se5m9BuHrqL4F8Zuvv0HBSzBBAvMDMKIHI1xww8osTE/CkEEcscMWN0Qx"
          + "OxLfmbHGF3+cZsfibJyYyOqQ3KbJI4PM8pwqY1MAADs=";

  private ImageProcessingService service;

  @Before
  public void setUp() {
    service = new ImageProcessingService();
  }

  @Test
  public void getProportionalWidth() {
    // If height unchanged then width unchanged
    Assert.assertEquals(100, service.getProportionalWidth(300, 100, 300));

    // Target height 50%
    Assert.assertEquals(50, service.getProportionalWidth(300, 100, 150));
  }

  @Test
  public void readJpgGood() {
    BufferedImage image = service.getBufferedImageFromBase64(IMAGE_JPG_BASE64_GOOD, BADGE_NUMBER);
    Assert.assertNotNull(image);
  }

  @Test
  public void readPngGood() {
    BufferedImage image = service.getBufferedImageFromBase64(IMAGE_PNG_BASE64_GOOD, BADGE_NUMBER);
    Assert.assertNotNull(image);
  }

  @Test
  public void readGifGood() {
    BufferedImage image = service.getBufferedImageFromBase64(IMAGE_GIF_BASE64, BADGE_NUMBER);
    Assert.assertNotNull(image);
  }

  @Test(expected = BadRequestException.class)
  public void readCorruptImage() {
    // Following is just some text encoded
    String IMAGE_CORRUPT = "QSBiaXQgb2YgdGV4dC4=";
    service.getBufferedImageFromBase64(IMAGE_CORRUPT, BADGE_NUMBER);
    Assert.fail();
  }

  @Test(expected = BadRequestException.class)
  public void readInvalidBase64() {
    String input = "||___££$$";
    service.getBufferedImageFromBase64(input, BADGE_NUMBER);
    Assert.fail();
  }

  @Test
  public void getInputStreamForSizedBufferedImage() {
    // Given a valid buffered image
    BufferedImage image = service.getBufferedImageFromBase64(IMAGE_GIF_BASE64, BADGE_NUMBER);

    // When request an input stream for the image sized
    InputStream result = service.getInputStreamForSizedBufferedImage(image, 300);

    // Then no exceptions
    Assert.assertNotNull(result);
  }
}
