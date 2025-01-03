package com.ict.camping.admin.members.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.camping.admin.members.service.AMemberService;
import com.ict.camping.admin.members.vo.AMemberVO;
import com.ict.camping.admin.members.vo.AOperatorVO;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/api/member")
public class AMemberController {
    @Autowired
    private AMemberService memberService;

    @GetMapping("/members")
    public List<AMemberVO> getAllMembers(){
        List<AMemberVO> allMembers = memberService.getAllMembers();
        return allMembers;
    }

    @GetMapping("/members/{user_idx}")
    public AMemberVO getMemberDetail(@PathVariable("user_idx") String user_idx) {
        System.out.println("잘 왔나 보려고 찍는거 : " + user_idx);
        return memberService.getMemberDetail(user_idx);
    }
    
    // 중복검사
    @GetMapping("members/check-id")
    public boolean checkIdDuplicate(@RequestParam("id") String id) {
        boolean isDuplicate = memberService.isIdDuplicate(id);
        if (isDuplicate) {
            System.out.println("중복");
        } else  {
            System.out.println("사용 가능");
        }
        return isDuplicate;
    }

    // 사용자 확인
    @GetMapping("members/check-idx")
    public boolean checkIdxDuplicate(@RequestParam("user_idx") String id) {
        boolean isDuplicate = memberService.isIdxDuplicate(id);
        if (isDuplicate) {
            System.out.println("존재");
        } else  {
            System.out.println("존재하지 않음");
        }
        return isDuplicate;
    }
    
    @GetMapping("/operators")
    public List<AOperatorVO> getAllOperators() {
        List<AOperatorVO> allOperators = memberService.getAllOperators();
        return allOperators;
    }

    @GetMapping("/operators/{business_idx}")
    public AOperatorVO getOperatorDetail(@PathVariable("business_idx") String business_idx) {
        System.out.println("잘 넘어오나 : " + business_idx);
        return memberService.getOperatorDetail(business_idx);
    }

    @GetMapping("/operators/check/{user_idx}")
    public int getBusinessIdx(@PathVariable("user_idx") String user_idx) {
        System.out.println("잘 넘어오나 : " + user_idx);
        return memberService.getBusinessIdxByUserIdx(user_idx);
    }

    @GetMapping("/operators/check-user")
    public boolean cheeckBusinessDuplicate(@RequestParam("user_idx") String user_idx) {
        boolean isDuplicate = memberService.isBuisnessDuplicate(user_idx);
        if (isDuplicate) {
            System.out.println("이미 존재");
        } else {
            System.out.println("사용가능");
        }
        return isDuplicate;
    }

    @DeleteMapping("/operators/delete/{business_idx}")
    public ResponseEntity<String> deleteOperator(@PathVariable("business_idx") String business_idx){
        try {
            // 댓글 삭제 서비스 호출
            boolean isDeleted = memberService.deleteOperator(business_idx);
            if (isDeleted) {
                return ResponseEntity.ok("사업자가 성공적으로 삭제되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제하려는 사업자가 존재하지 않습니다.");
            }
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사업자 삭제 중 오류가 발생했습니다.");
        }
    }

    @DeleteMapping("/members/delete/{user_idx}")
    public ResponseEntity<String> deleteMember(@PathVariable("user_idx") String user_idx){
        try {
            // 댓글 삭제 서비스 호출
            boolean isDeleted = memberService.deleteUser(user_idx);
            if (isDeleted) {
                return ResponseEntity.ok("회원이 성공적으로 삭제되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제하려는 회원이 존재하지 않습니다.");
            }
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 삭제 중 오류가 발생했습니다.");
        }
    }
    
    
    
    @PostMapping("/insert")
    public int postMethodName(@ModelAttribute("data") AMemberVO formData) {
        int result = memberService.insertMembers(formData);
        if (result == 0) {
            System.out.println("데이터 삽입 실패");
            return 0;
        }
        System.out.println("데이터 삽입 성공");
        return result;
    }
    
    @PostMapping("/insert/operator")
    public int postMethodName(@ModelAttribute("data") AOperatorVO formData) {
        int result = memberService.insertOperators(formData);
        if (result == 0) {
            System.out.println("데이터 삽입 실패");
            return 0;
        }
        System.out.println("데이터 삽입 성공");
        return result;
    }

    @PostMapping("/members/update/{user_idx}")
    public ResponseEntity<String> updateMembers(@PathVariable("user_idx") String user_idx,
    @ModelAttribute AMemberVO formData) {
        AMemberVO existingMember = memberService.getMemberDetail(user_idx);
        if (existingMember == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 user_idx에 해당되는 user가 존재하지 않음");
        }
        existingMember.setUsername(formData.getUsername() != null ? formData.getUsername() : existingMember.getUsername());
        existingMember.setEmail(formData.getEmail() != null ? formData.getEmail() : existingMember.getEmail());
        existingMember.setAddress(formData.getAddress() != null ? formData.getAddress() : existingMember.getAddress());
        existingMember.setPhone(formData.getPhone() != null ? formData.getPhone() : existingMember.getPhone());
        
        int result = memberService.updateMembers(existingMember);
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 정보 업데이트에 실패했습니다.");
        }
        return ResponseEntity.ok("회원 정보가 성공적으로 업데이트되었습니다.");
    }

    @PostMapping("/business/update/{business_idx}")
    public ResponseEntity<String> updateBusiness(@PathVariable("business_idx") String business_idx,
    @ModelAttribute AOperatorVO formData) {
        AOperatorVO existingBusiness = memberService.getOperatorDetail(business_idx);
        if (existingBusiness == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 business_idx 해당되는 user가 존재하지 않음");
        }
        existingBusiness.setBusiness_name(formData.getBusiness_name() != null ? formData.getBusiness_name() : existingBusiness.getBusiness_name());
        existingBusiness.setBusiness_number(formData.getBusiness_number() != null ? formData.getBusiness_number() : existingBusiness.getBusiness_number());
        existingBusiness.setStarted_date(formData.getStarted_date() != null ? formData.getStarted_date() : existingBusiness.getStarted_date());
        existingBusiness.setContentId(formData.getContentId() != 0 ? formData.getContentId() : existingBusiness.getContentId());
        
        int result = memberService.updateBusiness(existingBusiness);
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사업자 정보 업데이트에 실패했습니다.");
        }
        return ResponseEntity.ok("사업자 정보가 성공적으로 업데이트되었습니다.");
    }
    
    
    
}
