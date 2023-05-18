package com.jires.Bank.controller;

import com.jires.Bank.app.controller.AppController;
import com.jires.Bank.app.domain.Account;
import com.jires.Bank.app.domain.ExchangeRate;
import com.jires.Bank.app.domain.User;
import com.jires.Bank.app.repository.AccountRepository;
import com.jires.Bank.app.repository.ExchangeRateRepository;
import com.jires.Bank.app.repository.UserRepository;
import com.jires.Bank.app.service.CustomUserDetailsServiceImpl;
import com.jires.Bank.app.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
//How to get test coverage:
//mvn clean verify
//java -jar C:\Users\jires\Downloads\jacoco-0.8.10\lib\jacococli.jar report C:\Users\jires\IdeaProjects\Bank\target\coverage-reports\jacoco-ut.exec --classfiles C:\Users\jires\IdeaProjects\Bank\target\classes --html C:\Users\jires\IdeaProjects\Bank\target\coverage-reports
public class AppControllerTests {
    @Mock
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private AppController appController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testViewHomePage() {
        String result = appController.viewHomePage();
        assertEquals("index", result);
    }

    @Test
    public void testShowLoginForm() {
        String result = appController.showLoginForm();
        assertEquals("login", result);
    }

    @Test
    public void testConfirm() {
        Model model = mock(Model.class);
        String token = "exampleToken";
        when(customUserDetailsService.confirmToken(token)).thenReturn(String.valueOf(true));
        String result = appController.confirm(model, token);
        assertEquals("confirm", result);
    }

    @Test
    void testHandleDeposit() throws IOException {
    //    Authentication authentication = Mockito.mock(Authentication.class);
    //    Mockito.when(authentication.getName()).thenReturn("sampleUsername");
    //    Model model = null; // Create a mock instance of Model if needed
    //    double amount = 100.0;
    //    String accountType = "sampleAccountType";

    //    String result = appController.handleDeposit(accountType, amount, model, authentication);
    //    Assertions.assertEquals("dashboard", result);
    }

    @Test
    void testDashboard() throws IOException {
    //    Model model = mock(Model.class);
    //    Authentication authentication = mock(Authentication.class);

    //    String result = appController.dashboard(model, authentication);
    }

    @Test
    void testConfigurateDashboard() throws IOException {
    //    Model model = mock(Model.class);
    //    Authentication authentication = mock(Authentication.class);

    //    String result = appController.configurateDashboard(model, authentication);
    }

    @Test
    void testHandlePayment() throws IOException {
    //    Model model = mock(Model.class);
    //    Authentication authentication = mock(Authentication.class);

    //    double amount = 100.0;
    //    String accountType = "sampleAccountType";

    //    String result = appController.handlePayment(accountType, amount, model, authentication);
    }

    @Test
    void testHandleOpen() throws IOException {
    //    Model model = mock(Model.class);
    //    Authentication authentication = mock(Authentication.class);

    //    String accountType = "sampleAccountType";

    //    String result = appController.handleOpen(accountType, model, authentication);
    }


}
