package cn.farwalker.waka.util;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * IO工具类
 *
 * @author Administrator
 */
public class IOUtil {

    /**
     * 关闭流
     *
     * @param stream
     * @return
     */
    public boolean close(Closeable stream) {
        try {
            if (stream != null) {
                stream.close();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void close(Connection cnt) {
        try {
            if (cnt != null && !cnt.isClosed()) {
                cnt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(Statement st) {
        try {
            if (st != null && !st.isClosed()) {
                st.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
