package com.jires.Bank.app.controller;

import com.jires.Bank.app.repository.AccountRepository;
import com.jires.Bank.app.repository.ExchangeRateRepository;
import com.jires.Bank.app.service.CustomUserDetailsServiceImpl;
import com.jires.Bank.app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.jires.Bank.app.domain.*;
import com.jires.Bank.app.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
//@RequestMapping("/api")
public class AppController {

    private final CustomUserDetailsServiceImpl customUserDetailsServiceImpl;


    Boolean state = true;

    public AppController(CustomUserDetailsServiceImpl customUserDetailsServiceImpl) {
        this.customUserDetailsServiceImpl = customUserDetailsServiceImpl;
    }

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("confirm")
    public String confirm(Model model,@RequestParam("token") String token) {
        model.addAttribute("token", customUserDetailsServiceImpl.confirmToken(token));
        return "confirm";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) throws IOException {
        String name = authentication.getName();
        long id = UserRepository.getId(name);
        User user = UserRepository.findUser(id);
        model.addAttribute("user", user);
        List<Account> listAccounts = AccountRepository.findAccountsByUserId(user.getId());
        model.addAttribute("listAccounts", listAccounts);
        List<String> listOfLogs = UserService.readLog(user.getId());
        model.addAttribute("listOfLogs", listOfLogs);
        model.addAttribute("show", false);

        List<ExchangeRate> listExchangeRates = ExchangeRateRepository.getExchangeRates();
        model.addAttribute("listExchangeRates", listExchangeRates);

        return "dashboard";
    }


    @RequestMapping(value = {"/deposit", "/payment", "/open"})
    public String configurateDashboard(Model model, Authentication authentication) throws IOException {
        String name = authentication.getName();
        long id = UserRepository.getId(name);
        User user = UserRepository.findUser(id);
        model.addAttribute("user", user);
        List<Account> listAccounts = AccountRepository.findAccountsByUserId(user.getId());
        model.addAttribute("listAccounts", listAccounts);
        List<String> listOfLogs = UserService.readLog(user.getId());
        model.addAttribute("listOfLogs", listOfLogs);
        model.addAttribute("show", true);
        model.addAttribute("success", state);
        model.addAttribute("message", "/deposit");
        List<ExchangeRate> listExchangeRates = ExchangeRateRepository.getExchangeRates();
        model.addAttribute("listExchangeRates", listExchangeRates);

        return "dashboard";
    }

    @PostMapping("/deposit")
    public String handleDeposit(@RequestParam("account-type-deposit") String accountType, @RequestParam(value = "amount-deposit", defaultValue = "0") double amount, Model model, Authentication authentication) throws IOException {
        String message = "Vklad proběhl úspěšně";
        String name = authentication.getName();
        long id = UserRepository.getId(name);
        try {
            if (UserService.accountExists(id, accountType)) {
                UserService.depositMoney(id, accountType, amount);
                state = true;
            } else {
                state = false;
                message = "Účet v měně " + accountType + " neexistuje, pro vklad založte nejdříve účet";
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
            state = false;
        }

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

    @PostMapping("/payment")
    public String handlePayment(@RequestParam("account-type-payment") String accountType, @RequestParam(value = "amount-payment", defaultValue = "0") double amount, Model model, Authentication authentication) throws IOException {
        String message = "";
        String name = authentication.getName();
        long id = UserRepository.getId(name);
        try {
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
        User user = UserRepository.findUser(id);
        // Add the success or error message to the model
        model.addAttribute("user", user); // for the html page variables !
        List<Account> listAccounts = AccountRepository.findAccountsByUserId(user.getId());
        model.addAttribute("listAccounts", listAccounts);
        List<String> listOfLogs = UserService.readLog(user.getId());
        model.addAttribute("listOfLogs", listOfLogs);
        model.addAttribute("show", true);
        model.addAttribute("success", state);
        model.addAttribute("message", message);

        List<ExchangeRate> listExchangeRates = ExchangeRateRepository.getExchangeRates();
        model.addAttribute("listExchangeRates", listExchangeRates);

        // Return the name of the view to render
        // return the SAME html page !
        return "dashboard";
    }

    @PostMapping("/open")
    public String handleOpen(@RequestParam("account-type-open") String accountType,  Model model, Authentication authentication) throws IOException {
        String message = "Účet byl úspěšně otevřen";
        String name = authentication.getName();
        long id = UserRepository.getId(name);

        // Perform the open action
        // if accountType NOT exist on user account accounts
        if (!UserService.accountExists(id, accountType)) {
            // add account of given accountType with given amount of finance
            UserService.addAccount(id, accountType, 0);
            state = true;
        } else {
            state = false;
            message = "Omlouváme se, účet nebyl otevřen. Již máte otevřený účet v měně " + accountType;
        }

        // Add the success or error message to the model
        User user = UserRepository.findUser(id);
        model.addAttribute("user", user); // for the html page variables !
        List<Account> listAccounts = AccountRepository.findAccountsByUserId(user.getId());
        model.addAttribute("listAccounts", listAccounts);
        List<String> listOfLogs = UserService.readLog(user.getId());
        model.addAttribute("listOfLogs", listOfLogs);
        model.addAttribute("show", true);
        model.addAttribute("success", state);
        model.addAttribute("message", message);

        List<ExchangeRate> listExchangeRates = ExchangeRateRepository.getExchangeRates();
        model.addAttribute("listExchangeRates", listExchangeRates);

        // Return the name of the view to render
        // return the SAME html page !
        return "dashboard";
    }

}