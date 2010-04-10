package org.mashbot.server.types.web;

public class PrimitiveEntity extends Entity implements MutableEntity {
  /* Type */
  protected Type type; 

  public Type getType();

  /* Associations */
  protected ArrayList<Assoc> assocs;

  public Assoc[] getAssoc() { return assocs.toArray(); }
  public boolean hasAssoc(Assoc assoc) { return assocs.contains(assoc); }
  public void addAssoc(Assoc assoc);
  public void removeAssoc(Assoc assoc);

  /* Properties */
  protected ArrayList<Entity> properties;

  public Entity[] getProperties();
  public Entity getProperty(Ident id);
  public Ident addProperty(Entity property);
  public void removeProperty(Ident id);

  /* Primitive */
  protected PValue primitive = null;

  public boolean hasPrimitiveValue() { return primitive != null }
  public PValue getPrimitiveValue();
  public void setPrimitiveValue(PValue value);
  public void removePrimitiveValue();

}
