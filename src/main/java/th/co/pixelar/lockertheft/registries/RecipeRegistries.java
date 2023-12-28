package th.co.pixelar.lockertheft.registries;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;

public class RecipeRegistries {
    public RecipeRegistries(Server server) {
        server.addRecipe(LockRecipe());
        server.addRecipe(LockPickerRecipe());
    }

    private ShapedRecipe LockRecipe() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey("locktheft" ,"lock"), ItemRegistries.LOCK);
        shapedRecipe.shape("IRI", "IRI", "IRI");
        shapedRecipe.setIngredient('I', new ItemStack(Material.IRON_INGOT));
        shapedRecipe.setIngredient('R', new ItemStack(Material.REDSTONE));
        shapedRecipe.setCategory(CraftingBookCategory.MISC);

        return shapedRecipe;

    }

    private ShapedRecipe LockPickerRecipe() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey("locktheft" ,"lock_picker"), ItemRegistries.LOCK_PICKER);
        shapedRecipe.shape("IAA", "AIA", "AAI");
        shapedRecipe.setIngredient('I', new ItemStack(Material.IRON_INGOT));
        shapedRecipe.setIngredient('A', new ItemStack(Material.AIR));
        shapedRecipe.setCategory(CraftingBookCategory.MISC);

        return shapedRecipe;
    }



}
