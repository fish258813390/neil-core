package com.neil.core.util;

import com.alibaba.fastjson.annotation.JSONField;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by aladdin on 2016-03-14.
 *创建Excel文件
 * 创建流程：<br/>
 * open();<br/>
 * createTitle();<br/>
 * appendData(); 循环写入<br/>
 * close();<br/>
 */
public class WriteExcelUtil {
    private OutputStream out;
    private Workbook workbook;
    private Sheet sheet;
    private CellStyle cellStyle;

    /**
     * 创建并打开文件
     * @param filePath 文件路径
     * @throws Exception
     */
    public void open(String filePath) throws Exception{
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {// 如果父目录不存在则创建父目录
            file.getParentFile().mkdirs();
        }
        workbook = new SXSSFWorkbook(100);
        out = new FileOutputStream(file);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        out = new FileOutputStream(file);
        sheet = workbook.createSheet("导出数据");
        cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
    }

    /**
     * 写入列头
     * @param title 第一行列头信息数组
     */
    public void createTitle(String [] title){
        createRow(workbook, sheet, title, 0);
    }

    /**
     * 写入数据
     * @param data 字符串数组类型集合
     */
    public void appendData(List<String[]> data){
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); ++i) {
                createRow(workbook, sheet, data.get(i), i + 1);
            }
        }
    }

    /**
     * 写入数据
     * @param data 数据集合
     * @param fields 需要写入的model属性名称
     * @param map 字段值映射信息 例：<br/>
     *            <p>
     * <pre>
     * Map < String,Map < String,String > > fieldValMap = new HashMap < > ();<br/>
     * Map < String,String > keyValMap = new HashMap < > ();<br/>
     * keyValMap.put("1","成功");<br/>
     * keyValMap.put("-1","失败");<br/>
     * fieldValMap.put("status",keyValMap);<br/>
     *            </pre>
     *            </p>
     * @throws Exception
     */
    public <T> void  appendData(List<T> data, String[] fields,Map<String, Map<String, String>> map) throws Exception{
        if (data != null && data.size() > 0) {
            int startRowNo = sheet.getLastRowNum();
            for (int i = 0; i < data.size(); ++i) {
                T t = data.get(i);
                Row row = sheet.createRow(startRowNo+i + 1);
                for (int j = 0; j < fields.length; ++j) {
                    try {
                        Method method = t.getClass().getMethod("get"+fields[j].substring(0,1).toUpperCase()
                                +fields[j].substring(1,fields[j].length()));
                        boolean bool = method.isAnnotationPresent(JSONField.class);
                        String value = "";
                        Object obj = method.invoke(t);
                        if(bool) {
                            JSONField jsonField = method.getAnnotation(JSONField.class);
                            String format = jsonField.format();
                            if(format != null && !"".equals(format.trim()) && obj != null){
                                value = DateUtil.getDateTime(format,(Date)obj );
                            }
                        }else{
                            value = String.valueOf(obj == null ?"":obj);
                        }
                        String valueMapping = null;
                        if(map != null){
                            Map<String,String> dicMap = map.get(fields[j]);
                            if(dicMap != null){
                                valueMapping = dicMap.get(value);
                            }
                        }
                        if(valueMapping !=null){
                            value = valueMapping;
                        }
                        createCell(workbook,row,value,j);
                    } catch (NoSuchMethodException e) {
                        throw new Exception("没有找到属性" + fields[j] + "对应的get方法");
                    }
                }
            }
        }
    }

    public <T> void  appendData(List<T> data, String[] fields) throws Exception{
        if (data != null && data.size() > 0) {
            int startRowNo = sheet.getLastRowNum();
            for (int i = 0; i < data.size(); ++i) {
                T t = data.get(i);
                Row row = sheet.createRow(startRowNo+i + 1);
                for (int j = 0; j < fields.length; ++j) {
                    try {
                        Method method = t.getClass().getMethod("get"+fields[j].substring(0,1).toUpperCase()
                                +fields[j].substring(1,fields[j].length()));
                        createCell(workbook,row,String.valueOf(method.invoke(t)),j);
                    } catch (NoSuchMethodException e) {
                        throw new Exception("没有找到属性" + fields[j] + "对应的get方法");
                    }
                }
            }
        }
    }

    public void close() throws Exception{
        if(workbook != null && out != null){
            workbook.write(out);
        }
        if(out != null){
            out.close();
        }
    }

    private void createCell(Workbook workbook, Row row, String str, int cellIndex) {
        Cell cell = row.createCell(cellIndex);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue(str);
        cell.setCellStyle(cellStyle);
    }

    private void createRow(Workbook workbook, Sheet sheet, String[] strArray, int rowIndex) {
        Row row = sheet.createRow(rowIndex);
        for (int i = 0; i < strArray.length; ++i) {
            createCell(workbook, row, strArray[i], i);
        }
    }

    public <T> String[] objToString(T t, String[] fields, Map<String, Map<String, String>> map) throws Exception {
        String[] tStr = new String[fields.length];
        for (int j = 0; j < fields.length; ++j) {
            try {
                Method method = t.getClass().getMethod(fields[j]);
                String value = String.valueOf(method.invoke(t));
                String valueMapping = null;
                if(map != null){
                    Map<String,String> dicMap = map.get(fields[j]);
                    if(dicMap != null){
                        valueMapping = dicMap.get(value);
                    }
                }
                if(valueMapping ==null){
                    tStr[j] = value;
                }else{
                    tStr[j] = valueMapping;
                }
            } catch (NoSuchMethodException e) {
                throw new Exception("没有找到属性" + fields[j] + "对应的get方法");
            }
        }
        return tStr;
    }
}
