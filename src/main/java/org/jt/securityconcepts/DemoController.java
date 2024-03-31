package org.jt.securityconcepts;

import lombok.RequiredArgsConstructor;
import org.jt.securityconcepts.model.Customer;
import org.jt.securityconcepts.repository.CustomerRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class DemoController {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;

    @GetMapping
    public String hello(Principal principal) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        return "Hello " + principal.getName();
    }

    @PostMapping("/create")
    public Customer save(@RequestBody Customer customer) {
        var bcrypt = encoder.encode(customer.getPassword());
        customer.setPassword(bcrypt);

        return customerRepository.save(customer);
    }

    @PostMapping("/customer/login")
    public Customer login(@RequestBody Customer customer) {
        var authentication = new UsernamePasswordAuthenticationToken(customer.getUsername(), customer.getPassword());
        authenticationManager.authenticate(authentication);
        return customer;
    }
}
