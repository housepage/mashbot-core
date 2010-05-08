package org.mashbot.server.types.web;

public interface MutableEntity {
  /* Associations */
  public void addAssoc(Assoc assoc);
  public void removeAssoc(Assoc assoc);

  /* Properties */
  public Ident addProperty(Entity property);
  public void removeProperty(Ident id);

  /* Primitive */
  public void setPrimitiveValue(PValue value);
  public void removePrimitiveValue();
}
  
