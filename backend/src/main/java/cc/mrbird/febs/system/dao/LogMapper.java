package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.system.domain.SysLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

public interface LogMapper extends BaseMapper<SysLog> {


  @Select("select count(1) from t_log where datediff(CREATE_TIME,now())=0")
  long findTodayVisitCount();

  @Select("select count(distinct(ip)) from t_log where datediff(CREATE_TIME,now())=0")
  long findTodayIp();

  @Select("select count(1) from t_log where path='/log/lookResume'")
  long selectResumeCount();

  @Select("select count(distinct(ip)) from t_log")
  long selectTotalVisitorCount();
}