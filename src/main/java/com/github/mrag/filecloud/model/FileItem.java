package com.github.mrag.filecloud.model;

import java.io.Serializable;
import java.util.List;

public class FileItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer level; // 层级
    private String path; // 绝对路径
    private String pathname; // 文件/文件夹名
    private Boolean file; // 是否为文件
    private Boolean directory; // 是否为文件夹
    private List<FileItem> children; // 向下递归

    public String getPath() {
        return path;
    }

    public FileItem setPath(String path) {
        this.path = path;
        return this;
    }

    public Boolean getFile() {
        return file;
    }

    public FileItem setFile(Boolean file) {
        this.file = file;
        return this;
    }

    public Boolean getDirectory() {
        return directory;
    }

    public FileItem setDirectory(Boolean directory) {
        this.directory = directory;
        return this;
    }

    public List<FileItem> getChildren() {
        return children;
    }

    public FileItem setChildren(List<FileItem> children) {
        this.children = children;
        return this;
    }

    public Integer getLevel() {
        return level;
    }

    public FileItem setLevel(Integer level) {
        this.level = level;
        return this;
    }

    public String getPathname() {
        return pathname;
    }

    public FileItem setPathname(String pathname) {
        this.pathname = pathname;
        return this;
    }
}
