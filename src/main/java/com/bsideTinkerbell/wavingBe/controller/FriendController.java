package com.bsideTinkerbell.wavingBe.controller;

import com.bsideTinkerbell.wavingBe.domain.dto.ContactRequestDto;
import com.bsideTinkerbell.wavingBe.domain.dto.FriendProfileRequestDto;
import com.bsideTinkerbell.wavingBe.domain.dto.ResponseDto;
import com.bsideTinkerbell.wavingBe.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *  지인 서비스 API REST 엔드포인트 제공을 위한 Controller
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/friends")
public class FriendController {

		// 명명규칙, list(조회), detail(상세), save(등록,수정,삭제), add(등록), modify(수정), remove(삭제)

		private final FriendService friendService;

		// 지인 리스트 view
		@GetMapping("/list/{userId}")
		public ResponseEntity<ResponseDto> listFriend(@PathVariable("userId") Long userId) {
				ResponseDto responseDto = friendService.getAllFriendProfile(userId);
				return ResponseEntity.status(HttpStatus.valueOf(responseDto.getCode())).body(responseDto);
		}


		// 지인 등록
		@PostMapping("/register")
		public ResponseEntity<ResponseDto> saveFriend(@RequestBody ContactRequestDto contactRequestDto) {
				ResponseDto responseDto = friendService.saveFriend(contactRequestDto);
				return ResponseEntity.status(HttpStatus.valueOf(responseDto.getCode())).body(responseDto);
		}


		// 지인 수정
		@PostMapping("/modify/{profileId}")
		public ResponseEntity<ResponseDto> updateFriend(@PathVariable Long profileId, @RequestBody FriendProfileRequestDto profileRequestDto) {
				ResponseDto responseDto = friendService.modifyFriend(profileId, profileRequestDto);
				return ResponseEntity.status(HttpStatus.valueOf(responseDto.getCode())).body(responseDto);
		}


		// 지인 프로필 view
		@GetMapping("/profile/{profileId}")
		public ResponseEntity<ResponseDto> detailProfile(@PathVariable Long profileId) {
				ResponseDto responseDto = friendService.getFriendProfile(profileId);
				return ResponseEntity.status(HttpStatus.valueOf(responseDto.getCode())).body(responseDto);
		}


		// 지인 프로필 삭제
		@DeleteMapping("/profile/delete/{profileId}")
		public ResponseEntity<ResponseDto> deleteProfile(@PathVariable Long profileId) {
				ResponseDto responseDto = friendService.deleteFriendProfile(profileId);
				return ResponseEntity.status(HttpStatus.valueOf(responseDto.getCode())).body(responseDto);
		}


		// 지인 추억 view
//		@GetMapping("/history")
//		public void detailHistory() {
//				// return 추억dto
//
//		}
//
//
//		// 지인 추억 등록
//		@PostMapping("/history/register")
//		public void saveHistory() {
//
//		}
//
//
//		// 지인 추억 수정
//		@PostMapping("/history/modify")
//		public void updateHistory() {
//
//		}


}
