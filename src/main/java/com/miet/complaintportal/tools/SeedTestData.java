package com.miet.complaintportal.tools;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.miet.complaintportal.config.AppConfig;
import com.miet.complaintportal.model.Category;
import com.miet.complaintportal.model.Complaint;
import com.miet.complaintportal.model.ComplaintStatus;
import com.miet.complaintportal.model.Role;
import com.miet.complaintportal.model.User;
import com.miet.complaintportal.service.CategoryService;
import com.miet.complaintportal.service.ComplaintService;
import com.miet.complaintportal.service.UserService;

public class SeedTestData {
  public static void main(String[] args) throws Exception {
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    UserService userService = context.getBean(UserService.class);
    CategoryService categoryService = context.getBean(CategoryService.class);
    ComplaintService complaintService = context.getBean(ComplaintService.class);

    String password = "Password123";

    User admin = userService.registerUser("Test Admin", "admin@test.com", password, Role.CUSTOMER);
    userService.updateRole(admin.getId(), Role.ADMIN);

    User agentOne = userService.registerUser("Test Agent 1", "agent1@test.com", password, Role.CUSTOMER);
    userService.updateRole(agentOne.getId(), Role.AGENT);

    User agentTwo = userService.registerUser("Test Agent 2", "agent2@test.com", password, Role.CUSTOMER);
    userService.updateRole(agentTwo.getId(), Role.AGENT);

    User customerOne = userService.registerUser("Test Customer 1", "customer1@test.com", password, Role.CUSTOMER);

    User customerTwo = userService.registerUser("Test Customer 2", "customer2@test.com", password, Role.CUSTOMER);

    User customerThree = userService.registerUser("Test Customer 3", "customer3@test.com", password, Role.CUSTOMER);

    Category categoryOne = categoryService.createCategory("Billing", "Test category for billing related issues");
    Category categoryTwo = categoryService.createCategory("Account Access", "Test category for account related issues");
    Category categoryThree = categoryService.createCategory("Technical Support", "Test category for techinal issues");

    Complaint complaintOne = complaintService.fileComplaint(
        customerOne.getId(), categoryOne.getId(),
        "Wrong amount charged", "I was billed twice for the same invoice this month.");

    Complaint complaintTwo = complaintService.fileComplaint(
        customerOne.getId(), categoryTwo.getId(),
        "Cannot reset password", "The reset link in the email never arrives.");

    Complaint complaintThree = complaintService.fileComplaint(
        customerTwo.getId(), categoryThree.getId(),
        "App crashes on login", "Getting a blank screen every time I try to sign in.");

    Complaint complaintFour = complaintService.fileComplaint(
        customerTwo.getId(), categoryOne.getId(),
        "Refund not processed", "Requested a refund two weeks ago, still nothing.");

    Complaint complaintFive = complaintService.fileComplaint(
        customerThree.getId(), categoryThree.getId(),
        "Slow performance", "The dashboard takes over a minute to load.");

    Complaint complaintSix = complaintService.fileComplaint(
        customerThree.getId(), categoryTwo.getId(),
        "Locked out of account", "My account got locked after failed login attempts.");

    complaintService.assignAgent(complaintOne.getId(), agentOne.getId());
    complaintService.updateComplaintStatus(complaintOne.getId(), ComplaintStatus.IN_PROGRESS, agentOne.getId(),
        "Investigating the double charge.");

    System.exit(0);
  }
}
