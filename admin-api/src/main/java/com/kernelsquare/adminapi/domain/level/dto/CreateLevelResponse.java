package com.kernelsquare.adminapi.domain.level.dto;

import com.kernelsquare.domainmysql.domain.level.entity.Level;

// 과도한 줄바꿈
public record CreateLevelResponse(Long levelId) {

   //
   public static CreateLevelResponse from(Level level) {
      return new CreateLevelResponse(
         level.getId()
      );
   }
}