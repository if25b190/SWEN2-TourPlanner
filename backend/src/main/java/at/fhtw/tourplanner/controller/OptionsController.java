package at.fhtw.tourplanner.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OptionsController {

    @RequestMapping(path = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<Object> preflight() {
        return ResponseEntity.ok(null);
    }

}
