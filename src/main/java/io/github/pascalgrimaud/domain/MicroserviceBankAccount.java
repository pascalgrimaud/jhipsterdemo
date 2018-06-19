package io.github.pascalgrimaud.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import io.github.pascalgrimaud.domain.enumeration.BankAccountType;

/**
 * A MicroserviceBankAccount.
 */
@Entity
@Table(name = "microservice_bank_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MicroserviceBankAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "bank_number")
    private Integer bankNumber;

    @Column(name = "agency_number")
    private Long agencyNumber;

    @Column(name = "last_operation_duration")
    private Float lastOperationDuration;

    @Column(name = "mean_operation_duration")
    private Double meanOperationDuration;

    @NotNull
    @Column(name = "balance", precision = 10, scale = 2, nullable = false)
    private BigDecimal balance;

    @Column(name = "opening_day")
    private LocalDate openingDay;

    @Column(name = "last_operation_date")
    private Instant lastOperationDate;

    @Column(name = "active")
    private Boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private BankAccountType accountType;

    @Lob
    @Column(name = "attachment")
    private byte[] attachment;

    @Column(name = "attachment_content_type")
    private String attachmentContentType;

    @Lob
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "bankAccount")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MicroserviceOperation> operations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public MicroserviceBankAccount name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBankNumber() {
        return bankNumber;
    }

    public MicroserviceBankAccount bankNumber(Integer bankNumber) {
        this.bankNumber = bankNumber;
        return this;
    }

    public void setBankNumber(Integer bankNumber) {
        this.bankNumber = bankNumber;
    }

    public Long getAgencyNumber() {
        return agencyNumber;
    }

    public MicroserviceBankAccount agencyNumber(Long agencyNumber) {
        this.agencyNumber = agencyNumber;
        return this;
    }

    public void setAgencyNumber(Long agencyNumber) {
        this.agencyNumber = agencyNumber;
    }

    public Float getLastOperationDuration() {
        return lastOperationDuration;
    }

    public MicroserviceBankAccount lastOperationDuration(Float lastOperationDuration) {
        this.lastOperationDuration = lastOperationDuration;
        return this;
    }

    public void setLastOperationDuration(Float lastOperationDuration) {
        this.lastOperationDuration = lastOperationDuration;
    }

    public Double getMeanOperationDuration() {
        return meanOperationDuration;
    }

    public MicroserviceBankAccount meanOperationDuration(Double meanOperationDuration) {
        this.meanOperationDuration = meanOperationDuration;
        return this;
    }

    public void setMeanOperationDuration(Double meanOperationDuration) {
        this.meanOperationDuration = meanOperationDuration;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public MicroserviceBankAccount balance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDate getOpeningDay() {
        return openingDay;
    }

    public MicroserviceBankAccount openingDay(LocalDate openingDay) {
        this.openingDay = openingDay;
        return this;
    }

    public void setOpeningDay(LocalDate openingDay) {
        this.openingDay = openingDay;
    }

    public Instant getLastOperationDate() {
        return lastOperationDate;
    }

    public MicroserviceBankAccount lastOperationDate(Instant lastOperationDate) {
        this.lastOperationDate = lastOperationDate;
        return this;
    }

    public void setLastOperationDate(Instant lastOperationDate) {
        this.lastOperationDate = lastOperationDate;
    }

    public Boolean isActive() {
        return active;
    }

    public MicroserviceBankAccount active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public BankAccountType getAccountType() {
        return accountType;
    }

    public MicroserviceBankAccount accountType(BankAccountType accountType) {
        this.accountType = accountType;
        return this;
    }

    public void setAccountType(BankAccountType accountType) {
        this.accountType = accountType;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public MicroserviceBankAccount attachment(byte[] attachment) {
        this.attachment = attachment;
        return this;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public String getAttachmentContentType() {
        return attachmentContentType;
    }

    public MicroserviceBankAccount attachmentContentType(String attachmentContentType) {
        this.attachmentContentType = attachmentContentType;
        return this;
    }

    public void setAttachmentContentType(String attachmentContentType) {
        this.attachmentContentType = attachmentContentType;
    }

    public String getDescription() {
        return description;
    }

    public MicroserviceBankAccount description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<MicroserviceOperation> getOperations() {
        return operations;
    }

    public MicroserviceBankAccount operations(Set<MicroserviceOperation> microserviceOperations) {
        this.operations = microserviceOperations;
        return this;
    }

    public MicroserviceBankAccount addOperation(MicroserviceOperation microserviceOperation) {
        this.operations.add(microserviceOperation);
        microserviceOperation.setBankAccount(this);
        return this;
    }

    public MicroserviceBankAccount removeOperation(MicroserviceOperation microserviceOperation) {
        this.operations.remove(microserviceOperation);
        microserviceOperation.setBankAccount(null);
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
        MicroserviceBankAccount microserviceBankAccount = (MicroserviceBankAccount) o;
        if (microserviceBankAccount.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), microserviceBankAccount.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MicroserviceBankAccount{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", bankNumber=" + getBankNumber() +
            ", agencyNumber=" + getAgencyNumber() +
            ", lastOperationDuration=" + getLastOperationDuration() +
            ", meanOperationDuration=" + getMeanOperationDuration() +
            ", balance=" + getBalance() +
            ", openingDay='" + getOpeningDay() + "'" +
            ", lastOperationDate='" + getLastOperationDate() + "'" +
            ", active='" + isActive() + "'" +
            ", accountType='" + getAccountType() + "'" +
            ", attachment='" + getAttachment() + "'" +
            ", attachmentContentType='" + getAttachmentContentType() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
