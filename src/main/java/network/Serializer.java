package network;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.*;

//객체배열 혹은 객체를 바이트배열로 직렬화 해주는 클래스
public class Serializer {
    private final static int LEN_MAX_CLASSNAME = 64;
    private final static int LEN_LENGTH_FIELD = 4;
    private final static int LEN_COUNT_FIELD = 4;

    //객체 배열을 바이트배열로 직렬화 해주는 메서드
    public static byte[] objectArrToBytes(Object[] objs) throws IllegalAccessException, IllegalArgumentException{
        if(objs.length==0){
            throw new IllegalArgumentException();
        }

        List<byte[]> byteList = new LinkedList<>();

        //배열의 클래스 이름을 바이트배열로 변환
        byte[] classNameBytes = new byte[LEN_MAX_CLASSNAME];
        String className = objs.getClass().getName().replace(";","");
        System.arraycopy(
                className.getBytes(StandardCharsets.UTF_8), 0,
                classNameBytes, 0, className.getBytes(StandardCharsets.UTF_8).length
        );
        byteList.add(classNameBytes);


        int allLength = LEN_MAX_CLASSNAME;
        int count = 0;

        try{
            //배열내부의 객체를 바이트배열로 변환
            for(Object obj : objs){
                byte[] objBytes = objectToBytes(obj);

                count++;
                allLength+=objBytes.length;
                byteList.add(objBytes);
            }
        }catch(NullPointerException e){
        }

        byteList.add(1, intToBytes(count));

        byte[] result = new byte[allLength+LEN_COUNT_FIELD];
        int cursor = 0;
        for(byte[] b : byteList){
            System.arraycopy(b, 0, result, cursor, b.length);
            cursor+=b.length;
        }

        //바이트 배열 반환
        return result;
    }

    //객체를 바이트배열로 바꿔주는 메서드
    public static byte[] objectToBytes(Object obj) throws IllegalAccessException {
        List<byte[]> byteList = new LinkedList<>();

        //객체의 클래스 이름을 바이트배열로 변환
        byte[] classNameBytes = new byte[LEN_MAX_CLASSNAME];
        String className = obj.getClass().getName().replace(";","");
        System.arraycopy(
                className.getBytes(StandardCharsets.UTF_8), 0,
                classNameBytes, 0, className.getBytes(StandardCharsets.UTF_8).length
        );
        byteList.add(classNameBytes);

        int allLength = LEN_MAX_CLASSNAME;

        //객체의 필드들을 순서대로 바이트배열로 변환
        try{
            for(Field f : getAllFields(obj.getClass())){
                byte[] fieldBytes = getBytesFrom(obj, f);
                byte[] fieldLength = intToBytes(fieldBytes.length);

                byteList.add(fieldLength);
                byteList.add(fieldBytes);
                allLength+=fieldBytes.length+LEN_LENGTH_FIELD;
            }

        }catch(NullPointerException e){
        }

        byte[] result = new byte[allLength];
        int cursor = 0;
        for(byte[] b : byteList){
            System.arraycopy(b, 0, result, cursor, b.length);
            cursor+=b.length;
        }

        //바이트배열 반환
        return result;
    }

    //클래스의 필드 정보를 반환(부모클래스의 필드포함)
    public static List<Field> getAllFields(Class clazz){
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

    //객체의 필드를 바이트배열로 변환
    public static byte[] getBytesFrom(Object obj, Field f) throws IllegalAccessException {
        String typeName = f.getType().getSimpleName();
        f.setAccessible(true);

        if(typeName.contains("[")){
            typeName = "array";
        }

        switch(typeName){
            case "byte":
                return new byte[]{f.getByte(obj)};
            case "char":
                return charToBytes(f.getChar(obj));
            case "short":
                return shortToBytes(f.getShort(obj));
            case "int":
                return intToBytes(f.getInt(obj));
            case "long":
                return longToBytes(f.getLong(obj));
            case "String":
                try{
                    return new String((String)f.get(obj)).getBytes(StandardCharsets.UTF_8);
                }catch(NullPointerException e){
                    return new byte[0];
                }
            case "array":
                try{
                    return objectArrToBytes((Object[])f.get(obj));
                }catch(NullPointerException | IllegalArgumentException e){
                    return new byte[0];
                }
            default:
                try{
                    return objectToBytes(f.get(obj));
                }catch(NullPointerException e){
                    return new byte[0];
                }
        }
    }

    public static byte[] longToBytes(long l) {
        byte[] bytes = new byte[8];

        for(int i=0, shift=56; i<bytes.length; i++, shift-=8){
            bytes[i] = (byte) (l>>shift);
        }

        return bytes;
    }

    public static byte[] charToBytes(char c){
        byte[] bytes = new byte[2];

        for(int i=0, shift=8; i<bytes.length; i++, shift-=8){
            bytes[i] = (byte) (c>>shift);
        }

        return bytes;
    }

    public static byte[] shortToBytes(short s){
        byte[] bytes = new byte[2];

        for(int i=0, shift=8; i<bytes.length; i++, shift-=8){
            bytes[i] = (byte) (s>>shift);
        }

        return bytes;
    }

    public static byte[] intToBytes(int i){
        byte[] bytes = new byte[4];

        for(int j=0, shift=24; j<bytes.length; j++, shift-=8){
            bytes[j] = (byte) (i>>shift);
        }

        return bytes;
    }
}