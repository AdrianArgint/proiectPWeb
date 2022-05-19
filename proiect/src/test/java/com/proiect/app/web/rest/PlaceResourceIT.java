package com.proiect.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.proiect.app.IntegrationTest;
import com.proiect.app.domain.Place;
import com.proiect.app.repository.PlaceRepository;
import com.proiect.app.service.criteria.PlaceCriteria;
import com.proiect.app.service.dto.PlaceDTO;
import com.proiect.app.service.mapper.PlaceMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PlaceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlaceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;
    private static final Double SMALLER_LATITUDE = 1D - 1D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;
    private static final Double SMALLER_LONGITUDE = 1D - 1D;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/places";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PlaceMapper placeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlaceMockMvc;

    private Place place;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Place createEntity(EntityManager em) {
        Place place = new Place()
            .name(DEFAULT_NAME)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .address(DEFAULT_ADDRESS)
            .type(DEFAULT_TYPE)
            .description(DEFAULT_DESCRIPTION);
        return place;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Place createUpdatedEntity(EntityManager em) {
        Place place = new Place()
            .name(UPDATED_NAME)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .address(UPDATED_ADDRESS)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION);
        return place;
    }

    @BeforeEach
    public void initTest() {
        place = createEntity(em);
    }

    @Test
    @Transactional
    void createPlace() throws Exception {
        int databaseSizeBeforeCreate = placeRepository.findAll().size();
        // Create the Place
        PlaceDTO placeDTO = placeMapper.toDto(place);
        restPlaceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(placeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Place in the database
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeCreate + 1);
        Place testPlace = placeList.get(placeList.size() - 1);
        assertThat(testPlace.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlace.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testPlace.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testPlace.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testPlace.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPlace.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createPlaceWithExistingId() throws Exception {
        // Create the Place with an existing ID
        place.setId(1L);
        PlaceDTO placeDTO = placeMapper.toDto(place);

        int databaseSizeBeforeCreate = placeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlaceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(placeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Place in the database
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPlaces() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList
        restPlaceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(place.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getPlace() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get the place
        restPlaceMockMvc
            .perform(get(ENTITY_API_URL_ID, place.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(place.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getPlacesByIdFiltering() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        Long id = place.getId();

        defaultPlaceShouldBeFound("id.equals=" + id);
        defaultPlaceShouldNotBeFound("id.notEquals=" + id);

        defaultPlaceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPlaceShouldNotBeFound("id.greaterThan=" + id);

        defaultPlaceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPlaceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPlacesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where name equals to DEFAULT_NAME
        defaultPlaceShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the placeList where name equals to UPDATED_NAME
        defaultPlaceShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPlacesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where name not equals to DEFAULT_NAME
        defaultPlaceShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the placeList where name not equals to UPDATED_NAME
        defaultPlaceShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPlacesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPlaceShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the placeList where name equals to UPDATED_NAME
        defaultPlaceShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPlacesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where name is not null
        defaultPlaceShouldBeFound("name.specified=true");

        // Get all the placeList where name is null
        defaultPlaceShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllPlacesByNameContainsSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where name contains DEFAULT_NAME
        defaultPlaceShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the placeList where name contains UPDATED_NAME
        defaultPlaceShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPlacesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where name does not contain DEFAULT_NAME
        defaultPlaceShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the placeList where name does not contain UPDATED_NAME
        defaultPlaceShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPlacesByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where latitude equals to DEFAULT_LATITUDE
        defaultPlaceShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the placeList where latitude equals to UPDATED_LATITUDE
        defaultPlaceShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllPlacesByLatitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where latitude not equals to DEFAULT_LATITUDE
        defaultPlaceShouldNotBeFound("latitude.notEquals=" + DEFAULT_LATITUDE);

        // Get all the placeList where latitude not equals to UPDATED_LATITUDE
        defaultPlaceShouldBeFound("latitude.notEquals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllPlacesByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultPlaceShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the placeList where latitude equals to UPDATED_LATITUDE
        defaultPlaceShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllPlacesByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where latitude is not null
        defaultPlaceShouldBeFound("latitude.specified=true");

        // Get all the placeList where latitude is null
        defaultPlaceShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllPlacesByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where latitude is greater than or equal to DEFAULT_LATITUDE
        defaultPlaceShouldBeFound("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the placeList where latitude is greater than or equal to UPDATED_LATITUDE
        defaultPlaceShouldNotBeFound("latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllPlacesByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where latitude is less than or equal to DEFAULT_LATITUDE
        defaultPlaceShouldBeFound("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the placeList where latitude is less than or equal to SMALLER_LATITUDE
        defaultPlaceShouldNotBeFound("latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllPlacesByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where latitude is less than DEFAULT_LATITUDE
        defaultPlaceShouldNotBeFound("latitude.lessThan=" + DEFAULT_LATITUDE);

        // Get all the placeList where latitude is less than UPDATED_LATITUDE
        defaultPlaceShouldBeFound("latitude.lessThan=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllPlacesByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where latitude is greater than DEFAULT_LATITUDE
        defaultPlaceShouldNotBeFound("latitude.greaterThan=" + DEFAULT_LATITUDE);

        // Get all the placeList where latitude is greater than SMALLER_LATITUDE
        defaultPlaceShouldBeFound("latitude.greaterThan=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllPlacesByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where longitude equals to DEFAULT_LONGITUDE
        defaultPlaceShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the placeList where longitude equals to UPDATED_LONGITUDE
        defaultPlaceShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllPlacesByLongitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where longitude not equals to DEFAULT_LONGITUDE
        defaultPlaceShouldNotBeFound("longitude.notEquals=" + DEFAULT_LONGITUDE);

        // Get all the placeList where longitude not equals to UPDATED_LONGITUDE
        defaultPlaceShouldBeFound("longitude.notEquals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllPlacesByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultPlaceShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the placeList where longitude equals to UPDATED_LONGITUDE
        defaultPlaceShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllPlacesByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where longitude is not null
        defaultPlaceShouldBeFound("longitude.specified=true");

        // Get all the placeList where longitude is null
        defaultPlaceShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllPlacesByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where longitude is greater than or equal to DEFAULT_LONGITUDE
        defaultPlaceShouldBeFound("longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the placeList where longitude is greater than or equal to UPDATED_LONGITUDE
        defaultPlaceShouldNotBeFound("longitude.greaterThanOrEqual=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllPlacesByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where longitude is less than or equal to DEFAULT_LONGITUDE
        defaultPlaceShouldBeFound("longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the placeList where longitude is less than or equal to SMALLER_LONGITUDE
        defaultPlaceShouldNotBeFound("longitude.lessThanOrEqual=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllPlacesByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where longitude is less than DEFAULT_LONGITUDE
        defaultPlaceShouldNotBeFound("longitude.lessThan=" + DEFAULT_LONGITUDE);

        // Get all the placeList where longitude is less than UPDATED_LONGITUDE
        defaultPlaceShouldBeFound("longitude.lessThan=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllPlacesByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where longitude is greater than DEFAULT_LONGITUDE
        defaultPlaceShouldNotBeFound("longitude.greaterThan=" + DEFAULT_LONGITUDE);

        // Get all the placeList where longitude is greater than SMALLER_LONGITUDE
        defaultPlaceShouldBeFound("longitude.greaterThan=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllPlacesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where address equals to DEFAULT_ADDRESS
        defaultPlaceShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the placeList where address equals to UPDATED_ADDRESS
        defaultPlaceShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllPlacesByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where address not equals to DEFAULT_ADDRESS
        defaultPlaceShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the placeList where address not equals to UPDATED_ADDRESS
        defaultPlaceShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllPlacesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultPlaceShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the placeList where address equals to UPDATED_ADDRESS
        defaultPlaceShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllPlacesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where address is not null
        defaultPlaceShouldBeFound("address.specified=true");

        // Get all the placeList where address is null
        defaultPlaceShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllPlacesByAddressContainsSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where address contains DEFAULT_ADDRESS
        defaultPlaceShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the placeList where address contains UPDATED_ADDRESS
        defaultPlaceShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllPlacesByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where address does not contain DEFAULT_ADDRESS
        defaultPlaceShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the placeList where address does not contain UPDATED_ADDRESS
        defaultPlaceShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllPlacesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where type equals to DEFAULT_TYPE
        defaultPlaceShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the placeList where type equals to UPDATED_TYPE
        defaultPlaceShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPlacesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where type not equals to DEFAULT_TYPE
        defaultPlaceShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the placeList where type not equals to UPDATED_TYPE
        defaultPlaceShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPlacesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultPlaceShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the placeList where type equals to UPDATED_TYPE
        defaultPlaceShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPlacesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where type is not null
        defaultPlaceShouldBeFound("type.specified=true");

        // Get all the placeList where type is null
        defaultPlaceShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllPlacesByTypeContainsSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where type contains DEFAULT_TYPE
        defaultPlaceShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the placeList where type contains UPDATED_TYPE
        defaultPlaceShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPlacesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where type does not contain DEFAULT_TYPE
        defaultPlaceShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the placeList where type does not contain UPDATED_TYPE
        defaultPlaceShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPlacesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where description equals to DEFAULT_DESCRIPTION
        defaultPlaceShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the placeList where description equals to UPDATED_DESCRIPTION
        defaultPlaceShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPlacesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where description not equals to DEFAULT_DESCRIPTION
        defaultPlaceShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the placeList where description not equals to UPDATED_DESCRIPTION
        defaultPlaceShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPlacesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPlaceShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the placeList where description equals to UPDATED_DESCRIPTION
        defaultPlaceShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPlacesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where description is not null
        defaultPlaceShouldBeFound("description.specified=true");

        // Get all the placeList where description is null
        defaultPlaceShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllPlacesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where description contains DEFAULT_DESCRIPTION
        defaultPlaceShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the placeList where description contains UPDATED_DESCRIPTION
        defaultPlaceShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPlacesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where description does not contain DEFAULT_DESCRIPTION
        defaultPlaceShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the placeList where description does not contain UPDATED_DESCRIPTION
        defaultPlaceShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlaceShouldBeFound(String filter) throws Exception {
        restPlaceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(place.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restPlaceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlaceShouldNotBeFound(String filter) throws Exception {
        restPlaceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlaceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPlace() throws Exception {
        // Get the place
        restPlaceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPlace() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        int databaseSizeBeforeUpdate = placeRepository.findAll().size();

        // Update the place
        Place updatedPlace = placeRepository.findById(place.getId()).get();
        // Disconnect from session so that the updates on updatedPlace are not directly saved in db
        em.detach(updatedPlace);
        updatedPlace
            .name(UPDATED_NAME)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .address(UPDATED_ADDRESS)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION);
        PlaceDTO placeDTO = placeMapper.toDto(updatedPlace);

        restPlaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, placeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(placeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Place in the database
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeUpdate);
        Place testPlace = placeList.get(placeList.size() - 1);
        assertThat(testPlace.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlace.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testPlace.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testPlace.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPlace.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPlace.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingPlace() throws Exception {
        int databaseSizeBeforeUpdate = placeRepository.findAll().size();
        place.setId(count.incrementAndGet());

        // Create the Place
        PlaceDTO placeDTO = placeMapper.toDto(place);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, placeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(placeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Place in the database
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlace() throws Exception {
        int databaseSizeBeforeUpdate = placeRepository.findAll().size();
        place.setId(count.incrementAndGet());

        // Create the Place
        PlaceDTO placeDTO = placeMapper.toDto(place);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(placeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Place in the database
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlace() throws Exception {
        int databaseSizeBeforeUpdate = placeRepository.findAll().size();
        place.setId(count.incrementAndGet());

        // Create the Place
        PlaceDTO placeDTO = placeMapper.toDto(place);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(placeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Place in the database
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlaceWithPatch() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        int databaseSizeBeforeUpdate = placeRepository.findAll().size();

        // Update the place using partial update
        Place partialUpdatedPlace = new Place();
        partialUpdatedPlace.setId(place.getId());

        partialUpdatedPlace.description(UPDATED_DESCRIPTION);

        restPlaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlace.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlace))
            )
            .andExpect(status().isOk());

        // Validate the Place in the database
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeUpdate);
        Place testPlace = placeList.get(placeList.size() - 1);
        assertThat(testPlace.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlace.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testPlace.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testPlace.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testPlace.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPlace.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdatePlaceWithPatch() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        int databaseSizeBeforeUpdate = placeRepository.findAll().size();

        // Update the place using partial update
        Place partialUpdatedPlace = new Place();
        partialUpdatedPlace.setId(place.getId());

        partialUpdatedPlace
            .name(UPDATED_NAME)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .address(UPDATED_ADDRESS)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION);

        restPlaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlace.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlace))
            )
            .andExpect(status().isOk());

        // Validate the Place in the database
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeUpdate);
        Place testPlace = placeList.get(placeList.size() - 1);
        assertThat(testPlace.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlace.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testPlace.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testPlace.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPlace.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPlace.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingPlace() throws Exception {
        int databaseSizeBeforeUpdate = placeRepository.findAll().size();
        place.setId(count.incrementAndGet());

        // Create the Place
        PlaceDTO placeDTO = placeMapper.toDto(place);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, placeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(placeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Place in the database
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlace() throws Exception {
        int databaseSizeBeforeUpdate = placeRepository.findAll().size();
        place.setId(count.incrementAndGet());

        // Create the Place
        PlaceDTO placeDTO = placeMapper.toDto(place);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(placeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Place in the database
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlace() throws Exception {
        int databaseSizeBeforeUpdate = placeRepository.findAll().size();
        place.setId(count.incrementAndGet());

        // Create the Place
        PlaceDTO placeDTO = placeMapper.toDto(place);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(placeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Place in the database
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlace() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        int databaseSizeBeforeDelete = placeRepository.findAll().size();

        // Delete the place
        restPlaceMockMvc
            .perform(delete(ENTITY_API_URL_ID, place.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
