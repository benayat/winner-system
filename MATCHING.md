#### formula: 
Injury Adjusted Factor = Injury Factor - Skill Level* 0.005  
// The 0.005 here makes the skill mitigation less harsh

Goal Probability = Base Probability + (Skill Level - 1) * Skill Modifier

Revised Goal Probability = Goal Probability - (Injuries * Injury Adjusted Factor)

Example

Team A: Skill Level 8, Injuries 6.
Original Goal Probability: 0.45
Injury Factor: 0.03
Injury Adjusted Factor = 0.03 - (8/2) * 0.01 = 0.01 // Reduced due to the team's skill
Revised Goal Probability: 0.45 - (6 * 0.01) = 0.39 (or 39%)

#### matching groups and periods: 
- I wrote all possible combinations of matchups in a HashMap. 
- n - number of teams. m - number of matches for each period==n/2. p is number of periods, which is n-1.