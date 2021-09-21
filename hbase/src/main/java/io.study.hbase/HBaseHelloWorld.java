package io.study.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HBaseHelloWorld {
    static final Logger logger = LoggerFactory.getLogger(HBaseHelloWorld.class);
    static Connection connection;
    static Admin admin;
    /*static{
        try {
            connection = getConf();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    public static Connection getConf() throws IOException {
        Configuration config = HBaseConfiguration.create();
        //config.set("hbase.master",local);
        config.set("hbase.zookeeper.quorum", "localhost");

        connection = ConnectionFactory.createConnection(config);
        Admin admin = connection.getAdmin();
        return connection;
    }
    public static void putIntoHbase()throws IOException {
        TableName tn = TableName.valueOf("my_table2");
        HTable hTable = (HTable) connection.getTable(tn);
        Put p = new Put(Bytes.toBytes("3"));
        p.addColumn(Bytes.toBytes("f2"), Bytes.toBytes("ff2"), Bytes.toBytes("hbaseconnect6"));
        p.addColumn(Bytes.toBytes("f2"), Bytes.toBytes("ff3"), Bytes.toBytes("hbaseconnect6"));
        p.addColumn(Bytes.toBytes("f2"), Bytes.toBytes("ff4"), Bytes.toBytes("hbaseconnect6"));
        p.addColumn(Bytes.toBytes("f2"), Bytes.toBytes("ff5"), Bytes.toBytes("hbaseconnect6"));
        hTable.put(p);
        hTable.close();
    }
    public static  void scanTable() throws  IOException{
        TableName tn = TableName.valueOf("my_table2");

        HTable hTable = (HTable) connection.getTable(tn);
        Scan scan = new Scan();
        scan.addColumn(Bytes.toBytes("f16"),Bytes.toBytes("ff1"));
        ResultScanner scanner = hTable.getScanner(scan);

//        for (Result result = scanner.next(); result != null; result = scanner.next()) {
//            System.out.println("Found row : " + result);
//        }
        for (Result r :scanner){
            System.out.println("Found row : " + r);
        }
        scanner.close();
    }
    public static  void listTable() throws  IOException{
        HTableDescriptor[] tableDescriptor = admin.listTables();
        for (int i = 0; i < tableDescriptor.length; i++) {
            System.out.println(tableDescriptor[i].getNameAsString());
        }
    }
    public static void showTableDes()throws  IOException{
        TableName tn = TableName.valueOf("my_table2");
        HTableDescriptor hd =  admin.getTableDescriptor(tn);
        logger.info("hd.getFamilies():"+hd.getFamilies().toString());
    }

    public static void main(String[] args) throws IOException {
        getConf();
        putIntoHbase();
        System.out.println("computet");
    }
}

