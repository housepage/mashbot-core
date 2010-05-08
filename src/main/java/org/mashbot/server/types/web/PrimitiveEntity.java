package org.mashbot.server.types.web;

import javax.xml.bind.annotation.XmlRootElement;

/* @XmlRootElement(name="primitiveEntity")
public class PrimitiveEntity extends Entity implements MutableEntity {
  
  protected Type type; 

  public Type getType();

  protected ArrayList<Assoc> assocs;

  public Assoc[] getAssoc() { return assocs.toArray(); }

  public boolean hasAssoc(Assoc assoc) { return assocs.contains(assoc); }

  public void addAssoc(Assoc assoc) { assocs.add(assoc); }

  public void removeAssoc(Assoc assoc) { 
    int index = assocs.indexOf(assoc);

    if( index != -1 )
      assocs.remove(index);
  }

  protected ArrayList<Entity> properties;

  protected int findIndex(Ident id)
  {
    int i;

    for(i = 0; i < properties.size(); i++)
    {
      if(properties.get(i).getIdent().equals(id))
      {
        return i;
      }
    }

    return -1;

  }

  public Entity[] getProperties() { return properties.toArray(); }

  public Entity getProperty(Ident id)
  {
    int index = findIndex(id);

    if(index != -1)
      return properties.get(i);
    else
      return null;
  }

  public Ident addProperty(Entity property)
  {
    properties.add(property);
  }

  public void removeProperty(Ident id)
  {
    int index = findIndex(id);

    if(index != -1)
      properties.remove(index);
  }

  
  protected PValue primitive = null;

  public boolean hasPrimitiveValue() { return primitive != null; }
  public PValue getPrimitiveValue() { return primitive; }
  public void setPrimitiveValue(PValue value) { primitive = value; }
  public void removePrimitiveValue() { primitive = null; }

} */
