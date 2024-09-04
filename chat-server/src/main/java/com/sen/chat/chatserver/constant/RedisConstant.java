package com.sen.chat.chatserver.constant;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/27 4:26
 */
public interface RedisConstant {

    /*-------------------------Redis Key--------------------------*/

    String CAPTURE_CODE_KEY = "sen:chat-server:capture-code:";

    String TOKEN_KEY = "sen:chat-server:token:";

    String USER_INFO_CACHE_KEY = "sen:cache:user-cache";

    String USER_INFO_MODIFY_TIME_KEY = "sen:cache:user-modify-time";

    String ONLINE_UID_ZET = "sen:user:online-zset";
    String OFFLINE_UID_ZET = "sen:user:offline-zset";
    String AUTH_CODE_KEY_PREFIX = "sen:user:mail-code:";
    String TEMP_FILE_SIZE = "sen:file:temp-file-size:";


    /*-------------------------超时时间--------------------------*/
    Long ONE_MINUTE = 5 * 60L;

    Long FIVE_MINUTE = 5 * 60L;

    Long TEM_MINUTE = 10 * 60L;


}
