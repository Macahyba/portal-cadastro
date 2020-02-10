package com.sony.engineering.portalcadastro.services;

import com.sony.engineering.portalcadastro.model.Contact;
import com.sony.engineering.portalcadastro.repository.ContactDao;
import com.sony.engineering.portalcadastro.service.ContactServiceImpl;
import com.sony.engineering.portalcadastro.services.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;

@RunWith(MockitoJUnitRunner.class)
public class ContactServiceImplTests {

	@Mock
	private ContactDao dao;
    
    @Test
    public void shouldInstantiate() {
        ContactServiceImpl service = new ContactServiceImpl(dao);

        assertNotNull(service);
    }

    @Test
    @DisplayName("Should return the data in the DAO.")
    public void FindAll_None_TheListProvidedByTheDao() {
        // Arrange
        Contact contactStub1 = new Contact();
        contactStub1.setId(1);
        contactStub1.setEmail("javaDoCapeta@mailsac.com");
        contactStub1.setName("Chorão");
        contactStub1.setDepartment("Praia");
        
        List<Contact> listStub = new ArrayList<Contact>();
        listStub.add(contactStub1);

        when(dao.findAll()).thenReturn(listStub);

        // Act
        ContactServiceImpl service = new ContactServiceImpl(dao);
        List<Contact> values = service.findAll();

        // Assert
        assertEquals(1, values.size());
        assertEquals(contactStub1.getId(), values.get(0).getId());
    }
}