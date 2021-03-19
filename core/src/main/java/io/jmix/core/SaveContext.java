/*
 * Copyright 2019 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jmix.core;


import io.jmix.core.constraint.AccessConstraint;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;

/**
 *
 */
public class SaveContext implements Serializable {

    private static final long serialVersionUID = 7239959802146936706L;

    protected Collection<Object> entitiesToSave = new LinkedHashSet<>();
    protected Collection<Object> entitiesToRemove = new LinkedHashSet<>();

    protected Map<Object, FetchPlan> fetchPlans = new HashMap<>();

    protected boolean softDeletion = true;
    protected boolean discardSaved;
    protected boolean joinTransaction = true;
    protected List<AccessConstraint<?>> accessConstraints;
    protected Map<String, Serializable> hints;

    /**
     * /**
     * Adds an entity to be committed to the database.
     *
     * @param entities entity instances
     * @return this instance for chaining
     */
    public SaveContext saving(Object... entities) {
        for (Object item : entities) {
            if (item instanceof Collection) {
                entitiesToSave.addAll((Collection<?>) item);
            } else {
                entitiesToSave.add(item);
            }
        }
        return this;
    }

    /**
     * Adds an entity to be committed to the database.
     *
     * @param entity    entity instance
     * @param fetchPlan fetch plan which is used in merge operation to ensure all required attributes are loaded in the returned instance
     * @return this instance for chaining
     */
    public SaveContext saving(Object entity, @Nullable FetchPlan fetchPlan) {
        entitiesToSave.add(entity);
        if (fetchPlan != null)
            fetchPlans.put(entity, fetchPlan);
        return this;
    }

    /**
     * Adds an entity to be removed from the database.
     *
     * @param entities entity instances
     * @return this instance for chaining
     */
    public SaveContext removing(Object... entities) {
        for (Object item : entities) {
            if (item instanceof Collection) {
                entitiesToRemove.addAll((Collection<?>) item);
            } else {
                entitiesToRemove.add(item);
            }
        }
        return this;
    }

    /**
     * @return direct reference to collection of changed entities that will be committed to the database.
     * The collection is modifiable.
     */
    public Collection<Object> getEntitiesToSave() {
        return entitiesToSave;
    }

    /**
     * @return direct reference to collection of entities that will be removed from the database.
     * The collection is modifiable.
     */
    public Collection<Object> getEntitiesToRemove() {
        return entitiesToRemove;
    }

    /**
     * Enables defining a fetchPlan for each committed entity. These fetchPlans are used in merge operation to ensure all
     * required attributes are loaded in returned instances.
     *
     * @return mutable map of entities to their fetchPlans
     */
    public Map<Object, FetchPlan> getFetchPlans() {
        return fetchPlans;
    }

    /**
     * @return custom hints which are used by the query
     */
    public Map<String, Serializable> getHints() {
        return hints == null ? Collections.emptyMap() : Collections.unmodifiableMap(hints);
    }

    /**
     * Sets custom hint that should be used by the query.
     */
    public SaveContext setHint(String hintName, Serializable value) {
        if (hints == null) {
            hints = new HashMap<>();
        }
        hints.put(hintName, value);
        return this;
    }

    /**
     * Sets custom hints that should be used by the query.
     */
    public SaveContext setHints(Map<String, Serializable> hints) {
        this.hints = hints;
        return this;
    }

    /**
     * @return true if calling code does not need committed instances, which allows for performance optimization
     */
    public boolean isDiscardSaved() {
        return discardSaved;
    }

    /**
     * Set to true if calling code does not need saved instances, which allows for performance optimization.
     */
    public SaveContext setDiscardSaved(boolean discardSaved) {
        this.discardSaved = discardSaved;
        return this;
    }

    public List<AccessConstraint<?>> getAccessConstraints() {
        return this.accessConstraints == null ? Collections.emptyList() : this.accessConstraints;
    }

    public SaveContext setAccessConstraints(List<AccessConstraint<?>> accessConstraints) {
        this.accessConstraints = accessConstraints;
        return this;
    }

    public boolean isJoinTransaction() {
        return joinTransaction;
    }

    public SaveContext setJoinTransaction(boolean joinTransaction) {
        this.joinTransaction = joinTransaction;
        return this;
    }
}
