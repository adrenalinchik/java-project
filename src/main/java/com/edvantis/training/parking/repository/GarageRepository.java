package com.edvantis.training.parking.repository;

import com.edvantis.training.parking.models.Garage;

/**
 * Created by taras.fihurnyak on 2/13/2017.
 */
public interface GarageRepository {

    Garage getById(long id);

    void insert(Garage garage);

    void update(int garageId, Garage garage);

    void delete(int garageId);
}
