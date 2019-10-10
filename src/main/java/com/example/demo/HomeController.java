package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CarRepository carRepository;

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("categorys", categoryRepository.findAll());
        model.addAttribute("cars", carRepository.findAll());

        return "index";
    }

    @GetMapping("/addcategory")
    public String categoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "categoryform";
    }

    @PostMapping("/process_category")
    public String processCategoryForm(@Valid Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "categoryform";
        }
        categoryRepository.save(category);

        return "redirect:/categorylist";
    }

    @RequestMapping("/categorylist")
    public String categoryList(Model model) {
        model.addAttribute("categorys", categoryRepository.findAll());

        return "categorylist";
    }

    @GetMapping("/addcar")
    public String carForm(Model model) {
        model.addAttribute("categorys", categoryRepository.findAll());
        model.addAttribute("car", new Car());
        return "carform";
    }

    @PostMapping("/process_car")
    public String processCarForm(@Valid Car car, BindingResult result) {
        if (result.hasErrors()) {
            return "carform";
        }
        carRepository.save(car);

        return "redirect:/";
    }

    @RequestMapping("/carlist")
    public String carList(Model model) {
        model.addAttribute("cars", carRepository.findAll());

        return "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showCategory(@PathVariable("id") long id, Model model) {
        model.addAttribute("category", categoryRepository.findById(id).get());
        return "showcategory";
    }

    @RequestMapping("/update/{id}")
    public String updateCategory(@PathVariable("id") long id, Model model) {
        Category c = categoryRepository.findById(id).get();

        if (c.getCars().size() == 0) {
            model.addAttribute("category", categoryRepository.findById(id).get());
        } else {
//            for(Car b:c.getCars()){
//                b.setCategory(null);
//                carRepository.save(b);
//            }
//            model.addAttribute("category", categoryRepository.findById(id).get());
            return "redirect:/";
        }

        return "categoryform";
    }

    @RequestMapping("/delete/{id}")
    public String delCategory(@PathVariable("id") long id) {
        categoryRepository.deleteById(id);
        return "redirect:/";
    }

    @RequestMapping("/detail_car/{id}")
    public String showCar(@PathVariable("id") long id, Model model) {
        model.addAttribute("car", carRepository.findById(id).get());
        return "showcar";
    }

    @RequestMapping("/update_car/{id}")
    public String updateCar(@PathVariable("id") long id, Model model) {
        model.addAttribute("car", carRepository.findById(id).get());
        model.addAttribute("categorys", categoryRepository.findAll());
        return "carform";
    }

    @RequestMapping("/delete_car/{id}")
    public String delCar(@PathVariable("id") long id) {
        carRepository.deleteById(id);
        return "redirect:/";
    }

    @RequestMapping("/{categoryName}")
    public String searchc(Model model, @PathVariable("categoryName") String search) {
        ArrayList<Category> clist = new ArrayList<>();
        Set<Car> alllist = new HashSet();
        Set<Car> ccarlist;
        clist = categoryRepository.findByCategoryNameContainingIgnoreCase(search);
        for (Category c : clist) {
            ccarlist = c.getCars();
            for (Car b : ccarlist) {
                alllist.add(b);
            }
        model.addAttribute("cars", alllist);
    }
        return "index";
}



    @PostMapping("/processsearch")
    public String searchResult(Model model, @RequestParam(name = "search") String search) {
        String[] list = search.split(" ");
        ArrayList<Car>carlist = new ArrayList<>();
        ArrayList<Category> clist = new ArrayList<>();
        Set<Car> ccarlist;
        Set<Car> alllist = new HashSet();

        for (String s : list) {
           carlist = carRepository.findByCarNameContainingIgnoreCaseOrColorContainingIgnoreCaseOrMakeContainingIgnoreCase(s,s,s);
            clist = categoryRepository.findByCategoryNameContainingIgnoreCase(s);
            for (Car b :carlist) {
                alllist.add(b);
            }
            for (Category c : clist) {
                ccarlist = c.getCars();
                for (Car b : ccarlist) {
                    alllist.add(b);
                }
            }
            model.addAttribute("cars", alllist);
        }
        return "index";
    }
}
