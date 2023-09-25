package com.ljb.downloadx;

public class DownloadInfo {

    public String id;

    public String url;

    public String fileName;
    public long fileSize;
    public long fileDownloadSize;
    public int fileDownloadPercent;

    public String fileDownloadPath;

    public int status;

    long createTime;

    long completeTime;

    public DownloadInfo() {
    }

    public DownloadInfo(DownloadTask task) {
        if (task != null) {
            this.id = task.url;
            this.url = task.url;
            this.fileName = task.fileName;
            this.fileSize = task.fileSize;
            this.fileDownloadSize = task.fileDownloadSize;
            this.fileDownloadPercent = task.fileDownloadPercent;
            this.fileDownloadPath = task.fileDownloadPath;
            this.status = task.status;
        }
    }

    public static DownloadInfo copyFromTask(DownloadTask task) {
        return new DownloadInfo(task);
    }

    @Override
    public String toString() {
        return "DownloadInfo{" + "url='" + url + '\'' +  ", fileName='" + fileName + '\'' + ", fileSize=" + fileSize + ", fileDownloadSize=" + fileDownloadSize + ", fileDownloadPercent=" + fileDownloadPercent + ", fileDownloadPath='" + fileDownloadPath + '\'' + ", status=" + status + '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getFileDownloadSize() {
        return fileDownloadSize;
    }

    public void setFileDownloadSize(long fileDownloadSize) {
        this.fileDownloadSize = fileDownloadSize;
    }

    public int getFileDownloadPercent() {
        return fileDownloadPercent;
    }

    public void setFileDownloadPercent(int fileDownloadPercent) {
        this.fileDownloadPercent = fileDownloadPercent;
    }

    public String getFileDownloadPath() {
        return fileDownloadPath;
    }

    public void setFileDownloadPath(String fileDownloadPath) {
        this.fileDownloadPath = fileDownloadPath;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(long completeTime) {
        this.completeTime = completeTime;
    }
}
