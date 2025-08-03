package com.rpgsystem.rpg.domain.common;

import java.util.Objects;
import java.util.UUID;

public final class CodigoId {
    private final String valor;

    public CodigoId(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Código não pode ser nulo ou vazio");
        }
        this.valor = valor;
    }

    public static CodigoId novo() {
        return new CodigoId(UUID.randomUUID().toString());
    }

    public String getValue() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CodigoId other)) return false;
        return Objects.equals(valor, other.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor;
    }
}
