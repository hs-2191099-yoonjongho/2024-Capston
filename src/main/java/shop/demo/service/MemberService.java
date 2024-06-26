package shop.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.demo.domain.Member;
import shop.demo.dto.memberDTO.AddressUpdateDTO;
import shop.demo.dto.memberDTO.MemberDTO;
import shop.demo.dto.memberDTO.MemberInfoDTO;
import shop.demo.dto.orderDTO.OrderDTO;
import shop.demo.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /* 추가 5.19 MemberController에서 id조회시 카트와 오더를 받기 위함 */
    private final OrderService orderService;
    /* 수정 */

    @Transactional
    public void saveMember(MemberDTO memberDTO){
        /* 5.20 중복 방지 예외처리 추가 */
        /* 5.25 수정 */
        if (memberRepository.existsByEmail(memberDTO.getEmail())) {
            throw new IllegalStateException("중복된 이메일입니다."); // 예외 던져줌 컨트롤러에서 처리 .
        }
        Member member = Member.createMember(memberDTO, passwordEncoder);
        memberRepository.save(member);
    }


    //　이메일로 찾기
    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }


    public MemberInfoDTO findMemberById(Long id) {
        Member member = memberRepository.findOne(id);

    /* 수정 */
        if (member != null) {
            MemberInfoDTO memberInfoDTO = new MemberInfoDTO();
            /* 5.19 회원의 카트 정보*/
            /* 회원의 주문 정보 가져오기 */
            List<OrderDTO> orderDTOS = orderService.viewOrdersByMemberId(id);
            memberInfoDTO.setOrderDTOS(orderDTOS);
            /* 5.19 수정 */

            memberInfoDTO.setId(member.getId());
            memberInfoDTO.setName(member.getName());
            memberInfoDTO.setEmail(member.getEmail());
            memberInfoDTO.setAddress(member.getAddress());
            memberInfoDTO.setDate(member.getRegistrationDate());
            return memberInfoDTO;
                } else {
            return null;
    }
}



    public List<Member> findMembersByName(String name) {
        return memberRepository.findByName(name);
    }

    @Transactional
    public boolean deleteMemberById(Long id, String inputEmail, String inputPassword) {
        if (memberRepository.existsById(id)) {
            Member member = memberRepository.findOne(id);

            if (member.getEmail().equals(inputEmail) && passwordEncoder.matches(inputPassword, member.getPassword()))  {
                memberRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }

    /* 주소 수정 추가 */
    @Transactional
    public void updateMemberAddress(Long memberId, AddressUpdateDTO addressUpdateDTO) {
        Member member = memberRepository.findOne(memberId);
        member.setAddress(addressUpdateDTO.getNewAddress());
        memberRepository.save(member);

    }

    public String getMemberAddress(Long memberId){
        return memberRepository.findAddressByMemberId(memberId);
    }
}
