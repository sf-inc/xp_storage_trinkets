package com.github.charlyb01.xpstorage_trinkets.mixin;

import com.github.charlyb01.xpstorage.Utils;
import com.github.charlyb01.xpstorage.XpBook;
import com.github.charlyb01.xpstorage_trinkets.XpstorageTrinkets;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity {
    @Shadow public int experienceLevel;

    @Shadow public float experienceProgress;

    @Shadow public abstract void addExperience(int experience);

    @Shadow public abstract PlayerInventory getInventory();

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyVariable(method = "addExperience", at = @At("HEAD"), ordinal = 0)
    private int putXPOnConduit(int experience) {
        if (!(this.getMainHandStack().getItem() instanceof XpBook)
                && !(this.getOffHandStack().getItem() instanceof XpBook)
                && TrinketsApi.getTrinketComponent(this).isPresent()
                && TrinketsApi.getTrinketComponent(this).get().isEquipped(XpstorageTrinkets.xp_conduit)) {

            List<ItemStack> xpConduits = getXPConduits();
            List<ItemStack> xpBooks = getXPBooks();

            while (!xpBooks.isEmpty()
                    && !xpConduits.isEmpty()
                    && experience > 0) {

                ItemStack xpBook = xpBooks.get(0);
                int bookExperience = xpBook.getDamage();
                int bookDurability = xpBook.getMaxDamage() - bookExperience;
                if (bookDurability == 0) {
                    xpBooks.remove(0);
                    continue;
                }

                ItemStack xpConduit = xpConduits.get(0);
                int conduitDamage = xpConduit.getDamage();
                int conduitDurability = xpConduit.getMaxDamage() - conduitDamage;
                if (conduitDurability == 0) {
                    xpConduits.remove(0);
                    continue;
                }

                int toRemove = Math.min(Math.min(bookDurability, conduitDurability), experience);
                experience -= toRemove;
                xpBook.setDamage(bookExperience + toRemove);
                xpConduit.setDamage(conduitDamage + toRemove);
            }
        }

        return experience;
    }

    private List<ItemStack> getXPConduits() {
        List<ItemStack> list = new ArrayList<>();

        Map<String, TrinketInventory> group = TrinketsApi.getTrinketComponent(this).get().getInventory().get("hand");
        TrinketInventory slotInventory = group.get("ring");

        for (int i = 0; i < slotInventory.size(); i++) {
            if (slotInventory.getStack(i).getItem().equals(XpstorageTrinkets.xp_conduit)) {
                list.add(slotInventory.getStack(i));
            }
        }

        group = TrinketsApi.getTrinketComponent(this).get().getInventory().get("offhand");
        slotInventory = group.get("ring");

        for (int i = 0; i < slotInventory.size(); i++) {
            if (slotInventory.getStack(i).getItem().equals(XpstorageTrinkets.xp_conduit)) {
                list.add(slotInventory.getStack(i));
            }
        }

        return list;
    }

    private List<ItemStack> getXPBooks() {
        List<ItemStack> list = new ArrayList<>();
        for (ItemStack itemStack : this.getInventory().main) {
            if (itemStack.getItem() instanceof XpBook) {
                list.add(itemStack);
            }
        }

        return list;
    }
}
