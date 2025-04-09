    package org.balikin.entity;

    import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.util.Date;

    @Entity
    @Table(name = "friend_request")
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public class FriendRequest extends PanacheEntityBase {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "sender_id", nullable = false)
        private Auth sender;

        @ManyToOne
        @JoinColumn(name = "receiver_id", nullable = false)
        private Auth to;



        @Temporal(TemporalType.TIMESTAMP)
        private Date createdDate;
        public enum FriendRequestStatus {
            PENDING,
            ACCEPTED,
            REJECTED
        }
        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private FriendRequestStatus status;


    }
