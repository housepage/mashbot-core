package org.mashbot.server.types.web;

import org.mashbot.server.types.PValue;

public abstract class Entity {
  /* Type */
  public abstract Type getType();

  /* Associations */
  public abstract Assoc[] getAssoc();

  /* Properties */
  public abstract Entity[] getProperties();

  /* Primtive */
  public abstract boolean hasPrimitiveValue();
  public abstract PValue getPrimitiveValue();
}
