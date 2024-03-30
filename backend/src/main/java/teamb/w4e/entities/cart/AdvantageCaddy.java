package teamb.w4e.entities.cart;

import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.exceptions.AlreadyExistingException;

import java.util.HashSet;
import java.util.Set;

@Embeddable
public class AdvantageCaddy {
    @OneToMany
    private Set<Advantage> advantages = new HashSet<>();

    public AdvantageCaddy() {
    }

    public AdvantageCaddy(Set<Advantage> advantages) {
        this.advantages = advantages;
    }

    public Set<Advantage> getAdvantages() {
        return advantages;
    }

    public void setAdvantages(Set<Advantage> advantages) {
        this.advantages = advantages;
    }

    public void addAdvantage(Advantage advantage) throws AlreadyExistingException {
        if (this.advantages.contains(advantage))
            throw new AlreadyExistingException("Advantage already exists in the caddy");
        this.advantages.add(advantage);
    }


    public void clear() {
        this.advantages.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdvantageCaddy that)) return false;
        return advantages.equals(that.advantages);
    }

    @Override
    public int hashCode() {
        return advantages.hashCode();
    }
}

