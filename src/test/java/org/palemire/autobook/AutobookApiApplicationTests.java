package org.palemire.autobook;


import org.junit.jupiter.api.Test;
import org.palemire.autobook.user.UserRepository;
import org.palemire.autobook.vehicle.cud.CUDVehicleDto;
import org.palemire.autobook.vehicle.cud.CUDVehicleService;
import org.palemire.autobook.vehicle.cud.VehicleRepository;
import org.palemire.autobook.vehicle.fetch.FetchVehicleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class AutobookApiApplicationTests {

	@Autowired
	private FetchVehicleDao fetchVehicleDao;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private CUDVehicleService cudVehicleService;


	@Test
	void fetchVehicleDao() {
		var x = fetchVehicleDao.fetchAllVehicles("plemire@nuance.com");
		assertThat(x.size() ).isEqualTo(3);
	}

	@Test
	void createVehicle() {
		assertDoesNotThrow( () -> {

			var userEntity = userRepository.findById("plemire@nuance.com").get();

			var vehicleDto = CUDVehicleDto.builder().brand("Mazda").model("Miata").year((short) 2011).build();

			cudVehicleService.createVehicle(vehicleDto, userEntity);

		});

	}

	@Test
	@Transactional
	void fetchVehicleBasic() {
		assertDoesNotThrow( () -> {

			var vehicle = vehicleRepository.findById(4).get();
			vehicle.getUserSet().forEach( v -> System.out.println(v.getId()) );
		});
	}

	@Test
	void modifyVehicle() {
		assertDoesNotThrow( () -> {
			var vehicleDto = CUDVehicleDto.builder()
					.brand("Mazda")
					.model("Miata")
					.year((short) 2011)
					.id(4)
					.tag("Miracle")
					.build();

			cudVehicleService.modifyVehicle(vehicleDto);

		});
	}



}
