package com.tanglover.zuul;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author TangXu
 * @create 2019-07-26 10:25
 * @description:
 */
public class Test {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Test thumbnailatorTest = new Test();
//        thumbnailatorTest.test1();
        thumbnailatorTest.test2();
//        thumbnailatorTest.test3();
//        thumbnailatorTest.test4();
//        thumbnailatorTest.test5();
//        thumbnailatorTest.test6();
//        thumbnailatorTest.test7();
//        thumbnailatorTest.test8();
//        thumbnailatorTest.test9();
//        InputStream is = new FileInputStream(new File("E:\\tanglover\\projects\\micro-service\\sleuth\\src\\main\\resources\\images\\timg.jpg"));
//        thumbnailatorTest.specifySize(is, 200, 300, "E:/image_200x300.jpg");
//        thumbnailatorTest.specifySize("E:\\工作\\天融汇\\前端\\测试服\\图片.jpg", 1366, 768, null);
    }

    /**
     * 指定大小进行缩放
     *
     * @throws IOException
     */
    private void test1() throws IOException {
        /*
         * size(width,height) 若图片横比200小，高比300小，不变
         * 若图片横比200小，高比300大，高缩小到300，图片比例不变 若图片横比200大，高比300小，横缩小到200，图片比例不变
         * 若图片横比200大，高比300大，图片按比例缩小，横为200或高为300
         */
        Thumbnails.of("E:\\tanglover\\projects\\micro-service\\sleuth\\src\\main\\resources\\images\\timg.jpg").size(200, 300).toFile("E:/image_200x300.jpg");
        Thumbnails.of("E:\\tanglover\\projects\\micro-service\\sleuth\\src\\main\\resources\\images\\timg.jpg").size(2560, 2048).toFile("E:/image_2560x2048.jpg");
    }

    /**
     * @Author TangXu
     * @Description 按照指定长宽缩放图片
     * @Date 2019/7/26 10:56
     * @Param [filePath, width, height]
     */
    public void specifySize(String filePath, int width, int height, String targetFilePath) throws IOException {
        String toFilePath = null;
        if (StringUtils.isEmpty(targetFilePath)) {
            toFilePath = targetFilePath;
        } else {
            toFilePath = getOutFilePath(filePath, width, height);
        }
        Thumbnails.of(filePath).size(width, height).toFile(toFilePath);
    }

    private String getOutFilePath(String filePath, int width, int height) {
        File file = new File(filePath);
        String path = file.getPath();
        int i = path.lastIndexOf(".");
        String filePrefix = path.substring(0, i);
        String fileSuffix = path.substring(i);
        String toFilePath = filePrefix + "_" + width + "x" + height + "_" + System.nanoTime() + fileSuffix;
        return toFilePath;
    }

    private void specifySize(InputStream inputStream, int width, int height, String targetFilePath) throws IOException {
        Thumbnails.of(inputStream).size(width, height).toFile(targetFilePath);
    }

    /**
     * 按照比例进行缩放
     *
     * @throws IOException
     */
    private void test2() throws IOException {
        /**
         * scale(比例)
         */
        Thumbnails.of("E:\\工作\\天融汇\\前端\\测试服\\图片.jpg").scale(1f).outputQuality(0.5f).toFile("E:/image_25%.jpg");
        Thumbnails.of("E:\\工作\\天融汇\\前端\\测试服\\图片.jpg").scale(1.10f).outputQuality(0.7f).toFile("E:/image_110%.jpg");
    }

    /**
     * 不按照比例，指定大小进行缩放
     *
     * @throws IOException
     */
    private void test3() throws IOException {
        /**
         * keepAspectRatio(false) 默认是按照比例缩放的
         */
        Thumbnails.of("E:\\tanglover\\projects\\micro-service\\sleuth\\src\\main\\resources\\images\\timg.jpg").size(120, 120).keepAspectRatio(false).toFile("E:/image_120x120.jpg");
    }

    /**
     * 旋转
     *
     * @throws IOException
     */
    private void test4() throws IOException {
        /**
         * rotate(角度),正数：顺时针 负数：逆时针
         */
        Thumbnails.of("E:\\tanglover\\projects\\micro-service\\sleuth\\src\\main\\resources\\images\\timg.jpg").size(1280, 1024).rotate(90).toFile("E:/image+90.jpg");
        Thumbnails.of("E:\\tanglover\\projects\\micro-service\\sleuth\\src\\main\\resources\\images\\timg.jpg").size(1280, 1024).rotate(-90).toFile("E:/iamge-90.jpg");
    }

    /**
     * 水印
     *
     * @throws IOException
     */
    private void test5() throws IOException {
        /**
         * watermark(位置，水印图，透明度)
         */
        Thumbnails.of("E:\\tanglover\\projects\\micro-service\\sleuth\\src\\main\\resources\\images\\timg.jpg").size(1280, 1024).watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File("images/watermark.png")), 0.5f)
                .outputQuality(0.8f).toFile("E:/image_watermark_bottom_right.jpg");
        Thumbnails.of("E:\\tanglover\\projects\\micro-service\\sleuth\\src\\main\\resources\\images\\timg.jpg").size(1280, 1024).watermark(Positions.CENTER, ImageIO.read(new File("images/watermark.png")), 0.5f)
                .outputQuality(0.8f).toFile("E:/image_watermark_center.jpg");
    }

    /**
     * 裁剪
     *
     * @throws IOException
     */
    private void test6() throws IOException {
        /**
         * 图片中心400*400的区域
         */
        Thumbnails.of("E:\\tanglover\\projects\\micro-service\\sleuth\\src\\main\\resources\\images\\timg.jpg").sourceRegion(Positions.CENTER, 400, 400).size(200, 200).keepAspectRatio(false)
                .toFile("E:/image_region_center.jpg");
        /**
         * 图片右下400*400的区域
         */
        Thumbnails.of("E:\\tanglover\\projects\\micro-service\\sleuth\\src\\main\\resources\\images\\timg.jpg").sourceRegion(Positions.BOTTOM_RIGHT, 400, 400).size(200, 200).keepAspectRatio(false)
                .toFile("E:/image_region_bootom_right.jpg");
        /**
         * 指定坐标
         */
        Thumbnails.of("E:\\tanglover\\projects\\micro-service\\sleuth\\src\\main\\resources\\images\\timg.jpg").sourceRegion(600, 500, 400, 400).size(200, 200).keepAspectRatio(false).toFile("E:/image_region_coord.jpg");
    }

    /**
     * 转化图像格式
     *
     * @throws IOException
     */
    private void test7() throws IOException {
        /**
         * outputFormat(图像格式)
         */
        Thumbnails.of("E:\\tanglover\\projects\\micro-service\\sleuth\\src\\main\\resources\\images\\timg.jpg").size(1280, 1024).outputFormat("png").toFile("E:/image_1280x1024.png");
        Thumbnails.of("E:\\tanglover\\projects\\micro-service\\sleuth\\src\\main\\resources\\images\\timg.jpg").size(1280, 1024).outputFormat("gif").toFile("E:/image_1280x1024.gif");
    }

    /**
     * 输出到OutputStream
     *
     * @throws IOException
     */
    private void test8() throws IOException {
        /**
         * toOutputStream(流对象)
         */
        OutputStream os = new FileOutputStream("E:/image_1280x1024_OutputStream.png");
        Thumbnails.of("E:\\tanglover\\projects\\micro-service\\sleuth\\src\\main\\resources\\images\\timg.jpg").size(1280, 1024).toOutputStream(os);
    }

    /**
     * 输出到BufferedImage
     *
     * @throws IOException
     */
    private void test9() throws IOException {
        /**
         * asBufferedImage() 返回BufferedImage
         */
        BufferedImage thumbnail = Thumbnails.of("E:\\tanglover\\projects\\micro-service\\sleuth\\src\\main\\resources\\images\\timg.jpg").size(1280, 1024).asBufferedImage();
        ImageIO.write(thumbnail, "jpg", new File("E:/image_1280x1024_BufferedImage.jpg"));
    }
}