package co.za.access.profiler.dataProcessing.controller;

import co.za.access.profiler.dataCollection.facebookSearch.service.FacebookServiceImpl;
import co.za.access.profiler.dataProcessing.model.Target;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
//@RequestMapping("/result")
public class ResultController {

    @Autowired
    private FacebookServiceImpl facebookService;
    @GetMapping("/results/{target}")
    public String getUsers(Model model, @RequestParam("targetName") String targetName) {
//        List<Target> targets = facebookService.searchPerson(targetName,null);
//                model.addAttribute("users", targets);
        return "results";
    }

}
