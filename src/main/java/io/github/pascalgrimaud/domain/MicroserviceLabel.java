package io.github.pascalgrimaud.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A MicroserviceLabel.
 */
@Entity
@Table(name = "microservice_label")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MicroserviceLabel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "jhi_label", nullable = false)
    private String label;

    @ManyToMany(mappedBy = "labels")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MicroserviceOperation> operations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public MicroserviceLabel label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Set<MicroserviceOperation> getOperations() {
        return operations;
    }

    public MicroserviceLabel operations(Set<MicroserviceOperation> microserviceOperations) {
        this.operations = microserviceOperations;
        return this;
    }

    public MicroserviceLabel addOperation(MicroserviceOperation microserviceOperation) {
        this.operations.add(microserviceOperation);
        microserviceOperation.getLabels().add(this);
        return this;
    }

    public MicroserviceLabel removeOperation(MicroserviceOperation microserviceOperation) {
        this.operations.remove(microserviceOperation);
        microserviceOperation.getLabels().remove(this);
        return this;
    }

    public void setOperations(Set<MicroserviceOperation> microserviceOperations) {
        this.operations = microserviceOperations;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MicroserviceLabel microserviceLabel = (MicroserviceLabel) o;
        if (microserviceLabel.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), microserviceLabel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MicroserviceLabel{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            "}";
    }
}
