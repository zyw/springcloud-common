package cn.v5cn.minio.demo;

import cn.v5cn.minio.demo.service.MinioService;
import cn.v5cn.minio.demo.service.model.FileMetadata;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-11 20:54
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private MinioService minioService;

    @Test
    public void putFileTest() throws IOException {
        File file = FileUtils.getFile("/Users/zhuyanwei/Downloads/22.pdf");
        FileInputStream stream = FileUtils.openInputStream(file);
        Map<String,String> metadata = new HashMap<>();
        metadata.put(MinioService.FILE_ORIGINAL_NAME, Base64Utils.encodeToString("中国.pdf".getBytes()));
        minioService.putFile("test1","test2.pdf",stream,file.length(),metadata,null);
    }

    @Test
    public void getFileMetadata() {
        FileMetadata metadata = minioService.getFileMetadata("test1", "test2.pdf");
        System.out.println(metadata);
        System.out.println(metadata.getOrigName());
    }

    @Test
    public void removeFileTest() {
        minioService.removeFile("test","《FFmpeg从入门到精通》.pdf");
    }
}
