package cn.edu.whu;

import cn.edu.whu.util.SAXHandle;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class TileStoreMain {
    private static void kmlToDB() throws ParserConfigurationException, SAXException, IOException, SQLException {
        String url = "jdbc:postgresql://localhost:5432/geodata";
        String user = "postgres";
        String password = "123456";
        Driver driver = new org.postgresql.Driver();

        Properties prop = new Properties();
        prop.setProperty("user", user);
        prop.setProperty("password", password);

        Connection conn = driver.connect(url, prop);
        Statement stmt = conn.createStatement();

        //1.或去SAXParserFactory实例
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //2.获取SAXparser实例
        SAXParser saxParser = factory.newSAXParser();
        //创建Handel对象
        SAXHandle handle = new SAXHandle();

        String pre = "/media/whu/New_Y";
        String[] paths = {"/吉林省"};
        for (String path : paths) {
            String rootPath = pre + path;
            File rootDir = new File(rootPath);
            File[] subFiles = rootDir.listFiles();
            for (File subFile : subFiles) {
                if (subFile.isFile() && subFile.getName().endsWith(".kml")) {
                    saxParser.parse(rootPath + "/" + subFile.getName(), handle);
                    String code = subFile.getName().split("\\.")[0];
                    String name = "'" + code + ".tif'";
                    String p = "'" + path + "/" + code + "_大图/L19'";
                    String bbox = "ST_GeomFromText('"+ handle.getWkt() + "', 4326)";
                    String province = "'" + path.substring(1) + "'";
                    String sql = "insert into gd_tiff(name, path, bbox, create_time, province) values("
                            + name + ", "+ p + ", " + bbox + ", now(), " + province + ");";
                    System.out.println(sql);
                    int a = stmt.executeUpdate(sql);
                }
            }
        }

        stmt.close();
        conn.close();
    }

    private static void tiffToDB() throws ParserConfigurationException, SAXException, IOException, SQLException {
        gdal.AllRegister();
        Dataset dataset = gdal.Open("/media/whu/New_Y/吉林省/K51F001027_大图/L19/K51F001027.tif", gdalconst.GA_ReadOnly);
        double[] geoTransform = dataset.GetGeoTransform();
//        for (double g : geoTransform) {
//            System.out.println(g);
//        }
        System.out.println(dataset.GetRasterXSize());
        System.out.println(dataset.GetRasterYSize());
        System.out.println(geoTransform[0]);
        System.out.println(geoTransform[3]);
        System.out.println(geoTransform[0] + dataset.GetRasterXSize() * geoTransform[1]);
        System.out.println(geoTransform[3] + dataset.GetRasterYSize() * geoTransform[5]);
        System.out.println("xccc");
//        String url = "jdbc:postgresql://localhost:5432/geodata";
//        String user = "postgres";
//        String password = "123456";
//        Driver driver = new org.postgresql.Driver();
//
//        Properties prop = new Properties();
//        prop.setProperty("user", user);
//        prop.setProperty("password", password);
//
//        Connection conn = driver.connect(url, prop);
//        Statement stmt = conn.createStatement();
//
//        //1.或去SAXParserFactory实例
//        SAXParserFactory factory = SAXParserFactory.newInstance();
//        //2.获取SAXparser实例
//        SAXParser saxParser = factory.newSAXParser();
//        //创建Handel对象
//        SAXHandle handle = new SAXHandle();
//
//        String pre = "/media/whu/New_Y";
//        String[] paths = {"/吉林省"};
//        for (String path : paths) {
//            String rootPath = pre + path;
//            File rootDir = new File(rootPath);
//            File[] subFiles = rootDir.listFiles();
//            for (File subFile : subFiles) {
//                if (subFile.isFile() && subFile.getName().endsWith(".kml")) {
//                    saxParser.parse(rootPath + "/" + subFile.getName(), handle);
//                    String code = subFile.getName().split("\\.")[0];
//                    String name = "'" + code + ".tif'";
//                    String p = "'" + path + "/" + code + "_大图/L19'";
//                    String bbox = "ST_GeomFromText('"+ handle.getWkt() + "', 4326)";
//                    String province = "'" + path.substring(1) + "'";
//                    String sql = "insert into gd_tiff(name, path, bbox, create_time, province) values("
//                            + name + ", "+ p + ", " + bbox + ", now(), " + province + ");";
//                    System.out.println(sql);
//                    int a = stmt.executeUpdate(sql);
//                }
//            }
//        }
//
//        stmt.close();
//        conn.close();
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, SQLException {
        System.out.println("Hello world!");

//        kmlToDB();
        tiffToDB();
    }
}