package com.green.attaparunever2.admin.system;

import com.green.attaparunever2.admin.system.model.SelSystemPostCommentReq;
import com.green.attaparunever2.admin.system.model.SelSystemPostCommentRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminSystemMapper {
    List<SelSystemPostCommentRes> selSystemPostComment(SelSystemPostCommentReq req);
}
