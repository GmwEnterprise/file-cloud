package com.github.mrag.filecloud.service;

import com.github.mrag.filecloud.common.Encryption;
import com.github.mrag.filecloud.model.FileItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Gmw
 */
@Service
public class MainService {
    @Value("${filecloud.local-path}")
    private String rootPath;

    /**
     * 返回所有文件的文档树
     */
    public FileItem allFiles() {
        return generateFileItem(new File(rootPath), -1);
    }

    private FileItem generateFileItem(File path, int level) {
        String hashPath = Encryption.encrypt(path.getAbsolutePath());
        FileItem item = new FileItem().setFile(path.isFile()).setLevel(level).setPathname(path.getName())
                .setDirectory(path.isDirectory()).setPath(hashPath);
        if (item.getDirectory()) {
            // 是文件夹
            File[] listFiles = path.listFiles();
            if (listFiles != null) {
                // 是文件夹就不可能为空
                for (File subPath : listFiles) {
                    List<FileItem> children;
                    if (item.getChildren() == null) {
                        children = new ArrayList<>(listFiles.length);
                        item.setChildren(children);
                    } else {
                        children = item.getChildren();
                    }
                    children.add(generateFileItem(subPath, level + 1));
                }
            }
        }
        return item;
    }

    public Map<String, List<String>> saveFiles(Map<String, MultipartFile> fileMap) {
        List<String> failed = new ArrayList<>(), success = new ArrayList<>();
        fileMap.forEach((key, multiFile) -> {
            String originalFilename = multiFile.getOriginalFilename();
            File file = new File(key.replace("/root", "") + "/" + originalFilename);
            try {
                multiFile.transferTo(file);
                success.add(String.format("key=[%s], filename=[%s]", key, originalFilename));
            } catch (IOException e) {
                failed.add(String.format("key=[%s], filename=[%s], errorDesc=[%s]", key, originalFilename, e.getMessage()));
            }
        });
        return Map.of("success", success, "fail", failed);
    }

    public boolean creatFolder(String subFolder) {
        String target = rootPath + subFolder;
        File file = new File(target);
        return !file.exists() && file.mkdirs();
    }
}
