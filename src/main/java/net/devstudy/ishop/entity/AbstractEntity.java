package net.devstudy.ishop.entity;

import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractEntity<T> implements Serializable {
    T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractEntity other = (AbstractEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s [id=%s]", getClass().getSimpleName(), id);
    }

}
