
package com.pearl.maxkey.demo;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author pearl
 * @since 2023-03-23
 */
@RestController
public class TestController {

    @GetMapping("/home")
    String permit() {
        return "home";
    }
}
