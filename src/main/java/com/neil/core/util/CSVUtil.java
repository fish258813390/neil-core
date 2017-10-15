package com.neil.core.util;


import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV操作(导出和导入)
 */
public class CSVUtil {

    /**
     * 导出
     *
     * @param filePath csv文件完整路径
     * @param dataList 数据
     * @return
     */
    public static void writeCsvFile(String filePath, List<String []> dataList) throws Exception{
        CsvWriter wr =new CsvWriter(filePath,',',Charset.forName("GBK"));
        try{
            for(String [] str:dataList){
                wr.writeRecord(str);
            }
        }catch(Exception e){
            throw e;
        }finally {
            wr.close();
        }
    }

    /**
     * 导入
     *
     * @param filePath csv文件完整路径
     * @return
     */
    public static List<String[]> readCsvFile(String filePath){
        List<String[]> list=new ArrayList<String[]>();
        CsvReader reader = null;
        try{
            reader = new CsvReader(filePath,',', Charset.forName("GBK"));
            //reader.readHeaders(); // 跳过表头   如果需要表头的话，不要写这句。
            while(reader.readRecord()){ //逐行读入除表头的数据
                list.add(reader.getValues());
            }
        }catch(Exception e){

        }finally {
            if(reader != null){
                reader.close();
            }
        }
        return list;
    }
}