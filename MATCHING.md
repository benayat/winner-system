### Algorithms in use for matching teams and periods
- The entire "algorithmic" part is encapsulated in the `SeasonRunnerService` class, and the explanation will follow its order of methods.
- The algorithm will run from SeasonRunnerService in an async manner, to avoid blocking the main thread.

#### matching groups and periods:
- In method generateMatchUps
- If n is number of teams, and m is the number of matches for each period, m=ceil(n/2). p is number of periods, which is n-1.

#### formula for chance to win/score: 
- To calculate any team goal probability(same as chances to win), I used the following components - injuries, skill level, and multipliers to adjust the goal probability.
- The injuries are always random and reset in every game, but the skill level starts at random and updates after each game results.
- BASE_PROBABILITY: 0.3, SKILL_PROBABILITY_MODIFIER_STEP multiplier: 0.05, injury factor: 0.03
- All the results in the folowing: The basic probability for a team to score, the injuries factor, and the overall goal probability. 
1. Basic probability based on skill level: BASE_PROBABILITY + team.getSkillLevel() * SKILL_PROBABILITY_MODIFIER_STEP;
2. Injuries factor: BASE_INJURY_FACTOR - team.getSkillLevel() * SKILL_INJURY_SCALE.
3. ResultProbability = probability - injuryFactor * team.getInjuries(). however, probability cant go bellow 0.1, so I put a limit on it. Similarly, it can't go above 0.9, so I put a limit on it.
4. Eventually, I normalized the probability by calculating the relative part of each from the sum of both teams probability, in order to base the calculation on both teams skill level, and multiplied the result by 0.9. 

#### skill level updates:
- after each game, the skill level of the teams will be updated, based on the result of the game.
- The skill level will be updated by the following formula: teams skill level + (points earned i the game-1)*SKILL_MODIFIER_STEP.
- This way, if a team wins it will get a slight increase, if it loses, a slight decrease, and if it's a draw, no change.

#### guessing the scorer in a goal event:
- We have a constant number of goal events in each game, in each - the result could be either team1 or team 2 scores, or neither.
- It's calculated with a random number, and the probability of each team to score.

