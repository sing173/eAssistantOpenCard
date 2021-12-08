package com.dysen.commom_library.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import com.dysen.commom_library.bean.CommonBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by dysen on 2017/9/14.
 */

public class FileUtils {

    /**
     * 获取SD卡的路径
     *
     * @param newDir 新建的文件夹
     * @return
     */
    public static File getSDdir(String newDir) {
        String dirPath = Environment.getExternalStorageDirectory() + File.separator + newDir + File.separator;
        //建立文件下载的目录
        final File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 根据对象获取属性名
     *
     * @param o
     * @return
     */
    public List<String> getFiledName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        List<String> fieldNames = new ArrayList<String>();
        for (int i = 0; i < fields.length; i++) {
            fieldNames.add(fields[i].getName());
            // fields[i].getType();
        }

        return fieldNames;
    }

    /**
     * 根据属性值获取值
     *
     * @param fieldName
     * @param o
     * @return
     */
    public Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase(Locale.CHINA);
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 保存方法
     *
     * @param bitmap
     * @return
     */
    public static void saveBitmap(Bitmap bitmap, File f) {
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 刪除文件或文件夹
     */
    public static void deleteFileByFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFileByFile(files[i]);
                }
            }
            file.delete();
        }
    }

    /**
     * 取得文件夹大小
     *
     * @param f
     * @return
     * @throws Exception
     */
    public static long getFileSize(File f) {
        long size = 0;
        File flist[] = f.listFiles();
        if (flist != null) {
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {
                    size = size + getFileSize(flist[i]);
                } else {
                    size = size + flist[i].length();
                }
            }
        }
        return size;
    }

    /**
     * Created by hutian on 2018/1/26.
     * 读取Assets目录下的文件
     *
     * @Info
     */
    public static String readFromAssets(Context context, String fileName) {
        String content = "";
        try {
            InputStream is = context.getAssets().open(fileName);
            content = AnalysisInputStream(is);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return content;
    }

    /**
     * Created by hutian on 2018/1/26.
     * 解析输入的字节流
     *
     * @Info
     */
    private static String AnalysisInputStream(InputStream iso) throws Exception {
        InputStreamReader ireader = new InputStreamReader(iso);
        BufferedReader bd = new BufferedReader(ireader);
        StringBuffer sbr = new StringBuffer("");
        String str;
        while ((str = bd.readLine()) != null) {
            sbr.append(str);
            sbr.append(",");//每一行接“,”
            //sbr.append("\n");
        }
        return sbr.toString();
    }

    /**
     * Created by hutian on 2018/1/26.
     * 解析读取文本字符串是：==" "," "
     * "AD","AD:安道尔"
     * "AE","AE:阿联酋"
     * "AF","AF:阿富汗"
     * "AG","AG:安提瓜和巴布达"
     * 封装成JavaBean
     *
     * @Info
     */
    public static List<CommonBean.CountryBean> getCountArrayList(String str, int offset) {
        ArrayList<CommonBean.CountryBean> countryBeanList = new ArrayList<>();
        if (offset <= 0) {
            return countryBeanList;
        }
        //"AG","AG:安提瓜和巴布达",
        List<String> Alllist = Arrays.asList(str.split(","));
        ArrayList<String> NamelistTemp = new ArrayList<String>();
        ArrayList<String> Namelist = new ArrayList<String>();
        ArrayList<String> Idlist = new ArrayList<String>();
        //LogUtils.d("hut", "读取前面部分的是：==" + Alllist);
        int count = Alllist.size();
        //获取偶数位字符串
        for (int i = 0; i < count; i += offset) {
            //存取“AG”
            //Idlist.add(Alllist.get(i));
            //存取“AG:安提瓜和巴布达”
            NamelistTemp.add(Alllist.get(i + 1));
           // LogUtils.d("hut", "获取姓名=" + NamelistTemp.toString());
        }
        //在拆分"AG:安提瓜和巴布达"为“安提瓜和巴布达”
 /*       for(int i=0;i<NamelistTemp.size();i++){
            String result=NamelistTemp.get(i);
            result=(String) result.subSequence(result.indexOf(":")+1, result.length());
            Namelist.add(result);
        }*/
        //最终封装对象集合
        for (int i = 0; i < Idlist.size(); i++) {
            CommonBean.CountryBean countryBean = new CommonBean.CountryBean();
            countryBean.setId(Idlist.get(i));
            countryBean.setName(Namelist.get(i));
            countryBeanList.add(countryBean);
           // LogUtils.d("hut", "获取对象集合=" + countryBeanList.toString());
        }
        return countryBeanList;
    }

    public static String[] getArryString(String str, int offset) {
        if (offset <= 0) {
            return null;
        }
        str = str.replace("\"", "");
        //"AG","AG:安提瓜和巴布达",
        List<String> Alllist = Arrays.asList(str.split(","));
        ArrayList<String> Namelist = new ArrayList<String>();
      //LogUtils.d("hut", "读取前面部分的是：==" + Alllist);
        int count = Alllist.size();
        //获取偶数位字符串
        for (int i = 0; i < count; i += offset) {
            //存取“AG:安提瓜和巴布达”
            if (Alllist.get(i + 1).equals("CN:中国"))
                Namelist.add(0, Alllist.get(i + 1));
            else
            Namelist.add(Alllist.get(i + 1));
            //LogUtils.d("hut", "获取姓名=" + Namelist.toString());
        }

        String[] arry= new String[Namelist.size()];
            arry=Namelist.toArray(arry);


        return arry;
    }
    /**
     * Created by hutian on 2018/3/05
     *  */
    public static void writeFile(String path, String content) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (TextUtils.isEmpty(content)) {
            return;
        }
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(content.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * Created by hutian on 2018/5/05
     *  根据日期删除目录中的文件
     *  */
    public static void deleteLogFilebyDate(Context context,Date currentDate,int diff) {
        String logFileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "fkezslog";
        String logTransDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "fkezslog"+ File.separator+"Trans";
        String errorlogDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Crash"+ File.separator+"FkezsOpenCard";
        final File dir = new File(logFileDir);
        final File transdir = new File(logTransDir);
        if (!dir.exists()) {
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String date=DatetimeUtil.getTodayDate();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH,diff);
        String fileName="";
        Date deleteDate=calendar.getTime();
        String deleteFlag=format.format(deleteDate);
        //new File(dir, "logcat-" + simpleDateFormat1.format(new Date()) + ".txt"
        fileName="logcat-" + deleteFlag + ".txt";
        //1.删除LogCat控制台完整日志
        File file = new File(logFileDir, fileName);
        if (file.exists()) {
            file.delete();
        }
        if (!transdir.exists()) {
            return;
        }
        File files[] = transdir.listFiles();
        File tempFile;
        //2.删除7天之外的交易的日志日志
        if(files!=null) {
//            for (int i = 0; i < files.length; i++) {
//                String fileName=files[i].getName();
//            }
            int i=0;
                for (File childfile : files) {
                    if (childfile.isDirectory()) {
                        LogUtils.d("这是个文件夹");
                    } else {
                       String name=childfile.getName();
                        LogUtils.d("第"+i++ +"个"+"文件名称："+name);
                        //N00100_response_20180427.txt
                        if(name.contains(deleteFlag)){
                            childfile.delete();
                        }
                    }
                }
            }
        //3.删除异常日志，保留最近7天
        final File crashlog = new File(errorlogDir);
        if (!crashlog.exists()) {
            return;
        }
        //遍历异常日志目录下所以错误日志文件
        File allerror[] = crashlog.listFiles();
        //异常日志文件名
        fileName="crash-" + deleteFlag + ".log";
        //1.删除LogCat控制台完整日志
        File errorFile = new File(crashlog, fileName);
        if (file.exists()) {
            errorFile.delete();
        }

        }

}