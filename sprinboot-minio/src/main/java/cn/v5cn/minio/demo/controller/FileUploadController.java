package cn.v5cn.minio.demo.controller;

import cn.v5cn.minio.demo.exception.MyMinioException;
import cn.v5cn.minio.demo.service.MinioService;
import cn.v5cn.minio.demo.service.model.FileMetadata;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-12 14:04
 */
@RestController
public class FileUploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private MinioService minioService;

    @PostMapping("/upload")
    public ResponseEntity<Object> upload(@RequestParam(value = "file", required = false)MultipartFile file) throws IOException {

        Instant now = Instant.now();
        String bucketName = String.valueOf(now.getEpochSecond());


        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        long size = file.getSize();

        String extName = FilenameUtils.getExtension(originalFilename);

        String newFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYYMMDDHHmmss")) + "." + extName;

        boolean result = minioService.putFile(bucketName, newFileName, file.getInputStream(), size, originalFilename, contentType);

        if(result) {
            return ResponseEntity.ok(ImmutableMap.of("id", Base64Utils.encodeToString((bucketName+"@@"+newFileName).getBytes())));
        }

        return ResponseEntity.badRequest().body(ImmutableMap.of("message", "失败"));
    }

    @GetMapping("/download/{id}")
    public void downLoadFile(@PathVariable("id") String id, HttpServletResponse response) {
        try {
            if (StringUtils.isEmpty(id)) {
                throw new MyMinioException("文件ID为空！");
            }
            String[] bucketObjectName = new String(Base64Utils.decodeFromString(id)).split("@@");;
            if(bucketObjectName.length != 2) {
                throw new MyMinioException("ID格式错误！");
            }
            FileMetadata fileMetadata = minioService.getFileMetadata(bucketObjectName[0], bucketObjectName[1]);

            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType(fileMetadata.getContentType());
            String filename = URLEncoder.encode(fileMetadata.getOrigName(), "UTF-8");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            IOUtils.copy(fileMetadata.getStream(),response.getOutputStream());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
            throw new MyMinioException(id + "缓存冲读取错误");
        }
    }
}
