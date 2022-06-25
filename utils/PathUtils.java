package utils;

public class PathUtils {
//修改为自己的文件路径
    private static final String P_PATH="D:\\xxx\\xxx\\images";
    

    //相对路径

    public static String getRealPath(String relativePath){
        return P_PATH+relativePath;
    }


}
