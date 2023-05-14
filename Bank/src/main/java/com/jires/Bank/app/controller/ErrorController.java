package com.jires.Bank.app.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;

@ControllerAdvice
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handle404Error(HttpServletRequest request) {
        // Get the HTTP status code of the error
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            String statusCode = status.toString();
            // If the status code is 404, return the "error" page
            if (statusCode.equals("404")) {
                return "error";
            }
        }
        // Otherwise, return the default page
        return "index";
    }
}