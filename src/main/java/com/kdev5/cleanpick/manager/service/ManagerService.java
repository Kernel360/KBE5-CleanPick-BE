package com.kdev5.cleanpick.manager.service;

import com.kdev5.cleanpick.cleaning.domain.Cleaning;
import com.kdev5.cleanpick.global.exception.ErrorCode;
import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.manager.domain.ManagerAvailableCleaning;
import com.kdev5.cleanpick.manager.domain.ManagerAvailableTime;
import com.kdev5.cleanpick.manager.domain.enumeration.SortType;
import com.kdev5.cleanpick.manager.domain.exception.ManagerNotFoundException;
import com.kdev5.cleanpick.manager.infra.repository.ManagerAvailableCleaningRepository;
import com.kdev5.cleanpick.manager.infra.repository.ManagerAvailableRegionRepository;
import com.kdev5.cleanpick.manager.infra.repository.ManagerAvailableTimeRepository;
import com.kdev5.cleanpick.manager.infra.repository.ManagerRepository;
import com.kdev5.cleanpick.manager.service.dto.request.ManagerDetailRequestDto;
import com.kdev5.cleanpick.manager.service.dto.response.ManagerPrivateResponseDto;
import com.kdev5.cleanpick.manager.service.dto.response.ManagerSearchResponseDto;
import com.kdev5.cleanpick.review.Infra.ReviewRepository;
import com.kdev5.cleanpick.user.domain.User;
import com.kdev5.cleanpick.user.domain.exception.UserNotFoundException;
import com.kdev5.cleanpick.user.infra.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;
    private final ReviewRepository reviewRepository;
    private final ManagerAvailableRegionRepository managerAvailableRegionRepository;
    private final ManagerAvailableCleaningRepository managerAvailableCleaningRepository;
    private final ManagerAvailableTimeRepository managerAvailableTimeRepository;

    @Transactional
    public ManagerPrivateResponseDto enrollManager(Long userId, ManagerDetailRequestDto managerDetailRequestDto) {

        User user = userRepository.findById(userId).orElseThrow(
            () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)
        );

        user.activate();

        final Manager manager = managerRepository.save(managerDetailRequestDto.toManagerEntity(user));

        List<ManagerAvailableCleaning> managerAvailableCleanings = managerDetailRequestDto.getAvailableCleans().stream().map(
            cleaningId -> new ManagerAvailableCleaning(manager, Cleaning.reference(cleaningId))
        ).toList();

        managerAvailableCleaningRepository.saveAll(managerAvailableCleanings);

        List<ManagerAvailableTime> managerAvailableTimes = managerDetailRequestDto.getAvailableTimes().stream().map(
            availableTimeDto -> availableTimeDto.toManagerAvailableTime(manager)
        ).toList();

        managerAvailableTimeRepository.saveAll(managerAvailableTimes);

        return ManagerPrivateResponseDto.fromEntity(manager);
    }

    public ManagerPrivateResponseDto getManager(Long managerId) {
        final Manager manager = managerRepository.findById(managerId).orElseThrow(
            () -> new ManagerNotFoundException(ErrorCode.MANAGER_NOT_FOUND)
        );
        return ManagerPrivateResponseDto.fromEntity(manager);
    }

    public Page<ManagerSearchResponseDto> searchManagers(
            String cleaning,
            String region,
            String keyword,
            SortType sortType,
            Pageable pageable
    ) {
        Page<Manager> filteredManagers = managerRepository.findManagersByFilter(cleaning, region, keyword, sortType, pageable);

        List<Long> managerIds = filteredManagers.stream().map(Manager::getId).toList();

        Map<Long, Double> avgMap = loadAvgRating(managerIds);
        Map<Long, Long> countMap = loadReviewCount(managerIds);
        Map<Long, List<String>> regionMap = loadRegions(managerIds);
        Map<Long, List<String>> cleaningMap = loadCleanings(managerIds);

        List<ManagerSearchResponseDto> content = filteredManagers.stream()
                .map(m -> new ManagerSearchResponseDto(
                        m.getId(),
                        m.getName(),
                        avgMap.getOrDefault(m.getId(), 0.0),
                        countMap.getOrDefault(m.getId(), 0L),
                        m.getProfileImageUrl(),
                        m.getProfileMessage(),
                        extractRegionSummary(m.getId(), region, regionMap),
                        cleaningMap.get(m.getId())
                )).toList();

        return new PageImpl<>(content, pageable, filteredManagers.getTotalElements());
    }

    private Map<Long, Double> loadAvgRating(List<Long> managerIds) {
        return reviewRepository.getAvgRatingForMangers(managerIds);
    }

    private Map<Long, Long> loadReviewCount(List<Long> managerIds) {
        return reviewRepository.getReviewCountForManagers(managerIds);
    }

    private Map<Long, List<String>> loadRegions(List<Long> managerIds) {
        return managerAvailableRegionRepository.loadRegions(managerIds);
    }

    private Map<Long, List<String>> loadCleanings(List<Long> managerIds) {
        return managerAvailableCleaningRepository.loadCleanings(managerIds);
    }

    private String extractRegionSummary(Long managerId, String regionFilter, Map<Long, List<String>> regionMap) {
        List<String> regionNames = regionMap.getOrDefault(managerId, List.of());

        if (regionNames.isEmpty()) return "";

        if (regionFilter != null && regionNames.contains(regionFilter)) {
            long others = regionNames.stream()
                    .filter(name -> !name.equals(regionFilter))
                    .count();
            return others == 0 ? regionFilter : regionFilter + " 외 " + others + "곳";
        }

        String firstRegion = regionNames.get(0);
        long others = regionNames.size() - 1;

        return others == 0 ? firstRegion : firstRegion + " 외 " + others + "곳";
    }

}

