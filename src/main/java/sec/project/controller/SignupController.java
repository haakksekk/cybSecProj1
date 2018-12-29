package sec.project.controller;

import java.sql.SQLException;
import java.util.List;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;

    @PostConstruct
    public void init() {
        signupRepository.save(new Signup("Ted", "Ted Street"));
        signupRepository.save(new Signup("Ed", "Ed Street"));
    }

    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address, Model model) throws SQLException {
        signupRepository.save(new Signup(name, address));
        model.addAttribute("name", signupRepository.findFirstByOrderByIdDesc().getName());
        model.addAttribute("address", address);
        return "done";
    }

    @RequestMapping(value = "/signups", method = RequestMethod.GET)
    public String loadSignups(Model model) {
        List<Signup> signups = signupRepository.findAll();
        model.addAttribute("signups", signups);
        return "signups";
    }

}