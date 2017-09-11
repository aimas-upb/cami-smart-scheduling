/*
 * Copyright 2010 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.aimas.cami.scheduler.CAMIScheduler.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.persistence.xstream.impl.domain.solution.XStreamSolutionFileIO;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

/**
 * @param <Solution_>
 *            the solution type, the class with the {@link PlanningSolution}
 *            annotation
 */
public abstract class XStreamSolutionDao<Solution_> extends AbstractSolutionDao<Solution_> {

	protected XStreamSolutionFileIO<Solution_> xStreamSolutionFileIO;

	protected XStream xstream;

	public XStreamSolutionDao(String dirName, Class... xStreamAnnotations) {
		super(dirName);
		xStreamSolutionFileIO = new XStreamSolutionFileIO<>(xStreamAnnotations);

		xstream = new XStream(new JsonHierarchicalStreamDriver() {

			public HierarchicalStreamWriter createWriter(Writer writer) {
				return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
			}
		});
		
		xstream.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
		xstream.alias("ActivitySchedule", ActivitySchedule.class);
	}

	@Override
	public String getFileExtension() {
		return xStreamSolutionFileIO.getOutputFileExtension();
	}

	@Override
	public Solution_ readSolution(File inputSolutionFile) {
		
		try (Reader reader = new InputStreamReader(new FileInputStream(inputSolutionFile), "UTF-8")) {
            return (Solution_) xstream.fromXML(reader);
        } catch (XStreamException | IOException e) {
            throw new IllegalArgumentException("Failed reading inputSolutionFile (" + inputSolutionFile + ").", e);
        }
	}

	@Override
	public void writeSolution(Solution_ solution, File outputSolutionFile) {
		try (Writer writer = new OutputStreamWriter(new FileOutputStream(outputSolutionFile), "UTF-8")) {
			xstream.toXML(solution, writer);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed writing outputSolutionFile (" + outputSolutionFile + ").", e);
        }
	}

}
