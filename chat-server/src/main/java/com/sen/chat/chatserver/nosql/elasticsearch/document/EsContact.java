package com.sen.chat.chatserver.nosql.elasticsearch.document;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/1 14:18
 */
//@Document(indexName = "chat-server", shards = 1, replicas = 0)
@Data
public class EsContact implements Serializable {
    private static final long serialVersionUID = -1L;

    //    @Id
    private Long uid;

    /**
     * 类别,1-群组 2-账号
     */
    private Integer contactType;

    /**
     * 联系人ID
     */
    private Integer contactId;


    /**
     * 会话ID
     */
    private Integer roomId;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 修改时间
     */
    private Timestamp updateTime;

    /**
     * 群组名称
     */
    //    @Field(type = FieldType.Keyword)
    private String groupName;


    /**
     * 用户名称
     */
    //    @Field(type = FieldType.Keyword)
    private String userName;

}
