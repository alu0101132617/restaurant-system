package es.ull.esit.app.middleware.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;

class CashierTest {

  @Test
  void testNoArgsConstructorAndSettersGetters() {
    Cashier c = new Cashier();
    assertNull(c.getId());
    assertNull(c.getName());
    assertNull(c.getSalary());

    c.setId(1L);
    c.setName("Ana");
    c.setSalary(1200);

    assertEquals(1L, c.getId());
    assertEquals("Ana", c.getName());
    assertEquals(1200, c.getSalary());
  }

  @Test
  void testAllArgsConstructor() {
    Cashier c = new Cashier(2L, "Bob", 2000);
    assertEquals(2L, c.getId());
    assertEquals("Bob", c.getName());
    assertEquals(2000, c.getSalary());
  }

  @Test
  void testSettersAcceptNull() {
    Cashier c = new Cashier(3L, "X", 500);
    c.setName(null);
    c.setSalary(null);
    c.setId(null);

    assertNull(c.getId());
    assertNull(c.getName());
    assertNull(c.getSalary());
  }

  @Test
  void testJsonSerialization() throws Exception {
    Cashier c = new Cashier(10L, "Maria", 1500);
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(c);
    assertTrue(json.contains("Maria"));

    Cashier des = mapper.readValue(json, Cashier.class);
    assertEquals(10L, des.getId());
    assertEquals("Maria", des.getName());
    assertEquals(1500, des.getSalary());
  }
}
