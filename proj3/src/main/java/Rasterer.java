import java.util.HashMap;
import java.util.Map;


/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    public Rasterer() {
        // YOUR CODE HERE

    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();

        // Get query parameter from params
        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double lrlon = params.get("lrlon");
        double lrlat = params.get("lrlat");
        double w = params.get("w");
        double h = params.get("h");

        if (ullon > lrlon || ullat < lrlat) {
            results.put("query_success", false);
            return results;
        }

        // 第 1 步：计算用户查询范围的 LonDPP
        double queryLonDPP = (lrlon - ullon) / w;

        // 第 2 步：决定使用哪一层 tile（depth）
        double rootLonDPP = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / MapServer.TILE_SIZE;
        int d = 0;
        while (d < 7) {
            double thisLevelLonDPP = rootLonDPP / Math.pow(2, d);
            if (thisLevelLonDPP <= queryLonDPP) {
                break;
            }
            d++;
        }

        // 3. 确定所需 tile 的行列索引范围 (x, y)
        double lonTile = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, d);
        double latTile = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, d);

        int xMin = (int) Math.floor((ullon - MapServer.ROOT_ULLON) / lonTile);
        int xMax = (int) Math.floor((lrlon - MapServer.ROOT_ULLON) / lonTile);
        int yMin = (int) Math.floor((MapServer.ROOT_ULLAT - ullat) / latTile);
        int yMax = (int) Math.floor((MapServer.ROOT_ULLAT - lrlat) / latTile);

        int maxIndex = (int) Math.pow(2, d) - 1;
        xMin = clamp(xMin, 0, maxIndex);
        xMax = clamp(xMax, 0, maxIndex);
        yMin = clamp(yMin, 0, maxIndex);
        yMax = clamp(yMax, 0, maxIndex);

        if (xMin > xMax || yMin > yMax) {
            results.put("query_success", false);
            return results;
        }

        // 第 4 步：生成返回的二维文件名列表,拼装 render_grid
        int numCols = xMax - xMin + 1;
        int numRows = yMax - yMin + 1;
        String[][] renderGrid = new String[numRows][numCols];

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++){
                int xIndex = xMin + col;
                int yIndex = yMin + row;
                renderGrid[row][col] = "d" + d + "_x" + xIndex + "_y" + yIndex + ".png";
            }
        }

        //第 5 步：计算实际 raster 的最终边界
        double rasterUllon = MapServer.ROOT_ULLON + xMin * lonTile;
        double rasterLrlon = MapServer.ROOT_ULLON + (xMax + 1) * lonTile;
        double rasterUllat = MapServer.ROOT_ULLAT - yMin * latTile;
        double rasterLrLat = MapServer.ROOT_ULLAT - (yMax + 1) * latTile;
        int rasterWidth = (xMax - xMin + 1) * MapServer.TILE_SIZE;
        int rasterHeight = (yMax - yMin + 1) * MapServer.TILE_SIZE;

        // 第 6 步：将结果打包给前端
        results.put("render_grid", renderGrid);
        results.put("raster_ul_lon", rasterUllon);
        results.put("raster_ul_lat", rasterUllat);
        results.put("raster_lr_lon", rasterLrlon);
        results.put("raster_lr_lat", rasterLrLat);
        results.put("depth", d);
        results.put("query_success", true);
        results.put("raster_width", rasterWidth);
        results.put("raster_height", rasterHeight);

        return results;
    }

    /**
     * Helper method for clamp
     */
    private int clamp(int val, int minVal, int maxVal) {
        if (val < minVal) {
            return minVal;
        } else if (val > maxVal) {
            return maxVal;
        }
        return val;
    }

}
