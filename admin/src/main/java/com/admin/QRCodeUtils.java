package com.admin;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

/**
 * 条形码和二维码编码解码
 * 
 * @author ThinkGem
 * @version 2014-02-28
 */
public class QRCodeUtils {

	private static Logger logger = LoggerFactory.getLogger(QRCodeUtils.class);
	    private static int BARCODE_SIZE = 80;//条形码的单位宽度
	    private static int QRCODE_SIZE = 500;//二维码的单位宽度
	    private static String FORMAT = "jpg";// 生成的图片格式
	    private static int BLACK = 0xc70025;// 编码的颜色(中国红颜色）0xc70025   /0x000000
	    private static int WHITE = 0xFFFFFF;// 空白的颜色

	    // 二维码中间的图像配置。由于二维码的容错率有限，因此中间遮挡的面积不要太大，否则可能解析不出来。
	   // private static int ICON_WIDTH = (int) (QRCODE_SIZE / 6);//中间图像
		private static int ICON_WIDTH = (int) (QRCODE_SIZE / 8);
	    private static int HALF_ICON_WIDTH = ICON_WIDTH / 2;
	    private static int FRAME_WIDTH  = 1;// Icon四周的边框宽度

	    // 二维码读码器和写码器
	    private static final MultiFormatWriter WRITER = new MultiFormatWriter();
	    private static final MultiFormatReader READER = new MultiFormatReader();
	
	
	
	/**
	 * 条形码编码
	 * 
	 * @param contents
	 * @param width
	 * @param height
	 * @param imgPath
	 */
	public static void encode(String contents, int width, int height, String imgPath) {
		int codeWidth = 3 + // start guard
				(7 * 6) + // left bars
				5 + // middle guard
				(7 * 6) + // right bars
				3; // end guard
		codeWidth = Math.max(codeWidth, width);
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
					BarcodeFormat.EAN_13, codeWidth, height, null);

			MatrixToImageWriter
					.writeToFile(bitMatrix, "png", new File(imgPath));

		} catch (Exception e) {
			logger.error("条形码编码出错");
		}
	}

	/**
	 * 条形码解码
	 * 
	 * @param imgPath
	 * @return String
	 */
	public static String decode(String imgPath) {
		BufferedImage image = null;
		Result result = null;
		try {
			image = ImageIO.read(new File(imgPath));
			if (image == null) {
				System.out.println("the decode image may be not exit.");
			}
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

			result = new MultiFormatReader().decode(bitmap, null);
			return result.getText();
		} catch (Exception e) {
			logger.error("条形码解码出错");
		}
		return null;
	}
	
	/**
	 * 二维码编码
	 * 
	 * @param contents
	 * @param width
	 * @param height
	 * @param imgPath
	 */
	public static void encode2(String contents, int width, int height, String imgPath) {
		
				
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		// 指定纠错等级
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		// 指定编码格式
		hints.put(EncodeHintType.CHARACTER_SET, "GBK");
		try {
			if(!new File(imgPath).exists())   
			{ 
			    new File(imgPath).mkdirs(); 
			 }			
			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
					BarcodeFormat.QR_CODE, width, height, hints);
			MatrixToImageWriter
					.writeToFile(bitMatrix, "png", new File(imgPath));

		} catch (Exception e) {
			logger.error("二维码编码出错");
		}
	}

	/**
	 * 二维码解码
	 * 
	 * @param imgPath
	 * @return String
	 */
	public static String decode2(String imgPath) {
		BufferedImage image = null;
		Result result = null;
		try {
			image = ImageIO.read(new File(imgPath));
			if (image == null) {
				System.out.println("the decode image may be not exit.");
			}
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

			Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
			hints.put(DecodeHintType.CHARACTER_SET, "GBK");

			result = new MultiFormatReader().decode(bitmap, hints);
			return result.getText();
		} catch (Exception e) {
			logger.error("二维码解码出错");
		}
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// 条形码
		String imgPath = "target\\zxing_EAN13.png";
		String contents = "6923450657713";
		int width = 105, height = 50;
		
		QRCodeUtils.encode(contents, width, height, imgPath);
		System.out.println("finished zxing EAN-13 encode.");

		String decodeContent = QRCodeUtils.decode(imgPath);
		System.out.println("解码内容如下：" + decodeContent);
		System.out.println("finished zxing EAN-13 decode.");
		
		// 二维码
		String imgPath2 = "target\\zxing.png";
		String contents2 = "Hello Gem, welcome to Zxing!"
				+ "\nBlog [ http://thinkgem.iteye.com ]"
				+ "\nEMail [ thinkgem@163.com ]";
		int width2 = 300, height2 = 300;

		QRCodeUtils.encode2(contents2, width2, height2, imgPath2);
		System.out.println("finished zxing encode.");

		String decodeContent2 = QRCodeUtils.decode2(imgPath2);
		System.out.println("解码内容如下：" + decodeContent2);
		System.out.println("finished zxing decode.");		
	}
	
	public static String encodeAgent(String contents, int width, int height,String directory) throws Exception {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		// 指定纠错等级
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		// 指定编码格式
		hints.put(EncodeHintType.CHARACTER_SET, "GBK");
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		String path=request.getSession().getServletContext().
				getRealPath(directory)+"/"+contents+".png";
			if(!new File(path).exists())   
			{ 
			    new File(path).mkdirs(); 
			 }			
			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
					BarcodeFormat.QR_CODE, width, height, hints);
			MatrixToImageWriter
					.writeToFile(bitMatrix, "png", new File(path));
			return null;
	}
	  /**
     * 将String编码成二维码的图片后，使用字节数组表示，便于传输。
     */
    public static byte[] createQRCodeToBytes(String content)
            throws WriterException, IOException {
        BufferedImage image = createQRCode(content);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, FORMAT, os);
        return os.toByteArray();
    }
    /**
     * 把一个String编码成二维码的BufferedImage.
     */
    public static final BufferedImage createQRCode(String content)
            throws WriterException {
        // 长和宽一样，所以只需要定义一个SIZE即可
        BitMatrix matrix = WRITER.encode(
                content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE);
        return toBufferedImage(matrix);
    }
    /**
     * 将一个BitMatrix对象转换成BufferedImage对象
     */
    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        //去除白边
        matrix = deleteWhite(matrix);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
		//int width = 200;
		//int height = 200;
        //System.out.println("width："+width);
		//System.out.println("heigth："+height);
        BufferedImage image = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    //去除二维码白边
    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1])) {
					resMatrix.set(i, j);
				}
            }
        }
        return resMatrix;
    }


	/**
	 * 将String编码成二维码的图片后，使用字节数组表示，便于传输。
	 */
	public static byte[] createQRCodeToBytes(String content,int color)
			throws WriterException, IOException {
		BufferedImage image = createQRCode(content,color);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(image, FORMAT, os);
		return os.toByteArray();
	}
	/**
	 * 把一个String编码成二维码的BufferedImage.
	 */
	public static final BufferedImage createQRCode(String content,int color)
			throws WriterException {
		// 长和宽一样，所以只需要定义一个SIZE即可
		BitMatrix matrix = WRITER.encode(
				content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE);
		return toBufferedImage(matrix,color);
	}

	/**
	 * 将一个BitMatrix对象转换成BufferedImage对象
	 */
	private static BufferedImage toBufferedImage(BitMatrix matrix,int color) {
		//去除白边
		matrix = deleteWhite(matrix);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		//int width = 200;
		//int height = 200;
		//System.out.println("width："+width);
		//System.out.println("heigth："+height);
		BufferedImage image = new BufferedImage(
				width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? color : WHITE);
			}
		}
		return image;
	}
    
}