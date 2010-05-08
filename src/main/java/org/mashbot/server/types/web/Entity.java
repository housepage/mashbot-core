package org.mashbot.server.types.web;

import org.mashbot.server.types.PValue;

public abstract class Entity {
  /* Type */
  public abstract Type getType();

  /* Associations */
  public abstract Assoc[] getAssoc();
  public abstract boolean hasAssoc(Assoc assoc);

  /* Properties */
  public abstract Entity[] getProperties();
  public abstract Entity getProperty(Ident id);

  /* Primitive */
  public abstract boolean hasPrimitiveValue();
  public abstract PValue getPrimitiveValue();
}
