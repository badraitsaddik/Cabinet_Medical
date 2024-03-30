package ma.enset.springmvc;

import ma.enset.springmvc.Entities.Patient;
import ma.enset.springmvc.Repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@SpringBootApplication
public class SpringMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMvcApplication.class, args);
    }

    //@Bean
    public CommandLineRunner start(PatientRepository patientRepository) {
        return args -> {
            Patient patient= new Patient();
            patient.setNom("allaoui");
            patient.setPrenom("hamada");
            patient.setDateNaissance(new Date());
            patient.setScore(213);

            Patient patient1 = new Patient(null,"hachimi","laila",new Date(),false,50);

            Patient patient2 = Patient.builder()
                    .nom("alami")
                    .prenom("amina")
                    .dateNaissance(new Date())
                    .malade(true)
                    .score(70)
                    .build();

            patientRepository.save(patient);
            patientRepository.save(patient1);
            patientRepository.save(patient2);

            patientRepository.findAll().forEach(p->{
                System.out.println(p.toString());
            });

        };
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
