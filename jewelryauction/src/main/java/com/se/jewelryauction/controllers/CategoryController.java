package com.se.jewelryauction.controllers;

import com.se.jewelryauction.services.ICategoryServices;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${app.api.version.v1}/category")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryServices categoryServices;


}
