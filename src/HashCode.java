import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HashCode {
	static List<Pizza> pizzas;

	public static void main(String[] args) {

		pizzas = new ArrayList<>();
		pizzas.add(new Pizza(extractIngredients("onion pepper olive")));
		pizzas.add(new Pizza(extractIngredients("mushroom tomato basil")));
		pizzas.add(new Pizza(extractIngredients("chicken mushroom pepper")));
		pizzas.add(new Pizza(extractIngredients("tomato mushroom basil")));
		pizzas.add(new Pizza(extractIngredients("chicken basil")));

		int numberOfPizza = 5;
		int twoPersonTeam = 1;
		int threePersonTeam = 2;
		int fourPersonTeam = 1;
		int[] personPerTeamChances = { twoPersonTeam + 1, threePersonTeam + 1, fourPersonTeam + 1 };
		int[] numberOfPizzaGenerator = { numberOfPizza, numberOfPizza, numberOfPizza, numberOfPizza };

		Map<String, List<List<Integer>>> allCombinationsMap = new HashMap<>();
		allCombinationsMap.put("T2", createPossibleCombinations(2, numberOfPizzaGenerator));
		allCombinationsMap.put("T3", createPossibleCombinations(3, numberOfPizzaGenerator));
		allCombinationsMap.put("T4", createPossibleCombinations(4, numberOfPizzaGenerator));

		List<List<Integer>> allPossibleTeamConfig = createPossibleCombinations(3, personPerTeamChances);

		List<List<List<Integer>>> validPizzaConfigurations = findAllValidConfigurations(
				createPossibleCombinations(2, numberOfPizzaGenerator),
				createPossibleCombinations(3, numberOfPizzaGenerator),
				createPossibleCombinations(4, numberOfPizzaGenerator));

		List<List<Integer>> bestConfigList = getBestConfigurations(validPizzaConfigurations, allPossibleTeamConfig);
		for (List<Integer> t : bestConfigList) {
			System.out.println(t);
		}

	}


	static List<List<Integer>> createPossibleCombinations(int x, int... y) {
		if (x == 2) {
			List<List<Integer>> combinations = new ArrayList<>();
			for (int i = 0; i < y[0]; i++) {
				for (int j = 0; j < y[1]; j++) {
					List<Integer> combination = new ArrayList<>();
					combination.add(i);
					combination.add(j);
					combinations.add(combination);
				}
			}
			return combinations;
		} else if (x == 3) {
			List<List<Integer>> combinations = new ArrayList<>();
			for (int k = 0; k < y[0]; k++) {
				for (int i = 0; i < y[1]; i++) {
					for (int j = 0; j < y[2]; j++) {
						List<Integer> combination = new ArrayList<>();
						combination.add(k);
						combination.add(i);
						combination.add(j);
						combinations.add(combination);
					}
				}
			}
			return combinations;
		} else if (x == 4) {
			List<List<Integer>> combinations = new ArrayList<>();
			for (int m = 0; m < y[0]; m++) {
				for (int k = 0; k < y[1]; k++) {
					for (int i = 0; i < y[2]; i++) {
						for (int j = 0; j < y[3]; j++) {
							List<Integer> combination = new ArrayList<>();
							combination.add(m);
							combination.add(k);
							combination.add(i);
							combination.add(j);
							combinations.add(combination);
						}
					}
				}
			}

			return combinations;
		} else {
			throw new RuntimeException("not in Question scope");
		}

	}

	static List<List<List<Integer>>> findAllValidConfigurations(List<List<Integer>> t2combinations,
			List<List<Integer>> t3combinations, List<List<Integer>> t4combinations) {
		List<List<List<Integer>>> finalListOfConfigsList = new ArrayList<List<List<Integer>>>();
		for (int i = 0; i < t2combinations.size(); i++) {

			List<Integer> t2combination = t2combinations.get(i);

			for (int j = 0; j < t3combinations.size(); j++) {
				List<Integer> t3combination = t3combinations.get(j);

				if (!t3combination.contains(t2combination.get(0)) && !t3combination.contains(t2combination.get(1))) {
					for (int k = 0; k < t4combinations.size(); k++) {
						List<Integer> t4combination = t4combinations.get(k);
						if (!t4combination.contains(t3combination.get(0))
								&& !t4combination.contains(t3combination.get(1))
								&& !t4combination.contains(t3combination.get(2))) {
							List<List<Integer>> validConfig = new ArrayList<List<Integer>>();

							validConfig.add(t2combination);

							validConfig.add(t3combination);

							validConfig.add(t4combination);

							finalListOfConfigsList.add(validConfig);
						}
					}
				}
			}
		}
		return finalListOfConfigsList;
	}

	static List<List<Integer>> getBestConfigurations(List<List<List<Integer>>> allValidPizzaConfig,
			List<List<Integer>> validTeamConfig) {
		List<Integer> teamConfig = new ArrayList<Integer>();
		List<List<Integer>> highestScoringConfig = allValidPizzaConfig.get(0);
		int highestScore = 0;
		for (List<Integer> x : validTeamConfig) {
			for (List<List<Integer>> y : allValidPizzaConfig) {
				int oneConfigScore = 0;

				for (int i = 0; i < x.size(); i++) {
					oneConfigScore += x.get(i) * findTeamScore(y.get(i));
				}
				if (oneConfigScore > highestScore) {
					highestScore = oneConfigScore;
					highestScoringConfig = y;
					teamConfig = x;
				}

			}
		}

		List<List<Integer>> updatedHighestScoringConfig = prepareFinalConfig(highestScoringConfig, teamConfig);
		return updatedHighestScoringConfig;

	}

	private static List<List<Integer>> prepareFinalConfig(List<List<Integer>> highestScoringConfig,
			List<Integer> teamConfig) {
		
		 System.out.println(teamConfig+"\n"); 
		 for (List<Integer> t :highestScoringConfig) { 
			 System.out.println(t); 
		 } 
		 System.out.println();
		 

		List<List<Integer>> updatedHighestScoringConfig = new ArrayList<List<Integer>>();
		for (int x=0;x<teamConfig.size();x++) {
			for (int y = 0; y < teamConfig.get(x); y++) {
				updatedHighestScoringConfig.add(highestScoringConfig.get(x));
			}

		}
		return updatedHighestScoringConfig;

	}

	private static int findTeamScore(List<Integer> singleTeamOrder) {
		int ingredientsCount = 0;
		List<String> allIngredients = new ArrayList<String>();
		List<Pizza> pizzaForThatTeam = new ArrayList<>();
		for (int pizzaIndex : singleTeamOrder) {
			pizzaForThatTeam.add(pizzas.get(pizzaIndex));
		}
		for (Pizza p : pizzaForThatTeam) {
			allIngredients.addAll(p.getIngredients());
		}
		ingredientsCount += allIngredients.stream().distinct().collect(Collectors.toList()).size();
		return ingredientsCount * ingredientsCount;

	}

	static List<String> extractIngredients(String x) {
		return Arrays.asList(x.split(" "));
	}

}
