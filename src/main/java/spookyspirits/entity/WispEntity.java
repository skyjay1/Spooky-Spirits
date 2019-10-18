package spookyspirits.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.registries.ForgeRegistries;
import spookyspirits.effect.PhookaEffect;
import spookyspirits.init.ModObjects;
import spookyspirits.init.SpiritsConfig;
import spookyspirits.init.SpookySpirits;

public class WispEntity extends FlyingEntity {
	
	public WispEntity(EntityType<? extends WispEntity> type, World world) {
		super(type, world);
	}
	
	@Override
	public void livingTick() {
		super.livingTick();
		// check for nearby players
		if(this.ticksExisted % 2 == 0 && this.isAlive() && this.isServerWorld() && !this.world.isRemote) {
			final double range = 4.0D;
			final List<PlayerEntity> list = this.getEntityWorld().getEntitiesWithinAABB(PlayerEntity.class, 
					this.getBoundingBox().grow(range, range / 2.0D, range));
			if(!list.isEmpty()) {
				// find the nearest player
				double closestSq = range * range;
				PlayerEntity closest = null;
				for(final PlayerEntity p : list) {
					final double d = this.getDistanceSq(p);
					if(d < closestSq) {
						closestSq = d;
						closest = p;
					}
				}
				// if we have a valid player, choose some actions!
				if(closest != null) {
					final Set<WispAction> actions = WispAction.getRandomActionsWeighted(rand, 1 + rand.nextInt(3));
					for(final WispAction a : actions) {
						if(a.doAction(this, closest)) {
							SpookySpirits.LOGGER.info("Action performed! " + a.getName());
						}
					}
					this.remove();
				}
			}
		}
	}
	
	@Override
	@Nullable
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason,
			@Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
		// spawn WillOWisps nearby
		if(this.isServerWorld() /*// TODO re-enable after testing: && reason == SpawnReason.NATURAL */) {
			final BlockPos myPos = this.getPosition();
			final int minRadius = 24;
			final int extraRadius = 16;
			// dividing by 6 will cause approx. 6*2=12 spawns
			final double dt = Math.PI / 6.0D;
			// cycle through each angle in a full circle
			for(double t = 0; t < 2 * Math.PI; t += dt + rand.nextDouble() * dt * 0.5D) {
				double sine = Math.sin(t);
				double cosine = Math.cos(t);
				// try several times to find a location for the wisp
				for(int i = 0, attempts = 10; i < attempts; i++) {
					int x = (int)Math.round((minRadius + rand.nextInt(extraRadius)) * sine);
					int y = rand.nextInt(extraRadius + i);
					int z = (int)Math.round((minRadius + rand.nextInt(extraRadius)) * cosine);
					int dy = 1 + rand.nextInt(3);
					BlockPos p = WispEntity.getBestY(this.getEntityWorld(), myPos.add(x, y, z), dy);
					// if the chosen position is actually air, place a Will O WispEntity
					if(this.getEntityWorld().isAirBlock(p)) {
						final WillOWispEntity w = ModObjects.WILL_O_WISP.create(this.world, (CompoundNBT)null, (ITextComponent)null, (PlayerEntity)null, p, SpawnReason.MOB_SUMMONED, false, false);
						w.setPosition(p.getX() + 0.5D, p.getY() + 0.5D, p.getZ() + 0.5D);
						w.setWisp(this.getUniqueID());
						worldIn.addEntity(w);
						break;
					}
				}
			}
		}
		return spawnDataIn;
	}
	
	@Override
	public void collideWithEntity(final Entity entityIn) {
		// do nothing
	}

	@Override
	public boolean isPushedByWater() {
		return false;
	}
	
	/** 
	 * Tries to find the lowest y-value that is still air 
	 * @param p origin (does not affect x and z)
	 * @param dy the amount of change in y per test
	 * @return a BlockPos that may or may not actually be air
	 **/
	public static BlockPos getBestY(final World world, final BlockPos origin, final int dy) {
		final int deltaY = Math.max(1, dy);
		BlockPos p = origin;
		int attempts = 20;
		while(world.isAirBlock(p.down(deltaY)) && attempts-- > 0 && (p.getY() - deltaY) > 0) {
			p = p.down(deltaY);
		}
		return p;
	}
	
//	
//	public static void initWispActions() {
//		StringBuilder sb = new StringBuilder();
//		for(final WispAction a : WispAction.ACTIONS) {
//			sb.append("{" + a.getName() + "} ");
//		}
//		SpookySpirits.LOGGER.info("Registered WispActions:\n" + sb.toString());
//	}
//	
	public static ConfigValue<List<? extends String>> setupConfig(final SpiritsConfig config, final ForgeConfigSpec.Builder builder) {
		builder.comment("Percent chance that the following WispEntity actions can occur (0=disabled)");
		for(final WispAction a : WispAction.ACTIONS) {
			config.registerWispAction(builder, a.getName(), a.getDefaultChance());
		}
		final ConfigValue<List<? extends String>> blacklist =
				builder.comment("Potion effects that the WispEntity should not apply")
					.defineList("potion_blacklist", 
						Lists.newArrayList(
								Effects.WITHER.getRegistryName().toString(), Effects.LEVITATION.getRegistryName().toString(),
								Effects.HERO_OF_THE_VILLAGE.getRegistryName().toString(), SpookySpirits.MODID + PhookaEffect.Invisibility.NAME
								), // TODO add other modded effects
						o -> o instanceof String && 
						ForgeRegistries.POTIONS.containsKey(new ResourceLocation((String)o)));
		
		return blacklist;
	}

	private abstract static class WispAction implements IStringSerializable {
		
		private static final List<WispAction> ACTIONS = new ArrayList<>();
		
		//////////// DO NOTHING
		protected static final WispAction NOTHING = new WispAction("nothing", 15) {

			@Override
			protected boolean doAction(final WispEntity wispEntity, final PlayerEntity player) {
				return true;
			}
		};

		//////////// PLACE A TREASURE CHEST WITH LOOT
		protected static final WispAction LOOT_CHEST = new WispAction("loot_chest", 95) {

			@Override
			protected boolean doAction(final WispEntity wispEntity, final PlayerEntity player) {
				// find an empty block position
				BlockPos pos = wispEntity.getPosition();
				for (final Direction d : Direction.values()) {
					final BlockState state = wispEntity.getEntityWorld().getBlockState(pos);
					if (state.isAir(wispEntity.getEntityWorld(), pos) || state.getMaterial().isReplaceable()) {
						break;
					}
					pos = wispEntity.getPosition().offset(d, 1 + wispEntity.rand.nextInt(2));
				}
				// Assume we have a valid position to place the chest (we did our best)
				wispEntity.getEntityWorld().setBlockState(pos, Blocks.CHEST.getDefaultState());
				// set random loot table
				ChestTileEntity.setLootTable(wispEntity.getEntityWorld(), wispEntity.rand, pos, getLootTable(wispEntity.rand));

				return true;
			}

			/**
			 * @param rand a Random instance
			 * @return a random treasure chest Loot Table
			 **/
			private ResourceLocation getLootTable(final Random rand) {
				switch (rand.nextInt(8)) {
				case 0: return LootTables.CHESTS_DESERT_PYRAMID;
				case 1: return LootTables.CHESTS_SHIPWRECK_TREASURE;
				case 2: return LootTables.CHESTS_ABANDONED_MINESHAFT;
				case 3: return LootTables.CHESTS_VILLAGE_VILLAGE_WEAPONSMITH;
				case 4: return LootTables.CHESTS_VILLAGE_VILLAGE_TOOLSMITH;
				case 5: return LootTables.CHESTS_VILLAGE_VILLAGE_ARMORER;
				case 6: return LootTables.CHESTS_STRONGHOLD_LIBRARY;
				case 7: return LootTables.CHESTS_PILLAGER_OUTPOST;
				default: return LootTables.EMPTY;
				}
			}
		};

		//////////// SPAWN A HEAVILY ARMORED SKELETON
		protected static final WispAction SPAWN_SKELETON = new WispAction("skeleton", 30) {

			@Override
			protected boolean doAction(final WispEntity wispEntity, final PlayerEntity player) {
				SkeletonEntity sk = EntityType.SKELETON.create(wispEntity.getEntityWorld());
				sk.setLocationAndAngles(wispEntity.posX, wispEntity.posY, wispEntity.posZ, wispEntity.rotationYaw, wispEntity.rotationPitch);
				// armor and weapons for skeleton
				final ItemStack helmet = EnchantmentHelper.addRandomEnchantment(wispEntity.rand,
						new ItemStack(Items.CHAINMAIL_HELMET), wispEntity.rand.nextInt(2), true);
				final ItemStack chest = EnchantmentHelper.addRandomEnchantment(wispEntity.rand,
						new ItemStack(Items.CHAINMAIL_CHESTPLATE), wispEntity.rand.nextInt(2), true);
				final ItemStack legs = EnchantmentHelper.addRandomEnchantment(wispEntity.rand,
						new ItemStack(Items.CHAINMAIL_LEGGINGS), wispEntity.rand.nextInt(2), true);
				final ItemStack boots = EnchantmentHelper.addRandomEnchantment(wispEntity.rand,
						new ItemStack(Items.CHAINMAIL_BOOTS), wispEntity.rand.nextInt(2), true);
				final ItemStack sword = EnchantmentHelper.addRandomEnchantment(wispEntity.rand,
						new ItemStack(Items.IRON_SWORD), wispEntity.rand.nextInt(2), false);
				sword.addEnchantment(Enchantments.SHARPNESS, 1 + wispEntity.rand.nextInt(3));
				sword.addEnchantment(Enchantments.FIRE_ASPECT, 1 + wispEntity.rand.nextInt(2));
				sk.setItemStackToSlot(EquipmentSlotType.HEAD, helmet);
				sk.setItemStackToSlot(EquipmentSlotType.CHEST, chest);
				sk.setItemStackToSlot(EquipmentSlotType.LEGS, legs);
				sk.setItemStackToSlot(EquipmentSlotType.FEET, boots);
				sk.setItemStackToSlot(EquipmentSlotType.MAINHAND, sword);
				for (EquipmentSlotType type : EquipmentSlotType.values()) {
					sk.setDropChance(type, wispEntity.rand.nextInt(5) == 0 ? 0.8F : 0.01F);
				}
				sk.setAttackTarget(player);
				wispEntity.getEntityWorld().addEntity(sk);
				return true;
			}

		};

		//////////// SPAWN A SWARM OF BATS
		protected static final WispAction SPAWN_BATS = new WispAction("bats", 30) {

			@Override
			protected boolean doAction(final WispEntity wispEntity, final PlayerEntity player) {
				for (int i = 0, numBats = 16; i < numBats; i++) {
					final BatEntity bat = EntityType.BAT.create(wispEntity.getEntityWorld());
					// offset position just a bit (especially upward)
					bat.setPositionAndRotation(wispEntity.posX + wispEntity.rand.nextDouble() - 0.5D,
							wispEntity.posY + wispEntity.rand.nextDouble() + 0.5D, 
							wispEntity.posZ + wispEntity.rand.nextDouble() - 0.5D,
							wispEntity.rotationYaw + wispEntity.rand.nextFloat() * 180F,
							wispEntity.rotationPitch + wispEntity.rand.nextFloat() * 180F);
					wispEntity.getEntityWorld().addEntity(bat);
				}
				return true;
			}

		};

		//////////// GIVE PLAYER GOOD POTION EFFECTS
		protected static final WispAction POTION_EFFECT_GOOD = new WispAction("beneficial_effects", 20) {

			@Override
			protected boolean doAction(final WispEntity wispEntity, final PlayerEntity player) {
				// streams all BENEFICIAL potion effects to choose from
				final List<Effect> EFFECTS = SpiritsConfig.CONFIG.getGoodEffects();
				for (int i = 0, numPotions = 3; i < numPotions; i++) {
					final Effect e = EFFECTS.get(wispEntity.rand.nextInt(EFFECTS.size()));
					final int len = e.isInstant() ? 1 : 8000 + wispEntity.rand.nextInt(4000);
					player.addPotionEffect(new EffectInstance(e, len));
				}
				return true;
			}

		};

		//////////// GIVE PLAYER BAD POTION EFFECTS
		protected static final WispAction POTION_EFFECT_BAD = new WispAction("harmful_effects", 40) {

			@Override
			protected boolean doAction(final WispEntity wispEntity, final PlayerEntity player) {
				// streams all registered HARMFUL or NEUTRAL potion effects (except levitation and wither)
				final List<Effect> EFFECTS = SpiritsConfig.CONFIG.getBadEffects();
				for (int i = 0, numPotions = 3; i < numPotions; i++) {
					final Effect e = EFFECTS.get(wispEntity.rand.nextInt(EFFECTS.size()));
					int len = e.isInstant() ? 1 : 500 + wispEntity.rand.nextInt(500);
					player.addPotionEffect(new EffectInstance(e, len));
				}
				return true;
			}

		};
		//////////// MAKES PLAYER'S ITEMS SPILL ALL OVER
		protected static final WispAction THROW_ITEMS = new WispAction("drop_inv", 10) {

			@Override
			protected boolean doAction(final WispEntity wispEntity, final PlayerEntity player) {
				player.inventory.armorInventory.forEach(i -> player.dropItem(i, true, false));
				player.inventory.armorInventory.clear();
				return true;
			}

		};
		//////////// RANDOMLY ENCHANTS PLAYER'S ARMOR AND HELD ITEM(S), IF APPLICABLE
		protected static final WispAction ENCHANT_ITEMS = new WispAction("enchant_equipment", 10) {

			@Override
			protected boolean doAction(final WispEntity wispEntity, final PlayerEntity player) {
				for (final EquipmentSlotType s : EquipmentSlotType.values()) {
					ItemStack i = EnchantmentHelper.addRandomEnchantment(wispEntity.rand, player.getItemStackFromSlot(s),
							wispEntity.rand.nextInt(2), true);
					player.setItemStackToSlot(s, i);
				}
				return true;
			}

		};
		//////////// DROPS A LARGE NUMBER OF ENCHANTED BOOKS
		protected static final WispAction SPELLBOOKS = new WispAction("drop_spellbooks", 40) {

			@Override
			protected boolean doAction(final WispEntity wispEntity, final PlayerEntity player) {
				for (int i = 0, numBooks = 4 + wispEntity.rand.nextInt(8); i < numBooks; i++) {
					ItemStack book = EnchantmentHelper.addRandomEnchantment(wispEntity.rand, new ItemStack(Items.BOOK),
							2 + wispEntity.rand.nextInt(2), true);
					wispEntity.entityDropItem(book, 0.25F);
				}
				return true;
			}

		};

		protected static final WispAction PUMPKIN_HEAD = new WispAction("pumpkin_head", 20) {

			@Override
			protected boolean doAction(final WispEntity wispEntity, final PlayerEntity player) {
				// replace player's helmet with pumpkin
				ItemStack item = player.getItemStackFromSlot(EquipmentSlotType.HEAD).copy();
				player.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Blocks.CARVED_PUMPKIN));
				// spawn their helmet next to them (enable instant pick-up)
				ItemEntity itementity = new ItemEntity(player.world, player.posX, player.posY + 0.1D, player.posZ, item);
				itementity.setNoPickupDelay();
				itementity.setNoDespawn();
				player.getEntityWorld().addEntity(itementity);
				return true;
			}

		};

		private final String name;
		private final int defaultPercentChance;

		WispAction(final String nameIn, final int defaultChance) {
			name = nameIn;
			defaultPercentChance = defaultChance;
			ACTIONS.add(this);
		}
		
		@Override
		public String getName() {
			return name;
		}
		
		protected int getDefaultChance() {
			return defaultPercentChance;
		}
		
		protected int getPercentChance() {
			return SpiritsConfig.CONFIG.getActionChance(name);
		}

		protected boolean canSelect(final Random rand) {
			return rand.nextInt(100) < getPercentChance();
		}

		protected abstract boolean doAction(final WispEntity wispEntity, final PlayerEntity player);

		/**
		 * @param rand   a random instance
		 * @param number the number of WispAction elements to add to the set
		 * @return a set containing a specified number of unique WispAction objects,
		 *         each weighted by their percentage chance of being selected
		 **/
		protected static Set<WispAction> getRandomActionsWeighted(final Random rand, final int number) {
			final Set<WispAction> actions = new HashSet<>();
			while (actions.size() < number) {
				final WispAction a = getRandomActionUnweighted(rand);
				if (!actions.contains(a) && a.canSelect(rand)) {
					actions.add(a);
				}
			}
			return actions;
		}

		/**
		 * @param rand a random instance
		 * @return a random WispAction. Each WispAction has an equal chance of being
		 *         selected
		 **/
		protected static WispAction getRandomActionUnweighted(final Random rand) {
			return ACTIONS.isEmpty() ? NOTHING : ACTIONS.get(rand.nextInt(ACTIONS.size()));
		}
	}

}
