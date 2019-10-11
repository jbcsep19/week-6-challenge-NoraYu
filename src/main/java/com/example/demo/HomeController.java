package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Map;
import javax.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CarRepository carRepository;

    @Autowired
    CloudinaryConfig cloudc;

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
    public String processCarForm(@Valid Car car, @RequestParam("file") MultipartFile file, BindingResult result,@RequestParam String pic) {
        if (file.isEmpty()) {
            //if(!car.getPic().equalsIgnoreCase("https://article.images.consumerreports.org/f_auto/prod/content/dam/CRO%20Images%202018/Magazine/12December/CR-Magazine-Inline-Road-Test-December2018Issue-HondaInsight-10-18"))

            //car.setDefaultPic();
            car.setPic(pic);
            carRepository.save(car);
            return "redirect:/";
        }

        try {
            Map uploadResult = cloudc.upload(file.getBytes(),
                    ObjectUtils.asMap("resourcetype", "auto"));
            car.setPic(uploadResult.get("url").toString());
            carRepository.save(car);
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/addcar";
        }
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
    public String updateCar(@PathVariable("id") long id, Model model,@ModelAttribute Car car, @ModelAttribute Category category) {
        model.addAttribute("car", carRepository.findById(id).get());
        model.addAttribute("categorys", categoryRepository.findAll());
        car=carRepository.findById(id).get();
        String pic=car.getPic();
        model.addAttribute("pic",pic);

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
