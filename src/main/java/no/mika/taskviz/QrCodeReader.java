package no.mika.taskviz;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

public class QrCodeReader {

    public static String fromFile(final String filePath) throws IOException, NotFoundException {
        final BufferedImage bufferedImage = ImageIO.read(new FileInputStream(filePath));
        final BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        final BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        final Result result = new MultiFormatReader().decode(bitmap);

        return result.getText();
    }
}
