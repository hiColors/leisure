package com.github.lifelab.leisure.common.utils;

/**
 * Title: httpclient文件上传对象
 */
public class HttpClientFileUploadBean {

    // 包括扩展名
    private String fileName;
    // 文件对象[包括：InputStream、byte[]]
    private Object fileObj;

    public HttpClientFileUploadBean(String fileName, Object fileObj) {
        this.fileName = fileName;
        this.fileObj = fileObj;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Object getFileObj() {
        return fileObj;
    }

    public void setFileObj(Object fileObj) {
        this.fileObj = fileObj;
    }

}
