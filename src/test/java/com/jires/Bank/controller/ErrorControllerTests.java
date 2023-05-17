package com.jires.Bank.controller;


import com.jires.Bank.app.controller.ErrorController;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ErrorControllerTests {
    private ErrorController errorController;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        errorController = new ErrorController();
    }

    @Test
    public void testHandle404Error_WithStatusCode404_ReturnsErrorPage() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(404);
        String result = errorController.handle404Error(request);
        assertEquals("error", result);
    }

    @Test
    public void testHandle404Error_WithStatusCodeNot404_ReturnsIndexPage() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(500);
        String result = errorController.handle404Error(request);
        assertEquals("index", result);
    }
}
