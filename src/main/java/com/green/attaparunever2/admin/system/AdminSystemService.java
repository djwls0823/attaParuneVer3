package com.green.attaparunever2.admin.system;

import com.green.attaparunever2.admin.AdminRepository;
import com.green.attaparunever2.admin.SystemPostRepository;
import com.green.attaparunever2.admin.system.model.*;
import com.green.attaparunever2.common.excprion.CustomException;
import com.green.attaparunever2.common.repository.CodeRepository;
import com.green.attaparunever2.company.CompanyRepository;
import com.green.attaparunever2.company.RefundRepository;
import com.green.attaparunever2.config.security.AuthenticationFacade;
import com.green.attaparunever2.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminSystemService {
    private final AdminRepository adminRepository;
    private final AuthenticationFacade authenticationFacade;
    private final RefundRepository refundRepository;
    private final CompanyRepository companyRepository;
    private final SystemPostCommentRepository systemPostCommentRepository;
    private final SystemPostRepository systemPostRepository;
    private final AdminSystemMapper adminSystemMapper;
    private final CollectionScheduleService collectionScheduleService;
    private final CodeRepository codeRepository;
    private final SettlementListRepository settlementListRepository;
    private final SettlementDayRepository settlementDayRepository;

    public int patchCoalition(UpdCoalitionReq req) {
        // 관리자 로그인 인증
        Long signedAdminId = authenticationFacade.getSignedUserId();
        if (!signedAdminId.equals(req.getAdminSystemId())) {
            throw new CustomException("로그인한 관리자 계정과 일치하지 않는 관리자 정보입니다.", HttpStatus.BAD_REQUEST);
        }

        // 식당 & 회사 관리자 정보 조회
        Admin admin = adminRepository.findById(req.getAdminCompanyAndRestaurantId())
                .orElseThrow(() -> new CustomException("관리자 정보를 조회할 수 없습니다.", HttpStatus.NOT_FOUND));

        // 제휴 상태 변경
        if (admin.getCoalitionState() == 2) {
            admin.setCoalitionState(1);
            adminRepository.save(admin);
        }

        if (admin.getCoalitionState() == 3) {
            admin.setCoalitionState(0);
            adminRepository.save(admin);
        }

        adminRepository.save(admin);

        return 1;
    }

    //환불 상태 처리
    public int patchRefund(UpdRefundReq req) {
        Refund refund = refundRepository.findById(req.getRefundId())
                .orElseThrow(() -> new CustomException("해당 환불이 없습니다.", HttpStatus.BAD_REQUEST));

        Long adminId = refund.getAdmin().getAdminId();
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new CustomException("해당 관리자가 없습니다.", HttpStatus.BAD_REQUEST));

        Long divisionId = admin.getDivisionId();
        Company company = companyRepository.findById(divisionId)
                .orElseThrow(() -> new CustomException("해당 회사가 없습니다.", HttpStatus.BAD_REQUEST));
        if(req.getRefundYn() == 1) {
            int minPoint = refund.getRefundPoint();
            company.setCurrentPoint(company.getCurrentPoint() + minPoint);
            companyRepository.save(company);
        }
        refund.setRefundYn(req.getRefundYn());
        refundRepository.save(refund);



        return 1;
    }

    //입점신청서 승인 및 거절
    public int patchEnrollmentState(UpdAdmin req){
        Admin admin = adminRepository.findById(req.getAdminId())
                .orElseThrow(() -> new CustomException("해당 관라자가 없습니다", HttpStatus.BAD_REQUEST));

        admin.setCoalitionState(req.getCoalitionState());
        adminRepository.save(admin);

        return 1;
    }

    //시스템 문의 답변
    public int postSystemPostComment(InsSystemPostCommentReq req){
        SystemPost systemPost = systemPostRepository.findById(req.getInquiryId())
                .orElseThrow(() -> new CustomException("해당 게시물이 존재하지 않습니다.", HttpStatus.BAD_REQUEST));

        Admin admin = adminRepository.findById(req.getAdminId())
                .orElseThrow(() -> new CustomException("해당 관리자가 없습니다.", HttpStatus.BAD_REQUEST));

        if(!admin.getCode().getCode().equals("00103")){
            throw new CustomException("시스템 관리자가 아닙니다", HttpStatus.BAD_REQUEST);
        }

        SystemPostComment systemPostComment = new SystemPostComment();
        systemPostComment.setSystemPost(systemPost);
        systemPostComment.setAdmin(admin);
        systemPostComment.setCommentDetail(req.getCommentDetail());
        systemPostCommentRepository.save(systemPostComment);

        return 1;
    }

    //시스템 문의 답변 조회
    public List<SelSystemPostCommentRes> getSystemPostComment(SelSystemPostCommentReq req){
        List<SelSystemPostCommentRes> res = adminSystemMapper.selSystemPostComment(req);

        return res;
    }

    //시스템 문의 답변 수정
    public int patchSystemPostComment(UpdSystemPostCommentReq req){
        SystemPostComment systemPostComment = systemPostCommentRepository.findById(req.getInquiryCommentId())
                .orElseThrow(() -> new CustomException("해당 문의답변이 없습니다", HttpStatus.BAD_REQUEST));

        Admin admin = adminRepository.findById(systemPostComment.getAdmin().getAdminId())
                .orElseThrow(() -> new CustomException("해당 관리자가 없습니다.", HttpStatus.BAD_REQUEST));

        if(!admin.getCode().getCode().equals("00103")){
            throw new CustomException("시스템 관리자가 아닙니다", HttpStatus.BAD_REQUEST);
        }

        systemPostComment.setCommentDetail(req.getCommentDetail());
        systemPostCommentRepository.save(systemPostComment);

        return 1;
    }

    //시스템 문의 답변 삭제
    public int delSystemPostComment(DelSystemPostCommentReq req){
        SystemPostComment systemPostComment = systemPostCommentRepository.findById(req.getInquiryCommentId())
                .orElseThrow(() -> new CustomException("해당 문의답변이 없습니다", HttpStatus.BAD_REQUEST));

        Admin admin = adminRepository.findById(systemPostComment.getAdmin().getAdminId())
                .orElseThrow(() -> new CustomException("해당 관리자가 없습니다.", HttpStatus.BAD_REQUEST));

        if(!admin.getCode().getCode().equals("00103")){
            throw new CustomException("시스템 관리자가 아닙니다", HttpStatus.BAD_REQUEST);
        }

        systemPostCommentRepository.delete(systemPostComment);

        return 1;
    }

    @Transactional
    public int postSettlementDay(SettlementDayPostReq req) {
        Code codeEntity = codeRepository.findById(req.getCode()).orElseThrow();

        // 기존에 정산일자 행이 존재하는지 여부
        List<SettlementDay> prevSettlementDay = settlementDayRepository.findAll();

        SettlementDay settlementDay;

        if(prevSettlementDay.size() == 0) { // 없으면 삽입
            settlementDay = new SettlementDay();
            settlementDay.setCode(codeEntity);
        } else { // 있으면 수정
            settlementDay = prevSettlementDay.get(0);
            settlementDay.setCode(codeEntity);
        }

        settlementDayRepository.save(settlementDay);

        collectionScheduleService.scheduleTaskForDay(codeEntity.getName());

        return 1;
    }

    // 식당 미입금 정산 처리
    @Transactional
    public int postSettlementList(InsSettlementListReq p){
        settlementListRepository.insertSettlementList(p.getRestaurantId());

        return 1;
    }

    public static LocalDate getDateByCode(LocalDate baseDate, String code) {
        DayOfWeek dayOfWeek;

        switch (code) {
            case "00401": dayOfWeek = DayOfWeek.MONDAY; break;
            case "00402": dayOfWeek = DayOfWeek.TUESDAY; break;
            case "00403": dayOfWeek = DayOfWeek.WEDNESDAY; break;
            case "00404": dayOfWeek = DayOfWeek.THURSDAY; break;
            case "00405": dayOfWeek = DayOfWeek.FRIDAY; break;
            case "00406": dayOfWeek = DayOfWeek.SATURDAY; break;
            case "00407": dayOfWeek = DayOfWeek.SUNDAY; break;
            default: throw new IllegalArgumentException("유효하지 않은 코드: " + code);
        }

        return baseDate.with(dayOfWeek); // 해당 요일의 날짜 반환
    }

    // 정산 내역
    @Transactional
    public List<SelSettlementDetailRes> getSettlementList(){
        SelSettlementDetailReq req = new SelSettlementDetailReq();
        String code = settlementDayRepository.findSettlementDay();

        LocalDate today = LocalDate.now();
        LocalDate targetDate = getDateByCode(today, code); // code 에 따른 요일 설정
        LocalDate sixDayBeforeTargetDate = targetDate.minusDays(6); // targetDate 에 6일전

        // 날짜를 문자열로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String targetDateStr = targetDate.format(formatter);
        String sixDayBeforeTargetDateStr = sixDayBeforeTargetDate.format(formatter);

        req.setStartDate(sixDayBeforeTargetDateStr);
        req.setEndDate(targetDateStr);

        List<SelSettlementDetailRes> resList = adminSystemMapper.selSettlementDetail(req);

        return resList;

    }
}
