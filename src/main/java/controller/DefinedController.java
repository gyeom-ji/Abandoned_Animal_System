package controller;

import network.Protocol;

public interface DefinedController {        // 사용자가 로그인하여 사용자 종류가 지정된 클라이언트를 위한 controller
    int handler(Protocol recvPt) throws Exception;
}
