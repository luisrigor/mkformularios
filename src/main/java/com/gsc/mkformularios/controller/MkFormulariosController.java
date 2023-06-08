package com.gsc.mkformularios.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("${app.baseUrl}")
@Api(value = "",tags = "MK FORMULARIOS")
@RestController
@CrossOrigin("*")
public class MkFormulariosController {

        @PostMapping(value="/")
        public String test(){
            return "MKFormularios!!";
        }
}
