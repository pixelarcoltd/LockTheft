package th.co.pixelar.lockertheft.registries;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import th.co.pixelar.lockertheft.handlers.ConfigLoader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RecipeRegistries {
    private final Server server;
    public RecipeRegistries(Server server) {
        this.server = server;
        addRecipes();
    }

    private void addRecipes() {
        server.addRecipe(LockRecipe());
        server.addRecipe(LockPickerRecipe());
        server.addRecipe(ScrewDriverRecipe());
    }

    private void reloadRecipes() {
        server.clearRecipes();
        addRecipes();
    }

    private ShapedRecipe LockRecipe() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey("locktheft" ,"lock"), ItemRegistries.LOCK);

        String config = ConfigLoader.LOCK_CRAFTING_RECIPE;
        shapedRecipe.shape(getRecipes(config).get(0), getRecipes(config).get(1), getRecipes(config).get(2));
        HashMap<Character, Material> ingredients = getIngredients(config);
        ingredients.forEach((abb, value) -> {
            shapedRecipe.setIngredient(abb, new ItemStack(value));
        });

        shapedRecipe.setCategory(CraftingBookCategory.MISC);

        return shapedRecipe;
    }

    private ShapedRecipe LockPickerRecipe() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey("locktheft" ,"lock_picker"), ItemRegistries.LOCK_PICKER);
        String config = ConfigLoader.LOCK_PICKER_CRAFTING_RECIPE;
        shapedRecipe.shape(getRecipes(config).get(0), getRecipes(config).get(1), getRecipes(config).get(2));

        HashMap<Character, Material> ingredients = getIngredients(config);
        ingredients.forEach((abb, value) -> {
            shapedRecipe.setIngredient(abb, new ItemStack(value));
        });

        shapedRecipe.setCategory(CraftingBookCategory.MISC);

        return shapedRecipe;
    }

    private ShapedRecipe ScrewDriverRecipe() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey("locktheft" ,"screw_driver"), ItemRegistries.SCREW_DRIVER);
        shapedRecipe.shape("IRR", "AIA", "AAI");
        shapedRecipe.setIngredient('I', new ItemStack(Material.IRON_INGOT));
        shapedRecipe.setIngredient('A', new ItemStack(Material.AIR));
        shapedRecipe.setIngredient('R', new ItemStack(Material.REDSTONE));
        shapedRecipe.setCategory(CraftingBookCategory.MISC);

        return shapedRecipe;
    }

    private HashMap<Character, Material> getIngredients(String config) {
        String[] configSplits = config.split(";");
        String[] ingredients = configSplits[1].split(",");

        HashMap<Character, Material> ingredientsMap = new HashMap<>();

        for (String ingredient: ingredients) {
            String[] split = ingredient.split("=");
            ingredientsMap.put(split[0].charAt(0), Material.matchMaterial(split[1]));
        }

        return ingredientsMap;
    }

    private List<String> getRecipes(String config) {
        String[] configSplits = config.split(";");
        return Arrays.asList(configSplits[0].split(","));
    }

}
