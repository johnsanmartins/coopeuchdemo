package com.example.springboot;


import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.example.springboot.Request.TareaRequest;
import com.example.springboot.Service.TareaService;
import com.example.springboot.util.JsonUtils;

@SpringBootTest
class DemoApplicationTests {
	
	 private static final String PATH = "/tareas";

	private static final TareaRequest TareaRequest = null;
	
    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private TareaService service;

	 @Test
	    @Order(1)
	    void getAll() throws Exception {
	        mockMvc.perform(get(PATH)
	                        .accept(MediaType.APPLICATION_JSON)
	                        .contentType(MediaType.APPLICATION_JSON)
	                )
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.length()").value(1));

	        verify(service).listarTareas();
	    }
	 
	    @Test
	    @Order(2)
	    void saveTareas() throws Exception {
	        mockMvc.perform(post(PATH)
	                        .accept(MediaType.APPLICATION_JSON)
	                        .contentType(MediaType.APPLICATION_JSON)
	                        .content(JsonUtils.getContent("/request/save.json"))
	                )
	                .andExpect(status().isOk())
	                .andDo(print());

	        verify(service).saveTareas(TareaRequest);
	    }
	    
	    @Test
	    @Order(3)
	    void update() throws Exception {
	        mockMvc.perform(put(PATH + "/{id}", "tareas")
	                        .accept(MediaType.APPLICATION_JSON)
	                        .contentType(MediaType.APPLICATION_JSON)
	                        .content(JsonUtils.getContent("/request/update.json"))
	                )
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.descripcion").value("emails tareas"))
	                .andExpect(jsonPath("$.fecha").value("13-10-2023"))
	                .andExpect(jsonPath("$.vigente").exists());

	        verify(service).updateTareas(TareaRequest);
	    }

	    @Test
	    @Order(4)
	    void deleteTareas(Integer id) throws Exception {
	        mockMvc.perform(deleteTareas(PATH + "/{email}", "email@email.com")
	                        .accept(MediaType.APPLICATION_JSON)
	                        .contentType(MediaType.APPLICATION_JSON)
	                )
	                .andExpect(status().isOk());

	        verify(service).deleteTareas(id);
	    }

		private MockHttpServletRequestBuilder deleteTareas(String string, String string2) {
			// TODO Auto-generated method stub
			return null;
		}
}
