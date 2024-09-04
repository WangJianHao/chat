package com.sen.chat.chatserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sen.chat.chatserver.entity.ApplyDO;
import com.sen.chat.chatserver.entity.query.ApplyQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/1 15:20
 */
@Repository
public interface ApplyDAO extends BaseMapper<ApplyDO> {

    int selectCount(@Param("query") ApplyQuery query);

    int updateStatus(@Param("applyId") Long applyId, @Param("optUid") Long optUid,
                     @Param("srcStatus") Integer srcStatus, @Param("destStatus") Integer destStatus);

    /**
     * 修改入群申请状态
     *
     * @param groupId       要入的群组ID
     * @param applyUid      申请人UID
     * @param lastApplyTime 申请事件
     * @param optUid        操作人UID
     * @param srcStatus     申请原状态
     * @param destStatus    申请目标状态
     * @return
     */
    int updateGroupStatus(@Param("groupId") Long groupId, @Param("applyUid") Long applyUid,
                          @Param("lastApplyTime") Timestamp lastApplyTime,
                          @Param("optUid") Long optUid, @Param("srcStatus") Integer srcStatus,
                          @Param("destStatus") Integer destStatus);

    ApplyDO selectByPrimaryKey(@Param("applyId") Long applyId);

    Page<ApplyDO> selectPage(Page<ApplyDO> page, @Param("query") ApplyQuery applyQuery);

    int insertBatch(@Param("applyDOS") List<ApplyDO> applyDOS);
}
