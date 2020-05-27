//package com.sony.engineering.portalcadastro.services;
//
//import com.sony.engineering.portalcadastro.model.Contact;
//import com.sony.engineering.portalcadastro.model.Customer;
//import com.sony.engineering.portalcadastro.repository.ContactDao;
//import com.sony.engineering.portalcadastro.repository.CustomerDao;
//import com.sony.engineering.portalcadastro.repository.GenericDao;
//import com.sony.engineering.portalcadastro.service.ContactServiceImpl;
//import com.sony.engineering.portalcadastro.service.CustomerServiceImpl;
//import com.sony.engineering.portalcadastro.services.*;
//
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.AdditionalAnswers;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//import static org.mockito.Mockito.*;
//import static org.junit.Assert.*;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//
//import org.junit.jupiter.api.DisplayName;
//
//@RunWith(MockitoJUnitRunner.class)
//public class CustomerServiceImplTests {
//
//    @Mock
//	private GenericDao<Customer> genericDao;
//    @Mock
//    private CustomerDao customerDao;
//
//    @Mock
//	private ContactDao contactDao;
//
//    @Test
//    public void shouldInstantiate() {
//        ContactServiceImpl contactSvc = new ContactServiceImpl(contactDao);
//        CustomerServiceImpl service =
//            new CustomerServiceImpl(this.genericDao,  this.customerDao, contactSvc);
//
//        assertNotNull(service);
//    }
//
//    private ContactServiceImpl createContactServiceImpl() {
//        return new ContactServiceImpl(contactDao);
//    }
//
//    private CustomerServiceImpl createCustomerServiceImpl() {
//        ContactServiceImpl contactSvc = createContactServiceImpl();
//        return new CustomerServiceImpl(this.genericDao,  this.customerDao, contactSvc);
//    }
//
//    private Contact getContactStub() {
//        Contact contactStub1 = new Contact();
//        contactStub1.setId(1);
//        contactStub1.setEmail("javaDoCapeta@mailsac.com");
//        contactStub1.setName("Chorão");
//        contactStub1.setDepartment("Praia");
//
//        return contactStub1;
//    }
//
//    private Customer getCustomerStub() {
//        Customer customerStub = new Customer();
//        customerStub.setId(1);
//        customerStub.setName("Bobby Tables");
//        customerStub.setFullName("Robert Drop Table");
//        customerStub.setCnpj("00.000.000/0001-00");
//
//        Contact contactStub = this.getContactStub();
//        customerStub.addContact(contactStub);
//
//        return customerStub;
//    }
//
////    @Test(expected = NoSuchElementException.class)
////    @DisplayName("Should Throw Exception When trying to save a user with invalid Id.")
////    public void save_CustomerWithInvalidId_ThrowNoSuchElementException() {
////        // Arrange
////        Customer customerStub = getCustomerStub();
////        customerStub.setId(2);
////
////        when(genericDao.findById(2)).thenReturn(Optional.empty());
////
////        // Act
////        CustomerServiceImpl service = this.createCustomerServiceImpl();
////        service.save(customerStub);
////    }
//
//}