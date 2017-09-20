<?xml version="1.0" encoding="UTF-8"?>
<plannerBenchmark>
  <benchmarkDirectory>data/activityschedule</benchmarkDirectory>
  <parallelBenchmarkCount>AUTO</parallelBenchmarkCount>
  <inheritedSolverBenchmark>
    <problemBenchmarks>
      <xStreamAnnotatedClass>org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule</xStreamAnnotatedClass>
      <inputSolutionFile>data/activityschedule/unsolved/cami-scenario.xml</inputSolutionFile>
      <problemStatisticType>BEST_SCORE</problemStatisticType>
      <problemStatisticType>SCORE_CALCULATION_SPEED</problemStatisticType>
    </problemBenchmarks>
    <solver>
      <scanAnnotatedClasses/>
      <scoreDirectorFactory>
        <scoreDrl>solver/CAMITaskSchedulerScoreRules.drl</scoreDrl>
      </scoreDirectorFactory>
      <termination>
        <minutesSpentLimit>5</minutesSpentLimit>
      </termination>
    </solver>
  </inheritedSolverBenchmark>
  <solverBenchmark>
    <name>First Fit</name>
    <solver>
      <constructionHeuristic>
        <constructionHeuristicType>FIRST_FIT</constructionHeuristicType>
      </constructionHeuristic>
    </solver>
  </solverBenchmark>
<!--
  <solverBenchmark>
    <name>Tabu Search</name>
    <solver>
      <constructionHeuristic>
        <constructionHeuristicType>FIRST_FIT</constructionHeuristicType>
      </constructionHeuristic>
      <localSearch>
        <unionMoveSelector>
          <changeMoveSelector>
	        <cacheType>PHASE</cacheType>
            <entitySelector>
              <filterClass>org.aimas.cami.scheduler.CAMIScheduler.solver.move.MovableActivitySelectionFilter</filterClass>
            </entitySelector>
          </changeMoveSelector>
        </unionMoveSelector>
        <acceptor>
          <entityTabuRatio>0.02</entityTabuSize>
        </acceptor>
        <forager>
          <acceptedCountLimit>100</acceptedCountLimit>
        </forager>
      </localSearch>
    </solver>
  </solverBenchmark>

  <solverBenchmark>
    <name>Late Acceptance</name>
    <solver>
      <constructionHeuristic>
        <constructionHeuristicType>FIRST_FIT</constructionHeuristicType>
      </constructionHeuristic>
      <localSearch>
        <unionMoveSelector>
          <changeMoveSelector>
	        <cacheType>PHASE</cacheType>
            <entitySelector>
              <filterClass>org.aimas.cami.scheduler.CAMIScheduler.solver.move.MovableActivitySelectionFilter</filterClass>
            </entitySelector>
          </changeMoveSelector>
        </unionMoveSelector>
        <acceptor>
          <lateAcceptanceSize>1000</lateAcceptanceSize>
        </acceptor>
        <forager>
          <acceptedCountLimit>4</acceptedCountLimit>
        </forager>
      </localSearch>
    </solver>
  </solverBenchmark>
  
  <solverBenchmark>
    <name>Simulated Annealing</name>
    <solver>
      <constructionHeuristic>
        <constructionHeuristicType>FIRST_FIT</constructionHeuristicType>
      </constructionHeuristic>
      <localSearch>
        <unionMoveSelector>
          <changeMoveSelector>
	        <cacheType>PHASE</cacheType>
            <entitySelector>
              <filterClass>org.aimas.cami.scheduler.CAMIScheduler.solver.move.MovableActivitySelectionFilter</filterClass>
            </entitySelector>
          </changeMoveSelector>
        </unionMoveSelector>
        <acceptor>
          <simulatedAnnealingStartingTemperature>2hard/100soft</simulatedAnnealingStartingTemperature>
        </acceptor>
        <forager>
          <acceptedCountLimit>1</acceptedCountLimit>
        </forager>
      </localSearch>
    </solver>
  </solverBenchmark>
-->
<#list ["1hard/10soft", "1hard/20soft", "1hard/50soft", "1hard/70soft", "1hard/100soft", "2hard/10soft", "2hard/20soft", "2hard/50soft", "2hard/70soft", "2hard/100soft"] as startingTemperature>
  <solverBenchmark>
    <name>Simulated Annealing startingTemperature ${startingTemperature?replace("/", "_")}</name>
    <solver>
      <constructionHeuristic>
        <constructionHeuristicType>FIRST_FIT</constructionHeuristicType>
      </constructionHeuristic>
      <localSearch>
        <unionMoveSelector>
          <changeMoveSelector>
	        <cacheType>PHASE</cacheType>
            <entitySelector>
              <filterClass>org.aimas.cami.scheduler.CAMIScheduler.solver.move.MovableActivitySelectionFilter</filterClass>
            </entitySelector>
          </changeMoveSelector>
        </unionMoveSelector>
        <acceptor>
          <simulatedAnnealingStartingTemperature>${startingTemperature}</simulatedAnnealingStartingTemperature>
          <lateAcceptanceSize>100</lateAcceptanceSize>
        </acceptor>
        <forager>
          <acceptedCountLimit>1</acceptedCountLimit>
        </forager>
      </localSearch>
    </solver>
  </solverBenchmark>
</#list>
<!--
<#list [100, 200, 300, 400, 500, 600] as lateAcceptanceSize>
  <#list [1, 2, 3, 4] as acceptedCountLimit>
    <solverBenchmark>
      <name>Simulated Annealing and Late Acceptance ${lateAcceptanceSize} (acceptedCountLimit ${acceptedCountLimit})</name>
      <solver>
        <constructionHeuristic>
          <constructionHeuristicType>FIRST_FIT</constructionHeuristicType>
        </constructionHeuristic>
        <localSearch>
          <unionMoveSelector>
            <changeMoveSelector>
	          <cacheType>PHASE</cacheType>
              <entitySelector>
                <filterClass>org.aimas.cami.scheduler.CAMIScheduler.solver.move.MovableActivitySelectionFilter</filterClass>
              </entitySelector>
            </changeMoveSelector>
          </unionMoveSelector>
          <acceptor>
            <simulatedAnnealingStartingTemperature>2hard/100soft</simulatedAnnealingStartingTemperature>
            <lateAcceptanceSize>${lateAcceptanceSize}</lateAcceptanceSize>
          </acceptor>
          <forager>
            <acceptedCountLimit>${acceptedCountLimit}</acceptedCountLimit>
          </forager>
        </localSearch>
      </solver>
    </solverBenchmark>
  </#list>
</#list>
-->
</plannerBenchmark>