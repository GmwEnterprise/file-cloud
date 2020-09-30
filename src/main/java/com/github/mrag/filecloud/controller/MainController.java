package com.github.mrag.filecloud.controller;

import com.github.mrag.filecloud.common.Auth;
import com.github.mrag.filecloud.common.Encryption;
import com.github.mrag.filecloud.model.FileItem;
import com.github.mrag.filecloud.service.MainService;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Gmw
 */
@RestController
@RequestMapping("/api")
public class MainController {

    private final MainService mainService;

    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("files")
    public ResponseEntity<FileItem> files() {
        FileItem files = mainService.allFiles();
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam("path") String hashPath,
                                           @RequestHeader("User-Agent") String userAgent) throws IOException {
        File file = new File(Encryption.decrypt(hashPath));
        if (!file.isFile() || !file.exists()) {
            return ResponseEntity.badRequest().build();
        }
        // 转换为字节数组
        byte[] fileBytes = FileUtils.readFileToByteArray(file);
        HttpHeaders headers = new HttpHeaders();
        // 文件名编码，避免出现乱码问题
        String filename = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8);
        // 设置下载文件必须用到的 HTTP 头部，指定了文件名
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename);
        return ResponseEntity.ok() // 返回状态码 200
                .contentLength(fileBytes.length) // 设置响应体内容长度
                .contentType(MediaType.APPLICATION_OCTET_STREAM) // 返回体类型：二进制数据流
                .headers(headers) // 设置头部
                .body(fileBytes); // 设置内容并构建
    }

    @Auth
    @PostMapping("/upload")
    public ResponseEntity<?> upload(HttpServletRequest httpServletRequest) {
        try {
            MultipartHttpServletRequest req = (MultipartHttpServletRequest) httpServletRequest;
            Map<String, MultipartFile> fileMap = req.getFileMap();
            return ResponseEntity.ok(mainService.saveFiles(fileMap));
        } catch (ClassCastException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Auth
    @PostMapping("/create-folder")
    public ResponseEntity<?> createFolder(@RequestParam("folderName") String folderName) {
        String subFolder = URLDecoder.decode(folderName, StandardCharsets.UTF_8).substring(6);
        boolean result = mainService.creatFolder(subFolder);
        return ResponseEntity.status(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST).build();
    }
}
