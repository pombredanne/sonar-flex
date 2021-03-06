/*
 * Sonar Flex Plugin
 * Copyright (C) 2010 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.plugins.flex.flexpmd.xml;

import com.google.common.base.Objects;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("rule")
public class FlexRule implements Comparable<String> {

  @XStreamAlias("class")
  @XStreamAsAttribute
  private String clazz;

  @XStreamAsAttribute
  private String message;

  private String priority;

  private List<Property> properties;

  @XStreamOmitField
  private String description;

  @XStreamOmitField
  private String category;

  @XStreamOmitField
  private String exclude;

  @XStreamOmitField
  private String example;

  public FlexRule() {
  }

  public FlexRule(String clazz) {
    this(clazz, null);
  }

  public FlexRule(String clazz, String priority) {
    this.clazz = clazz;
    this.priority = priority;
  }

  public String getClazz() {
    return clazz;
  }

  public void setProperties(List<Property> properties) {
    this.properties = properties;
  }

  public List<Property> getProperties() {
    return properties;
  }

  public int compareTo(String o) {
    return o.compareTo(clazz);
  }

  public String getPriority() {
    return priority;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  public void addProperty(Property property) {
    if (properties == null) {
      properties = new ArrayList<Property>();
    }
    properties.add(property);
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getExclude() {
    return exclude;
  }

  public void setExclude(String exclude) {
    this.exclude = exclude;
  }

  public String getDescription() {
    String desc = "<p>" + (description == null ? "" : description) + "</p>";
    if (example != null) {
      desc += "<pre>" + example + "</pre>";
    }
    return desc;
  }

  public String getCategory() {
    return category;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(clazz);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    FlexRule other = (FlexRule) obj;
    return Objects.equal(this.clazz, other.clazz);
  }

}
