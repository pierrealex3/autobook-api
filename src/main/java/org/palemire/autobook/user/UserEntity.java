package org.palemire.autobook.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "api_user")
@Setter
@Getter
public class UserEntity {

    @Id
    @Column(name = "user_id")
    private String id;
}
