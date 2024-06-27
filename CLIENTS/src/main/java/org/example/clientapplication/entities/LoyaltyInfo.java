package org.example.clientapplication.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Setter
@Entity
@Getter
@NoArgsConstructor
@Table(name = "loyalty_info")
public class LoyaltyInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @Cascade(CascadeType.ALL)
    private UserAccount user;
    @Getter
    private int loyaltyPoints;
    @OneToOne
    private LoyaltyLevel level;

    @Override
    public String toString() {
        return "LoyaltyInfo{" +
                "id=" + id +
                ", user=" + user +
                ", loyaltyPoints=" + loyaltyPoints +
                ", level=" + level +
                '}';
    }

    public LoyaltyLevel getLoyaltyLevel() {
        return level;
    }
}
