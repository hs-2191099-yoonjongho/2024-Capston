package shop.demo.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.demo.domain.Member;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;


    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

//    public List<Member> findAll() {
//        return em.createQuery("select m from Member m", Member.class)
//                .getResultList();
//    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    public Member findByEmail(String email) {
        try {
            return em.createQuery("select m from Member m where m.email = :email", Member.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // 이메일로 찾지 못한 경우 null 반환
        }
    }

    public void deleteById(Long id) {
        Member member = em.find(Member.class, id);
        if (member != null) {
            em.remove(member);
        }
    }

    public boolean existsById(Long id) {
        Member member = em.find(Member.class, id);
        return member != null;
    }


    /* 이메일 중복 확인하기 위해 추가 5.20 */
    public boolean existsByEmail(String email) {
        try {
            return em.createQuery("select count(m) > 0 from Member m where m.email = :email", Boolean.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
    }

    public String findAddressByMemberId(Long memberId) {
        return em.createQuery("SELECT m.address FROM Member m WHERE m.id = :memberId", String.class)
                .setParameter("memberId", memberId)
                .getSingleResult();
    }
}

