package com.edvantis.training.parking.test.controllerTests;

import com.edvantis.training.parking.api.OwnerEndpoint;
import com.edvantis.training.parking.config.TestControllerContext;
import com.edvantis.training.parking.models.Garage;
import com.edvantis.training.parking.models.GarageType;
import com.edvantis.training.parking.models.Parking;
import com.edvantis.training.parking.models.Reservation;
import com.edvantis.training.parking.services.ParkingService;
import com.edvantis.training.parking.util.TestsHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by taras.fihurnyak on 4/28/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestControllerContext.class})
@WebAppConfiguration
public class ReservationControllerTest {
    private MockMvc mockMvc;
    private Date from = TestsHelper.parseDate("2017-04-25 19:16:59");
    private Date to = TestsHelper.parseDate("2018-05-29 19:16:59");

    @Autowired
    private ParkingService parkingServiceMock;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new OwnerEndpoint(parkingServiceMock))
                .build();
    }

    @Test
    public void findAvailableGarages() throws Exception {
        Parking p1 = new Parking();
        p1.setId(1L);
        p1.setAddress("Lviv, Main str 15");
        p1.setFreeGaragesNumber(15);
        Garage g1 = new Garage();
        g1.setGarageType(GarageType.BIG);
        g1.setId(1L);
        g1.setParking(p1);
        g1.setSquare(11F);
        Garage g2 = new Garage();
        g2.setGarageType(GarageType.MEDIUM);
        g2.setId(2L);
        g2.setParking(p1);
        g2.setSquare(22F);

        ArrayList<Garage> garageList = new ArrayList<>();
        garageList.add(g1);
        garageList.add(g2);

        when(parkingServiceMock.getAvailableGaragesByParking(from, to, 1)).thenReturn(garageList);

        mockMvc.perform(get("/parking/api/reservation/availableGarages/parking/{parkingId}", 1)
                .param("from","2017-04-29")
                .param("to","2017-04-29"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestsHelper.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].parking.id", is(1)))
                .andExpect(jsonPath("$[0].parking.address", is("Lviv, Main str 15")))
                .andExpect(jsonPath("$[0].parking.freeGaragesNumber", is(15)))
                .andExpect(jsonPath("$[0].square", is(11.0)))
                .andExpect(jsonPath("$[0].garageType", is(GarageType.BIG.toString())))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].parking.id", is(1)))
                .andExpect(jsonPath("$[0].parking.address", is("Lviv, Main str 15")))
                .andExpect(jsonPath("$[0].parking.freeGaragesNumber", is(15)))
                .andExpect(jsonPath("$[1].square", is(22.0)))
                .andExpect(jsonPath("$[1].garageType", is(GarageType.MEDIUM.toString())));

        //verify(parkingServiceMock, times(1)).getOwnerVehicles(1);
    }


    @Test
    public void makeReservation() throws Exception {
        Reservation r1 = new Reservation();
        r1.setBegin(from);
        r1.setEnd(to);
        r1.setGarageId(1);
        r1.setParkingId(1);
        r1.setOwnerId(1);
        r1.setId(1L);

        when(parkingServiceMock.makeReservation(from, to, 1)).thenReturn(r1);

        mockMvc.perform(get("/parking/api/reservation/owner/{ownerId}",1)
                .param("from","2017-04-29")
                .param("to","2017-04-29"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestsHelper.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.parkingId", is(1)))
                .andExpect(jsonPath("$.ownerId", is(1)))
                .andExpect(jsonPath("$.garageId", is(15)))
                .andExpect(jsonPath("$.begin", is("2017-04-27")))
                .andExpect(jsonPath("$.end", is("2017-05-04")));

        verify(parkingServiceMock, times(1)).makeReservation(from, to, 1);
    }
}
