package cn.edu.whu.gds.util;

public enum Bucket {
    TILES("oge-user");

    private final String bucketName;

    Bucket(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
