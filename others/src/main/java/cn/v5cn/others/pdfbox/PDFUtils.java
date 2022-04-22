package cn.v5cn.others.pdfbox;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PDFUtils {

    private static final Logger logger = LoggerFactory.getLogger(PDFUtils.class);

    /**
     * @param pdfPath pdf文件路径
     * @return
     */
    public static PDDocument initPDDocument(String pdfPath) throws Exception {
        File pdfFile = Paths.get(pdfPath).toFile();
        if (!pdfFile.exists()) {
            logger.error("pdf文件不存在");
            return null;
        }
        // 新建一个PDF解析器对象
        PDFParser pdfParser = new PDFParser(new RandomAccessBuffer(new FileInputStream(pdfFile)));
        // 对PDF文件进行解析
        pdfParser.parse();
        // 获取解析后得到的PDF文档对象
        PDDocument pdfDoc = pdfParser.getPDDocument();
        return pdfDoc;
    }

    /**
     * @param inputStream 输入流
     * @return
     */
    public static PDDocument initPDDocument(InputStream inputStream) throws Exception {
        // 新建一个PDF解析器对象
        PDFParser pdfParser = new PDFParser(new RandomAccessBuffer(inputStream));
        // 对PDF文件进行解析
        pdfParser.parse();
        // 获取解析后得到的PDF文档对象
        PDDocument pdfDoc = pdfParser.getPDDocument();
        return pdfDoc;
    }

    /**
     * 解析pdf文档中的字符内容
     *
     * @param pdDocument
     * @param startPage  开始页码
     * @param endPage    结束页码
     * @return
     */
    public static String getContent(PDDocument pdDocument, int startPage, int endPage) throws IOException {
        if (endPage <= startPage) {
            logger.error("页码参数不正确");
            return null;
        }
        // 新建一个PDF文本剥离器
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setStartPage(startPage); // 开始提取页数
        stripper.setEndPage(endPage); // 结束提取页数
        // 从PDF文档对象中剥离文本
        String result = stripper.getText(pdDocument);
        return result;
    }

    /**
     * 解析pdf文档中的所有图片列表
     *
     * @param pdDocument
     * @param startPage  开始页码
     * @param endPage    结束页码
     * @return
     */
    public static List<PDImageXObject> getImageList(PDDocument pdDocument, int startPage, int endPage) throws IOException {
        if (endPage <= startPage) {
            logger.error("页码参数不正确");
            return null;
        }
        List<PDImageXObject> imageList = new ArrayList<>();
        for (int i = startPage; i < endPage; i++) {
            PDPage page = pdDocument.getPage(i);
            Iterable<COSName> objectNames = page.getResources().getXObjectNames();
            for (COSName imageObjectName : objectNames) {
                if (page.getResources().isImageXObject(imageObjectName)) {
                    PDImageXObject imageXObject = (PDImageXObject) page.getResources()
                            .getXObject(imageObjectName);
                    imageList.add(imageXObject);
                }
            }
        }
        return imageList;
    }

    public static void main(String[] args) throws Exception {
        PDDocument pdDocument = PDFUtils.initPDDocument("D:\\users\\Destop\\学生健康监测台账.pdf");
        if (pdDocument != null) {
            // 获取文档文本内容
            String result = PDFUtils.getContent(pdDocument, 0, pdDocument.getNumberOfPages());
            System.out.println("PDF文件的文本内容如下：");
            System.out.println(result);
            // 获取文档中的所有图片
            List<PDImageXObject> imageList = PDFUtils.getImageList(pdDocument, 0,pdDocument.getNumberOfPages());
            for (int i = 0; i < imageList.size(); i++) {
                PDImageXObject imageXObject = imageList.get(i);
                BufferedImage bufferedImage = imageXObject.getImage();
                ImageIO.write(bufferedImage, imageXObject.getSuffix(),
                        new FileOutputStream(Paths
                                .get("D:\\users\\Destop\\" + i + "." + imageXObject.getSuffix()).toFile()));
            }
        }
    }
}
