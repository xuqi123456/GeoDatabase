package cn.edu.whu.gds.util;

public enum Bucket {
    TILES("geodata");  // 本地环境要改为bbl

    private final String bucketName;

    Bucket(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
