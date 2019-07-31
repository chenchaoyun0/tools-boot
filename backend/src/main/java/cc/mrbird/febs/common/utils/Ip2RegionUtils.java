package cc.mrbird.febs.common.utils;

import com.alibaba.fastjson.JSON;
import java.io.FileNotFoundException;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbMakerConfigException;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;

/**
 * 描述:
 *
 * @author chenchaoyun
 * @create 2019-07-20 15:00
 */
@Slf4j
public class Ip2RegionUtils {

  private static final String LOCALHOST = "localhost";

  private static final String UNKNOWN = "unknown";

  private static final String EMPTY = "empty";

  private static DbConfig config = null;
  private static DbSearcher searcher = null;

  static {
    String dbfile = "/u01/app/ip2region/ip2region.db";
    log.info("初始化 ip 地理位置数据库 begin...dbfilePath -> {}",dbfile);
    try {
      config = new DbConfig();
      searcher = new DbSearcher(config, dbfile);
      int indexBlockSize = searcher.getDbConfig().getIndexBlockSize();
      int totalHeaderSize = searcher.getDbConfig().getTotalHeaderSize();
      log.info("初始化 ip 地理位置数据库 end...indexBlockSize {},totalHeaderSize {}",indexBlockSize,totalHeaderSize);
    } catch (DbMakerConfigException e) {
      log.error("ip2region config init exception:" + e.getMessage());
    } catch (FileNotFoundException e) {
      log.error("ip2region file not found" + e.getMessage());
    }
  }

  public static DataBlock parseIp(String ip) throws IOException {
    DataBlock block = searcher.btreeSearch(ip);
    return block;
  }

  public static String getCityInfo(String ip) {
    try {

      if (StringUtils.isBlank(ip)) {
        return EMPTY;
      }

      if (LOCALHOST.equals(ip)) {
        return LOCALHOST;
      }

      boolean isIpAddress = Util.isIpAddress(ip);
      if (!isIpAddress) {
        return UNKNOWN + "-" + ip;
      }

      return parseIp(ip).getRegion();
    } catch (Exception e) {
      log.error("getCityInfo error ip->{}", ip, e);
    }
    return UNKNOWN + "-" + ip;
  }

  public static void main(String args[]) throws Exception {
    // 格式：国家|大区|省份|城市|运营商
    System.out.println(JSON.toJSONString(getCityInfo("localhost")));
    System.out.println(JSON.toJSONString(getCityInfo("127.0.0.1")));
    System.out.println(JSON.toJSONString(getCityInfo(null)));
  }
}