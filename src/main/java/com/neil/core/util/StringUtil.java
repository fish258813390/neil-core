package com.neil.core.util;


public class StringUtil {

    /**
     * 生成固定长度的数字随机码
     * @param length 长度必须大于0 小于等于 18
     * @return
     */
    public static String randomCode(int length) throws Exception{
        if(length > 0 && length <= 18){
            String str = "1";
            for(int i =1; i< length;++i){
                str += "0";
            }
            return String.valueOf((long)((Math.random()*9+1)*Long.parseLong(str)));
        }
        throw new Exception("长度必须在1到18之间");
    }

    /**
     * String[] 数组转 Long[] 数组
     * @param args
     * @return
     */
    public static Long[] parseLong(String[] args){
        Long[] array = new Long[args.length];
        for(int i=0; i<args.length; i++){
            if(args[i] != null && !"".equals(args[i].trim())){
                array[i] = Long.parseLong(args[i]);
            }
        }
        return array;
    }

    public static long[] parseLongPrimitive(String[] args){
        long[] array = new long[args.length];
        for(int i=0; i<args.length; i++){
            if(args[i] != null && !"".equals(args[i].trim())){
                array[i] = Long.parseLong(args[i]);
            }
        }
        return array;
    }

    /**
     * String[] 数组转 Double[] 数组
     * @param args
     * @return
     */
    public static Double[] parseDouble(String[] args){
        Double[] array = new Double[args.length];
        for(int i=0; i<args.length; i++){
            if(args[i] != null && !"".equals(args[i].trim())){
                array[i] = Double.parseDouble(args[i]);
            }
        }
        return array;
    }

    public static double[] parseDoublePrimitive(String[] args){
        double[] array = new double[args.length];
        for(int i=0; i<args.length; i++){
            if(args[i] != null && !"".equals(args[i].trim())){
                array[i] = Double.parseDouble(args[i]);
            }
        }
        return array;
    }

    /**
     * String[] 数组转 Integer[] 数组
     * @param args
     * @return
     */
    public static Integer[] parseInteger(String[] args){
        Integer[] array = new Integer[args.length];
        for(int i=0; i<args.length; i++){
            if(args[i] != null && !"".equals(args[i].trim())) {
                array[i] = Integer.parseInt(args[i]);
            }
        }
        return array;
    }

    public static int[] parseIntegerPrimitive(String[] args){
        int[] array = new int[args.length];
        for(int i=0; i<args.length; i++){
            if(args[i] != null && !"".equals(args[i].trim())) {
                array[i] = Integer.parseInt(args[i]);
            }
        }
        return array;
    }

    /**
     * 将Strng[]数组， 转换成以，号分割的字符串
     * @return
     */
    public static String arrayToStr(String[] args){
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<args.length; i++){
            sb.append(args[i]).append(",");
        }
        String str = sb.toString();
        return str.substring(0,str.length()-1);
    }

    /**
     * 将int[]数组， 转换成以，号分割的字符串
     * @return
     */
    public static String arrayToStr(int [] args){
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<args.length; i++){
            sb.append(String.valueOf(args[i])).append(",");
        }
        String str = sb.toString();
        return str.substring(0,str.length()-1);
    }

    /**
     * 将long[]数组， 转换成以，号分割的字符串
     * @return
     */
    public static String arrayToStr(long [] args){
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<args.length; i++){
            sb.append(String.valueOf(args[i])).append(",");
        }
        String str = sb.toString();
        return str.substring(0,str.length()-1);
    }

    /**
     * 增加空格到字符串的右边
     * @param str 需要增加空格的字符串
     * @param total 需要增加所达到的字符串长度
     * @return
     */
    public static String increaseSpaceToRight(String str, int total)
    {
        int lenth = str.getBytes().length;
        int number = total - lenth;
        if (number < 0){
            return "";
        }
        StringBuffer sb = new StringBuffer(str);
        for (int i = 0; i < number; i++){
            sb.append(" ");
        }
        return sb.toString();
    }

    public static String increaseZeroToLeft(String str,int total){
        int length = str.getBytes().length;
        if(total - length > 0){
            for(int i = 0; i < (total - length); ++i){
                str = "0"+str;
            }
        }
        return str;
    }

    /**
     * 将字符串中间部分数据变更为*号
     * @param str
     * @param leftLength 左边明文长度
     * @param rigthLength 右边明文长度
     * @return
     */
    public static String increaseAsteriskToLeftAndRight(String str,int leftLength,int rightLength)throws Exception{
        if(str != null && str.length() > 1){
            if(leftLength + rightLength <= str.length()){
                String leftStr = str.substring(0,leftLength);
                String rightStr = str.substring(str.length()- rightLength,str.length());
                String asterisk = "";
                for(int i = 0;i < str.length()-(leftLength+rightLength);++i){
                    asterisk+="*";
                }
                return leftStr+asterisk + rightStr;
            }else{
                throw new Exception("左右明文总长度大于字符串长度，无法处理");
            }
        }
        return "";
    }
}
