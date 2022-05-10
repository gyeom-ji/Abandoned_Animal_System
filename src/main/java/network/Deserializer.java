package network;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deserializer {
    private final static int LEN_MAX_CLASSNAME = 64;
    private final static int LEN_LENGTH_FIELD = 4;
    private final static int LEN_COUNT_FIELD = 4;

    public static Object bytesToObjectArr(byte[] bytes) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String className = new String(
                bytes, 0, LEN_MAX_CLASSNAME
        ).trim();

        int count = bytesToInt(bytes, LEN_MAX_CLASSNAME);

        className = className.replace("[L","");
        Class clazz = Class.forName(className);
//        Constructor constructor = clazz.getDeclaredConstructor(); //TODO : 각객체는 빈생성자 가져야함
        Object objectArr = Array.newInstance(clazz, count);

        int cursor = LEN_MAX_CLASSNAME+LEN_COUNT_FIELD;
        for(int i=0; i<count; i++){
            String objClassName = new String(
                    bytes, cursor, LEN_MAX_CLASSNAME
            ).trim();
            Class objClass = Class.forName(objClassName);
            Constructor constructor = objClass.getDeclaredConstructor();
            Object obj = constructor.newInstance();

            cursor += LEN_MAX_CLASSNAME;
            for(Field f : getAllFields(objClass)){
                int length = bytesToInt(bytes, cursor);
                cursor += LEN_LENGTH_FIELD;
                setData(obj, f, bytes, cursor, length);
                cursor += length;
            }

            Array.set(objectArr, i, obj);
        }

        return objectArr;
    }

    //TODO : 예외처리필요
    public static Object bytesToObject(byte[] bytes) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String className = new String(
                bytes, 0, LEN_MAX_CLASSNAME
        ).trim();


        Class clazz = Class.forName(className);
        Constructor constructor = clazz.getDeclaredConstructor(); //TODO : 각객체는 빈생성자 가져야함

        int cursor = LEN_MAX_CLASSNAME;
        Object obj = constructor.newInstance();

        for(Field f : getAllFields(clazz)){
            int length = bytesToInt(bytes, cursor);
            cursor += LEN_LENGTH_FIELD;
            setData(obj, f, bytes, cursor, length);
            cursor += length;
        }

        return obj;
    }

    private static List<Field> getAllFields(Class clazz){
        if(clazz==Object.class){
            return Collections.emptyList();
        }

        List<Field> result = new ArrayList<>(getAllFields(clazz.getSuperclass()));
        for(Field f : clazz.getDeclaredFields()){
            if(Modifier.isStatic(f.getModifiers())){
                continue;
            }

            result.add(f);
        }

        return result;
    }

    private static void setData(Object obj, Field f, byte[] bytes, int cursor, int length) throws IllegalAccessException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        String typeName = f.getType().getSimpleName();
        f.setAccessible(true);

        if(typeName.contains("[")){
            typeName = "array";
        }

        //TODO : primitive 타입 추가필요
        switch(typeName){
            case "byte":
                f.setByte(obj, bytes[cursor]);
                return;
            case "char":
                f.setChar(obj, bytesToChar(bytes, cursor));
                return;
            case "short":
                f.setShort(obj, bytesToShort(bytes, cursor));
                return;
            case "int":
                f.setInt(obj, bytesToInt(bytes, cursor));
                return;
            case "long":
                f.setLong(obj, bytesToLong(bytes, cursor));
                return;
            case "String":
                f.set(obj, new String(bytes, cursor, length));
                return;
            case "array":{
                if(length==0){
                    f.set(obj, null);
                    return;
                }
                byte[] a = new byte[length];
                System.arraycopy(bytes, cursor, a, 0, length);
                f.set(obj, bytesToObjectArr(a));
                return;
            }
            default:{
                if(length==0){
                    f.set(obj, null);
                    return;
                }
                byte[] a = new byte[length];
                System.arraycopy(bytes, cursor, a, 0, length);
                f.set(obj, bytesToObject(a));
                return;
            }
        }
    }

    public static char bytesToChar(byte[] bytes, int cursor){
        byte[] charBytes = new byte[2];
        System.arraycopy(bytes, cursor, charBytes, 0, 2);

        return (char)(((charBytes[0] & 0xFF) << 8) |
                (charBytes[1] & 0xFF));
    }

    public static short bytesToShort(byte[] bytes, int cursor){
        byte[] shortBytes = new byte[2];
        System.arraycopy(bytes, cursor, shortBytes, 0, 2);

        return (short)(((shortBytes[0] & 0xFF) << 8) |
                (shortBytes[1] & 0xFF));
    }

    public static int bytesToInt(byte[] bytes, int cursor){
        byte[] intBytes = new byte[4];
        System.arraycopy(bytes, cursor, intBytes, 0, 4);

        return ((intBytes[0] & 0xFF) << 24) |
                ((intBytes[1] & 0xFF) << 16) |
                ((intBytes[2] & 0xFF) << 8) |
                (intBytes[3] & 0xFF);
    }

    public static long bytesToLong(byte[] bytes, int cursor){
        byte[] longBytes = new byte[8];
        System.arraycopy(bytes, cursor, longBytes, 0, 8);

        return (long)((long)(longBytes[0] & 0xFF) << 56) |
                ((long)(longBytes[1] & 0xFF) << 48) |
                ((long)(longBytes[2] & 0xFF) << 40) |
                ((long)(longBytes[3] & 0xFF) << 32) |
                ((long)(longBytes[4] & 0xFF) << 24) |
                ((long)(longBytes[5] & 0xFF) << 16) |
                ((long)(longBytes[6] & 0xFF) << 8) |
                ((long)longBytes[7] & 0xFF);
    }
}

