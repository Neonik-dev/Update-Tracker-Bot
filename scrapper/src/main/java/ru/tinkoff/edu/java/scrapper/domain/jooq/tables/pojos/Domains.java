/*
 * This file is generated by jOOQ.
 */
package ru.tinkoff.edu.java.scrapper.domain.jooq.tables.pojos;


import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.time.LocalDate;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.17.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Domains implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private LocalDate createdDate;

    public Domains() {}

    public Domains(Domains value) {
        this.id = value.id;
        this.name = value.name;
        this.createdDate = value.createdDate;
    }

    @ConstructorProperties({ "id", "name", "createdDate" })
    public Domains(
        @NotNull Long id,
        @NotNull String name,
        @NotNull LocalDate createdDate
    ) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
    }

    /**
     * Getter for <code>public.domains.id</code>.
     */
    @NotNull
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for <code>public.domains.id</code>.
     */
    public void setId(@NotNull Long id) {
        this.id = id;
    }

    /**
     * Getter for <code>public.domains.name</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>public.domains.name</code>.
     */
    public void setName(@NotNull String name) {
        this.name = name;
    }

    /**
     * Getter for <code>public.domains.created_date</code>.
     */
    @NotNull
    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    /**
     * Setter for <code>public.domains.created_date</code>.
     */
    public void setCreatedDate(@NotNull LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Domains other = (Domains) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.name == null) {
            if (other.name != null)
                return false;
        }
        else if (!this.name.equals(other.name))
            return false;
        if (this.createdDate == null) {
            if (other.createdDate != null)
                return false;
        }
        else if (!this.createdDate.equals(other.createdDate))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.createdDate == null) ? 0 : this.createdDate.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Domains (");

        sb.append(id);
        sb.append(", ").append(name);
        sb.append(", ").append(createdDate);

        sb.append(")");
        return sb.toString();
    }
}
