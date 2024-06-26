package shop.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /*
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;*/

    private String address;

    private Long card;

    private Long amountpay;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime date;

    private OrderState orderState;


    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    /*
    public void setAddress(Address address) {
        this.address = address;
        address.setOrder(this);
    }*/
/*
    public void setPayment(Payment payment) {
        this.payment = payment;
        payment.setOrder(this);
    }*/


    public static Order createOrder(Member member, String address, Long card,Long amountpay,List<OrderItem> orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setAddress(address);
        order.setCard(card);
        order.setAmountpay(amountpay);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setOrderState(OrderState.PREPARING);
        order.setDate(LocalDateTime.now());
        return order;
    }

}
