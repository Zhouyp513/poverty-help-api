package cn.poverty.common.utils.common;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**

 * @packageName cn.poverty.common.utils.common
 * @Description: 操作类的工具类
 * @date 2021-01-21
 */
public class ClassUtil {

    /**
     * 根据一个接口返回该接口的所有类
     * 
     * @date 2021/3/30
     * @param clz 接口
     * @return
     */
    public static List<Class> getAllClassByInterface(Class clz){
        List returnClassList = new ArrayList<Class>();
        //判断是不是接口,不是接口不作处理
        if(clz.isInterface()){
            //拿到当前包名
            String packageName = clz.getPackage().getName();
            try {
                //拿到当前包以及子包下的所有类
                List<Class> allClass = getClasses(packageName);

                //判断是否是一个接口
                for(int i = 0; i < allClass.size(); i++){
                    if(clz.isAssignableFrom(allClass.get(i))){
                        if(!clz.equals(allClass.get(i))){
                            returnClassList.add(allClass.get(i));
                        }
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return returnClassList;
    }

    /**
     * 根据包名拿到该包以及子包下的所有类不查找jar包中的
     * @param packageName 包名
     * @return List<Class>    包下所有类
     */
    private static List<Class> getClasses(String packageName)
            throws ClassNotFoundException,IOException{
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace(".", "/");
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while(resources.hasMoreElements()){
            URL resource = resources.nextElement();
            String newPath = resource.getFile().replace("%20", " ");
            dirs.add(new File(newPath));
        }
        ArrayList<Class> classes = new ArrayList<>();
        for(File directory:dirs){
            classes.addAll(findClass(directory, packageName));
        }
        return classes;
    }

    private static  List<Class> findClass(File directory, String packageName)
            throws ClassNotFoundException{
        List<Class> classes = new ArrayList<Class>();
        if(!directory.exists()){
            return classes;
        }
        File[] files = directory.listFiles();
        for(File file:files){
            if(file.isDirectory()){
                assert !file.getName().contains(".");
                classes.addAll(findClass(file, packageName+"."+file.getName()));
            }else if(file.getName().endsWith(".class")){
                classes.add(Class.forName(packageName+"."+file.getName().substring(0,file.getName().length()-6)));
            }
        }
        return classes;
    }

    /**
     * 根据注解拿到标注注解的所有类
     * 
     * @date 2021/3/30
     * @param annotationClass 注解类
     * @return List
     */
    public static List<Class> getAllClassByAnnotation(Class annotationClass){
        List returnClassList = new ArrayList<Class>();
        //判断是不是注解
        if(annotationClass.isAnnotation()){
            //拿到当前包名
            String packageName = annotationClass.getPackage().getName();
            try {
                //拿到当前包以及子包下的所有类
                List<Class> allClass = getClasses(packageName);

                for(int i = 0; i < allClass.size(); i++){
                    if(allClass.get(i).isAnnotationPresent(annotationClass)){
                        returnClassList.add(allClass.get(i));
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return returnClassList;
    }

}
