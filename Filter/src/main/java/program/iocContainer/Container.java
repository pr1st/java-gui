package program.iocContainer;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;

public class Container {

    public Container(File directory, String pckge) {
        componentScan(directory, pckge).forEach(this::scanClass);
        container.keySet().forEach(this::createInstance);
        singletons.forEach((k, v) -> constructInstance(v));
        container.forEach((k, v) -> {
            try {
                if (v.postConstruct != null)
                    v.postConstruct.invoke(getBean(k));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private class BeanDefinition {
        List<Field> fields = new ArrayList<>();
        Method postConstruct;
    }

    private Map<Class<?>, BeanDefinition> container = new HashMap<>();
    private Map<Class<?>, Object> singletons = new HashMap<>();

    public <T> T getBean(Class<T> tClass) {
        return (T)singletons.get(tClass);
    }

    private void addRequierement(Class target, Field inThisField) {
        container.get(target).fields.add(inThisField);
    }

    private void addPostCOnstructMethod(Class target, Method method) {
        container.get(target).postConstruct = method;
    }


    private void scanClass(Class target) {
        if (!target.isAnnotationPresent(SingletonBean.class))
            return;

        container.put(target, new BeanDefinition());

        BiConsumer<Class, Class> scan = (targetClass, classToPutInto) -> {
            Field[] fields = targetClass.getDeclaredFields();
            Method[] methods = targetClass.getDeclaredMethods();

            for (Field field : fields) {
                if (field.isAnnotationPresent(Inject.class)) {
                    field.setAccessible(true);
                    addRequierement(classToPutInto, field);
                }
            }

            for (Method method : methods) {
                if (method.isAnnotationPresent(PostConstruct.class)) {
                    method.setAccessible(true);
                    addPostCOnstructMethod(classToPutInto, method);
                    break;
                }
            }
        };

        scan.accept(target, target);


        Class pClass = target.getSuperclass();
        if (pClass != null) {
            scan.accept(pClass, target);
        }
    }

    private void createInstance(Class cl) {
        try {
            singletons.put(cl, cl.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void constructInstance(Object object) {
        try {
            List<Field> fields = container.get(object.getClass()).fields;
            for (Field field : fields) {
                field.set(object, getBean(field.getType()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static List<Class<?>> componentScan(File directory, String pkgname) {

        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

        // Get the list of the files contained in the package
        String[] files = directory.list();
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i];
            String className = null;

            // we are only interested in .class files
            if (fileName.endsWith(".class")) {
                // removes the .class extension
                if (pkgname.equals(""))
                    className = fileName.substring(0, fileName.length() - 6);
                else
                    className = pkgname + '.' + fileName.substring(0, fileName.length() - 6);
            }

            if (className != null) {
                classes.add(loadClass(className));
            }

            //If the file is a directory recursively class this method.
            File subdir = new File(directory, fileName);
            if (subdir.isDirectory()) {
                if (pkgname.equals(""))
                    classes.addAll(componentScan(subdir, fileName));
                else
                    classes.addAll(componentScan(subdir, pkgname + '.' + fileName));
            }
        }
        return classes;
    }

    private static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException("Unexpected ClassNotFoundException loading class '" + className + "'");
        }
    }
}
