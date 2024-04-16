### Algorithms in use for matching teams and periods
- The entire "algorithmic" part is encapsulated in the `SeasonRunnerService` class, and the explanation will follow its order of methods.
- The algorithm will run from SeasonRunnerService in an async manner, to avoid blocking the main thread.

#### matching groups and periods:
- In method generateMatchUps
- If n is number of teams, and m is the number of matches for each period, m=ceil(n/2). p is number of periods, which is n-1.

#### formula for chance to win/score: 
- To calculate any team goal probability(same as chances to win), I used the following components - injuries, skill level, and multipliers to adjust the goal probability.
- BASE_PROBABILITY: 0.3, SKILL_PROBABILITY_MODIFIER_STEP multiplier: 0.05, injury factor: 0.03
- All the results in the folowing: The basic probability for a team to score, the injuries factor, and the overall goal probability. 
1. Basic probability based on skill level: BASE_PROBABILITY + team.getSkillLevel() * SKILL_PROBABILITY_MODIFIER_STEP;
2. Injuries factor: BASE_INJURY_FACTOR - team.getSkillLevel() * SKILL_INJURY_SCALE.
3. ResultProbability = probability - injuryFactor * team.getInjuries(). however, probability cant go bellow 0.1, so I put a limit on it. Similarly, it can't go above 0.9, so I put a limit on it.

#### guessing the scorer in a goal event:
- We have a constant number of goal events in each game, in each - the result could be either team1 or team 2 scores, or neither.
- It's calculated with a random number, and the probability of each team to score.

#### setup the database
- the spring datasource is set up in the application.yml, so I didn't need to set it up manually(transaction manager, datasource and entity manager).
- I run postgres on docker. For my settings, please see docker-compose.yml file.
- I added a listener to initialize the database with teams on start, based on the property - database.init.
