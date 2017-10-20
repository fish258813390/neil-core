///**
// *
// */
//package com.neil.core.db.datasource;
//
//import java.sql.SQLException;
//
//import org.apache.tomcat.jdbc.pool.ConnectionPool;
//import org.apache.tomcat.jdbc.pool.JdbcInterceptor;
//import org.apache.tomcat.jdbc.pool.PooledConnection;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import oracle.jdbc.OracleConnection;
//
///**
// * @author neil 2016年9月28日
// *
// */
//public class RollbackInterceptor extends JdbcInterceptor{
//
//    private static final Logger LOG = LoggerFactory.getLogger(RollbackInterceptor.class);
//
//    /* (non-Javadoc)
//     * @see org.apache.tomcat.jdbc.pool.JdbcInterceptor#reset(org.apache.tomcat.jdbc.pool.ConnectionPool, org.apache.tomcat.jdbc.pool.PooledConnection)
//     */
//    @Override
//    public void reset(ConnectionPool parent, PooledConnection con) {
//        return;
//    }
//
//    private org.apache.tomcat.jdbc.pool.DataSource dataSource;
//    @Override
//    public void disconnected(ConnectionPool parent, PooledConnection con, boolean finalizing) {
//        // if its oracle make sure we rollback here before disconnect just in case a running TX is open
//        try {
//            if (con.getConnection().isWrapperFor(OracleConnection.class)) {
//                if (!con.getConnection().getAutoCommit()) {
//                    LOG.error("Connection {} with Auto-Commit false is going to be closed. Doing an explicit Rollback here!", con);
//                    try {
//                        con.getConnection().rollback();
//                    } catch (SQLException e) {
//                        LOG.error("Failed to rollback connection {} before closing it.", con, e);
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            LOG.error("Failed to check auto commit of connection {}", con, e);
//        }
//        super.disconnected(parent, con, finalizing);
//    }
//}
