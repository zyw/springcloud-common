package cn.v5cn.others.pdfbox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;

public class PdfReader {
    public static void main(String[] args) {
        File pdfFile = new File("D:\\users\\Destop\\EFK安装与配置.pdf");
        PDDocument document = null;
        try
        {
            // 方式一：
            /**
             InputStream input = null;
             input = new FileInputStream( pdfFile );
             //加载 pdf 文档
             PDFParser parser = new PDFParser(new RandomAccessBuffer(input));
             parser.parse();
             document = parser.getPDDocument();
             **/

            // 方式二：
            document= PDDocument.load(pdfFile);

            // 获取页码
            int pages = document.getNumberOfPages();

            // 读文本内容
            PDFTextStripper stripper = new PDFTextStripper();
            // 设置按顺序输出
            stripper.setSortByPosition(true);
            stripper.setStartPage(1);
            stripper.setEndPage(pages);
            String content = stripper.getText(document);
            System.out.println(content);
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
