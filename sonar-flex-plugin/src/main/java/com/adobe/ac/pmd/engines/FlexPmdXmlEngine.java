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
package com.adobe.ac.pmd.engines;

import com.adobe.ac.pmd.FlexPmdParameters;
import com.adobe.ac.pmd.FlexPmdViolations;
import com.adobe.ac.pmd.IFlexViolation;
import com.adobe.ac.pmd.files.IFlexFile;
import net.sourceforge.pmd.PMDException;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;

public class FlexPmdXmlEngine extends AbstractFlexPmdEngine
{
  private static final Logger LOGGER = Logger.getLogger(FlexPmdXmlEngine.class.getName());

  public FlexPmdXmlEngine(final FlexPmdParameters parameters) throws URISyntaxException,
      IOException
  {
    super(parameters);
  }

  @Override
  protected final void writeReport(final FlexPmdViolations pmd) throws PMDException
  {
    final File realOutputDirectory = getOutputDirectory();
    final String filePath = realOutputDirectory.getAbsoluteFile()
        + File.separator + FlexPMDFormat.XML.toString();

    makeSureOutputDirectoryExists(realOutputDirectory);

    Writer writter = null;
    try
    {
      LOGGER.finest("Start writting XML report");
      LOGGER.info("Creating report in <"
          + filePath + ">");

      writter = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");
      writeReportHeader(writter);
      writeFileViolations(pmd,
          writter);
      writeReportFooter(writter);
      writter.close();
    } catch (final IOException e)
    {
      throw new PMDException("Error creating file "
          + filePath, e);
    } finally
    {
      finalizeReport(writter);
    }
  }

  private void finalizeReport(final Writer writter)
  {
    LOGGER.finest("End writting XML report");

    if (writter != null)
    {
      try
      {
        LOGGER.finest("Closing the XML writter");
        writter.close();
      } catch (final IOException e)
      {
        LOGGER.warning(Arrays.toString(e.getStackTrace()));
      }
      LOGGER.finest("Closed the XML writter");
    }
  }

  private void formatFileFiolation(final Writer writter,
      final IFlexFile sourceFile,
      final Collection<IFlexViolation> violations,
      final String sourceFilePath) throws IOException
  {
    if (!violations.isEmpty())
    {
      if (sourceFilePath.charAt(2) == ':')
      {
        writter.write("   <file name=\""
            + sourceFilePath.substring(1,
                sourceFilePath.length()) + "\">" + getNewLine());
      }
      else
      {
        writter.write("   <file name=\""
            + sourceFilePath + "\">" + getNewLine());

      }
      for (final IFlexViolation violation : violations)
      {
        writter.write(violation.toXmlString(sourceFile,
            violation.getRule().getRuleSetName()));
      }
      writter.write("   </file>"
          + getNewLine());
    }
  }

  private String getNewLine()
  {
    return System.getProperty("line.separator");
  }

  private void makeSureOutputDirectoryExists(final File realOutputDirectory)
  {
    if (!realOutputDirectory.exists()
        && !realOutputDirectory.mkdirs())
    {
      LOGGER.severe("Unable to create an output folder");
    }
  }

  private void writeFileViolations(final FlexPmdViolations pmd,
      final Writer writter) throws IOException
  {
    for (final IFlexFile sourceFile : pmd.getViolations().keySet())
    {
      final Collection<IFlexViolation> violations = pmd.getViolations().get(sourceFile);
      final String sourceFilePath = sourceFile.getFilePath();

      formatFileFiolation(writter,
          sourceFile,
          violations,
          sourceFilePath);
    }
  }

  private void writeReportFooter(final Writer writter) throws IOException
  {
    writter.write("</pmd>"
        + getNewLine());
  }

  private void writeReportHeader(final Writer writter) throws IOException
  {
    writter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + getNewLine());
    writter.write("<pmd version=\"4.2.1\" timestamp=\""
        + new Date().toString() + "\">" + getNewLine());
  }
}
