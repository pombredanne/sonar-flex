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
package org.sonar.plugins.flex;

import org.sonar.api.profiles.AnnotationProfileParser;
import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.profiles.XMLProfileParser;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.flex.checks.CheckList;
import org.sonar.plugins.flex.core.Flex;

public class FlexProfile extends ProfileDefinition {

  private final AnnotationProfileParser annotationProfileParser;
  private final XMLProfileParser xmlProfileParser;

  public FlexProfile(AnnotationProfileParser annotationProfileParser, XMLProfileParser xmlProfileParser) {
    this.annotationProfileParser = annotationProfileParser;
    this.xmlProfileParser = xmlProfileParser;
  }

  @Override
  public RulesProfile createProfile(ValidationMessages validation) {
    RulesProfile rulesProfile = annotationProfileParser.parse(CheckList.REPOSITORY_KEY, CheckList.SONAR_WAY_PROFILE, Flex.KEY, CheckList.getChecks(), validation);
    RulesProfile pmdRules = xmlProfileParser.parseResource(getClass().getClassLoader(), "org/sonar/plugins/flex/profile-sonar-way.xml", validation);
    for (ActiveRule activeRule : pmdRules.getActiveRules()) {
      rulesProfile.addActiveRule(activeRule);
    }
    return rulesProfile;
  }

}
