package cn.v5cn.others.ppt_to_image;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-04-16 20:03
 */
public class Main {
    public static void main(String[] args) {
        /*
         * 简单例子，以自身分辨率输出ppt的每一页
         */
//        PPTToImgConverter.dir("/Users/zhuyanwei/workspace/springcloud-common/others") //设置图片输出目录。
//                .file("/Users/zhuyanwei/Downloads/1.pptx") //选择一个文件或者文件夹
//                .convert(); //开始转换。

//        PPTToImgConverter.dir("/Users/zhuyanwei/workspace/springcloud-common/others/2")
//                //select multiple files or folders.设置多个文件或者文件夹。
//                .file("/Users/zhuyanwei/Downloads/1.pptx")
//                //set the start position. 设置起始页码。
//                .from(0)
//                //set the end position, note that the end page will be converted，-1 means last page.
//                //设置结束页码，-1表示倒数第一页，结束页码也会被转换。
//                .to(-1)
//                //set the width of resolution. 设置图片分辨率宽度。
//                .width(400)
//                //set the height of resolution. 设置图片分辨率高度。
//                .height(300)
//                //set the format of images. 设置图片格式
//                .imgFormat(PPTToImgConverter.PNG)
//                //start the conversion. 开始转换。
//                .convert();

        /**
         *other apis
         *note the scale dose not influence the resolution of final images, and it's unnecessary to set.
         *其他例子
         */
//set the parent directory of converted images. 设置图片输出目录。
        PPTToImgConverter.dir("/Users/zhuyanwei/workspace/springcloud-common/others/3")
                //set the scale of tmp images, not the final images!!!. 设置中间图片的放大倍数，根据自己需要进行尝试
                .scale(3)
                //select a file or folder. 选择一个文件或者文件夹。
                .file("/Users/zhuyanwei/Downloads/1.pptx")
                //set the compression ratio and it will be invalid once width or height set.
                //设置压缩比例，注意一旦你设置了宽度或者高度，则该设置无效。
                //.ratio(0.500)
                //start the conversion of first page. 开始转换，只转换第一页。
                .convert();
    }
}
