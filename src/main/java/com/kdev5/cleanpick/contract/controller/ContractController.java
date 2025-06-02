package com.kdev5.cleanpick.contract.controller;


import com.kdev5.cleanpick.contract.service.dto.request.ContractRequestDto;
import com.kdev5.cleanpick.contract.service.dto.request.UpdateContractRequestDto;
import com.kdev5.cleanpick.contract.service.dto.response.OneContractResponseDto;
import com.kdev5.cleanpick.contract.service.dto.response.RoutineContractResponseDto;
import com.kdev5.cleanpick.cleaning.domain.enumeration.ServiceName;
import com.kdev5.cleanpick.contract.service.ContractService;
import com.kdev5.cleanpick.contract.service.ReadContractService;
import com.kdev5.cleanpick.contract.service.ReadNomineeService;
import com.kdev5.cleanpick.contract.service.dto.request.ContractFilterStatus;
import com.kdev5.cleanpick.contract.service.dto.request.ContractRequestDto;
import com.kdev5.cleanpick.contract.service.dto.response.*;
import com.kdev5.cleanpick.global.response.ApiResponse;
import com.kdev5.cleanpick.global.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contract")
@Tag(name = "contract API", description = "계약글 CRUD 컨트롤러")
public class ContractController {

    private static final Long userId = 1L; // TODO
    private static final String role = "ROLE_MANAGER";

    private final ReadNomineeService readNomineeService;
    private final ReadContractService readContractService;
    private final ContractService contractService;

    @PostMapping("/one")
    @Operation(summary = "[수요자]1회성 청소 요청글 작성", description = "1회성 청소 요청글을 작성합니다.")
    public ResponseEntity<ApiResponse<OneContractResponseDto>> createContract(@RequestBody @Valid ContractRequestDto contractDto) {
        OneContractResponseDto newContract = contractService.createOneContract(contractDto);
        return ResponseEntity.ok(ApiResponse.ok(newContract));
    }

    @PostMapping("/routine")
    @Operation(summary = "[수요자]정기 청소 요청글 작성", description = "시작 날짜와 횟수를 통해 동일한 1회성 요청글을 반복해서 작성합니다.")
    public ResponseEntity<ApiResponse<RoutineContractResponseDto>> createRoutineContract(@RequestBody @Valid ContractRequestDto contractDto) {
        RoutineContractResponseDto newContracts = contractService.createRoutineContract(contractDto);
        return ResponseEntity.ok(ApiResponse.ok(newContracts));
    }

    @GetMapping("/{contractId}")
    public ResponseEntity<ApiResponse<ReadContractDetailResponseDto>> readDetailContract(@PathVariable("contractId") Long contractId) {
        return ResponseEntity.ok(ApiResponse.ok(readContractService.readContractDetail(contractId)));
    }

    @GetMapping("/user")
    @Operation(summary = "[수요자]예약 목록 조회", description = "수요자가 신청한 예약 목록을 조회합니다. 매칭 전, 작업 전(중)/후로 나누어 확인 가능합니다. ")
    public ResponseEntity<ApiResponse<PageResponse<ReadContractResponseDto>>> readCustomerContracts(
            @RequestParam("status") ContractFilterStatus status,
            @RequestParam("sortType") String sortType,
            @RequestParam("order") String order,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(order), sortType));

        return ResponseEntity.ok(ApiResponse.ok(new PageResponse<>(readContractService.readCustomerContracts(userId, role, status, pageable))));
    }

    @PutMapping("/{contractId}")
    @Operation(summary = "[수요자] 요청글 내용 수정", description = "반려동물, 요청사항, 예약날짜를 변경할 수 있습니다. 매칭되어있는 매니저가 존재하는 경우, 매니저의 일정을 확인 한 후 수정에 반영합니다.")
    public ResponseEntity<ApiResponse<Void>> changeContract(@RequestBody @Valid UpdateContractRequestDto dto, @PathVariable("contractId") Long contractId) {
        contractService.updateOneContract(dto, contractId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @DeleteMapping("/{contractId}")
    @Operation(summary = "[수요자] 요청글 삭제", description = "요청글을 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteContract(@PathVariable("contractId") Long contractId) {
        contractService.deleteOneContract(contractId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping("/manager/completed")
    @Operation(summary = "[매니저] 예약 목록 조회", description = "매니저가 완료한 예약 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<ReadConfirmedMatchingResponseDto>>> readManagerCompletedContracts(
            @RequestParam("sortType") String sortType,
            @RequestParam("order") String order,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(order), sortType));

        return ResponseEntity.ok(ApiResponse.ok(new PageResponse<>(readContractService.readManagerCompletedContracts(userId, role, pageable))));
    }

    @GetMapping("/pending")
    @Operation(summary = "매니저에게 들어온 요청 목록 조회", description = "매니저에게 들어온 모든 요청을 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<ReadRequestedMatchingResponseDto>>> readRequestedMatching(
            @RequestParam(value = "isPersonal", required = false) Boolean isPersonal,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return ResponseEntity.ok(ApiResponse.ok(new PageResponse<>(readNomineeService.readRequestedMatching(userId, isPersonal, PageRequest.of(page, size)))));
    }

    @GetMapping("/accepted")
    @Operation(summary = "매니저가 신청한 요청 목록 조회", description = "매니저가 수락한 모든 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<ReadAcceptedMatchingResponseDto>>> readAcceptedMatching(
            @RequestParam(value = "type", required = false) ServiceName type,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return ResponseEntity.ok(ApiResponse.ok(new PageResponse<>(readNomineeService.readAcceptedMatching(userId, type, PageRequest.of(page, size)))));
    }

    @GetMapping("/confirmed")
    @Operation(summary = "최종 매칭된 목록 조회", description = "매니저와 수요자 서로 accept한 목록(최종 매칭된 목록)을 조회합니다.sortType은 최근 매칭순(MATCHED), 계약일순(CONTRACT) 입니다.")
    public ResponseEntity<ApiResponse<PageResponse<ReadConfirmedMatchingResponseDto>>> readConfirmedMatching(
            @RequestParam(value = "serviceType", required = false) ServiceName serviceType,
            @RequestParam(value = "sortType") String sortType,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return ResponseEntity.ok(ApiResponse.ok(new PageResponse<>(readNomineeService.readConfirmedMatching(userId, serviceType, sortType, PageRequest.of(page, size)))));
    }
}



