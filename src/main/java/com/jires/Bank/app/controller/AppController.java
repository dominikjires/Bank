package com.jires.Bank.app.controller;

import com.jires.Bank.app.domain.Account;
import com.jires.Bank.app.domain.ExchangeRate;
import com.jires.Bank.app.domain.User;
import com.jires.Bank.app.repository.AccountRepository;
import com.jires.Bank.app.repository.ExchangeRateRepository;
import com.jires.Bank.app.repository.UserRepository;
import com.jires.Bank.app.service.CustomUserDetailsServiceImpl;
import com.jires.Bank.app.service.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
//@RequestMapping("/api")
public class AppController {
    public CustomUserDetailsServiceImpl customUserDetailsServiceImpl;

    public Boolean state = true; // variable to store state of recent operation

    public AppController() {
        this.customUserDetailsServiceImpl = customUserDetailsServiceImpl;
    }

    // handles GET request to homepage
    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    // handles GET request to login page
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // handles GET request to confirmation page after registration
    @GetMapping("/confirm")
    public String confirm(Model model, @RequestParam("token") String token) {
        // confirms user's email by token
        model.addAttribute("token", customUserDetailsServiceImpl.confirmToken(token));
        return "confirm";
    }

    // handles GET request to user dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) throws IOException {
        String name = authentication.getName();
        long id = UserRepository.getId(name);
        User user = UserRepository.findUser(id);

        // adds user info and list of accounts to the model
        model.addAttribute("user", user);
        List<Account> listAccounts = AccountRepository.findAccountsByUserId(user.getId());
        model.addAttribute("listAccounts", listAccounts);

        // adds user logs to the model
        List<String> listOfLogs = UserService.readLog(user.getId());
        model.addAttribute("listOfLogs", listOfLogs);

        model.addAttribute("show", false); // hide forms
        List<ExchangeRate> listExchangeRates = ExchangeRateRepository.getExchangeRates();
        model.addAttribute("listExchangeRates", listExchangeRates);

        return "dashboard";
    }


    // handles GET request to forms for deposit, payment and opening account
    @RequestMapping(value = {"/deposit", "/payment", "/open"})
    public String configurateDashboard(Model model, Authentication authentication) throws IOException {
        String name = authentication.getName();
        long id = UserRepository.getId(name);
        User user = UserRepository.findUser(id);

        // adds user info and list of accounts to the model
        model.addAttribute("user", user);
        List<Account> listAccounts = AccountRepository.findAccountsByUserId(user.getId());
        model.addAttribute("listAccounts", listAccounts);

        // adds user logs to the model
        List<String> listOfLogs = UserService.readLog(user.getId());
        model.addAttribute("listOfLogs", listOfLogs);

        model.addAttribute("show", true); // show forms
        model.addAttribute("success", state); // indicates state of recent operation
        model.addAttribute("message", "/deposit"); // message displayed to user after form submission
        List<ExchangeRate> listExchangeRates = ExchangeRateRepository.getExchangeRates();
        model.addAttribute("listExchangeRates", listExchangeRates);

        return "redirect:/dashboard";
    }

    // This method handles the deposit action
    @PostMapping("/deposit")
    public String handleDeposit(@RequestParam("account-type-deposit") String accountType, @RequestParam(value = "amount-deposit", defaultValue = "0") double amount, Model model, Authentication authentication) throws IOException {
        String message = "Vklad proběhl úspěšně";
        String name = authentication.getName();
        long id = UserRepository.getId(name);

        try {
            // Check if the user has the specified account type
            if (UserService.accountExists(id, accountType)) {
                if(UserService.depositMoney(id, accountType, amount)){
                    state = true;
                } else{
                    state = false;
                    message = "V účtu " + accountType + " překročena maximální povolená částka účtu, vklad by zamítnut";
                }

            } else {
                state = false;
                message = "Účet v měně " + accountType + " neexistuje, pro vklad založte nejdříve účet";
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
            state = false;
        }

        // Set model attributes
        User user = UserRepository.findUser(id);
        model.addAttribute("user", user);
        List<Account> listAccounts = AccountRepository.findAccountsByUserId(user.getId());
        model.addAttribute("listAccounts", listAccounts);
        List<String> listOfLogs = UserService.readLog(user.getId());
        model.addAttribute("listOfLogs", listOfLogs);
        model.addAttribute("show", true);
        model.addAttribute("success", state);
        model.addAttribute("message", message);

        List<ExchangeRate> listExchangeRates = ExchangeRateRepository.getExchangeRates();
        model.addAttribute("listExchangeRates", listExchangeRates);

        return "dashboard";
    }


    // This method handles the payment action
    @PostMapping("/payment")
    public String handlePayment(@RequestParam("account-type-payment") String accountType, @RequestParam(value = "amount-payment", defaultValue = "0") double amount, Model model, Authentication authentication) throws IOException {
        String message = "";
        String name = authentication.getName();
        long id = UserRepository.getId(name);

        try {
            // Check if the user has either the specified account type or CZK account
            if ((UserService.accountExists(id, accountType)) || (UserService.accountExists(id, "CZK"))) {
                int status = UserService.payment(id, accountType, amount);
                if (status == 1) {
                    state = true;
                    message = "Platba proběhla úspěšně";
                } else {
                    state = false;
                    message = "Platba se nezdařila. Zkontrolujte zda požadovaný účet existuje, nebo zda má dostatek finančních prstředků";
                }
            } else {
                state = false;
                message = "Omlouváme se, platba neproběhla - Účet " + accountType + " neexistuje";
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
            state = false;
        }

        // Set model attributes
        User user = UserRepository.findUser(id);
        model.addAttribute("user", user);
        List<Account> listAccounts = AccountRepository.findAccountsByUserId(user.getId());
        model.addAttribute("listAccounts", listAccounts);
        List<String> listOfLogs = UserService.readLog(user.getId());
        model.addAttribute("listOfLogs", listOfLogs);
        model.addAttribute("show", true);
        model.addAttribute("success", state);
        model.addAttribute("message", message);

        List<ExchangeRate> listExchangeRates = ExchangeRateRepository.getExchangeRates();
        model.addAttribute("listExchangeRates", listExchangeRates);

        return "dashboard";
    }

    // This method handles a POST request for opening a new account
    @PostMapping("/open")
    public String handleOpen(@RequestParam("account-type-open") String accountType,  Model model, Authentication authentication) throws IOException {
        String message = "Účet byl úspěšně otevřen";
        String name = authentication.getName();
        long id = UserRepository.getId(name);
        // Check if the user already has an account of the specified type
        if (!UserService.accountExists(id, accountType)) {
            UserService.addAccount(id, accountType, 0);
            state = true;
        } else {
            state = false;
            message = "Omlouváme se, účet nebyl otevřen. Již máte otevřený účet v měně " + accountType;
        }

        // Set model attributes
        User user = UserRepository.findUser(id);
        model.addAttribute("user", user);
        List<Account> listAccounts = AccountRepository.findAccountsByUserId(user.getId());
        model.addAttribute("listAccounts", listAccounts);
        List<String> listOfLogs = UserService.readLog(user.getId());
        model.addAttribute("listOfLogs", listOfLogs);
        model.addAttribute("show", true);
        model.addAttribute("success", state);
        model.addAttribute("message", message);

        // Get the list of exchange rates
        List<ExchangeRate> listExchangeRates = ExchangeRateRepository.getExchangeRates();
        model.addAttribute("listExchangeRates", listExchangeRates);
        return "dashboard";
    }

}