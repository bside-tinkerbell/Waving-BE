package com.bsideTinkerbell.wavingBe.service;

import com.bsideTinkerbell.wavingBe.domain.dto.*;
import com.bsideTinkerbell.wavingBe.domain.entity.ContactEntity;
import com.bsideTinkerbell.wavingBe.domain.entity.FriendProfileEntity;
import com.bsideTinkerbell.wavingBe.repository.ContactRepository;
import com.bsideTinkerbell.wavingBe.repository.FriendProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // DI
@Transactional // 정상 작업이면 커밋 아니면 오류 롤백
public class FriendService {

		private final ContactRepository contactRepository;
		private final FriendProfileRepository profileRepository;


		// 명명 규칙 find(조회), save(등록), modify(변경), remove(삭제)


		/**
		 * 지인 리스트
		 * @param userId
		 * @return
		 */
		public ResponseDto getAllFriendProfile(Long userId) {

				ResponseDto responseDto = new ResponseDto();
				ResponseResultDto result = new ResponseResultDto();

				Optional<ContactEntity> contactEntity = contactRepository.findByUserId(userId);

				if(contactEntity.isPresent()) {

						// 즐겨찾기 지인들
						List<FriendProfileEntity> favoriteProfileList = profileRepository.findAllByContactIdAndIsFavorite(contactEntity.get().getContactId(), 1);
						List<FriendProfileResponseDto> favoriteProfileDtoList = favoriteProfileList.stream().map(FriendProfileResponseDto::new).toList();

						// 지인들
						List<FriendProfileEntity> friendProfileList = profileRepository.findAllByContactIdAndIsFavorite(contactEntity.get().getContactId(), 0);
						List<FriendProfileResponseDto> friendProfileDtoList = friendProfileList.stream().map(FriendProfileResponseDto::new).toList();

						Long favoriteCnt = profileRepository.countAllByContactIdAndIsFavorite(contactEntity.get().getContactId(), 1);
						Long profileCnt = profileRepository.countAllByContactIdAndIsFavorite(contactEntity.get().getContactId(), 0);

						result.setMessage("success");
						result.setProfileListResponseDto(friendProfileDtoList);
						result.setFavoriteProfileList(favoriteProfileDtoList);
						result.setProfileCnt(profileCnt);
						result.setFavoriteCnt(favoriteCnt);
						responseDto.setResult(result);

				} else {
						responseDto.setCode(400);
						result.setMessage("user_id의 지인리스트가 없습니다.");
						responseDto.setResult(result);
				}

				// todo 페이징 -> 후순위

				return responseDto;
		}


		/**
		 * 지인 등록
		 * @param contactRequestDto
		 * @return
		 */
		public ResponseDto saveFriend(ContactRequestDto contactRequestDto) {
				ResponseDto responseDto = new ResponseDto();
				ResponseResultDto result = new ResponseResultDto();

				Optional<ContactEntity> byUserId = contactRepository.findByUserId(contactRequestDto.getUserId());

				// 최초 저장할 때
				if(byUserId.isEmpty()) {

						// 지인 유형 save
						ContactEntity contactSaveEntity = contactRepository.save(contactRequestDto.toFriendEntity());
						Long contactId = contactSaveEntity.getContactId();

						// 지인 프로필 save
						List<FriendProfileRequestDto> profileRequestDtoList = contactRequestDto.getProfileList();

						for (FriendProfileRequestDto profileRequestDto : profileRequestDtoList) {
								FriendProfileEntity profileEntity = profileRequestDto.toProfileEntity();
								profileEntity.setContactId(contactId);
								profileRepository.save(profileEntity);
						}

						List<FriendProfileEntity> profileEntityList = profileRepository.findAllByContactId(contactId);
						List<FriendProfileResponseDto> profileResponseList = profileEntityList.stream().map(FriendProfileResponseDto::new).toList();

						responseDto.setCode(200);
						result.setMessage("success");
						result.setProfileListResponseDto(profileResponseList);
						responseDto.setResult(result);

						// 리스트에서 저장할 때
				} else if(contactRequestDto.getContactId() != null) {

						// 지인 프로필 save
						List<FriendProfileRequestDto> profileRequestDtoList = contactRequestDto.getProfileList();

						for (FriendProfileRequestDto profileRequestDto : profileRequestDtoList) {
								FriendProfileEntity profileEntity = profileRequestDto.toProfileEntity();
								profileEntity.setContactId(contactRequestDto.getContactId());
								profileRepository.save(profileEntity);

								List<FriendProfileEntity> profileEntityList = profileRepository.findAllByContactId(contactRequestDto.getContactId());
								List<FriendProfileResponseDto> profileResponseList = profileEntityList.stream().map(FriendProfileResponseDto::new).toList();

								result.setMessage("success");
								result.setProfileListResponseDto(profileResponseList);
								responseDto.setResult(result);
						}
				} else {

						responseDto.setCode(400);
						result.setMessage("이미 user_id가 존재합니다.");
						responseDto.setResult(result);

				}
				return responseDto;
		}


		/**
		 * 지인 수정
		 * @param profileRequestDto
		 * @return
		 */
		public ResponseDto modifyFriend(Long profileId, FriendProfileRequestDto profileRequestDto) {
				ResponseDto responseDto = new ResponseDto();
				ResponseResultDto result = new ResponseResultDto();

				try {

						Optional<FriendProfileEntity> profileEntityOptional = profileRepository.findOneByFriendProfileId(profileId);
						if(profileEntityOptional.isPresent()) {
								FriendProfileEntity existingProfileEntity = profileEntityOptional.get();
								FriendProfileEntity updatedProfileEntity = profileRequestDto.toProfileEntity();

								// 기존에 DB에 저장된 friendProfileId와 contactId를 새로운 엔티티에 추가
								updatedProfileEntity.setFriendProfileId(profileId);
								updatedProfileEntity.setContactId(existingProfileEntity.getContactId());

								profileRepository.save(updatedProfileEntity);

								FriendProfileResponseDto profileDto = new FriendProfileResponseDto(updatedProfileEntity);

								result.setMessage("success");
								result.setProfile(profileDto);
								responseDto.setResult(result);
						} else {
								responseDto.setCode(400);
								result.setMessage("프로필이 존재하지 않습니다.");
								responseDto.setResult(result);
						}

				}catch (Exception e) {
						responseDto.setCode(400);
						result.setMessage(String.valueOf(e));
						responseDto.setResult(result);
				}

				return responseDto;
		}

		/**
		 * 지인 프로필 view
		 * @param profileId
		 * @return
		 */
		public ResponseDto getFriendProfile(Long profileId) {

				ResponseDto responseDto = new ResponseDto();
				ResponseResultDto result = new ResponseResultDto();

				Optional<FriendProfileEntity> profileEntityOptional = profileRepository.findOneByFriendProfileId(profileId);

				if(profileEntityOptional.isPresent()) {

						FriendProfileEntity profileEntity = profileEntityOptional.get();
						FriendProfileResponseDto profileDto = new FriendProfileResponseDto(profileEntity);

						result.setMessage("success");
						result.setProfile(profileDto);
						responseDto.setResult(result);

				} else {
						responseDto.setCode(400);
						result.setMessage("프로필이 존재하지 않습니다.");
						responseDto.setResult(result);
				}

				return responseDto;
		}

		/**
		 * 지인 프로필 delete
		 * @param profileId
		 * @return
		 */
		public ResponseDto deleteFriendProfile(Long profileId) {
				ResponseDto responseDto = new ResponseDto();
				ResponseResultDto result = new ResponseResultDto();

				Optional<FriendProfileEntity> profileEntityOptional = profileRepository.findOneByFriendProfileId(profileId);

				if(profileEntityOptional.isPresent()) {

						FriendProfileEntity profileEntity = profileEntityOptional.get();
						profileEntity.delete();

						result.setMessage("success");
						responseDto.setResult(result);

				} else {
						responseDto.setCode(400);
						result.setMessage("프로필이 존재하지 않습니다.");
						responseDto.setResult(result);
				}

				return responseDto;
		}



		// 지인 추억 view



		// 지인 추억 등록



		// 지인 추억 수정


}
