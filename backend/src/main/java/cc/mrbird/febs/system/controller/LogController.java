package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.IPUtil;
import cc.mrbird.febs.system.dao.LogMapper;
import cc.mrbird.febs.system.domain.SysLog;
import cc.mrbird.febs.system.domain.vo.LookResumeReq;
import cc.mrbird.febs.system.domain.vo.LookResumeResp;
import cc.mrbird.febs.system.service.LogService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("log")
public class LogController extends BaseController {

    private String message;

    @Autowired
    private LogService logService;

    @Autowired
    private LogMapper logMapper;

    @GetMapping
    @RequiresPermissions("log:view")
    public Map<String, Object> logList(QueryRequest request, SysLog sysLog) {
        return getDataTable(logService.findLogs(request, sysLog));
    }

    @Log("删除系统日志")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("log:delete")
    public void deleteLogss(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] logIds = ids.split(StringPool.COMMA);
            this.logService.deleteLogs(logIds);
        } catch (Exception e) {
            message = "删除日志失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log
    @PostMapping("excel")
    @RequiresPermissions("log:export")
    public void export(QueryRequest request, SysLog sysLog, HttpServletResponse response) throws FebsException {
        try {
            List<SysLog> sysLogs = this.logService.findLogs(request, sysLog).getRecords();
            ExcelKit.$Export(SysLog.class, response).downXlsx(sysLogs, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("访问主页")
    @RequestMapping(value = "/lookResume", method = RequestMethod.POST)
    @CrossOrigin
    public @ResponseBody LookResumeResp lookResume(@RequestBody LookResumeReq req, HttpServletRequest request) {
        log.info("=================== 查看网站主页 http://www.shopbop.ink/ =========================");
        log.info("查看网站主页 req:{}", JSONObject.toJSONString(req));
        log.info("浏览器的正式名称:{}", req.getAppName());
        log.info("浏览器的版本号:{}", req.getAppVersion());
        log.info("返回用户浏览器是否启用了cookie:{}", req.getCookieEnabled());
        log.info("返回用户计算机的cpu的型号:{}", req.getCpuClass());
        log.info("浏览器正在运行的操作系统平台:{}", req.getPlatform());
        log.info("浏览器的产品名（IE没有）:{}", req.getProduct());
        log.info("浏览器正在运行的操作系统，其中可能有CPU的信息（IE没有）:{}", req.getOscpu());
        log.info("关于浏览器更多信息（IE没有）:{}", req.getProductSub());
        log.info("浏览器 userAgent:{}", req.getUserAgent());
        log.info("返回一个UserProfile对象，它存储用户的个人信息（火狐没有）:{}", req.getUserProfile());
        String ip = IPUtil.getIpAddr(request);
        log.info("用户ip地址:{}", ip);

        LookResumeResp resp = new LookResumeResp();
        // resp
        // 当日访问量
        long todayVisitCount=logMapper.findTodayVisitCount();
        resp.setTodayCount(todayVisitCount);
        // 当日访问人数
        long todayIp=logMapper.findTodayIp();
        resp.setTodayVisitorCount(todayIp);
        // 网站总访问量
        Wrapper<SysLog> objectWrapper = new QueryWrapper<>();
        Integer totalcount = logMapper.selectCount(objectWrapper);
        resp.setTotalcount(totalcount);
        // 简历访问量
        long resumeCount=logMapper.selectResumeCount();
        resp.setResumeCount(resumeCount);
        // 总访问人数
        long totalVisitorCount=logMapper.selectTotalVisitorCount();
        resp.setTotalVisitorCount(totalVisitorCount);

        resp.setMsg("操作成功");
        log.info("查看网站主页 resp:{}", JSONObject.toJSONString(resp));
        return resp;
    }
}
