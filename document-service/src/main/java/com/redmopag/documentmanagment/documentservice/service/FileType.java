package com.redmopag.documentmanagment.documentservice.service;

public enum FileType {
    JPEG("image/jpeg"),
    PNG("image/png"),
    PDF("application/pdf");

    private final String mimeType;

    FileType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static boolean isValidMimeType(String mimeType) {
        for (FileType type : FileType.values()) {
            if (type.getMimeType().equals(mimeType)) {
                return true;
            }
        }
        return false;
    }
}
