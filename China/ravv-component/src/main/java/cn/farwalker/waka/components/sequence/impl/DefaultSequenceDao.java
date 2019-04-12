/**
 * 
 */
package cn.farwalker.waka.components.sequence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.sql.DataSource;

import cn.farwalker.waka.core.WakaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
 


import cn.farwalker.waka.components.sequence.SequenceRange;
import cn.farwalker.waka.components.sequence.iface.ISequenceDao;
 

/**
 * 这里直接使用jdbc处理
 * @author mingxing.fmx
 * drop table tsh_sequence;
create table tsh_sequence(
    name VARCHAR(64) NOT NULL ,
    val BIGINT  NOT NULL , 
    primary key (name)
) ;
 */
@Component
public class DefaultSequenceDao implements ISequenceDao {
    private static final Logger log = LoggerFactory.getLogger(DefaultSequenceDao.class); 
 
    //drop table tsh_sequence;
    private static final String D_CREATE="CREATE TABLE "+ D_TABLE_NAME +"(" + D_COLUMN_NAME +" VARCHAR(64) NOT NULL ," + D_COLUMN_VALUE
            + " BIGINT  NOT NULL , PRIMARY KEY (" + D_COLUMN_NAME +")) ";
    private static final String updateSql ="UPDATE " + D_TABLE_NAME + " SET "  + D_COLUMN_VALUE + " = ? WHERE "+ D_COLUMN_NAME + " = ? AND " + D_COLUMN_VALUE + " = ?"; 
    private static final String selectSql = "SELECT "+ D_COLUMN_VALUE + " FROM "+ D_TABLE_NAME+ " WHERE "+ D_COLUMN_NAME +" = ?";  
    private static final String insertSql = "INSERT INTO "+ D_TABLE_NAME + "(" + D_COLUMN_NAME +"," + D_COLUMN_VALUE +") VALUES(?,?)";
 
 
    @Resource
    /** 数据源*/
    private DataSource dataSource; 
    
    /** 取值, -1没有记录,-999:异常 */
    private long getDatabaseValue(String name) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(getSelectSql());
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            boolean rnext = rs.next();
            if(rnext){
                Number v = (Number) rs.getObject(1);
                long oldValue = (v == null || v.longValue() < 0 ? 0 : v.longValue()); 
                return oldValue ;
            }
            else{
                return -1;//属于
            }
        } catch (SQLException e) { 
            //throw new SequenceException(e);
            //可能没有创建表
            log.error("数据键值出错:" + name,e);
            return -999;
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }
    }
    /**
     * 回写数据库值
     * @param name
     * @param newValue
     * @param oldValue
     * @throws SQLException
     */
    private void setDatabaseValue(String name,long newValue,long oldValue) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null; 
        Boolean readOnly = null; 
        try {
            conn = dataSource.getConnection();
            readOnly =Boolean.valueOf(conn.isReadOnly());
            conn.setReadOnly(false);
            if(oldValue >=0){
                stmt = conn.prepareStatement(getUpdateSql()); 
                stmt.setLong(1, newValue); 
                stmt.setString(2, name);
                stmt.setLong(3, oldValue);
                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new WakaException(updateSql  + ":" + name +"," + newValue + "," + oldValue + "，没有更新记录！");
                } 
            }
            else{
                stmt = conn.prepareStatement(getInsertSql());
                stmt.setString(1, name); 
                stmt.setLong(2, newValue);  
                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new WakaException(insertSql  + ":" + name +"," + newValue + "," + oldValue + "，没有更新记录！");
                } 
            } 
        } catch (SQLException e) {
            throw new WakaException("执行SQL:" + e.getMessage());
        } finally {
            if(readOnly!=null && readOnly.booleanValue()){
                conn.setReadOnly(true);//只有为true时，才设置true
            }
            close(stmt); 
            close(conn); 
        }
    }
    /** 创建表*/
    private void createTable() throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        Boolean readOnly = null; 
        try {
            conn = dataSource.getConnection();
            readOnly =Boolean.valueOf(conn.isReadOnly());
            conn.setReadOnly(false);
            
            stmt = conn.createStatement();
            stmt.executeUpdate(D_CREATE);
        } finally {
            if(readOnly!=null && readOnly.booleanValue()){
                conn.setReadOnly(true);//只有为true时，才设置true
            }
            close(rs);
            close(stmt);
            close(conn);
        }
    }
    
    @Override
    public SequenceRange nextRange(String name,int pstep)  {
        if (name == null) {
            throw new IllegalArgumentException("序列名称不能为空");
        } 
        int step = checkStep(pstep);
        SequenceRange rs = null;
        for (int i = 0; rs == null && i < DEFAULT_RETRY_TIMES ; i++) {
            try{
                //取最大值
                long oldValue =  getDatabaseValue(name);
                long newValue  ;
                if(oldValue == -999){ 
                    createTable(); 
                    newValue=step;
                    oldValue=-1;
                }
                else if(oldValue == -1){
                    newValue=step;
                }
                else{
                    newValue = oldValue + step;
                }
                
                //更新值
                setDatabaseValue(name,newValue,oldValue);
                rs = new SequenceRange(newValue - step + 1, newValue);
            }
            catch(Exception e){
                log.error("取序列名称出错:" , e);
            }
        } 
        if(rs == null){
            throw new WakaException("取序列名称失败!");
        }
        return rs;
    }

    private String getSelectSql() { 
        return selectSql;
    }

    private String getUpdateSql() { 
        return updateSql;
    }
    private String getInsertSql() { 
        return insertSql;
    }
    private static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.debug("Could not close JDBC ResultSet", e);
            } catch (Throwable e) {
                log.debug("Unexpected exception on closing JDBC ResultSet", e);
            }
        }
    }

    private static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.debug("Could not close JDBC Statement", e);
            } catch (Throwable e) {
                log.debug("Unexpected exception on closing JDBC Statement", e);
            }
        }
    }

    private static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.debug("Could not close JDBC Connection", e);
            } catch (Throwable e) {
                log.debug("Unexpected exception on closing JDBC Connection", e);
            }
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
 
 
    /** 检查步长*/
    private int checkStep(int step) {
        if (step < MIN_STEP || step > MAX_STEP) {
            StringBuilder message = new StringBuilder();
            message.append("Property step out of range [").append(MIN_STEP);
            message.append(",").append(MAX_STEP).append("], step = ").append(step);

            throw new IllegalArgumentException(message.toString());
        }
        return step;
    }
 
}
