package no.roedt;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class RoedtPanacheEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue
    public Integer id;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "<" + id + ">";
    }
}
