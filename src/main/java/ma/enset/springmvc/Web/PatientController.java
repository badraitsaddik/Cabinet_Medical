package ma.enset.springmvc.Web;

import jakarta.validation.Valid;
import ma.enset.springmvc.Entities.Patient;
import ma.enset.springmvc.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;


import java.util.List;

@Controller
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/user/index")
    public String index(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "5") int size,
                        @RequestParam(name = "keyword", defaultValue = "") String keyword
    ) {
        Page<Patient> patientsPage = patientRepository.findByNomContainsIgnoreCaseOrPrenomContainsIgnoreCase(keyword, keyword, PageRequest.of(page, size));

        model.addAttribute("listPatients", patientsPage.getContent());
        model.addAttribute("pages", new int[patientsPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "patients";
    }

    @GetMapping("/admin/deletePatient")
    @PreAuthorize("hasRole('Role_ADMIN')")
    public String deletePatient(@RequestParam(name = "id") Long id, int page, String keyword) {
        patientRepository.deleteById(id);
        return "redirect:/user/index?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/user/index";
    }

    @GetMapping("/user/Patients")
    public List<Patient> patients() {
        return patientRepository.findAll();
    }

    @GetMapping("/admin/formPatient")
    @PreAuthorize("hasRole('Role_ADMIN')")
    public String formPatient(Model model) {
        model.addAttribute("patient", new Patient());
        return "formPatient";

    }

    @PostMapping(path = "/admin/save")
    @PreAuthorize("hasRole('Role_ADMIN')")
    public String save(Model model, @Valid Patient patient, BindingResult bindingResult, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "") String keyword) {
        if (bindingResult.hasErrors()) return "formPatient";
        patientRepository.save(patient);
        return "redirect:/user/index";
    }

    @GetMapping("/admin/editPatient")
    @PreAuthorize("hasRole('Role_ADMIN')")
    public String editPatient(Model model, Long id, int page, String keyword) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient == null) throw new RuntimeException("Patient not found");
        model.addAttribute("patient", patient);
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);
        return "editPatient";

    }

}
