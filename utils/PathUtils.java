package utils;

public class PathUtils {

    private static final String P_PATH="D:\\IDEA-workspace\\图书馆javaswing\\images";

    //相对路径

    public static String getRealPath(String relativePath){
        return P_PATH+relativePath;
    }


}
