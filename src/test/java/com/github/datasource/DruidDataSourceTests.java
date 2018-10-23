package com.github.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.github.SearchHouseApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sound.midi.Soundbank;
import javax.sql.DataSource;
import java.sql.SQLException;

public class DruidDataSourceTests extends SearchHouseApplicationTests {

    @Autowired
    DruidDataSource dataSource;

    @Test
    public void testConnection() throws SQLException {
        DruidPooledConnection connection = dataSource.getConnection();

        System.out.println("Druid连接-->" + connection);
    }
}
