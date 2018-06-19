package io.github.pascalgrimaud.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A MicroserviceOperation.
 */
@Entity
@Table(name = "microservice_operation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MicroserviceOperation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private Instant date;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @ManyToOne
    @JsonIgnoreProperties("operations")
    private MicroserviceBankAccount bankAccount;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "microservice_operation_label",
               joinColumns = @JoinColumn(name = "microservice_operations_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "labels_id", referencedColumnName = "id"))
    private Set<MicroserviceLabel> labels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public MicroserviceOperation date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public MicroserviceOperation description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public MicroserviceOperation amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public MicroserviceBankAccount getBankAccount() {
        return bankAccount;
    }

    public MicroserviceOperation bankAccount(MicroserviceBankAccount microserviceBankAccount) {
        this.bankAccount = microserviceBankAccount;
        return this;
    }

    public void setBankAccount(MicroserviceBankAccount microserviceBankAccount) {
        this.bankAccount = microserviceBankAccount;
    }

    public Set<MicroserviceLabel> getLabels() {
        return labels;
    }

    public MicroserviceOperation labels(Set<MicroserviceLabel> microserviceLabels) {
        this.labels = microserviceLabels;
        return this;
    }

    public MicroserviceOperation addLabel(MicroserviceLabel microserviceLabel) {
        this.labels.add(microserviceLabel);
        microserviceLabel.getOperations().add(this);
        return this;
    }

    public MicroserviceOperation removeLabel(MicroserviceLabel microserviceLabel) {
        this.labels.remove(microserviceLabel);
        microserviceLabel.getOperations().remove(this);
        return this;
    }

    public void setLabels(Set<MicroserviceLabel> microserviceLabels) {
        this.labels = microserviceLabels;
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
        MicroserviceOperation microserviceOperation = (MicroserviceOperation) o;
        if (microserviceOperation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), microserviceOperation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MicroserviceOperation{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", amount=" + getAmount() +
            "}";
    }
}
