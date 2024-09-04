package com.sen.chat.chatserver.convert;

import com.sen.chat.chatserver.dto.resp.ApplyResp;
import com.sen.chat.chatserver.entity.ApplyDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/3 14:41
 */
@Mapper
public interface ApplyConverter {

    ApplyConverter INSTANCE = Mappers.getMapper(ApplyConverter.class);

    ApplyResp convert(ApplyDO applyDO);
}
