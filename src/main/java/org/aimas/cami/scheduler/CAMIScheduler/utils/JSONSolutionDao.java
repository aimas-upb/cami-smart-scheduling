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
import java.io.IOException;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.optaplanner.core.api.domain.solution.PlanningSolution;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @param <Solution_>
 *            the solution type, the class with the {@link PlanningSolution}
 *            annotation
 */
public abstract class JSONSolutionDao<Solution_> extends AbstractSolutionDao<Solution_> {

	protected ObjectMapper mapper;

	public JSONSolutionDao(String dirName, Class... xStreamAnnotations) {
		super(dirName);

		mapper = new ObjectMapper();

	}

	@Override
	public String getFileExtension() {
		return "json";
	}

	@Override
	public Solution_ readSolution(File inputSolutionFile) {

		try {
			return (Solution_) mapper.readValue(inputSolutionFile, ActivitySchedule.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	@Override
	public void writeSolution(Solution_ solution, File outputSolutionFile) {
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(outputSolutionFile, solution);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
