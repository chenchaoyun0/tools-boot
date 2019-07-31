package cc.mrbird.febs.system.domain;

import cc.mrbird.febs.common.converter.TimeConverter;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_log")
@Excel("系统日志表")
public class SysLog implements Serializable {

    private static final long serialVersionUID = -8878596941954995444L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ExcelField(value = "操作人")
    private String username;

    @ExcelField(value = "操作描述")
    private String operation;

    @ExcelField(value = "耗时（毫秒）")
    private Long time;

    @ExcelField(value = "执行方法")
    private String method;

    @ExcelField(value = "方法参数")
    private String params;

    @ExcelField(value = "IP地址")
    private String ip;

    @ExcelField(value = "操作时间", writeConverter = TimeConverter.class)
    private Date createTime;

    private transient String createTimeFrom;
    private transient String createTimeTo;

    @ExcelField(value = "操作地点")
    private String location;

    //请求路径
    @ExcelField(value = "请求路径")
    private String path;

    //浏览器版本
    @ExcelField(value = "浏览器版本")
    private String browserType;

    // 浏览器名称和版本
    @ExcelField(value = "浏览器名称和版本")
    private String browserAndVersion;

    // 浏览器厂商
    @ExcelField(value = "浏览器厂商")
    private String manufacturer;

    // 浏览器引擎
    @ExcelField(value = "浏览器引擎")
    private String renderingEngine;

    // 系统名称
    @ExcelField(value = "系统名称")
    private String sysName;

    // 产品系列
    @ExcelField(value = "产品系列")
    private String operatingSystem;

    // 生成厂商
    @ExcelField(value = "生成厂商")
    private String sysManufacturer;

    // 设备类型
    @ExcelField(value = "设备类型")
    private String deviceType;

    @ExcelField(value = "请求信息")
    private String userAgent;

}