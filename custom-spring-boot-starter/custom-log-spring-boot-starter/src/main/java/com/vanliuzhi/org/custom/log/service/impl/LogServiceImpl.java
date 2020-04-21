// package com.vanliuzhi.org.custom.log.service.impl;
//
// import com.open.capacity.common.model.SysLog;
// import com.open.capacity.datasource.annotation.DataSource;
// import com.vanliuzhi.org.custom.log.dao.LogDao;
// import com.vanliuzhi.org.custom.log.service.LogService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.scheduling.annotation.Async;
//
// import java.util.Date;
//
// /**
//  * @Description 切换数据源，存储log-center
//  * @Author VanLiuZhi
//  * @Date 2020-04-19 23:48
//  */
// public class LogServiceImpl implements LogService {
//
//     @Autowired
//     private LogDao logDao;
//
//     @Async
//     @Override
//     @DataSource(name = "log")
//     public void save(SysLog log) {
//         if (log.getCreateTime() == null) {
//             log.setCreateTime(new Date());
//         }
//         if (log.getFlag() == null) {
//             log.setFlag(Boolean.TRUE);
//         }
//
//         logDao.save(log);
//     }
//
// }
