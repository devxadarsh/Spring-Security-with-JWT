package org.devx.springscurityclient.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor // It is used to define constructor with no arguments (default constructor)
public class VerificationToken {

    // Here we are hardcoding the expiration time to 10 minutes for now
    private static final int EXPIRATION_TIME =1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date expiryDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_Id", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_VERIFY_TOKEN"))
    private User user;

    public VerificationToken(User user, String token) {
        super();
        this.user = user;
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION_TIME);
    }

    public VerificationToken(String token) {
        super();
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION_TIME);
    }

    private Date calculateExpiryDate(int expiryTime){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTime);
        return new Date(cal.getTime().getTime());
    }
}
