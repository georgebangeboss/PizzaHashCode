import java.util.List;

public class Pizza {
	private List<String> ingredient;
	Pizza(List<String> ingredients){
		this.ingredient=ingredients;
	}
	List<String> getIngredients(){
		return this.ingredient;
	}

}
