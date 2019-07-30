package com.pratik.jlreductions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JLReductionsApplicationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testBasic() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(not(empty()))))
                .andExpect(jsonPath("$[0].priceLabel", containsString("Was")))
                .andExpect(jsonPath("$[0].priceLabel", containsString(", now")))
                .andExpect(jsonPath("$[0].priceLabel", not(containsString("then"))));
    }

    @Test
    public void testWasThenNow() throws Exception {
        mockMvc.perform(get("/?labelType=ShowWasThenNow"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(not(empty()))))
                .andExpect(jsonPath("$[0].priceLabel", containsString("Was")))
                .andExpect(jsonPath("$[0].priceLabel", containsString(", now")))
                .andExpect(jsonPath("$[0].priceLabel", containsString(", then")));
    }

    @Test
    public void testShowPercDiscount() throws Exception {
        mockMvc.perform(get("/?labelType=ShowPercDscount"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(not(empty()))))
                .andExpect(jsonPath("$[0].priceLabel", containsString(" off - now")))
                .andExpect(jsonPath("$[0].priceLabel", not(containsString("Was"))))
                .andExpect(jsonPath("$[0].priceLabel", not(containsString("then"))))
                .andExpect(jsonPath("$[0].priceLabel", not(containsString(", now"))));
    }
}
