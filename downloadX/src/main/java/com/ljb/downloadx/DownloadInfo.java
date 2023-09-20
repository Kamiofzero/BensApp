package com.ljb.downloadx;

public class DownloadInfo {

    public String url;

    public String taskName;
    public String fileName;
    public long fileSize;
    public long fileDownloadSize;
    public int fileDownloadPercent;

    public String fileDownloadPath;

    public int status;


    public DownloadInfo(DownloadTask task) {
        if (task != null) {
            this.url = task.url;
            this.taskName = task.taskName;
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
        return "DownloadInfo{" +
                "url='" + url + '\'' +
                ", taskName='" + taskName + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", fileDownloadSize=" + fileDownloadSize +
                ", fileDownloadPercent=" + fileDownloadPercent +
                ", fileDownloadPath='" + fileDownloadPath + '\'' +
                ", status=" + status +
                '}';
    }
}
