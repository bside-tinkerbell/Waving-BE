package com.bsideTinkerbell.wavingBe.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResponseResultDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    // 회원
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("user_info")
    private UserInfoResponseDto userInfoResponseDto;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("user_join_result")
    private UserJoinResponseDto userJoinResponseDto;

    // 인사말
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("greeting_category_list")
    private List<GreetingCategoryDto> greetingCategoryDtoList;
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonProperty("greeting")
//    private GreetingDto greetingDto;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("greeting_category_id")
    private Long greetingCategoryId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("greeting")
    private String greeting;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("greeting_list")
    private List<GreetingDto> greetingDtoList;
    @JsonInclude(JsonInclude.Include.NON_NULL)

    // 인증
    @JsonProperty("token")
    private AuthenticationResponseDto authenticationResponseDto;

    // 지인
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("profile_list")
    private List<FriendProfileResponseDto> profileListResponseDto;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("favorite_profile_list")
    private List<FriendProfileResponseDto> favoriteProfileList;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("profile_cnt")
    private Long profileCnt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("favorite_cnt")
    private Long favoriteCnt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("profile")
    private FriendProfileResponseDto profile;
}
