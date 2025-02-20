package com.green.attaparunever2.company;

import com.green.attaparunever2.common.DateTimeUtils;
import com.green.attaparunever2.common.excprion.CustomException;
import com.green.attaparunever2.common.repository.CodeRepository;
import com.green.attaparunever2.company.model.*;
import com.green.attaparunever2.config.jwt.JwtUser;
import com.green.attaparunever2.entity.Code;
import com.green.attaparunever2.entity.Company;
import com.green.attaparunever2.entity.User;
import com.green.attaparunever2.user.UserEmailVerificationRepository;
import com.green.attaparunever2.user.UserMapper;
import com.green.attaparunever2.user.UserRepository;
import com.green.attaparunever2.user.model.UserMailVerificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyMapper companyMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UserEmailVerificationRepository userEmailVerificationRepository;
    private final CompanyRepository companyRepository;
    private final CodeRepository codeRepository;

    //Status API 호출 URL
    private String STATUS_URL = "https://api.odcloud.kr/api/nts-businessman/v1/status";
    //Encoding 서비스키
//    private String SERVICE_KEY = "VukMM9i5Y7Mk%2F8YKm512TGLzUvx52TA1JycK%2BcdzZx1O6yfWkanTKlPznHV%2Fb6p2ENnjMK1U9FdgztyxUVpVdQ%3D%3D";
    //Decoding 서비스키
    private String SERVICE_KEY = "VukMM9i5Y7Mk/8YKm512TGLzUvx52TA1JycK+cdzZx1O6yfWkanTKlPznHV/b6p2ENnjMK1U9FdgztyxUVpVdQ==";
    //Return 타입 JSON/XML
    private String RETURN_TYPE = "JSON";

    public CompanyStatusRes status(CompanyStatusReq req) {
        Map<String, List<String>> reqBody = new HashMap<>();
        reqBody.put("b_no", List.of(req.getBNo())); // 요청받은 b_no를 리스트로 생성

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 서비스 키 인코딩 및 URI 생성
        String encodedServiceKey;
        URI uri;
        try {
            encodedServiceKey = URLEncoder.encode(SERVICE_KEY, StandardCharsets.UTF_8.toString());
            uri = new URI(STATUS_URL + "?serviceKey=" + encodedServiceKey);
            log.info("Encoded URI: {}", uri);
        } catch (Exception e) {
            log.error("URI 생성 실패", e);
            throw new RuntimeException("URI 생성 중 오류 발생", e);
        }
        HttpEntity<Map<String, List<String>>> entity = new HttpEntity<>(reqBody, headers);

        //API 호출
        ResponseEntity<Map> response = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                entity,
                Map.class
        );

        // 응답 데이터 확인
        log.info("Response Status Code: {}", response.getStatusCode());
        log.info("Response Headers: {}", response.getHeaders());
        log.info("Response Body: {}", response.getBody());

        //결과값 parse 후 res에 맞게 매핑
        Map<String, Object> responseBody = response.getBody();
        CompanyStatusRes companyStatusRes = new CompanyStatusRes();

        try {
            if (responseBody != null && responseBody.containsKey("data")) {
                List<Map<String, Object>> data = (List<Map<String, Object>>) responseBody.get("data");
                if (!data.isEmpty()) {
                    Map<String, Object> firstResult = data.get(0);
                    companyStatusRes.setStatusCode(response.getStatusCode().toString());
                    companyStatusRes.setStatusCodeNm((String) responseBody.get("status_code"));
                    companyStatusRes.setBNo((String) firstResult.get("b_no"));
                    companyStatusRes.setBStt((String) firstResult.get("b_stt"));
                    companyStatusRes.setBSttCd((String) firstResult.get("b_stt_cd"));
                    companyStatusRes.setEndDt((String) firstResult.get("end_dt"));
                }
            }

//            if("01".equals(companyStatusRes.getBSttCd()))
//            {
//                정상적인 사업일 경우 사용하고싶은데로 코딩
//            } else {
//                02, 03이 있을건데 코딩을하느냐, 마느냐
//            }

//            "b_no" -> "3848801297"
//            "b_stt" -> "계속사업자"
//            "b_stt_cd" -> "01"
//            "tax_type" -> "부가가치세 일반과세자"
//            "tax_type_cd" -> "01"
//            "end_dt" -> ""
//            "utcc_yn" -> "N"
//            "tax_type_change_dt" -> ""
//            "invoice_apply_dt" -> ""
//            "rbf_tax_type" -> "해당없음"
//            "rbf_tax_type_cd" -> "99"

            log.debug("api 호출 후 결과:: "+companyStatusRes.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return companyStatusRes;

    }

    @Transactional
    public int postEmployee(SignUpEmployeeReq req) {
        // 회사 정보 조회
        Company company = companyRepository.findById(req.getCompanyId()).orElse(null);
        if (company == null) {
            throw new CustomException("회사를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
        // 코드 조회
        Code code = codeRepository.findById("00104").orElse(null);
        if (code == null) {
            throw new CustomException("코드를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // uid 설정
        String companyCd = company.getCompanyCd();
        String uid = companyCd + req.getEmployeeNum();

        // 인증정보 조회(만약 인증이 만료된 아이디로 가입하려는 경우 인증 정보를 지우고 유저 정보도 지워야 함)
        UserMailVerificationDTO userMailVerificationDTO = userMapper.selUserEmailVerificationByUId(uid);

        if(userMailVerificationDTO != null) {
            LocalDateTime now = LocalDateTime.now();

            // 인증 기간이 만료되었다면 인증, 회원정보 삭제
            if (now.isAfter(DateTimeUtils.convertToLocalDateTime(userMailVerificationDTO.getExpiredDate()))) {
                userEmailVerificationRepository.deleteById(userMailVerificationDTO.getUserId());
                userRepository.deleteById(userMailVerificationDTO.getUserId());
            }
        }

        // 비밀번호 암호화
        String hashedPassword = BCrypt.hashpw(req.getUpw(), BCrypt.gensalt());

        // 유저 정보 설정
        User user = new User();
        user.setCompany(company);
        user.setName(req.getName());
        user.setPhone(req.getPhone());
        user.setEmail(req.getEmail());
        user.setUid(uid);
        user.setUpw(hashedPassword);
        user.setCode(code);

        userRepository.save(user);

        return 1;
    }

    public List<GetEmployeeRes> getEmployee(GetEmployeeReq req) {
        List<GetEmployeeRes> list = companyMapper.selEmployee(req);

        if (list == null || list.isEmpty()) {
            throw new CustomException("조건에 맞는 사원이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        return list;
    }
}
