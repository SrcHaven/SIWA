package com.srchaven.siwa.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.srchaven.siwa.model.Observation;

/**
 * DAO for {@code Observation} objects.
 */
public class ObservationDao extends HibernateDaoSupport implements ObservationDaoIF
{
    @Override
    public void saveObservation(Observation obs)
    {
        getHibernateTemplate().save(obs);
    }
}
