package io.barrowisp.craftastrophe.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import io.barrowisp.craftastrophe.ModLogger;
import io.barrowisp.craftastrophe.capabilities.Knowledge;
import io.barrowisp.craftastrophe.recipes.CustomRecipes;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Blueprint extends ItemBase {

    /**
     * Maximum allowed number of custom recipes that each blueprint can have
     * @see CustomRecipes#getRandom(int)
     */
    private static final int MAX_CUSTOM_RECIPES = 4;

    /**
     * Internal class used to store and retrieve custom recipes contained within each blueprint.
     * Each read blueprint will be assigned a list of custom recipes that can be learned by players.
     * These lists are stored here and accompanied by customized ID's for fast access.
     */
    private static class BlueprintData {

        /**
         * <p>Maximum allowed size for the blueprint map.</p>
         * <p>Once the map exceeds this limit it will be cleared before adding new entries.</p>
         * The reason for doing this is the inability to find out when itemstacks get destroyed.
         * <br/>
         * Once an itemstack gets destroyed it's NBTTagCompound object reference gets lost and
         * eventually cleaned up from memory leaving us with ID map keys which represent now
         * non-existing data entries.
         */
        static final int MAX_MAP_SIZE = 200;

        /**
         * <p>Internal map that assigns a list of custom recipes to blueprint item data.</p>
         * <p>Storing data this way instead of constructing it from NBT on each demand
         * should increase performance when faced with a large number of rapid queries.</p>
         * <ul>
         *     <li>Keys represent NBTTagCompound ID entries.</li>
         *     <li>Values represent lists of custom mod recipes.</li>
         * </ul>
         */
        static final java.util.Map<java.util.UUID, java.util.List<IRecipe>> map = new java.util.Hashtable<>();

        /**
         * Main method to add new entries to blueprint recipe registry
         * @param uuid NBTTagCompound unique id object
         * @param recipes list of custom blueprint recipes
         */
        static void add(java.util.UUID uuid, java.util.List<IRecipe> recipes) {

            if (map.size() > MAX_MAP_SIZE) {
                ModLogger.debug("Clearing blueprint data map");
                map.clear();
            }
            ModLogger.debug("Storing new BlueprintData map entry: %s", uuid.toString());
            map.put(uuid, java.util.Collections.unmodifiableList(recipes));
        }
        /**
         * Helper method to add new entries to blueprint recipe registry
         * @see BlueprintData#add(java.util.UUID, List)
         */
        static void add(@NotNull NBTTagCompound nbt, java.util.List<IRecipe> recipes) {
            add(nbt.getUniqueId(BlueprintNBT.ID), recipes);
        }
        /**
         * Retrieve list of blueprint recipes mapped to given itemstack
         * @return empty list if no data entry for blueprint was found
         */
        static @NotNull java.util.List<IRecipe> get(ItemStack stack) {

            NBTTagCompound nbtTagCompound = BlueprintNBT.getSavedData(stack);
            if (nbtTagCompound != null)
            {
                java.util.UUID uuid = nbtTagCompound.getUniqueId(BlueprintNBT.ID);
                ModLogger.debug("Retrieving BlueprintData entry: %s", uuid.toString());

                java.util.List<IRecipe> recipes = map.get(uuid);
                return recipes != null ? recipes : new java.util.ArrayList<>();
            }
            else return new java.util.ArrayList<>();
        }
        static boolean exist(ItemStack stack) {
            NBTTagCompound nbtTagCompound = BlueprintNBT.getSavedData(stack);
            return nbtTagCompound != null && map.containsKey(nbtTagCompound.getUniqueId(BlueprintNBT.ID));
        }
    }

    /**
     * Construct and register this mod item with Forge
     * @param name registry and unlocalized item name
     */
    public Blueprint(String name) {
        super(name);
    }

    /**
     * <p>This class contains NBT related methods for blueprints in addition to NBTTagCompound
     * tagmap key names that make it easier for us to set and get keys when accessing Itemstack stored data.</p>
     * <br/>
     *     List of map names and what they contain:
     * <ul>
     *     <li><b>TAG</b> - NBT subcompound tag key that maps blueprint data.</li>
     *     <li><b>ID</b> - Blueprint NBT unique identified used for internal data mapping.</li>
     *     <li><b>RECIPES</b> - NBTTagList key that maps custom blueprint recipes.</li>
     *     <li><b>TOOLTIP</b> - String NBT tag key that maps blueprint data tooltips.</li>
     * </ul>
     */
    private static class BlueprintNBT {

        static final String TAG = "blueprint";
        static final String ID = "uuid";
        static final String RECIPES = "recipes";
        static final String TOOLTIP = "tooltip";

        /**
         * <p>Used to retrieve recipe resource locations from NBTTagCompound.</p>
         * Don't change this as resource locations can only be stored as strings in NBT
         */
        static final int listType =
                net.minecraftforge.common.util.Constants.NBT.TAG_STRING;

        /**
         * <p>Blueprint ID's are unique numbers used to map itemstack NBTTagCompounds.</p>
         * <p>Normally we would map itemstack object references themselves but since these object references
         * cannot be relied upon as their are copied over internally multiple times in a course of their lifespan.</p>
         * <p>The solution to this is to generate unique ID numbers and store them as itemstack tags which can be
         * retrieved from any registered blueprint itemstack as soon as they are passed through method arguments.</p>
         *
         * @return a randomly generated ID
         */
        static @NotNull java.util.UUID generateUniqueId() {
            return java.util.UUID.randomUUID();
        }

        /**
         * Retrieve itemstack {@code NBT} subcompound that represents blueprint data
         * @return {@code null} if no blueprint data was found
         */
        static @Nullable NBTTagCompound getSavedData(ItemStack stack) {
            return stack.getSubCompound(BlueprintNBT.TAG);
        }
        /**
         * <p>Store blueprint data to itemstack {@code NBTTagCompound} subcompound.</p>
         * <i>Note: If blueprint {@code NBT} subcompound is already mapped it will be overwritten.</i>
         */
        static void saveData(ItemStack stack, NBTTagCompound blueprintData) {
            stack.setTagInfo(BlueprintNBT.TAG, blueprintData);
        }

        /**
         * <p>Retrieve a pre-constructed string that lists all blueprint recipe output item names</p>
         * The string is constructed and stored as an {@code NBT} entry in
         * {@link #initializeBlueprint(ItemStack, EntityPlayerMP)}
         *
         * @param stack blueprint to get the tooltip for
         * @return recipe output item names in a single string ready to be displayed as a tooltip,
         *         or an {@code empty} string if no tooltip {@code NBT} was found in stack {@code NBTTagCompound}.
         */
        static @NotNull String getTooltip(ItemStack stack) {

            NBTTagCompound blueprintData = BlueprintNBT.getSavedData(stack);
            return blueprintData != null && blueprintData.hasKey(TOOLTIP)
                    ? blueprintData.getString(TOOLTIP) : "";
        }
    }

    /**
     * This method will initialize and store our blueprint data in the following steps:
     * <ul>
     *     <li>Split the stack into two and add <i>safely</i> add the second one to player inventory
     *         if multiple blueprints are present in a stack.</li>
     *     <li>Create a new NBTTagCompound for blueprint itemstack if it doesn't have one already.</li>
     *     <li>Generate a unique blueprint itemstack id and add it to NBT data. </li>
     *     <li>Get a random number of custom recipes and add them to NBT data.</li>
     *     <li>Construct a tooltip that displays recipe outputs and add it to NBT data.</li>
     *     <li>Create and register a new blueprint mapping so we can quickly access this data again.</li>
     * </ul>
     * @param stack blueprint itemstack to register
     * @param player instance of the player reading the blueprint
     * @return {@code true} if the blueprint was successfully registered
     */
    private static boolean initializeBlueprint(final ItemStack stack, EntityPlayerMP player) {

        if (stack == null || !stack.getItem().equals(ModItem.BLUEPRINT.get())) {
            ModLogger.error("Unable to register blueprint, invalid ItemStack argument!");
        }
        else if (!isUnregistered(stack)) {
            ModLogger.warn("Trying to register same blueprint twice");
        }
        /* Register blueprint only if we found some custom recipes in Forge registry */
        else if (CustomRecipes.exist())
        {
            ModLogger.debug("Registering new blueprint entry...");
            if(!stack.hasTagCompound())
            {
                ModLogger.debug("Creating new NBTTagCompound for blueprint");
                /*
                 *  If the stack has multiple items don't assign recipes to the whole stack.
                 *  We always want to register ONLY stacks with a count of 1
                 */
                if (player != null && stack.getCount() > 1) {
                    /*
                     *  When dealing with a stack of multiple items
                     *
                     *  It's very important that we set stack NBTTagCompound before we add the
                     *  new ItemStack to player inventory so that they don't merge
                     */
                    ItemStack newStack = new ItemStack(stack.getItem(), stack.getCount() - 1);
                    stack.setCount(1); stack.setTagCompound(new NBTTagCompound());
                    ItemHandlerHelper.giveItemToPlayer(player, newStack);
                }
                else stack.setTagCompound(new NBTTagCompound());
            }
            else {
                /*
                 * TODO: Handle cases when the stack already has NBTTagCompound
                 *       The way to go about this would be to copy the stack over
                 *       in a new inventory slot and asign it the existing NBTTagCompound
                 */
            }
            if (BlueprintNBT.getSavedData(stack) == null)
            {
                NBTTagCompound blueprintData = new NBTTagCompound();
                java.util.List<IRecipe> randomRecipes = CustomRecipes.getRandom(Blueprint.MAX_CUSTOM_RECIPES);
                /*
                 * Convert all random recipe resource locations or registry names to strings
                 * so they can be stored in itemstack NBTTagCompound
                 */
                NBTTagList nbtTagList = new NBTTagList();
                randomRecipes.forEach(recipe -> nbtTagList.appendTag(new NBTTagString(recipe.getRegistryName().toString())));

                String tooltip = String.join(", ", CustomRecipes.getOutputs(randomRecipes));
                java.util.UUID uniqueId = BlueprintNBT.generateUniqueId();
                /*
                 * Store all blueprint information that we want persisting over world reloads
                 */
                blueprintData.setUniqueId(BlueprintNBT.ID, uniqueId);
                blueprintData.setTag(BlueprintNBT.RECIPES, nbtTagList);
                blueprintData.setString(BlueprintNBT.TOOLTIP, tooltip);

                BlueprintData.add(uniqueId, randomRecipes);
                BlueprintNBT.saveData(stack, blueprintData);
                return true;
            }
            else ModLogger.warn("Trying to register blueprint with existing BlueprintNBT data");
        }
        return false;
    }

    /**
     * @return {@code true} if blueprint ID mapping data exists
     */
    private static boolean isUnregistered(ItemStack stack) {
        return !BlueprintData.exist(stack);
    }

    /**
     * <p>This method will first try to load blueprint data from NBTTagCompound.</p>
     * If no blueprint NBT data is found it will start the initialization process.
     *
     * @param blueprint blueprint itemstack to load or initialize
     * @param player instance of the player reading the blueprint
     */
    private static void loadOrRegisterBlueprint(@NotNull ItemStack blueprint, @Nullable EntityPlayerMP player) {

        ModLogger.debug("Found unregistered blueprint: %s", blueprint.toString());
        if (!loadBlueprint(blueprint) && !initializeBlueprint(blueprint, player))
            ModLogger.error("Unable to load or register blueprint data");
    }

    /**
     * <p>Load and register blueprint data from itemstack NBTTagCompound.</p>
     * <i>Note: if data is already registered it will be ovewritten.</i>
     * @param stack itemstack to load the blueprint from
     * @return {@code true} if the blueprint was successfully loaded
     */
    private static boolean loadBlueprint(ItemStack stack) {

        ModLogger.debug("Checking if blueprint has saved recipe entries...");
        NBTTagCompound blueprintData = BlueprintNBT.getSavedData(stack);

        if (blueprintData != null)
        {
            ModLogger.debug("Loading custom recipes for blueprint");
            NBTTagList recipeList = blueprintData.getTagList(BlueprintNBT.RECIPES, BlueprintNBT.listType);
            java.util.List<IRecipe> customRecipes = new java.util.ArrayList<>();

            for (NBTBase nbtBase : recipeList)
            {
                /* Read recipe resource location from NBT in String format and convert it
                 * to a ResourceLocation object so we can find the recipe mapped to it
                 */
                String sLocation = ((NBTTagString) nbtBase).getString();
                customRecipes.add(CustomRecipes.getRecipe(new ResourceLocation(sLocation)));
            }
            if (!customRecipes.isEmpty()) {
                ModLogger.debug("Loaded blueprint recipes from NBT: %s", recipeList);
                BlueprintData.add(blueprintData, customRecipes);
                return true;
            }
        }
        return false;
    }

    /**
     * <p>Called when the player attempts to read the blueprint by right clicking in the air.</p>
     * <i>Note that this will <b>not get called</b> if the player clicks on a block.</i>
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @NotNull EnumHand handIn) {

        if (playerIn instanceof EntityPlayerMP) {
            /*
             *  Don't learn recipes if the player is in creative mode
             */
            if (!playerIn.isCreative())
            {
                EntityPlayerMP player = (EntityPlayerMP) playerIn;
                ItemStack blueprint = player.getHeldItem(handIn);

                ModLogger.debug("%s is trying to read a blueprint", player.getName());
                /*
                 * Try to find blueprint data stored in an internal map
                 * before trying to construct them from NBT
                 */
                if (isUnregistered(blueprint))
                    loadOrRegisterBlueprint(blueprint, player);

                java.util.List<IRecipe> blueprintRecipes = BlueprintData.get(blueprint);
                java.util.List<IRecipe> recipesToLearn = new ArrayList<>();

                if (!blueprintRecipes.isEmpty())
                {
                    ModLogger.debug("Found %d potential blueprint recipes to learn", blueprintRecipes.size());
                    for (IRecipe recipe : blueprintRecipes) {
                        if (!player.getRecipeBook().isUnlocked(recipe))
                        {
                            ModLogger.debug("Going to learn recipe %s", recipe.getRecipeOutput().getDisplayName());
                            recipesToLearn.add(recipe);
                        }
                        else ModLogger.debug("Skipping recipe %s, already know that one", recipe.getRecipeOutput().getDisplayName());
                    }
                    if (!recipesToLearn.isEmpty())
                    {
                        playerIn.unlockRecipes(recipesToLearn);
                        Knowledge.increasePlayerKnowledge(playerIn, 1);
                        ModLogger.debug("%s learned %d new recipes from blueprint", player.getName(), recipesToLearn.size());
                    }
                    else ModLogger.debug("Blueprint doesn't contain any new recipes to learn");
                }
                else ModLogger.error("Blueprint recipe list is empty, something went wrong");
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, java.util.List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(ChatFormatting.GRAY + "This paper contains instructions on how to craft simple items.");
        /*
         * TODO: Improve performance here by setting a local class ItemStack variable that points to the
         *  last stack that displayed tooltip information and skip these checks if we are viewing the same one
         */
        String recipeOutputs = BlueprintNBT.getTooltip(stack);
        tooltip.add(recipeOutputs.isEmpty() ? ChatFormatting.GRAY + "" + TextFormatting.ITALIC + "Read to learn more information." :
                ChatFormatting.BLUE /*+ "" + TextFormatting.ITALIC*/ + recipeOutputs);
    }
}
