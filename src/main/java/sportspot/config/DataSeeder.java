package sportspot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sportspot.model.AppUser;
import sportspot.model.Locations;
import sportspot.model.Role;
import sportspot.model.Sport;
import sportspot.repository.AppUserRepository;
import sportspot.repository.LocationsRepository;
import sportspot.repository.SportRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final SportRepository sportRepository;
    private final LocationsRepository locationsRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(
            SportRepository sportRepository,
            LocationsRepository locationsRepository,
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.sportRepository = sportRepository;
        this.locationsRepository = locationsRepository;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seedUsers();
        Map<String, Sport> sportsByName = seedSports();
        seedLocations(sportsByName);
    }

    private void seedUsers() {
        List<AppUser> users = List.of(
                AppUser.builder()
                        .email("player@sportspot.bg")
                        .passwordHash(passwordEncoder.encode("Player123!"))
                        .displayName("Иван Играча")
                        .role(Role.USER)
                        .active(true)
                        .build(),
                AppUser.builder()
                        .email("owner@sportspot.bg")
                        .passwordHash(passwordEncoder.encode("Owner123!"))
                        .displayName("Петър Собственик")
                        .role(Role.OWNER)
                        .active(true)
                        .build(),
                AppUser.builder()
                        .email("admin@sportspot.bg")
                        .passwordHash(passwordEncoder.encode("Admin123!"))
                        .displayName("Администратор SportSpot")
                        .role(Role.ADMIN)
                        .active(true)
                        .build()
        );

        int inserted = 0;
        for (AppUser user : users) {
            if (!appUserRepository.existsByEmail(user.getEmail())) {
                appUserRepository.save(user);
                inserted++;
            }
        }

        log.info("Users seeding finished: {} added, {} total.", inserted, appUserRepository.count());
    }

    private Map<String, Sport> seedSports() {
        List<Sport> sports = List.of(
                Sport.builder()
                        .name("Table Tennis")
                        .indoorAllowed(true)
                        .minTemperature(14.0)
                        .maxTemperature(28.0)
                        .windSpeedKph(14.4)
                        .rainAllowed(false)
                        .build(),
                Sport.builder()
                        .name("Mini Football")
                        .indoorAllowed(true)
                        .minTemperature(5.0)
                        .maxTemperature(34.0)
                        .windSpeedKph(36.0)
                        .rainAllowed(false)
                        .build(),
                Sport.builder()
                        .name("Basketball")
                        .indoorAllowed(true)
                        .minTemperature(10.0)
                        .maxTemperature(32.0)
                        .windSpeedKph(25.2)
                        .rainAllowed(false)
                        .build()
        );

        Map<String, Sport> result = new HashMap<>();
        int inserted = 0;

        for (Sport sport : sports) {
            Sport existing = sportRepository.findByName(sport.getName()).orElse(null);
            if (existing == null) {
                existing = sportRepository.save(sport);
                inserted++;
            }
            result.put(existing.getName(), existing);
        }

        log.info("Sports seeding finished: {} added, {} total.", inserted, result.size());
        return result;
    }

    private void seedLocations(Map<String, Sport> sportsByName) {
        Sport tableTennis = sportsByName.get("Table Tennis");
        Sport miniFootball = sportsByName.get("Mini Football");
        Sport basketball = sportsByName.get("Basketball");

        List<Locations> locations = List.of(
                Locations.builder()
                        .name("Борисовата градина — Маси за тенис на маса (открито)")
                        .address("бул. Евлоги и Христо Георгиеви, Борисовата градина")
                        .city("София")
                        .latitude(42.6836)
                        .longitude(23.3389)
                        .isIndoor(false)
                        .sport(tableTennis)
                        .build(),
                Locations.builder()
                        .name("Спортен център Диана — Зала тенис на маса (закрито)")
                        .address("ул. Никола Габровски 56, Дианабад")
                        .city("София")
                        .latitude(42.6687)
                        .longitude(23.3478)
                        .isIndoor(true)
                        .sport(tableTennis)
                        .build(),
                Locations.builder()
                        .name("Спортен комплекс Младost — Игрище мини футбол (открито)")
                        .address("бул. Александър Малинов, ж.к. Младost 1")
                        .city("София")
                        .latitude(42.6558)
                        .longitude(23.3794)
                        .isIndoor(false)
                        .sport(miniFootball)
                        .build(),
                Locations.builder()
                        .name("Зона Б-5 — Закрит манеж за мини футбол")
                        .address("ул. Българска морава, Зона Б-5")
                        .city("София")
                        .latitude(42.7031)
                        .longitude(23.3095)
                        .isIndoor(true)
                        .sport(miniFootball)
                        .build(),
                Locations.builder()
                        .name("Южен парк — Открито баскетболно игрище")
                        .address("бул. Гоце Делчев, Южен парк")
                        .city("София")
                        .latitude(42.6714)
                        .longitude(23.3082)
                        .isIndoor(false)
                        .sport(basketball)
                        .build(),
                Locations.builder()
                        .name("Зала Триадица — Закрито баскетболно игрище")
                        .address("ул. Хан Аспарух, Триадица")
                        .city("София")
                        .latitude(42.6879)
                        .longitude(23.3176)
                        .isIndoor(true)
                        .sport(basketball)
                        .build()
        );

        int inserted = 0;
        for (Locations location : locations) {
            if (location.getSport() != null && !locationsRepository.existsByName(location.getName())) {
                locationsRepository.save(location);
                inserted++;
            }
        }

        log.info("Locations seeding finished: {} added, {} total.", inserted, locationsRepository.count());
    }
}