package cn.v5cn.springboot.fastdfs.controller;

import com.github.tobato.fastdfs.domain.MetaData;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ZYW
 * @version 1.0
 * @date 2018/12/17 15:37
 */
@RestController
@RequestMapping("/file")
public class UploadController {

    @Autowired
    private FastFileStorageClient fileStorageClient;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(MultipartFile file) throws IOException {
        //设置文件元数据
        Set<MetaData> metaDataSet = new HashSet<>();
        metaDataSet.add(new MetaData("originalFilename",file.getOriginalFilename()));
        metaDataSet.add(new MetaData("size",String.valueOf(file.getSize())));
        metaDataSet.add(new MetaData("fileExt",FilenameUtils.getExtension(file.getOriginalFilename())));
        StorePath storePath = fileStorageClient.uploadFile(file.getInputStream()
                , file.getSize()
                , FilenameUtils.getExtension(file.getOriginalFilename())
                , metaDataSet);

        return ResponseEntity.ok(storePath.getFullPath() + "=====" + storePath.getPath() + "+++" + storePath.getGroup());
    }

    @GetMapping("/download")
    public void download(String groupName, String path, HttpServletResponse response) {
        //获取文件元数据
        Set<MetaData> metadataSet = fileStorageClient.getMetadata(groupName, path);
        String origName = metadataSet.stream()
                .filter(item -> StringUtils.equals(item.getName(), "originalFilename"))
                .findFirst()
                .orElse(new MetaData("originalFilename", ""))
                .getValue();
        fileStorageClient.downloadFile(groupName,path, (is) -> {
            ServletOutputStream responseOutputStream = null;
            try {
                // 设置response参数，可以打开下载页面
                response.reset();
                response.setContentType("binary/octet-stream;charset=UTF-8");
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + new String(origName.getBytes(), "iso-8859-1"));
                responseOutputStream = response.getOutputStream();
                IOUtils.copy(is,responseOutputStream);
                responseOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(is);
                IOUtils.closeQuietly(responseOutputStream);
            }
            return Void.TYPE;
        });
    }

}
