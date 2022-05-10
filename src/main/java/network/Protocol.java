package network;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;

public class Protocol {

    public static final int UNDEFINED  = 0;
    // TYPE
    public static final int TYPE_REQUEST = 1;
    public static final int TYPE_RESPONSE = 2;
    // TYPE_REQUEST_CODE
    public static final int T1_CODE_CREATE = 1;
    public static final int T1_CODE_READ = 2;
    public static final int T1_CODE_UPDATE = 3;
    public static final int T1_CODE_DELETE = 4;
    public static final int T1_CODE_LOGIN = 5;
    public static final int T1_CODE_LOGOUT = 6;
    public static final int T1_CODE_EXIT = 7;
    // TYPE_RESPONSE_CODE
    public static final int T2_CODE_SUCCESS = 1;
    public static final int T2_CODE_FAIL = 2;

    // ENTITY
    public static final int ENTITY_ACCOUNT = 1;
    public static final int ENTITY_COURSE = 2;
    public static final int ENTITY_LECTURE = 3;
    public static final int ENTITY_REGIS_PERIOD= 4;
    public static final int ENTITY_PLANNER_PERIOD = 5;
    public static final int ENTITY_REGISTRATION = 6;
    public static final int ENTITY_STUDENT = 7;
    public static final int ENTITY_PROFESSOR = 8;
    public static final int ENTITY_ADMIN = 9;
    public static final int ENTITY_LECTURE_STUD_LIST = 10;

    //READ_OPTION
    public static final int READ_ALL = 1;
    public static final int READ_BY_ID = 2;
    public static final int READ_BY_OPTION = 3;

    // LENGTH
    public static final int LEN_HEADER = 8;
    public static final int LEN_TYPE = 1;
    public static final int LEN_CODE = 1;
    public static final int LEN_ENTITY = 1;
    public static final int LEN_READ_OPTION = 1;
    public static final int LEN_BODYLENGTH = 4;

    private byte type;
    private byte code;
    private byte entity;
    private byte readOption;
    private int bodyLength;
    private byte[] body;

    public Protocol() {
        this(UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED);
    }

    public Protocol(int type) {
        this(type, UNDEFINED, UNDEFINED, UNDEFINED);
    }

    public Protocol(int type, int code) {
        this(type, code, UNDEFINED, UNDEFINED);
    }

    public Protocol(int type, int code, int entity) {
        this(type, code, entity, UNDEFINED);
    }

    public Protocol(int type, int code, int entity, int readOption){
        setType(type);
        setCode(code);
        setEntity(entity);
        setReadOption(readOption);
        setBodyLength(0);
    }

    public byte getType() {
        return type;
    }

    public void setType(int type) {
        this.type = (byte) type;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = (byte) code;
    }

    public byte getEntity() {
        return entity;
    }

    public void setEntity(int entity) {
        this.entity = (byte) entity;
    }

    public byte getReadOption(){
        return readOption;
    }

    public void setReadOption(int readOption){
        this.readOption = (byte) readOption;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    private void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    // 패킷을 생성하여 리턴 (패킷 전송시 사용)
    public byte[] getPacket() {
        byte[] packet = new byte[LEN_HEADER + bodyLength];
        packet[0] = type;          // 타입 담기
        packet[LEN_TYPE] = code;   // 코드 담기
        packet[LEN_TYPE + LEN_CODE] = entity; // 엔티티 담기
        packet[LEN_TYPE+LEN_CODE+LEN_ENTITY] = readOption;
        // 데이터 길이 담기
        System.arraycopy(intToByte(bodyLength), 0, packet,
                LEN_TYPE + LEN_CODE + LEN_ENTITY+LEN_READ_OPTION, LEN_BODYLENGTH);
        if (bodyLength > 0) // 바디 담기
            System.arraycopy(body, 0, packet, LEN_HEADER, bodyLength);
        return packet;
    }

    // 보낼 data - 객체 배열을 직렬화하여 body 초기화
    public void setObjectArray(Object[] bytes) throws IllegalAccessException {
        byte[] serializedObject = Serializer.objectArrToBytes(bytes);  // bytes = data length + data
        this.body = serializedObject;
        setBodyLength(serializedObject.length);
    }

    // 보낼 data - 객체를 직렬화하여 body 초기화
    public void setObject(Object bytes) throws IllegalAccessException {
        byte[] serializedObject = Serializer.objectToBytes(bytes);
        this.body = serializedObject;
        setBodyLength(serializedObject.length);
    }

    // 받은 패킷 -> 헤더 초기화
    public void setHeader(byte[] packet) {
        type = packet[0];
        code = packet[LEN_TYPE];
        entity = packet[LEN_TYPE + LEN_CODE];
        readOption = packet[LEN_TYPE+LEN_CODE+LEN_ENTITY];
        byte[] temp = new byte[LEN_BODYLENGTH];
        System.arraycopy(packet, LEN_TYPE + LEN_CODE + LEN_ENTITY+LEN_READ_OPTION,
                temp, 0, LEN_BODYLENGTH);
        setBodyLength(byteToInt(temp));
    }

    // 받은 패킷 -> 바디 초기화
    public void setBody(byte[] packet) {
        if (bodyLength > 0) {
            byte[] data = new byte[bodyLength];
            System.arraycopy(packet, 0, data, 0, bodyLength);
            body = data;
        }
    }

    // 받은 data 객체 배열로 역직렬화하여 return
    public Object getObjectArray() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        return Deserializer.bytesToObjectArr(body);
    }

    // 받은 data 객체로 역직렬화하여 return
    public Object getObject() throws Exception {
        return Deserializer.bytesToObject(body);
    }

    private byte[] intToByte(int i) {
        return ByteBuffer.allocate(Integer.SIZE / 8).putInt(i).array();
    }

    private int byteToInt(byte[] b) {
        return ByteBuffer.wrap(b).getInt();
    }

    public void send(OutputStream os) throws IOException {
        os.write(getPacket());
        os.flush();
    }

    public Protocol read(InputStream is) throws Exception {
        byte[] header = new byte[Protocol.LEN_HEADER];
        int totalReceived = 0;
        int readSize;

        try {
            is.read(header);
            setHeader(header);

            if (this.type == Protocol.UNDEFINED)
                throw new Exception("통신 오류 > 연결 오류");

            byte[] buf = new byte[getBodyLength()];
            is.read(buf);
//            while (totalReceived < getBodyLength()) {
//                readSize = is.read(buf, totalReceived, getBodyLength() - totalReceived);
//                totalReceived += readSize;
//                if (readSize == -1) {
//                    throw new Exception("통신 오류 > 연결 끊김");
//                }
//            }
            setBody(buf);
            return this;
        }
        catch (IOException e) {
            throw new IOException("통신 오류 > 데이터 수신 실패");
        }

    }
}