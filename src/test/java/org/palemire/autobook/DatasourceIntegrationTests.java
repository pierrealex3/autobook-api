package org.palemire.autobook;

import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.palemire.autobook.user.UserEntity;
import org.palemire.autobook.user.UserRepository;
import org.palemire.autobook.user.UserVehicleAssociationEntity;
import org.palemire.autobook.user.UserVehiclePossessionId;
import org.palemire.autobook.vehicle.cud.VehicleEntity;
import org.palemire.autobook.vehicle.cud.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("itest")
@DataJpaTest
@Testcontainers
public class DatasourceIntegrationTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    void noUser_insertUser_userExists() {

        var user = new UserEntity();
        String id = "bamboo@me.com";
        user.setId(id);

        userRepository.save(user);

        assertThat(userRepository.findById(id)).isPresent();
    }

    @Test
    void noVehicle_insertVehicle_vehicleExists() {
        var vehicle = new VehicleEntity();
        String brand = "Toyota";
        vehicle.setBrand(brand);
        String model = "Sienna";
        vehicle.setModel(model);
        Short year = 2009;
        vehicle.setYear(year);
        String tag = "Unbreakable Unit";
        vehicle.setTag(tag);

        vehicleRepository.save(vehicle);

        assertThat(vehicleRepository.findAll()).hasSize(1).allMatch( v ->
            brand.equals(v.getBrand()) && model.equals(v.getModel()) && year.equals(v.getYear()) && tag.equals(v.getTag())
        );
    }

    @Sql(scripts = "/sql/insertUserAndVehicle.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void existingUserAndVehicle_associationIsCreated_vehicleAppearsInUserEntity() {

        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        var userEntity = entityManager.find(UserEntity.class, "me@whoami.com");
        var vehicleEntity = entityManager.find(VehicleEntity.class, 1);

        // assign vehicle to user by persisting asso
        var userVehicleAsso = new UserVehicleAssociationEntity();
        var possessionId = new UserVehiclePossessionId();
        possessionId.setUserEntity(userEntity);
        possessionId.setVehicleEntity(vehicleEntity);
        possessionId.setPossessionDate(Timestamp.valueOf(LocalDateTime.now()));
        userVehicleAsso.setId(possessionId);

        entityManager.persist(userVehicleAsso);
        entityManager.getTransaction().commit();

        // check out the asso
        entityManager.refresh(userEntity);


        assertThat(userEntity.getVehicleSet()).hasSize(1);
    }

}
