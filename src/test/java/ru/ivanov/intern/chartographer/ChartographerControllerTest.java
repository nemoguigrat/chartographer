package ru.ivanov.intern.chartographer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.ivanov.intern.chartographer.controller.ChartographerController;
import ru.ivanov.intern.chartographer.exeption.ChartNotFoundException;
import ru.ivanov.intern.chartographer.exeption.ValidationException;
import ru.ivanov.intern.chartographer.service.ChartService;
import ru.ivanov.intern.chartographer.service.FileService;

import java.util.UUID;

import static org.mockito.Mockito.*;

@WebAppConfiguration
@ExtendWith(MockitoExtension.class)
class ChartographerControllerTest {

    @InjectMocks
    private ChartographerController chartographerController;

    @Mock
    private ChartService chartService;

    @MockBean
    private FileService fileService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(chartographerController).build();
    }

    @Test
    public void createChart_THROW_EXEPTION() throws Exception {
        doThrow(ValidationException.class).when(chartService).createChart(anyInt(), anyInt());

        mockMvc.perform(MockMvcRequestBuilders.post("/chartas/?width=-1&height=-10"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void createChart_SAVE_IMAGE_RESPONSE() throws Exception {
        doReturn(1).when(chartService).createChart(10, 10);

        mockMvc.perform(MockMvcRequestBuilders.post("/chartas/?width=10&height=10"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("1"))
                .andReturn().getResponse();
    }

    @Test
    void savePartChart_IMAGE_IS_INSERTED() throws Exception {
        doNothing().when(chartService).insertPartChart(
                anyInt(),
                intThat(arg -> arg > 0 && arg <= 5000),
                intThat(arg -> arg > 0 && arg <= 5000),
                intThat(arg -> arg > 0 && arg <= 5000),
                intThat(arg -> arg > 0 && arg <= 5000),
                any(byte[].class));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/chartas/1/?x=10&y=10&width=10&height=10")
                .contentType("image/bmp")
                .content(new byte[]{1, 1, 1}))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void savePartChart_THROW_NOT_FOUND() throws Exception {
        String testId = UUID.randomUUID().toString();
        doThrow(ChartNotFoundException.class).when(chartService).insertPartChart(
                anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), any(byte[].class));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/chartas/1/?x=10&y=10&width=10&height=10")
                .contentType("image/bmp")
                .content(new byte[]{1, 1, 1}))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void savePartChart_THROW_VALIDATION() throws Exception {
        doThrow(ValidationException.class).when(chartService).insertPartChart(
                anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), any(byte[].class));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/chartas/1/?x=10&y=10&width=10&height=10")
                .contentType("image/bmp")
                .content(new byte[]{1, 1, 1}))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getPartChart() throws Exception {
        doReturn(new byte[]{1, 1, 1}).when(chartService).getPartChart(
                anyInt(), anyInt(), anyInt(), anyInt(), anyInt());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/chartas/1/?x=10&y=10&width=10&height=10"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deletePartFile() throws Exception {
        doNothing().when(chartService).deleteChart(anyInt());

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/chartas/1/"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}