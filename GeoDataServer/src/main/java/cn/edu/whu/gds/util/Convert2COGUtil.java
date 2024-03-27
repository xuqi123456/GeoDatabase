package cn.edu.whu.gds.util;

import lombok.extern.slf4j.Slf4j;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
import org.gdal.osr.SpatialReference;

@Slf4j
public class Convert2COGUtil {
//    private static String LIBRARIES_PATH = "D:/Projects/Java/oge-user-boot/lib/gdaldll";

    public static void process(String inputPath, String outputPath) {
        log.info("Start load gdal library");
//        librariesLoad();
        log.info("Start image dataset");
        Integer srid = null;
        Dataset originDataset = getTiffDataset(inputPath, srid);
        log.info("Translate to COG");
        translateToCOG(originDataset, outputPath, 256);
        originDataset.delete();
    }

//    public static void librariesLoad() {
//        File file = new File(LIBRARIES_PATH);
//        File[] tempList = file.listFiles();
//        for (int i = 0; i < tempList.length; i++) {
//            if (tempList[i].isFile()) {
//                System.load(String.valueOf(tempList[i]));
//            }
//        }
//    }

    public static void translateToCOG(Dataset inDs, String outputPath, int tileSize) {
        int imBands = inDs.GetRasterCount();
        for (int i = 0; i < imBands; i++) {
            // 获取nodata和波段统计值
            Double[] nodataVal = new Double[1];
            inDs.GetRasterBand(i + 1).GetNoDataValue(nodataVal);
            Double[] maxBandValue = new Double[1];
            inDs.GetRasterBand(i + 1).GetMaximum(maxBandValue);
        }

        int maxLevel;
        if (inDs.GetRasterXSize() >= inDs.GetRasterYSize()) {
            maxLevel = inDs.GetRasterYSize() / tileSize;
        } else {
            maxLevel = inDs.GetRasterXSize() / tileSize;
        }

        if (maxLevel % 2 != 0) {
            maxLevel -= 1;
        }

        // Calculate the number of levels
        int numLevels = (int) (Math.log(maxLevel) / Math.log(2));
        int[] levels = new int[numLevels];


        int i = 0;
        int level = 2;
        while (level < maxLevel + 1) {
            levels[i] = level;
            i++;
            level = (int) Math.pow(2, i + 1);
        }

        inDs.BuildOverviews("NEAREST", levels);

        gdal.SetConfigOption("GDAL_TIFF_INTERNAL_MASK", "YES");

        gdal.GetDriverByName("GTiff").CreateCopy(outputPath, inDs,
                new String[]{"COPY_SRC_OVERVIEWS=YES",
                        "TILED=YES",
                        "INTERLEAVE=BAND",
                        "BLOCKXSIZE=" + tileSize,
                        "BLOCKYSIZE=" + tileSize}).delete();
    }

    public static Dataset warpDataset(Dataset inDs, String proj, int resampling) {
        int[] resamplingModel = {gdalconst.GRA_NearestNeighbour, gdalconst.GRA_Bilinear, gdalconst.GRA_Cubic};
        int resampleAlg = resamplingModel[resampling];

        return gdal.AutoCreateWarpedVRT(inDs, null, proj, resampleAlg);
    }

    public static Dataset getTiffDataset(String fileDir, Integer srid) {
        gdal.AllRegister();
        Dataset dataset = gdal.Open(fileDir, gdalconst.GA_ReadOnly);
        if (dataset == null) {
            System.out.println(fileDir + "文件无法打开");
            return null;
        }

        SpatialReference fileSrs = new SpatialReference(dataset.GetProjectionRef());

        if (srid == null) {
            return dataset;
        } else {
            SpatialReference outSrs = new SpatialReference();
            outSrs.ImportFromEPSG(srid);

            if (fileSrs.IsSame(outSrs) == 0) {
                return warpDataset(dataset, outSrs.ExportToWkt(), 1);
            } else {
                return dataset;
            }
        }
    }
}
