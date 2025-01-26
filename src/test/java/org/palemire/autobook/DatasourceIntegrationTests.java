package org.palemire.autobook;

import org.junit.jupiter.api.Test;
import org.palemire.autobook.user.UserEntity;
import org.palemire.autobook.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("itest")
@DataJpaTest
public class DatasourceIntegrationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void noUser_insertUser_userExists() {

        var user = new UserEntity();
        String id = "bamboo@me.com";
        user.setId(id);

        userRepository.save(user);

        assertThat(userRepository.findById(id)).isPresent();
    }
}
