package org.mashbot.server.web;

import org.springframework.web.servlet.ModelAndView;

import org.mashbot.server.web.HelloController;

import junit.framework.TestCase;

public class HelloControllerTests extends TestCase {

    public void testHandleRequestView() throws Exception{		
        HelloController controller = new HelloController();
        ModelAndView modelAndView = controller.handleRequest(null, null);		
        assertEquals("hello.jsp", modelAndView.getViewName());
    }
}
